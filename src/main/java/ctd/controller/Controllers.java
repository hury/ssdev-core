package ctd.controller;

import ctd.account.role.Role;
import ctd.account.role.RoleController;
import ctd.account.tenant.Tenant;
import ctd.account.tenant.TenantController;
import ctd.account.user.User;
import ctd.account.user.UserController;
import ctd.app.Application;
import ctd.app.ApplicationController;
import ctd.controller.exception.ControllerException;
import ctd.dictionary.Dictionary;
import ctd.dictionary.DictionaryController;
import ctd.schema.Schema;
import ctd.schema.SchemaController;
import ctd.security.CategoryNode;
import ctd.security.CategoryNodeController;

public class Controllers {
	
	public static User getUser(String id) throws ControllerException{
		return UserController.instance().get(id);
	}
	
	public static Role getRole(String id) throws ControllerException{
		return RoleController.instance().get(id);
	}
	
	public static Tenant getTenant(String id) throws ControllerException{
		return TenantController.instance().get(id);
	}
	
	public static Schema getSchema(String id) throws ControllerException{
		return SchemaController.instance().get(id);
	}
	
	public static Application getApplication(String id) throws ControllerException{
		return ApplicationController.instance().get(id);
	}
	
	public static Dictionary getDictionary(String id) throws ControllerException{
		return DictionaryController.instance().get(id);
	}
	
	public static CategoryNode getCategoryNode(String id) throws ControllerException{
		return CategoryNodeController.instance().get(id);
	}
}
