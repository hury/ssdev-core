package ctd.account;

import ctd.controller.exception.ControllerException;
import ctd.util.annotation.RpcService;

public interface UserTokenUpdater {
	@RpcService
	void setLastLogon(Integer urt,String ip,String userAgent) throws ControllerException;
	
	@RpcService
	void setProperty(Integer urt,String nm,Object v) throws ControllerException;
}
