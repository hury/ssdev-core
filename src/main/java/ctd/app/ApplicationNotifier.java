package ctd.app;

import ctd.controller.notifier.ConfigurableNotifier;
import ctd.controller.watcher.WatcherTopics;

public class ApplicationNotifier extends ConfigurableNotifier {

	public ApplicationNotifier() {
		super(WatcherTopics.APPLICATION);
	}


}
