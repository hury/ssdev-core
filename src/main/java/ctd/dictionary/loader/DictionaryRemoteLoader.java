package ctd.dictionary.loader;

import ctd.controller.ConfigurableLoader;
import ctd.controller.exception.ControllerException;
import ctd.dictionary.Dictionary;
import ctd.dictionary.DictionaryController;
import ctd.util.annotation.RpcService;

public class DictionaryRemoteLoader implements ConfigurableLoader<Dictionary> {

	@Override
	@RpcService
	public Dictionary load(String id) throws ControllerException {
		return DictionaryController.instance().get(id);
	}

}
