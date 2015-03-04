package ctd.app.updater;

import java.util.List;

import ctd.app.Application;
import ctd.app.ApplicationNode;
import ctd.controller.exception.ControllerException;
import ctd.controller.updater.AbstractConfigurableItemUpdater;
import ctd.controller.watcher.WatcherTopics;

public class ApplicationUpdater extends AbstractConfigurableItemUpdater<Application,ApplicationNode>{

	public ApplicationUpdater() {
		super(WatcherTopics.APPLICATION);
	}

	@Override
	public void create(Application t) throws ControllerException {
		
	}

	@Override
	protected void processRemoveItems(String id, List<Object> keys) throws ControllerException {
		
	}

	@Override
	protected void processUpdate(Application t) throws ControllerException {

	}

	@Override
	protected void processSetProperty(String id, String nm, Object v) throws ControllerException {
		
	}
	
	

}
