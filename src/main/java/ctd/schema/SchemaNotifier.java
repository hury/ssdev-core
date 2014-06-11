package ctd.schema;

import ctd.controller.notifier.ConfigurableNotifier;
import ctd.controller.watcher.WatcherTopics;

public class SchemaNotifier extends ConfigurableNotifier {

	public SchemaNotifier() {
		super(WatcherTopics.DICTIONARY);
	}


}
