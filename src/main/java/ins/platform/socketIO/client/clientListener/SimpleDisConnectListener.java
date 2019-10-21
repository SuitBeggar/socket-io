package ins.platform.socketIO.client.clientListener;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SimpleDisConnectListener implements Emitter.Listener{
	
	private Socket socket ; 
	
	/**
	 * @param socket 连接到服务器的客户端的 socket
	 */
	public SimpleDisConnectListener(Socket socket){
		this.socket = socket;
	}
	
	@Override
	public void call(Object... arg0) {
		socket.close();
		System.out.println("关闭连接");
	}
}
