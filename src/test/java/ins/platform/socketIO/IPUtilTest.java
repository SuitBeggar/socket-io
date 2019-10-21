package ins.platform.socketIO;

import org.junit.Test;

import ins.platform.socketIO.util.IPUtil;

/**
 * 
 * @author YanZhaoHu
 * @date 2018/10/10 16:46:36
 * 测试类
 */
public class IPUtilTest {
	
	@Test
	public void testGetServerIp() {
	 System.out.println(IPUtil.getServerIp());
	}
}
