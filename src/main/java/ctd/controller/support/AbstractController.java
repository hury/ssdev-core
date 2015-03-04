package ctd.controller.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import ctd.controller.Configurable;
import ctd.controller.ConfigurableLoader;
import ctd.controller.Controller;
import ctd.controller.exception.ControllerException;
import ctd.controller.updater.ConfigurableUpdater;

public abstract class AbstractController<T extends Configurable> implements Controller<T> {
	protected LoadingCache<String, T> store;
	protected ConfigurableLoader<T> loader;
	protected ConfigurableUpdater<T> updater;
	
	public AbstractController(){
		createCacheStore();
	}
	
	protected void createCacheStore(){
		store = CacheBuilder.newBuilder().build(createCacheLoader());
	}
	
	protected CacheLoader<String,T> createCacheLoader(){
		return new CacheLoader<String,T>(){
			@Override
			public T load(String id) throws Exception {
				T t = loader.load(id);
				if(!t.isInited()){
					t.init();
				}
				return t;
			}
		};
	}
	
	@Override
	public void setLoader(ConfigurableLoader<T> loader){
		this.loader = loader;
	}
	
	@Override
	public ConfigurableLoader<T> getLoader(){
		return loader;
	}
	
	@Override
	public ConfigurableUpdater<T> getUpdater(){
		return updater;
	}
	
	@Override
	public void setUpdater(ConfigurableUpdater<T> updater){
		this.updater = updater;
	}
	
	@Override
	public boolean isLoaded(String id){
		id = parseId(id);
		return store.asMap().containsKey(id);
	}
	
	@Override
	public void reload(String id) {
		id = parseId(id);
		store.invalidate(id);
	}
	
	@Override
	public void reloadAll() {
		Set<String> keys = store.asMap().keySet();
		for(String id : keys){
			reload(id);
		}
	}
	
	protected String parseId(String id) {
		return id;
	}
	
	@Override
	public T get(String id) throws ControllerException {
		
		try {
			id = parseId(id);
			T t = store.get(id);
			return t;
		} 
		catch (ExecutionException e) {
			Throwable cause = e.getCause(); 
			if(cause instanceof ControllerException){
				throw (ControllerException)cause;
			}
			else{
				throw new ControllerException(cause);
			}
		}
		
	}
	
	@Override
	public void add(T t){
		store.put(t.getId(), t);
	}
	
	@Override
	public List<T> getCachedList(){
		List<T> ls = new ArrayList<T>();
		ls.addAll(store.asMap().values());
		return ls;
	}
	
	public void add(String id,T t){
		store.put(id, t);
	}
	
	public void setInitList(List<T> ls){
		for(T t : ls){
			add(t);
		}
	}

}
