package ins.platform.socketIO.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
/**
 * 
 * @author YanZhaoHu
 * @date 2018/10/10
 * 加解密工具类
 */
public class Base64Util {
	private static Base64 base64 = new Base64();
	
	/**
	 * @param content
	 * @return
	 * 加密报文
	 */
	public static String encode(String content) {
		try {
			return base64.encodeToString(content.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
	
	/**
	 * 
	 * @param content
	 * @return
	 * 加密解密
	 */
	public static String decode(String content) {
		try {
			return new String(base64.decode(content),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
}
