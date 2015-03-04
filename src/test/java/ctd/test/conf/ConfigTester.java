package ctd.test.conf;

import ctd.config.Config;
import ctd.config.ConfigController;
import ctd.controller.exception.ControllerException;
import junit.framework.TestCase;

public class ConfigTester extends TestCase {
	
	public void testConfSystem() throws ControllerException{
		Config conf = ConfigController.instance().get("ctd.test.conf.System");
		String s = conf.getString("system.appName");
		int port = conf.getInt("system.rpcserver.port");
		System.out.print(s + ",port=" + port);
	}
	

}
