package ctd.dictionary;

import java.util.List;
import ctd.controller.watcher.ConfigurableWatcher;
import ctd.controller.watcher.WatcherTopics;
import ctd.util.converter.ConversionUtils;

public class DictionaryWatcher extends ConfigurableWatcher {
	
	public DictionaryWatcher(){
		super(WatcherTopics.DICTIONARY);
		setController(DictionaryController.instance());
	}	
	
	@Override
	public void onItemUpdate(String id,List<Object> updatedItems){
		try {
			for(Object o : updatedItems){
				DictionaryItem item = ConversionUtils.convert(o, DictionaryItem.class);
				DictionaryController.instance().updateItem(id, item);
				logger.info("dictionary[{}] item[{}] updated.",id,item.getKey());
			}
			
		} 
		catch (Exception e) {
			logger.error("dictionary[" + id + "] item update failed.",e);
		}
	}

	@Override
	public void onItemCreate(String id,List<Object> createdItems){
		onItemUpdate(id,createdItems);
	}
	
	@Override
	public void onItemRemove(String id,List<Object> keys){
		try {
			for(Object o : keys){
				String key = (String)o;
				DictionaryController.instance().removeItem(id, key);
				logger.info("dictionary[{}] item[{}] removed",id,key);
			}
		} 
		catch (Exception e) {
			logger.error("dictionary[" + id + "] item remove failed.",e);
		}
	}
}
