package ctd.schema.updater;

import java.util.List;

import ctd.controller.exception.ControllerException;
import ctd.controller.updater.AbstractConfigurableItemUpdater;
import ctd.controller.watcher.WatcherTopics;
import ctd.schema.Schema;
import ctd.schema.SchemaItem;

public class SchemaUpdater extends AbstractConfigurableItemUpdater<Schema,SchemaItem>{

	public SchemaUpdater() {
		super(WatcherTopics.SCHEMA);
	}

	@Override
	public void create(Schema t) throws ControllerException {
		
	}

	@Override
	protected void processRemoveItems(String id, List<Object> keys) throws ControllerException {
		
	}

	@Override
	protected void processUpdate(Schema t) throws ControllerException {
		
	}

	@Override
	protected void processSetProperty(String id, String nm, Object v) throws ControllerException {
		
	}
	
}
