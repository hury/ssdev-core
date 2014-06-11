package ctd.account.user;


import java.util.List;
import java.util.Map;

import ctd.account.UserRoleToken;
import ctd.controller.exception.ControllerException;
import ctd.controller.watcher.ConfigurableWatcher;
import ctd.controller.watcher.WatcherTopics;
import ctd.util.converter.ConversionUtils;

public class UserWatcher extends ConfigurableWatcher {

	public UserWatcher() {
		super(WatcherTopics.USER);
	}

	@Override
	public void onPropertiesUpdate(String id,Map<String, Object> updatedProperties) {
		if(controller == null || !controller.isLoaded(id)){
			return;
		}
		try {
			User user = (User)controller.get(id);
			if(updatedProperties.containsKey("password")){
				user.setPassword((String)updatedProperties.get("password"));
			}
			if(updatedProperties.containsKey("name")){
				user.setName((String)updatedProperties.get("name"));
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
			User user = (User)controller.get(id);
			for(Object o : updatedItems){
				UserRoleTokenEntity  ur = ConversionUtils.convert(o,UserRoleTokenEntity.class);
				user.addUserRoleToken(ur);
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
			User user = (User)controller.get(id);
			for(Object o : keys){
				int itemId = (Integer)o;
				user.removeUserRoleToken(itemId);
			}	
		} 
		catch (ControllerException e) {
			
		}
	}

}
