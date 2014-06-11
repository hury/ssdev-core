package ctd.schema;

import java.util.List;
import java.util.Map;

import ctd.controller.exception.ControllerException;
import ctd.controller.watcher.ConfigurableWatcher;
import ctd.controller.watcher.WatcherTopics;
import ctd.util.converter.ConversionUtils;

public class SchemaWatcher extends ConfigurableWatcher {
	
	public SchemaWatcher() {
		super(WatcherTopics.SCHEMA);
	}

	@Override
	public void onPropertiesUpdate(String id,Map<String, Object> updatedProperties) {
		if(controller == null || !controller.isLoaded(id)){
			return;
		}
		try {
			Schema schema = (Schema) controller.get(id);
			
			if(updatedProperties.containsKey("alias")){
				schema.setAlias((String)updatedProperties.get("alias"));
			}
			if(updatedProperties.containsKey("entityName")){
				schema.setEntityName((String)updatedProperties.get("entityName"));
			}
			if(updatedProperties.containsKey("sortInfo")){
				schema.setSortInfo((String)updatedProperties.get("sortInfo"));
			}
		}
		catch (ControllerException e) {
		
		}
		super.onPropertiesUpdate(id, updatedProperties);
	}
	
	
	@Override
	public void onItemUpdate(String id,List<Object> updatedItems){
		if(controller == null || !controller.isLoaded(id)){
			return;
		}
		try {
			for(Object o : updatedItems){
				SchemaItem  item = ConversionUtils.convert(o, SchemaItem.class);
				SchemaController.instance().updateItem(id, item);
			}
		}
		catch (ControllerException e) {
		
		}
	}
	
	@Override
	public void onItemRemove(String id,List<Object> keys){
		if(controller == null || !controller.isLoaded(id)){
			return;
		}
		try {
			for(Object o : keys){
				String itemId = (String) o;
				SchemaController.instance().removeItem(id, itemId);
			}
		}
		catch (ControllerException e) {
		
		}
	}
}
