package ins.platform.socketIO.msg;


/**
 * 用户连接的信息，包含客户端信息，为其服务的服务器ip及端口信息，主要用户服务器之间的通信使用
 * @author admin
 */
public class UserConnectMes {
	private String uuidStr ;
	private String serverIpAndPort;
	
	public UserConnectMes(String uuidStr , String serverIpAndPort){
		this.uuidStr = uuidStr ;
		this.serverIpAndPort = serverIpAndPort ;
	}
	public String getServerIpAndPort() {
		return serverIpAndPort;
	}
	public void setServerIpAndPort(String serverIpAndPort) {
		this.serverIpAndPort = serverIpAndPort;
	}
	public String getUuidStr() {
		return uuidStr;
	}
	public void setUuidStr(String uuidStr) {
		this.uuidStr = uuidStr;
	}
}
