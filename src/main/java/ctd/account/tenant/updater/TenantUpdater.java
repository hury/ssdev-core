package ctd.account.tenant.updater;

import java.util.List;

import ctd.account.tenant.ManageUnit;
import ctd.account.tenant.Tenant;
import ctd.controller.exception.ControllerException;
import ctd.controller.updater.AbstractConfigurableItemUpdater;
import ctd.controller.watcher.WatcherTopics;



public class TenantUpdater extends AbstractConfigurableItemUpdater<Tenant,ManageUnit> {

	public TenantUpdater() {
		super(WatcherTopics.TENANT);
	}

	@Override
	public void create(Tenant t) throws ControllerException {
		
	}
	
	protected void processCreateItem(String id, ManageUnit item) throws ControllerException{
	
	}	

	@Override
	protected void processRemoveItems(String id, List<Object> keys)throws ControllerException {
				
	}

	@Override
	protected void processUpdate(Tenant t) throws ControllerException {
		
	}

	@Override
	protected void processSetProperty(String id, String nm, Object v) throws ControllerException {
		
	}
	
	

}
