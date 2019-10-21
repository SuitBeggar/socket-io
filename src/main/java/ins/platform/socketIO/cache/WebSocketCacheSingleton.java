package ins.platform.socketIO.cache;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.corundumstudio.socketio.SocketIOClient;

/**
 * 
 * @author YanZhaoHu
 * @date 2018/10/10 17:49:04
 * 定义存储，存储usercode sessionId
 * 单例
 */
public class WebSocketCacheSingleton {
	 //存储人员
    private Map<String,Set<SocketIOClient>> userCodeSessionIdMap  = new ConcurrentHashMap<>();
    
	private static WebSocketCacheSingleton instance = new WebSocketCacheSingleton();  
	
    private WebSocketCacheSingleton (){
    	
    }  
    
    public static WebSocketCacheSingleton getInstance() {  
    	return instance;  
    }

    /**查询人员的连接id**/
    public Set<SocketIOClient> getUserClients(String userCode) throws Exception{
       return userCodeSessionIdMap.get(userCode);
    }
    /**注册登录的用户**/
    public void registUser(String userCode , SocketIOClient client){
    	Set<SocketIOClient> userClients = null;
     	if (userCodeSessionIdMap.get(userCode) == null) {
     		userClients = new HashSet<>();
		}else{
			userClients = userCodeSessionIdMap.get(userCode);
		}
     	userClients.add(client);
    	userCodeSessionIdMap.put(userCode,userClients);
    }
    /**移除一个客户端**/
    public void remove(String userCode , SocketIOClient client){
    	Set<SocketIOClient> userClients = null;
     	if (userCodeSessionIdMap.get(userCode) == null) {
     		return;
		}else{
			userClients = userCodeSessionIdMap.get(userCode);
		}
     	userClients.remove(client);
     	if (userClients.size() == 0) {
     		userCodeSessionIdMap.remove(userCode);
		}
    }
    
    /**获取所有的登录的人员**/
    public Map<String, Set<SocketIOClient>> getAll(){
    	return userCodeSessionIdMap;
    }

}
