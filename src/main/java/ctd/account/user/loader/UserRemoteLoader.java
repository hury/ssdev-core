package ctd.account.user.loader;

import ctd.account.user.User;
import ctd.account.user.UserController;
import ctd.controller.ConfigurableLoader;
import ctd.controller.exception.ControllerException;
import ctd.util.annotation.RpcService;

public class UserRemoteLoader implements ConfigurableLoader<User> {

	@Override
	@RpcService
	public User load(String id) throws ControllerException {
		return UserController.instance().get(id);
	}

}
