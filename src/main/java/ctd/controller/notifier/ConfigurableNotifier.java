package ctd.controller.notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ctd.account.UserRoleToken;
import ctd.net.broadcast.Publisher;
import ctd.net.broadcast.exception.BroadException;
import ctd.net.broadcast.injvm.InjvmPublisher;


public class ConfigurableNotifier {
	private static final Logger logger = LoggerFactory.getLogger(ConfigurableNotifier.class);
	private Publisher publisher;
	private String topic;
	
	public ConfigurableNotifier(String topic){
		this.topic = topic;
		publisher = InjvmPublisher.instance();
	}
	
	public void setPublisher(Publisher publisher){
		this.publisher = publisher;
	}
	
	public void notifyMessage(NotifierMessage message){
		if(publisher == null){
			logger.warn("publisher not setup.");
			return;
		}
		try {
			publisher.publish(topic,message);
		} 
		catch (BroadException e) {
			logger.error("publisher notify message failed:",e);
		}
	}
	
	public void notifyReload(String id){
		if(id.endsWith("@")){
			id = id + UserRoleToken.getCurrent().getTenantId();
		}
		NotifierMessage message = new NotifierMessage(NotifierCommands.RELOAD,id);
		notifyMessage(message);
	}
	
	public void notifyReloadAll(){
		NotifierMessage message = new NotifierMessage(NotifierCommands.RELOAD_ALL);
		notifyMessage(message);
	}
	
}
