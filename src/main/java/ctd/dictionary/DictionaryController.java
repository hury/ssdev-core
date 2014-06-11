package ctd.dictionary;

import ctd.controller.exception.ControllerException;
import ctd.controller.support.TenantSupportController;
import ctd.dictionary.loader.DictionaryLocalLoader;

public class DictionaryController extends TenantSupportController<Dictionary> {
	private static DictionaryController instance;
	
	private DictionaryController(){
		setLoader(new DictionaryLocalLoader());
		setNotifier(new DictionaryNotifier());
		instance = this;
	}

	public static DictionaryController instance() {
		if(instance == null){
			instance = new DictionaryController();
		}
		return instance;
	}
	
	public void updateItem(String id,DictionaryItem item) throws ControllerException{
		Dictionary d = get(id);
		try{
			lockManager.writeLock(id);
			d.updateItem(item);
		}
		finally{
			lockManager.writeUnlock(id);
		}
	}
	
	public void removeItem(String id,String key) throws ControllerException{
		Dictionary d = get(id);
		try{
			lockManager.writeLock(id);
			d.removeItem(key);
		}
		finally{
			lockManager.writeUnlock(id);
		}
	}
	
	@Override
	public void reload(String id) {
		if(!isLoaded(id)){
			return;
		}
		try {
			Dictionary dic = get(id);
			if(dic.isReloadable()){
				super.reload(id);
			}
		} 
		catch (ControllerException e) {
			
		}
		
	}
}
