package ins.platform.socketIO.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {
	
	private static Map<String , Properties> allProperties = new HashMap<>();
	
	public static String getPropertiesVal(String fileName , String key){
		Properties properties = null;
		if (allProperties.get(fileName) != null) {
			properties = allProperties.get(fileName);
		}else{
			properties = new Properties();
		    // 使用ClassLoader加载properties配置文件生成对应的输入流
		    InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName+".properties");
		    // 使用properties对象加载输入流
		    try {
				properties.load(in);
				allProperties.put(fileName, properties);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	    //获取key对应的value值
	    return properties.getProperty(key);
	}
}
