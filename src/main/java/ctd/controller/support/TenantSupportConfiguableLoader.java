package ctd.controller.support;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import ctd.account.tenant.Tenant;
import ctd.account.tenant.TenantController;
import ctd.account.tenant.TenantHomeUtils;
import ctd.controller.Configurable;
import ctd.controller.exception.ControllerException;
import ctd.resource.ResourceCenter;
import ctd.util.xml.XMLHelper;

public abstract class TenantSupportConfiguableLoader<T extends Configurable> extends AbstractConfigurableLoader<T> {
	
	@Override
	protected String getPathFromId(String id) throws ControllerException{
		if(StringUtils.contains(id, Tenant.PLACEHOLDER)){
			String tenantId = StringUtils.substringAfter(id, Tenant.PLACEHOLDER);
			String home = TenantController.instance().get(tenantId).getProfileHome();
			id = StringUtils.substringBefore(id, Tenant.PLACEHOLDER);
			return home + super.getPathFromId(id);
		}
		else{
			return super.getPathFromId(id);
		}
	}	
	
	@Override
	protected T loadFromPath(String id,String path) throws ControllerException {
		
		if(StringUtils.contains(id, Tenant.PLACEHOLDER)){
			Resource r = ResourceCenter.loadWithoutExistsCheck(ResourceUtils.CLASSPATH_URL_PREFIX,path);
			
			if(!r.exists()){
				String orginalId = StringUtils.substringBefore(id, Tenant.PLACEHOLDER);
				String orginalPath = super.getPathFromId(orginalId);
				
				Resource original = ResourceCenter.loadWithoutExistsCheck(ResourceUtils.CLASSPATH_URL_PREFIX,orginalPath);
				
				if(original.exists()){
					try {
						String parentDirectory = StringUtils.substringBeforeLast(orginalPath, "/");
						String tenantId = StringUtils.substringAfterLast(id, Tenant.PLACEHOLDER);
						String filename = original.getFilename();
						TenantHomeUtils.copyFile(tenantId, parentDirectory, filename, ResourceCenter.getInputStream(original));
					} 
					catch (Exception e) {
						throw new ControllerException(e,ControllerException.IO_ERROR,"copy file from[" + orginalPath + "] io error.");
					} 
				}//if(original.exists())
				else{
					throw new ControllerException(ControllerException.INSTANCE_NOT_FOUND,"file[" + path + "] not found.");
				}
			}//if(!r.exists)
			
			try{
				Document doc = XMLHelper.getDocument(ResourceCenter.getInputStream(r));
				return createInstanceFormDoc(id,doc,r.lastModified());
			}
			catch (IOException e) {
				throw new ControllerException(e,ControllerException.IO_ERROR,"load file[" + path + "] io error.");
			} 
			catch (DocumentException e) {
				throw new ControllerException(e,ControllerException.PARSE_ERROR,"load file[" + path + "] parse document error.");
			}
			
		}
		else{
			return super.loadFromPath(id, path);
		}
	}
	
}
