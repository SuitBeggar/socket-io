package ins.platform.socketIO.Listener;

import ins.platform.socketIO.cache.WebSocketCacheSingleton;
import ins.platform.socketIO.config.LoadConfiguration;
import ins.platform.socketIO.msg.UserConnectMes;
import ins.platform.socketIO.service.SocketIOServiceSingleton;
import ins.platform.socketIO.util.RedisCacheUtil;

import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
/**
 * 
 * @author yanzhaohu
 * @date 2018/10/10
 * 初始化Socketio监听器，启动socketio服务
 */
public class SocketIOServletContextListener implements ServletContextListener{
	
	private WebSocketCacheSingleton cache = WebSocketCacheSingleton.getInstance();//获取存储
	
	public void contextInitialized(ServletContextEvent sce) {
		Configuration configuration = LoadConfiguration.load(sce);
		SocketIOServiceSingleton socketIOService = SocketIOServiceSingleton.getInstance();
		socketIOService.startServer(configuration);
	}

	public void contextDestroyed(ServletContextEvent sce) {
		SocketIOServiceSingleton socketIOService = SocketIOServiceSingleton.getInstance();
		socketIOService.stopSocketio();
		System.out.println("清理redis缓存");
		Map<String, Set<SocketIOClient>> all = cache.getAll();
		Set<String> keySet = all.keySet();
		UserConnectMes userConnectMes = null;
		for (String userCode : keySet) {
			Set<SocketIOClient> set = all.get(userCode);
			for (SocketIOClient socketIOClient : set) {
				userConnectMes = new UserConnectMes(socketIOClient.getSessionId().toString(), SocketIOConnectListener.SERVET_IP_PORT);
				RedisCacheUtil.removeStringSetCacheData(userCode, JSON.toJSONString(userConnectMes));
			}
		}
	}

}
