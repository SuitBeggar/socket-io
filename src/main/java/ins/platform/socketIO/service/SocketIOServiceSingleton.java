package ins.platform.socketIO.service;

import ins.platform.socketIO.Listener.SocketIOConnectListener;
import ins.platform.socketIO.Listener.SocketIODataListener;
import ins.platform.socketIO.cache.WebSocketCacheSingleton;
import ins.platform.socketIO.enums.lable.SocketIoLable;
import ins.platform.socketIO.util.Base64Util;
import ins.platform.socketIO.util.RedisCacheUtil;

import java.util.Collection;
import java.util.Set;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

/**
 * 
 * @author yanzhaohu
 * @date 2018/10/10
 * socket 服务
 * 单例，类加载就初始化
 */
public class SocketIOServiceSingleton {
	
	private static SocketIOServiceSingleton instance = new SocketIOServiceSingleton();  
	
    private SocketIOServiceSingleton (){
    	
    }  
    
    public static SocketIOServiceSingleton getInstance() {  
    	return instance;  
    }  
	
	private SocketIOServer socketIOServer;
	
	private WebSocketCacheSingleton cache = WebSocketCacheSingleton.getInstance();//获取存储
	
	public void startSocketio(Configuration conf) throws InterruptedException {
		socketIOServer = new SocketIOServer(conf);
		SocketIOConnectListener connectListener = new SocketIOConnectListener();
		socketIOServer.addConnectListener(connectListener);
		socketIOServer.addDisconnectListener(connectListener);
		//socketIOServer.getAllClients()
		socketIOServer.addEventListener(SocketIoLable.CHAT_EVENT.getValue(), String.class,new SocketIODataListener());
		System.out.println("启动socket服务");
		socketIOServer.start();
		
		System.out.println("清理redis缓存");
		//清空缓存中的数据
		System.out.println(SocketIOConnectListener.SERVET_IP_PORT);
		Set<String> userCodes = RedisCacheUtil.getStringSetCacheData(SocketIoLable.REDIS_USERS.getValue());
		for (String userCode : userCodes) {
			Set<String> stringSetCacheData = RedisCacheUtil.getStringSetCacheData(userCode);
			if (stringSetCacheData != null && stringSetCacheData.size() > 0) {
				for (String userConnectMes : stringSetCacheData) {
					//如果之前是与本机相连的客户端，则移除
					if (userConnectMes.contains(SocketIOConnectListener.SERVET_IP_PORT)) {
						System.out.println("清除 ： " + userCode + "-" + userConnectMes);
						RedisCacheUtil.removeStringSetCacheData(userCode, userConnectMes);
						Set<String> userConns = RedisCacheUtil.getStringSetCacheData(userCode);
						if (userConns == null || userConns.size() == 0) {
							RedisCacheUtil.removeStringSetCacheData(SocketIoLable.REDIS_USERS.getValue(), userCode);
							break;
						}
					}
				}
			}
		}
		
		//设置超时时间
        Thread.sleep(Integer.MAX_VALUE);
        socketIOServer.stop();
	}
	
	/**
	 * 获取 socketIOServer 
	 * @return
	 */
	public SocketIOServer getSocketIOServer() {
		return socketIOServer;
	}

	/**
	 * 给所有在线的人推送消息
	 * @param type
	 * @param content
	 */
	public void pushMsg(String content) {
		content = Base64Util.encode(content);
        Collection<SocketIOClient> allClients = socketIOServer.getAllClients();
        for (SocketIOClient socket : allClients) {
			 socket.sendEvent(SocketIoLable.NOTICE_EVENT.getValue(), content);
        }
	}
	/**
	 * 定义推送消息给某一个人
	 * @param content
	 * @throws Exception 
	 */
	public void pushMsgToOne(String userCode,String content) throws Exception {
		content = Base64Util.encode(content);
		Set<SocketIOClient> userClients = cache.getUserClients(userCode);
		for (SocketIOClient socketIOClient : userClients) {
			socketIOClient.sendEvent(SocketIoLable.NOTICE_EVENT_ONE.getValue(), content);
		}
	}
	/**
     * 启动服务
     */
    public void startServer(Configuration configuration) {
    	
        if (socketIOServer == null) {
        	final Configuration configurationFinal = configuration;
            new Thread(new Runnable() {
                @Override
                public void run() {
                   try {
	                    startSocketio(configurationFinal);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
                }
            }).start();
        }
    }
    
    /*
     * 停止服务
     */
    public void stopSocketio() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }
}
