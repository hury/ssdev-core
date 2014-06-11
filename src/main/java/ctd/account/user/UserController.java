package ctd.account.user;

import ctd.account.user.loader.UserLocalLoader;
import ctd.controller.support.AbstractController;

public class UserController extends AbstractController<User> {
	private static UserController instance;
	
	public UserController(){
		setLoader(new UserLocalLoader());
		setNotifier(new UserNotifier());
		instance = this;
	}
	
	public static UserController instance() {
		if(instance == null){
			instance = new UserController();
		}
		return instance;
	}
	
}
