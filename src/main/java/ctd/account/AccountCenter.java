package ctd.account;


import ctd.account.role.Role;
import ctd.account.role.RoleController;
import ctd.account.tenant.ManageUnit;
import ctd.account.tenant.TenantController;
import ctd.account.user.User;
import ctd.account.user.UserController;
import ctd.controller.exception.ControllerException;

public class AccountCenter { 
	public static User getUser(String id) throws ControllerException{
		return UserController.instance().get(id);
	}
	
	public static Role getRole(String id) throws ControllerException{
		return RoleController.instance().get(id);
	}
	
	public static ManageUnit getManageUnit(String id) throws ControllerException{
		return TenantController.lookupManageUnit(id);
		
	}
}
