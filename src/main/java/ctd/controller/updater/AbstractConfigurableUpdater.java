package ctd.controller.updater;

import ctd.controller.Configurable;
import ctd.controller.exception.ControllerException;
import ctd.controller.notifier.NotifierCommands;
import ctd.controller.notifier.NotifierMessage;
import ctd.net.broadcast.Publisher;
import ctd.net.broadcast.exception.BroadException;
import ctd.net.broadcast.injvm.InjvmPublisher;

public abstract class AbstractConfigurableUpdater<T extends Configurable> implements ConfigurableUpdater<T> {

	private Publisher publisher;
	private String topic;
	
	public AbstractConfigurableUpdater(String topic){
		this.topic = topic;
		publisher = InjvmPublisher.instance();
	}
	
	public void setPublisher(Publisher publisher){
		this.publisher = publisher;
	}
	
	@Override
	public void notifyMessage(NotifierMessage message) throws ControllerException{
		if(publisher == null){
			throw new ControllerException("publisher not setup for topic[" + topic + "].");
		}
		try {
			publisher.publish(topic,message);
		} 
		catch (BroadException e) {
			throw new ControllerException(e,"publisher notify message for topic[" + topic +"] failed.");
		}
	}
	
	protected long getLastModify(String id) throws ControllerException{
		return 0l;
	}
	
	@Override
	public final void update(T t) throws ControllerException{
		processUpdate(t);
		notifyUpdate(t);
	}
	
	protected abstract void processUpdate(T t) throws ControllerException;

	protected void notifyUpdate(T t) throws ControllerException{
		NotifierMessage msg = new NotifierMessage(NotifierCommands.RELOAD,t.getId());
		notifyMessage(msg);
	}
	
	@Override
	public void setProperty(String id,String nm, Object v) throws ControllerException{
		processSetProperty(id,nm,v);
		notifySetProperty(id,nm,v);
	}
	
	protected abstract void processSetProperty(String id,String nm, Object v) throws ControllerException;
	
	protected void notifySetProperty(String id,String nm, Object v) throws ControllerException{
		NotifierMessage msg = new NotifierMessage(NotifierCommands.PROPERTIES_UPDATE,id);
		msg.addUpdatedProperty(nm, v);
		msg.setLastModify(getLastModify(id));
		notifyMessage(msg);
	}

}
