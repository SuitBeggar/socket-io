package ins.platform.socketIO.enums.lable;
/**
 * 
 * @author YanZhaoHu
 * @date 2018/10/10 18:01:28
 * 
 * 定义Socket Io常量标签
 */
public enum SocketIoLable {
	
	/**登录情况**/
	ON_CONNECT("connect"),
	/**登出情况**/
	DIS_CONNECT("disconnect"),
	/**全部人员消息通知**/
	NOTICE_EVENT("fullNoticeEvent"),
	/**给一个人的消息通知**/
	NOTICE_EVENT_ONE("separateNoticeEvent"),
	/**客户端两人交流**/
	CHAT_EVENT("chatEvent"),
	/**回调错误消息**/
	ERROR_EVENT("error"),
	/**用户id参数**/
	USER_LABLE("userCode"),
	/** redis缓存所有的用户的代码的key **/
	REDIS_USERS("REDIS_USERS");
	
	private String value;
	
	private SocketIoLable(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
