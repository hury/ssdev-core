package ctd.config.loader;

import ctd.config.Config;
import ctd.config.ConfigController;
import ctd.controller.ConfigurableLoader;
import ctd.controller.exception.ControllerException;
import ctd.util.annotation.RpcService;

public class ConfigRemoteLoader implements ConfigurableLoader<Config> {

	@Override
	@RpcService
	public Config load(String id) throws ControllerException {
		return ConfigController.instance().get(id);
	}

	

}
