package ins.platform.socketIO.Listener;

import ins.platform.socketIO.cache.WebSocketCacheSingleton;
import ins.platform.socketIO.enums.exception.ExceptionEnum;
import ins.platform.socketIO.enums.lable.SocketIoLable;
import ins.platform.socketIO.msg.MessageInfo;
import ins.platform.socketIO.service.SocketIOServiceSingleton;
import ins.platform.socketIO.util.Base64Util;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;

public class SocketIODataListener implements DataListener<String>{
	private static Logger logger = LoggerFactory.getLogger(SocketIODataListener.class);
	private WebSocketCacheSingleton cache = WebSocketCacheSingleton.getInstance();//获取存储
	private SocketIOServer socketIoServer = SocketIOServiceSingleton.getInstance().getSocketIOServer();//获取socket实例
	
	@Override
	public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
		data = Base64Util.decode(data);
		System.out.println("接收到的消息" + data);
		MessageInfo messageInfo = JSON.parseObject(data, MessageInfo.class);
		String userCode = messageInfo.getUserCode();
		String msg = messageInfo.getMsgContent();
		if(userCode==null||"".equals(userCode)){
			logger.info(ExceptionEnum.NOUSER.toString());
			client.sendEvent(SocketIoLable.CHAT_EVENT.getValue(),Base64Util.encode(ExceptionEnum.NOUSER.toString()));
            return;
        }
		try {
            msg = Base64Util.encode(userCode + ": " + msg);
            Map<String, Set<SocketIOClient>> all = cache.getAll();
            
            for (String key : all.keySet()) {
            	Set<SocketIOClient> set = all.get(key);
            	for (SocketIOClient socketIOClient : set) {
            		socketIOClient.sendEvent(SocketIoLable.CHAT_EVENT.getValue(),msg);
				}
			}
        }catch (Exception e){
        	e.printStackTrace();
        	logger.info(ExceptionEnum.USERNOONLINE.toString());
            socketIoServer.getClient(client.getSessionId()).sendEvent(SocketIoLable.CHAT_EVENT.getValue(),Base64Util.encode(ExceptionEnum.USERNOONLINE.toString()));
        }
	}

}
