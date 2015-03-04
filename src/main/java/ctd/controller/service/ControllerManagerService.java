package ctd.controller.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ctd.account.role.RoleController;
import ctd.account.user.UserController;
import ctd.app.ApplicationController;
import ctd.controller.Configurable;
import ctd.controller.Controller;
import ctd.dictionary.DictionaryController;
import ctd.schema.SchemaController;
import ctd.security.CategoryNodeController;
import ctd.util.annotation.RpcService;
import ctd.util.converter.ConversionUtils;


public class ControllerManagerService {
	
	private Controller<? extends Configurable> getController(ConfigurableType type){
		Controller<? extends Configurable> c = null;
		switch(type){
		case APPLICATION:
			c = ApplicationController.instance();
			break;
		case CATEGORYNODE:
			c = CategoryNodeController.instance();
			break;
		case DICTIONARY:
			c = DictionaryController.instance();
			break;
		case ROLE:
			c = RoleController.instance();
			break;
		case SCHEMA:
			c = SchemaController.instance();
			break;
		case USER:
			c = UserController.instance();
			break;
		default:
			break;
			
		}
		return c;
	}
	
	@RpcService
	public List<?> getCachedList(ConfigurableType type){
		Controller<? extends Configurable> c = getController(type);
		List<HashMap<String,Object>> rs = new ArrayList<HashMap<String,Object>>();
		if(c != null){
			List<? extends Configurable> instanceLs =  c.getCachedList();
			for(Configurable instance : instanceLs){
				HashMap<String,Object> o = new HashMap<String,Object>();
				o.put("id", instance.getId());
				o.put("name", instance.getName());
				o.put("lastModify", ConversionUtils.convert(instance.getlastModify(),Date.class));
				rs.add(o);
			}
		}
		return rs;
	}
	
	
}
