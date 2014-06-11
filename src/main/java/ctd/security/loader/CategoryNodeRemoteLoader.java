package ctd.security.loader;

import ctd.controller.ConfigurableLoader;
import ctd.controller.exception.ControllerException;
import ctd.security.CategoryNode;
import ctd.security.CategoryNodeController;
import ctd.util.annotation.RpcService;

public class CategoryNodeRemoteLoader implements ConfigurableLoader<CategoryNode> {

	@Override
	@RpcService
	public CategoryNode load(String id) throws ControllerException {
		return  CategoryNodeController.instance().get(id);
	}

}
