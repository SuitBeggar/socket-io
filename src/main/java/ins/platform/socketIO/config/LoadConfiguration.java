package ins.platform.socketIO.config;

import java.util.ResourceBundle;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.Configuration;

import ins.platform.socketIO.enums.exception.ExceptionEnum;
import ins.platform.socketIO.util.IPUtil;
/**
 * 
 * @author YanZhaoHu
 * @date 2018/10/10 16:24:23
 * 
 * 配置加载类，如果没有采用默认的配置
 */
public class LoadConfiguration {
	private static Logger logger = LoggerFactory.getLogger(LoadConfiguration.class);
	private static String CONFIG_SUFFIX = ".properties";//定义配置文件后缀名
	private static String SOCKET_PORT = "ws.port";//定义端口号的key
	private static String SOCKET_HOST = "ws.host";//定义网络地址的 key
	private static int DEFALUT_PORT = 9999; //设置默认的端口号
	private static String INITPARAMETER = "socketIOConfigLocation";
	/**
	 * 装载配置
	 * @param sce
	 * @return
	 */
	public static Configuration load(ServletContextEvent sce) {
		String socketIOConfig = (String) sce.getServletContext().getInitParameter(INITPARAMETER);
		Configuration configuration = new Configuration();
		if(socketIOConfig==null) {//加载默认的配置属性
			setDefaultConfig(configuration);
		}else if(socketIOConfig.endsWith(CONFIG_SUFFIX)){//读取配置文件
			socketIOConfig = socketIOConfig.substring(0,socketIOConfig.lastIndexOf(CONFIG_SUFFIX));
			ResourceBundle resourceBundle = ResourceBundle.getBundle(socketIOConfig);
			loadProperties(configuration, resourceBundle);
		}else {
			logger.error(ExceptionEnum.CONFIG.toString());
			throw new RuntimeException(ExceptionEnum.CONFIG.toString());//抛出运行时的异常，配置文件类型错误
		}
		return configuration;
	}
	/**
	 * 加载默认的配置
	 * @param configuration
	 */
	private static void setDefaultConfig(Configuration configuration) {
		configuration.setPort(DEFALUT_PORT);
		configuration.setHostname(checkGetIp());
	}
	private static String checkGetIp() {
		String ip = IPUtil.getServerIp();
		if(ip==null) {
			logger.error(ExceptionEnum.IPNOTFUND.toString());
			throw new RuntimeException(ExceptionEnum.IPNOTFUND.toString());//抛出运行时的异常，ip找不到异常
		}
		return ip;
	}
	/**
	 * 加载配置文件属性
	 * @param configuration
	 * @param resourceBundle
	 */
	private static  void loadProperties(Configuration configuration,ResourceBundle resourceBundle) {
		String portStr = resourceBundle.getString(SOCKET_PORT);
		if(portStr==null) {//为空设置为默认端口号
			configuration.setPort(DEFALUT_PORT);
		}else {
			try {
				int port = Integer.valueOf(portStr);
				configuration.setPort(port);
			}catch (NumberFormatException e) {
				logger.error(ExceptionEnum.PORT.toString());
				throw new RuntimeException(ExceptionEnum.PORT.toString());//抛出运行时的异常，端口号配置非法
			}
		}
		String host = resourceBundle.getString(SOCKET_HOST);
		if(host == null) {//为空设置为默认查找本机IP
			configuration.setHostname(checkGetIp());
		}else if(IPUtil.isIP(host)){
			configuration.setHostname(host);
		}else {
			logger.error(ExceptionEnum.PORT.toString());
			throw new RuntimeException(ExceptionEnum.PORT.toString());//抛出运行时的异常，端口号配置非法
		}
	}
}
