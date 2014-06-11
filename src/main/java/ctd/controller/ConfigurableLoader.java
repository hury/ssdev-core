package ctd.controller;

import ctd.controller.exception.ControllerException;
import ctd.util.annotation.RpcService;

public interface ConfigurableLoader<T extends Configurable> {
	@RpcService
	public T load(String id) throws ControllerException;
}
