package ctd.account.role;

import ctd.account.role.loader.RoleLocalLoader;
import ctd.controller.support.AbstractController;

public class RoleController extends AbstractController<Role> {
	private static RoleController instance;
	
	public RoleController(){
		setLoader(new RoleLocalLoader());
		setNotifier(new RoleNotifier());
		instance = this;
	}
	
	public static RoleController instance() {
		if(instance == null){
			instance = new RoleController();
		}
		return instance;
	}

}
