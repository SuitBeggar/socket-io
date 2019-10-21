package ins.platform.socketIO.Listener;

import ins.platform.socketIO.cache.WebSocketCacheSingleton;
import ins.platform.socketIO.enums.lable.SocketIoLable;
import ins.platform.socketIO.msg.UserConnectMes;
import ins.platform.socketIO.service.SocketIOServiceSingleton;
import ins.platform.socketIO.util.RedisCacheUtil;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
/***
 * @author yanzhaoHu
 * @data 2018/10/10
 * 实现soketio的事件监听
 */
public class SocketIOConnectListener implements ConnectListener,DisconnectListener {
	private static Logger logger = LoggerFactory.getLogger(SocketIOConnectListener.class);
	private WebSocketCacheSingleton cache = WebSocketCacheSingleton.getInstance();//获取存储
	//服务器实例
	private static SocketIOServer socketIoServer = SocketIOServiceSingleton.getInstance().getSocketIOServer();//获取socket实例
	//服务器的配置信息
	private static Configuration configuration = socketIoServer.getConfiguration();
	//服务器的ip及端口
	public final static String SERVET_IP_PORT = configuration.getHostname() + ":" + configuration.getPort();
	
	@Override
	public void onConnect(SocketIOClient client) {
		System.out.println("服务器地址： " + SERVET_IP_PORT);
		System.out.println(client.getRemoteAddress() + "链接了");
		String userCode = client.getHandshakeData().getSingleUrlParam(SocketIoLable.USER_LABLE.getValue());
		System.out.println("usercode : " + userCode);
		if(userCode==null) {
			//连接的客户端用户代码为空时，主动断开于客户端的连接
			client.disconnect();
			return;
		}else {
			logger.info("用户: " + userCode + "连接成功!"); 
 			//修改本地缓存的内容， 将 map<String , Set<SocketIOClient>>  ，map的key为用户代码 ，  Set<SocketIOClient>为当前用户的所有的登录的客户端
			cache.registUser(userCode, client);
			//更新redis缓存的内容，redis缓存的内容为 map<String ,Set<String>>  , map的key为用户代码。 set<String>为当前用户登录的所有服务器的ip及端口
			UserConnectMes userConnectMes = new UserConnectMes(client.getSessionId().toString(), SERVET_IP_PORT);
			RedisCacheUtil.putStringSetCacheData(userCode, JSON.toJSONString(userConnectMes));
			//更新所有的用户
			RedisCacheUtil.putStringSetCacheData(SocketIoLable.REDIS_USERS.getValue(), userCode);
			
			//通知客户端连接成功
			client.sendEvent(SocketIoLable.ON_CONNECT.getValue(), SocketIoLable.ON_CONNECT.getValue());
		}
		
		//遍历缓存的数据:
		System.out.println("缓存中存储的数据: ");
		Map<String, Set<SocketIOClient>> all = cache.getAll();
		Set<String> keySet = all.keySet();
		for (String string : keySet) {
			System.out.println("服务器中的缓存的用户" + string );
			for (SocketIOClient socketIOClient : all.get(string)) {
				System.out.println("服务器中的缓存的用户的连接的客户端" + socketIOClient.getSessionId() );
			}
			Set<String> stringSetCacheData = RedisCacheUtil.getStringSetCacheData(string);
			System.out.println(stringSetCacheData);
			for (String string2 : stringSetCacheData) {
				System.out.println("redis缓存的用户的信息: " + string + " ， 客户端信息" + string2);
			}
		}
		
	}

	@Override
	public void onDisconnect(SocketIOClient client) {
		System.out.println(client.getRemoteAddress() + "失去连接了");
		String userCode = client.getHandshakeData().getSingleUrlParam(SocketIoLable.USER_LABLE.getValue());
		if (userCode == null) {
			boolean breakFlag = false;
			Map<String, Set<SocketIOClient>> allClient = cache.getAll();
			for (String key : allClient.keySet()) {
				Set<SocketIOClient> set = allClient.get(key);
				for (SocketIOClient socketIOClient : set) {
					if (client.getSessionId().toString().equals(socketIOClient.getSessionId().toString())) {
						userCode = key;
						break;
					}
				}
				if (breakFlag) {
					break;
				}
			}
		}
		
		logger.info("用户: " + userCode + "登出!"); 
		client.disconnect();
		
		if (userCode != null) {
			//从本地内存中移除
			cache.remove(userCode, client);
			//从redis中移除
			UserConnectMes userConnectMes = new UserConnectMes(client.getSessionId().toString(), SERVET_IP_PORT);
			RedisCacheUtil.removeStringSetCacheData(userCode, JSON.toJSONString(userConnectMes));
			//更新所有的用户代码缓存
			Set<String> stringSetCacheData = RedisCacheUtil.getStringSetCacheData(userCode);
			if (stringSetCacheData == null || stringSetCacheData.size() == 0) {
				RedisCacheUtil.removeStringSetCacheData(SocketIoLable.REDIS_USERS.getValue(), userCode);
			}
		}
		
		//遍历缓存的数据:
		System.out.println("缓存中存储的数据: ");
		Map<String, Set<SocketIOClient>> all = cache.getAll();
		Set<String> keySet = all.keySet();
		for (String string : keySet) {
			System.out.println("服务器中的缓存的用户" + string );
			for (SocketIOClient socketIOClient : all.get(string)) {
				System.out.println("服务器中的缓存的用户的连接的客户端" + socketIOClient.getSessionId() );
			}
			Set<String> stringSetCacheData = RedisCacheUtil.getStringSetCacheData(string);
			for (String string2 : stringSetCacheData) {
				System.out.println("redis缓存的用户的信息: " + string + " ， 客户端信息" + string2);
			}
		}
		
	}

}
