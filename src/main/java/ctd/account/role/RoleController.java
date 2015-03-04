package ctd.account.role;

import ctd.controller.support.AbstractController;

public class RoleController extends AbstractController<Role> {
	private static RoleController instance;
	
	
	public RoleController(){
		super();
		
		if(instance != null){
			this.setInitList(instance.getCachedList());
		}
		instance = this;
		
	}
	
	public static RoleController instance() {
		if(instance == null){
			instance = new RoleController();
		}
		return instance;	
	}

}
