package ins.platform.socketIO.client.clientListener;

import ins.platform.socketIO.util.DataUtil;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SimpleConnectListener implements Emitter.Listener{
	
	//private Socket socket ; 
	
	/**
	 * @param socket 连接到服务器的客户端的 socket
	 */
	public SimpleConnectListener(Socket socket){
		//this.socket = socket;
	}
	
	@Override
	public void call(Object... arg0) {
		System.out.println(DataUtil.getCurrentDataTime() + ": 成功连接上服务器! ");
	}
	
}
