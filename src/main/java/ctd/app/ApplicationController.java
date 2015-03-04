package ctd.app;

import java.util.List;

import ctd.app.loader.ApplicationLocalLoader;
import ctd.app.updater.ApplicationUpdater;
import ctd.controller.support.AbstractController;

public class ApplicationController extends AbstractController<Application> {
	private static ApplicationController instance;
	
	public ApplicationController(){
		super();
		setLoader(new ApplicationLocalLoader());
		setUpdater(new ApplicationUpdater());

		if(instance != null){
			List<Application> loaded = instance.getCachedList();
			for(Application t : loaded){
				add(t);
			}
		}
		instance = this;
	}
	
	public static ApplicationController instance() {
		if(instance == null){
			instance = new ApplicationController();
		}
		return instance;
	}

}
