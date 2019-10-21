package ins.platform.socketIO;

import ins.platform.socketIO.client.SimpleChatEventClient;

import java.net.URISyntaxException;

public class TestClient {
	
	public static void main(String[] args) {
		try {
			System.out.println("启动客户端");
			SimpleChatEventClient simpleChatEventClient = new SimpleChatEventClient("11.203.17.174", 9999, "woshigezhiwang");
			simpleChatEventClient.send("哈哈哈哈");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
}
