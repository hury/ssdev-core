package ctd.security;

import ctd.controller.support.AbstractController;
import ctd.security.loader.CategoryNodeLocalLoader;
import ctd.security.updater.CategoryNodeUpdater;

public class CategoryNodeController extends AbstractController<CategoryNode> {
	private static CategoryNodeController instance;
	
	public CategoryNodeController(){
		super();
		setLoader(new CategoryNodeLocalLoader());
		setUpdater(new CategoryNodeUpdater());
		if(instance != null){
			this.setInitList(instance.getCachedList());
		}
		instance = this;
	}
	
	public static CategoryNodeController instance(){
		if(instance == null){
			instance = new CategoryNodeController();
		}
		return instance;
	}
	
	
}
