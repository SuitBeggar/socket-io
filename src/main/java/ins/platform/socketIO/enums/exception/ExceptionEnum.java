package ins.platform.socketIO.enums.exception;
/**
 * 
 * @author YanZhaoHu
 * @date 2018/10/10 16:29:26
 * 
 * 自定义异常消息，code - describe
 */
public enum ExceptionEnum {
	CONFIG(90001,"socketIO Configuration file type error"),
	IPNOTFUND(90002,"socketIO Configuration init error,IP can't find it"), 
	IPILLEGAL(90003,"socketIO Configuration init error,The IP in configuration is not illegal"),
	PORT(90004,"socketIO Configuration init error,The PORT in configuration is not numeric"),
	NOUSER(90005,"There is no message recipient"),
	USERNOONLINE(90006,"Users are not online"),
	NOSESSIONID(90007,"not sessionId"),
	NULLUSER(90008,"userCode is null,disconnect...");
	private int code;
	
	private String describe;
	
	private ExceptionEnum(int code,String describe) {
		this.code = code;
		this.describe = describe;
	}

	public int getCode() {
		return code;
	}

	public String getDescribe() {
		return describe;
	}
	
	@Override
	public String toString() {
		return "{\"code\":"+code+",\"describe\":"+describe+"}";
	}

}
