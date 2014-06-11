package ctd.security;

import ctd.controller.notifier.ConfigurableNotifier;
import ctd.controller.watcher.WatcherTopics;

public class CategoryNodeNotifier extends ConfigurableNotifier {

	public CategoryNodeNotifier() {
		super(WatcherTopics.SECURITY);
	}


}
