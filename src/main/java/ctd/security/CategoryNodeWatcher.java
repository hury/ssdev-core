package ctd.security;


import ctd.controller.watcher.ConfigurableWatcher;
import ctd.controller.watcher.WatcherTopics;

public class CategoryNodeWatcher extends ConfigurableWatcher {

	public CategoryNodeWatcher() {
		super(WatcherTopics.SECURITY);
	}

}
