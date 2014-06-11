package ctd.app;



import ctd.controller.watcher.ConfigurableWatcher;
import ctd.controller.watcher.WatcherTopics;

public class ApplicationWatcher extends ConfigurableWatcher {

	public ApplicationWatcher() {
		super(WatcherTopics.APPLICATION);
	}

}
