package ctd.config.loader;

import org.dom4j.Document;

import ctd.config.Config;
import ctd.controller.exception.ControllerException;
import ctd.controller.support.AbstractConfigurableLoader;

public class ConfigLocalLoader extends AbstractConfigurableLoader<Config> {
	
	public ConfigLocalLoader(){
		postfix = ".conf";
	}
	
	@Override
	public Config createInstanceFormDoc(String id, Document doc, long lastModi) throws ControllerException {
		
		Config conf = new Config();
		conf.setId(id);
		conf.setLastModify(lastModi);
		try{
			setupProperties(conf, doc.getRootElement());
		}
		catch (Exception e){
			throw new ControllerException(e,ControllerException.PARSE_ERROR,"Config[" + id + "] init unknow error:"+ e.getMessage());
		}
		
		return conf;
	}

}
