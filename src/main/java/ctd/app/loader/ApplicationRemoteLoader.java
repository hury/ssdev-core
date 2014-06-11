package ctd.app.loader;

import ctd.app.Application;
import ctd.app.ApplicationController;
import ctd.controller.ConfigurableLoader;
import ctd.controller.exception.ControllerException;
import ctd.util.annotation.RpcService;

public class ApplicationRemoteLoader implements ConfigurableLoader<Application> {

	@Override
	@RpcService
	public Application load(String id) throws ControllerException {
		return ApplicationController.instance().get(id);
	}

}
