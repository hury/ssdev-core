package ctd.security.updater;

import java.util.List;

import ctd.controller.exception.ControllerException;
import ctd.controller.updater.AbstractConfigurableItemUpdater;
import ctd.controller.watcher.WatcherTopics;
import ctd.security.CategoryNode;
import ctd.security.ResourceNode;

public class CategoryNodeUpdater extends AbstractConfigurableItemUpdater<CategoryNode,ResourceNode>{

	public CategoryNodeUpdater() {
		super(WatcherTopics.SECURITY);
	}

	@Override
	public void create(CategoryNode t) throws ControllerException {
		
	}

	@Override
	protected void processRemoveItems(String id, List<Object> keys) throws ControllerException {
		
	}

	@Override
	protected void processUpdate(CategoryNode t) throws ControllerException {
		
	}

	@Override
	protected void processSetProperty(String id, String nm, Object v) throws ControllerException {
		
	}
	
	

}
