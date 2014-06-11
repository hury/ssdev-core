package ctd.test.app;

import ctd.account.UserRoleToken;
import ctd.account.user.UserRoleTokenEntity;
import ctd.app.Application;
import ctd.app.loader.ApplicationLocalLoader;
import ctd.controller.exception.ControllerException;
import ctd.security.Mode;
import ctd.security.Permission;
import ctd.security.Repository;
import ctd.security.ResourceNode;
import ctd.util.JSONUtils;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;

public class AppLoaderTester {

	/**
	 * @param args
	 * @throws ControllerException 
	 */
	public static void main(String[] args) throws ControllerException {
		UserRoleTokenEntity ur = new UserRoleTokenEntity();
		ur.setId(123);
		ur.setRoleId("system");
		ContextUtils.put(Context.USER_ROLE_TOKEN, ur);
		//ContextUtils.put(Context.REQUEST_APPNODE_DEEP, 1);
		
		ResourceNode r =  Repository.getNode();
		Permission p = new Permission();
		p.setPrincipal("system");
		p.setMode(Mode.parseFromInt(1));
		r.addPermission(p);
		
		ResourceNode appNode = new ResourceNode("chis");
		r.appendChild(appNode);
		
		
		ApplicationLocalLoader loader = new ApplicationLocalLoader();
		Application app = loader.load("ctd.test.app.chis");
		
		System.out.println(JSONUtils.toString(app));
	}

}
