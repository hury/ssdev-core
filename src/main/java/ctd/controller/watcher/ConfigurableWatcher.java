package ctd.controller.watcher;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ctd.controller.Configurable;
import ctd.controller.Controller;
import ctd.controller.exception.ControllerException;
import ctd.controller.notifier.NotifierCommands;
import ctd.controller.notifier.NotifierMessage;
import ctd.net.broadcast.Observer;
import ctd.net.broadcast.Subscriber;
import ctd.net.broadcast.exception.BroadException;
import ctd.net.broadcast.injvm.InjvmSubscriber;


public abstract class ConfigurableWatcher{
	protected static final Logger logger = LoggerFactory.getLogger(ConfigurableWatcher.class);
	protected Controller<?> controller;
	protected Subscriber subscriber; 
	protected String topic;
	
	public ConfigurableWatcher(String topic){
		this.topic = topic;
		setSubscriber(InjvmSubscriber.instance());
	}
	
	public void setController(Controller<?> controller){
		this.controller = controller;
	}
	
	public void setSubscriber(Subscriber subscriber){
		this.subscriber = subscriber;
		try {
			subscriber.attach(topic, new Observer() {
				@Override
				public void onMessage(Serializable... arg) {
					onNotify((NotifierMessage) arg[0]);
				}
			});
		} 
		catch (BroadException e) {
			throw new IllegalStateException(e);
		}
	}

	
	public void onNotify(NotifierMessage msg){
		int cmd = msg.getCommand();
		String id = msg.getInstanceId();
		
		if(controller == null){
			logger.error("controller not setup.");
			return;
		}
		
		if(!controller.isLoaded(id)){
			return;
		}
		Configurable c = null;
		try {
			c = controller.get(id);
			if(msg.getLastModify() > 0 && c.getlastModify() >= msg.getLastModify()){
				logger.info("instance[" + id + "] ignored notifier message for the lastModify value[" + c.getlastModify() + "]");
				return;
			}
			
		} 
		catch (ControllerException e) {
			logger.error("load instance[" + id + "] failed.",e);
			return;
		}
		
		switch(cmd){
			case NotifierCommands.RELOAD:
				onReload(id);
				break;
				
			case NotifierCommands.RELOAD_ALL:
				onReloadAll();
				break;
				
			case NotifierCommands.PROPERTIES_UPDATE:
				Map<String,Object> updatedProperties = msg.getUpdatedProperties();
				onPropertiesUpdate(id,updatedProperties);
				break;
				
			case NotifierCommands.ITEM_UPDATE:
				List<Object> updatedItems = msg.getUpdatedItems();
				onItemUpdate(id,updatedItems);
				break;
				
			case NotifierCommands.ITEM_CREATE:
				List<Object> createdItems = msg.getUpdatedItems();
				onItemCreate(id,createdItems);
				break;
			
			case NotifierCommands.ITEM_REMOVE:
				List<Object> keys = msg.getUpdatedItems();
				onItemRemove(id,keys);
				break;
		}
		if(cmd != NotifierCommands.RELOAD && cmd != NotifierCommands.RELOAD_ALL){
			c.setLastModify(msg.getLastModify());
		}
	}

	public void onReload(String id){
		if(controller != null){
			controller.reload(id);
			logger.info("controller[" + controller.getClass().getSimpleName() + "] instance[" + id + "] reloaded");
		}
	}
	
	public void onReloadAll(){
		if(controller != null){
			controller.reloadAll();
			logger.info("controller[" + controller.getClass().getSimpleName() + "] reload all");
		}
	}
	
	public void onPropertiesUpdate(String id,Map<String, Object> updatedProperties) {
		try {
			if(updatedProperties == null){
				return;
			}
			Configurable c = controller.get(id);
			Set<String> keys = updatedProperties.keySet();
			for(String k : keys){
				c.setProperty(k, updatedProperties.get(k));
			}
			logger.info("instance[" + id + "] properties updated");
		} 
		catch (Exception e) {
			logger.error("instance[" + id + "] properties update failed:",e);
		}
	}
	
	public void onItemRemove(String id, List<Object> keys) {
		
	}
	
	public void onItemUpdate(String id, List<Object> updatedItems) {
		
	}

	public void onItemCreate(String id, List<Object> createdItems) {
		
	}
	
}
