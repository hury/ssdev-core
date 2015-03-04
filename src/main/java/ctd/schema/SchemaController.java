package ctd.schema;

import ctd.controller.exception.ControllerException;
import ctd.controller.support.AbstractController;
import ctd.schema.loader.SchemaLocalLoader;
import ctd.schema.updater.SchemaUpdater;


public class SchemaController extends AbstractController<Schema> {
	private static SchemaController instance;
	
	public SchemaController(){
		super();
		setLoader(new SchemaLocalLoader());
		setUpdater(new SchemaUpdater());

		if(instance != null){
			this.setInitList(instance.getCachedList());
		}
		instance = this;
	}
	
	public static SchemaController instance() {
		if(instance == null){
			instance = new SchemaController();
		}
		return instance;
	}
	
	public void updateItem(String id,SchemaItem item) throws ControllerException{
		Schema sc = get(id);
		sc.addItem(item);
	}
	
	public void removeItem(String id,String itemId) throws ControllerException{
		Schema sc = get(id);
		sc.removeItem(itemId);
	}
}
