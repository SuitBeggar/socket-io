package ins.platform.socketIO.client.clientListener;

import ins.platform.socketIO.util.DataUtil;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SimpleMesListener implements Emitter.Listener{
	
	@SuppressWarnings("unused")
	private Socket socket ; 
	
	/**
	 * @param socket 连接到服务器的客户端的 socket
	 */
	public SimpleMesListener(Socket socket){
		this.socket = socket;
	}
	
	@Override
	public void call(Object... arg0) {
		 for (Object obj : arg0) {
             System.out.println(DataUtil.getCurrentDataTime() + ":receive server message="+obj);
         }
	}
}
