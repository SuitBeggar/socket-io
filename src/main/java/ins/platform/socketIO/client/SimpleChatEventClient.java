package ins.platform.socketIO.client;

import ins.platform.socketIO.client.clientListener.SimpleConnectListener;
import ins.platform.socketIO.client.clientListener.SimpleDisConnectListener;
import ins.platform.socketIO.client.clientListener.SimpleMesListener;
import ins.platform.socketIO.enums.lable.SocketIoLable;
import ins.platform.socketIO.util.Base64Util;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * @描述： 根据给定的服务器地址、端口、用户名连接服务器
 * @author gezhiwang
 * @date 2018-10-29
 *
 */
public class SimpleChatEventClient {
	
	//默认的连接方式，比如为 new String[]{"polling","websocket"} ， 会先以轮询的方式连接，然后再通过 websocket 的方式连接
	private String[] transports = new String[]{"websocket"}; 
	//默认的重连最大次数
	private int reconnectionAttempts = 2 ; 
	//默认的失败重连的时间间隔
	private long reconnectionDelay = 1000l;
    //默认的连接超时时间(ms)
	private long timeout = 500;
	//连接
	private String serverHost = "127.0.0.1";
	private int serverPort = 9999;
	private String userCode ;
	private Socket socket ; 
	
	//发送的信息
	Map<String, String> sendMes = new HashMap<>();
	
	/**
	 * @描述： 构造方法
	 * @param serverHost ， 服务器的 ip
	 * @param serverPort ，服务器的端口
	 * @param userCode ， 用于与服务器通信用的用户名
	 * @throws URISyntaxException 
	 */
	public SimpleChatEventClient(String serverHost , int serverPort , String userCode) throws URISyntaxException{
		this.serverHost = serverHost ;
		this.serverPort = serverPort ;
		this.userCode = userCode;
		sendMes.put("userCode", userCode);
		
		IO.Options options = new IO.Options();
        options.transports = transports;
        options.reconnectionAttempts = reconnectionAttempts;
        //失败重连的时间间隔
        options.reconnectionDelay = reconnectionDelay;
        //连接超时时间(ms)
        options.timeout = timeout;
		this.socket = IO.socket("http://"+this.serverHost+":"+this.serverPort+"?userCode="+this.userCode, options);
		
		//注册各种监听事件
		socket.on("connect", new SimpleConnectListener(socket));
		socket.on(Socket.EVENT_DISCONNECT, new SimpleDisConnectListener(socket));
		socket.on(Socket.EVENT_MESSAGE, new SimpleMesListener(socket));
		
		//连接服务器
		socket.connect();
	}
	
	public void close(){
		socket.close();
	}
	
	public void send(String mes){
    	sendMes.put("msgContent", mes);
    	socket.emit(SocketIoLable.CHAT_EVENT.getValue(), Base64Util.encode(JSON.toJSONString(sendMes)));
	}
	
}
