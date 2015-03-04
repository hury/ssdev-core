package ctd.dictionary.updater;

import java.util.List;

import ctd.controller.exception.ControllerException;
import ctd.controller.updater.AbstractConfigurableItemUpdater;
import ctd.controller.watcher.WatcherTopics;
import ctd.dictionary.Dictionary;
import ctd.dictionary.DictionaryItem;;

public class DictionaryUpdater  extends AbstractConfigurableItemUpdater<Dictionary,DictionaryItem>{

	public DictionaryUpdater() {
		super(WatcherTopics.DICTIONARY);
	}

	@Override
	public void create(Dictionary t) throws ControllerException {
		
	}

	@Override
	protected void processRemoveItems(String id, List<Object> keys) throws ControllerException {
		
	}

	@Override
	protected void processUpdate(Dictionary t) throws ControllerException {
		
	}

	@Override
	protected void processSetProperty(String id, String nm, Object v) throws ControllerException {
		
	}
	

}
