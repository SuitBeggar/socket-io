package ins.platform.socketIO.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 
 * @author YanZhaoHu
 * @date 2018/10/10 16:44:15
 * IP操作工具类
 */
public class IPUtil {
	/**
	 * 查找主机的IP地址
	 * @return
	 */
	public static String getServerIp(){
        String SERVER_IP = null;
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni =  netInterfaces.nextElement();
                if(ni.getInetAddresses().hasMoreElements()) {
                	ip = ni.getInetAddresses().nextElement();
	                SERVER_IP = ip.getHostAddress();
	                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
	                        && ip.getHostAddress().indexOf(":") == -1) {
	                    SERVER_IP = ip.getHostAddress();
	                    break;
	                } else {
	                    ip = null;
	                }
                }
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
        return SERVER_IP;
    }
	/**
	 * 检查是否为IPV4
	 * @param addr
	 * @return
	 */
	public static boolean isIP(String addr)  
    {  
        if(addr.length() < 7 || addr.length() > 15 || "".equals(addr))  
        {  
            return false;  
        }  
        /** 
         * 判断IP格式和范围 
         */  
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";  
          
        Pattern pat = Pattern.compile(rexp);    
          
        Matcher mat = pat.matcher(addr);    
          
        boolean ipAddress = mat.find();  

        return ipAddress;  
    }    
}
