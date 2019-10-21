package ins.platform.socketIO.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtil {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String getCurrentDataTime(){
		return sdf.format(new Date());
	}
}
