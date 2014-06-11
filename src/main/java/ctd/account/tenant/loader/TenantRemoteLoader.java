package ctd.account.tenant.loader;

import ctd.account.tenant.Tenant;
import ctd.account.tenant.TenantController;
import ctd.controller.ConfigurableLoader;
import ctd.controller.exception.ControllerException;
import ctd.util.annotation.RpcService;

public class TenantRemoteLoader implements ConfigurableLoader<Tenant> {

	@Override
	@RpcService
	public Tenant load(String id) throws ControllerException {
		return TenantController.instance().get(id);
	}

}
