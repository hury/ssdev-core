package ctd.security;

import ctd.controller.support.AbstractController;
import ctd.security.loader.CategoryNodeLocalLoader;

public class CategoryNodeController extends AbstractController<CategoryNode> {
	private static CategoryNodeController instance;
	
	public CategoryNodeController(){
		setLoader(new CategoryNodeLocalLoader());
		setNotifier(new CategoryNodeNotifier());
		instance = this;
	}
	
	public static CategoryNodeController instance(){
		if(instance == null){
			instance = new CategoryNodeController();
		}
		return instance;
	}
	
	
}
