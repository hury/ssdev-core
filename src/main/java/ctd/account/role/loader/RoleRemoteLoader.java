package ctd.account.role.loader;

import ctd.account.role.Role;
import ctd.account.role.RoleController;
import ctd.controller.ConfigurableLoader;
import ctd.controller.exception.ControllerException;
import ctd.util.annotation.RpcService;

public class RoleRemoteLoader implements ConfigurableLoader<Role> {

	@Override
	@RpcService
	public Role load(String id) throws ControllerException {
		return RoleController.instance().get(id);
	}

}
