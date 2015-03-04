package ctd.config;

import ctd.config.loader.ConfigLocalLoader;
import ctd.config.updater.ConfigUpdater;
import ctd.controller.exception.ControllerException;
import ctd.controller.support.AbstractController;

public class ConfigController extends AbstractController<Config> {
	private static ConfigController instance = null;
	
	public ConfigController(){
		super();
		setLoader(new ConfigLocalLoader());
		setUpdater(new ConfigUpdater());
	}
	
	public static ConfigController instance(){
		if(instance == null){
			instance = new ConfigController();
		}
		return instance;
	}
	
	public Object getConf(String configId,String itemId){
		try {
			return get(configId).getProperty(itemId);
		} 
		catch (ControllerException e) {
			return null;
		}
	}
	
	public  <T> T getConf(String configId,String itemId,Class<T> clz){
		try { 
			return get(configId).getProperty(itemId,clz);
		} 
		catch (ControllerException e) {
			return null;
		}
	}
}
