package ctd.controller.updater;

import java.util.List;

import ctd.controller.Configurable;
import ctd.controller.exception.ControllerException;
import ctd.controller.notifier.NotifierCommands;
import ctd.controller.notifier.NotifierMessage;

public abstract class AbstractConfigurableItemUpdater<T extends Configurable,I> extends AbstractConfigurableUpdater<T> implements ConfigurableItemUpdater<T,I> {
	
	public AbstractConfigurableItemUpdater(String topic) {
		super(topic);
	}

	
	
	@Override
	public void createItem(String id,I item) throws ControllerException{
		processCreateItem(id,item);
		notifyCreateItem(id,item);
	}

	protected void processCreateItem(String id, I item) throws ControllerException{
		
	};

	protected void notifyCreateItem(String id, I item) throws ControllerException{
		NotifierMessage msg = new NotifierMessage(NotifierCommands.ITEM_CREATE,id);
		msg.setLastModify(getLastModify(id));
		msg.addUpdatedItems(item);
		notifyMessage(msg);
	}

	@Override
	public void updateItem(String id,I item) throws ControllerException{
		processUpdateItem(id,item);
		notifyUpdateItem(id,item);
	}

	protected void processUpdateItem(String id, I item) throws ControllerException{
		
	}
	
	protected void notifyUpdateItem(String id, I item) throws ControllerException{
		NotifierMessage msg = new NotifierMessage(NotifierCommands.ITEM_UPDATE,id);
		msg.addUpdatedItems(item);
		msg.setLastModify(getLastModify(id));
		notifyMessage(msg);
	}

	@Override
	public void removeItems(String id,List<Object> keys) throws ControllerException{
		processRemoveItems(id,keys);
		notifyRemoveItems(id,keys);
	}

	protected abstract void processRemoveItems(String id, List<Object> keys) throws ControllerException;

	private void notifyRemoveItems(String id, List<Object> keys) throws ControllerException{

		NotifierMessage msg = new NotifierMessage(NotifierCommands.ITEM_REMOVE,id);
		msg.addUpdatedItems(keys);
		msg.setLastModify(getLastModify(id));
		notifyMessage(msg);
	}

}
