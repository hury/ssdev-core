package ctd.schema;

import ctd.controller.exception.ControllerException;
import ctd.controller.support.AbstractController;
import ctd.schema.loader.SchemaLocalLoader;


public class SchemaController extends AbstractController<Schema> {
	private static SchemaController instance;
	
	public SchemaController(){
		setLoader(new SchemaLocalLoader());
		setNotifier(new SchemaNotifier());
		instance = this;
	}
	
	public static SchemaController instance() {
		if(instance == null){
			instance = new SchemaController();
		}
		return instance;
	}
	
	public void updateItem(String id,SchemaItem item) throws ControllerException{
		
		try{
			Schema sc = get(id);
			lockManager.writeLock(id);
			sc.addItem(item);
		}
		finally{
			lockManager.writeUnlock(id);
		}
	}
	
	public void removeItem(String id,String itemId) throws ControllerException{
		try{
			Schema sc = get(id);
			lockManager.writeLock(id);
			sc.removeItem(itemId);
		}
		finally{
			lockManager.writeUnlock(id);
		}
	}
}
