package ctd.dictionary;

import ctd.controller.notifier.ConfigurableNotifier;
import ctd.controller.watcher.WatcherTopics;

public class DictionaryNotifier extends ConfigurableNotifier {

	public DictionaryNotifier() {
		super(WatcherTopics.DICTIONARY);
	}


}
