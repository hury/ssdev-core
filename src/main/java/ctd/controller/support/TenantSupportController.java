package ctd.controller.support;

import org.apache.commons.lang3.StringUtils;

import ctd.account.UserRoleToken;
import ctd.account.tenant.Tenant;
import ctd.controller.Configurable;
import ctd.controller.exception.ControllerException;

public abstract class TenantSupportController<T extends Configurable> extends AbstractController<T> {
	
	@Override
	protected String parseId(String id){
		if(StringUtils.endsWith(id, Tenant.PLACEHOLDER)){
			try {
				id = id + UserRoleToken.getCurrent().getTenantId(true);
			} 
			catch (ControllerException e) {
				throw new IllegalArgumentException("instance ["+id+"] has invaid tenantId");
			}
		}
		return id;
	}
	
}
