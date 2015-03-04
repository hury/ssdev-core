package ctd.account.tenant;

import org.apache.commons.lang3.StringUtils;

import ctd.account.tenant.loader.TenantLocalLoader;
import ctd.account.tenant.updater.TenantUpdater;
import ctd.controller.exception.ControllerException;
import ctd.controller.support.TenantSupportController;

public class TenantController extends TenantSupportController<Tenant> {
	
	private static TenantController instance;
	
	public TenantController(){
		super();
		setLoader(new TenantLocalLoader());
		setUpdater(new TenantUpdater());
		
		if(instance != null){
			this.setInitList(instance.getCachedList());
		}
		instance = this;	
	}
	
	public static TenantController instance() {
		if(instance == null){
			instance = new TenantController();
		}
		return instance;
	}
	
	public static ManageUnit lookupManageUnit(String id) throws ControllerException{
		ManageUnit target = null;
		if(StringUtils.contains(id, Tenant.SEPARATOR)){
			String tenantId = StringUtils.substringBefore(id, Tenant.SEPARATOR);
			Tenant tenant =  TenantController.instance().get(tenantId);
			target = tenant.lookup(id);
		}
		else{
			target = TenantController.instance().get(id);
		}
		return target;
	}

}
