package ctd.dictionary;

import ctd.controller.exception.ControllerException;
import ctd.controller.support.TenantSupportController;
import ctd.dictionary.loader.DictionaryLocalLoader;
import ctd.dictionary.updater.DictionaryUpdater;

public class DictionaryController extends TenantSupportController<Dictionary> {
	private static DictionaryController instance;
	
	private DictionaryController(){
		super();
		setLoader(new DictionaryLocalLoader());
		setUpdater(new DictionaryUpdater());
		if(instance != null){
			this.setInitList(instance.getCachedList());
		}
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
		d.updateItem(item);
	}
	
	public void removeItem(String id,String key) throws ControllerException{
		Dictionary d = get(id);
		d.removeItem(key);
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
				dic.destory();
			}
		} 
		catch (ControllerException e) {
			
		}
		
	}
}
