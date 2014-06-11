package ctd.test.organ;

import ctd.account.UserRoleToken;
import ctd.account.tenant.Tenant;
import ctd.account.tenant.loader.TenantLocalLoader;
import ctd.account.user.UserRoleTokenEntity;
import ctd.controller.exception.ControllerException;
import ctd.security.Mode;
import ctd.security.Permission;
import ctd.security.Repository;
import ctd.security.ResourceNode;
import ctd.util.JSONUtils;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;

public class OrganTester {

	/**
	 * @param args
	 * @throws ControllerException 
	 */
	public static void main(String[] args) throws ControllerException {
		UserRoleTokenEntity ur = new UserRoleTokenEntity();
		ur.setId(123);
		ur.setRoleId("system");
		ContextUtils.put(Context.USER_ROLE_TOKEN, ur);
		ContextUtils.put(Context.REQUEST_APPNODE_DEEP, 0);
		//ContextUtils.put(Context.REQUEST_UNIT_DEEP, 1);
		
		ResourceNode r =  Repository.getNode();
		Permission p = new Permission();
		p.setPrincipal("system");
		p.setMode(Mode.parseFromInt(1));
		r.addPermission(p);
		
		TenantLocalLoader loader = new TenantLocalLoader();
		Tenant tenant = loader.load("ctd.test.organ.organization");
		//System.out.println(JSONUtils.toString(organ));
		System.out.println(JSONUtils.toString(tenant.findAuthorizedApps()));
	}

}
