package ins.platform.socketIO;

import ins.platform.socketIO.service.SocketIOServiceSingleton;

import com.corundumstudio.socketio.Configuration;

public class StartServer {
	public static void main(String[] args) {
		Configuration configuration = new Configuration();
		configuration.setPort(9999);
		configuration.setHostname("11.203.17.174");
		SocketIOServiceSingleton socketServer = SocketIOServiceSingleton.getInstance();
		socketServer.startServer(configuration);
	}
}
