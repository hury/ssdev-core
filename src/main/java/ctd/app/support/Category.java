package ctd.app.support;

import java.util.List;

import ctd.app.ApplicationNode;

public class Category extends ApplicationNode {
	private static final long serialVersionUID = 6735979041711571147L;
	
	public List<Module> getModules(){
		if(deep >= getRequestDeep()){
			return null;
		}
		return getAuthorizedItems();
	}
	
}
