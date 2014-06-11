package ctd.app;

import ctd.app.loader.ApplicationLocalLoader;
import ctd.controller.support.AbstractController;

public class ApplicationController extends AbstractController<Application> {
	private static ApplicationController instance;
	
	public ApplicationController(){
		setLoader(new ApplicationLocalLoader());
		setNotifier(new ApplicationNotifier());
		instance = this;
	}
	
	public static ApplicationController instance() {
		if(instance == null){
			instance = new ApplicationController();
		}
		return instance;
	}

}
