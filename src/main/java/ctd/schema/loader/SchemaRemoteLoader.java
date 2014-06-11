package ctd.schema.loader;

import ctd.controller.ConfigurableLoader;
import ctd.controller.exception.ControllerException;
import ctd.schema.Schema;
import ctd.schema.SchemaController;
import ctd.util.annotation.RpcService;

public class SchemaRemoteLoader implements ConfigurableLoader<Schema> {

	@Override
	@RpcService
	public Schema load(String id) throws ControllerException {
		return SchemaController.instance().get(id);
	}

}
