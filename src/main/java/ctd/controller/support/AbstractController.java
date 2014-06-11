package ctd.controller.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ctd.controller.Configurable;
import ctd.controller.ConfigurableLoader;
import ctd.controller.Controller;
import ctd.controller.exception.ControllerException;
import ctd.controller.notifier.ConfigurableNotifier;
import ctd.util.lock.KeyEntityRWLockManager;

public abstract class AbstractController<T extends Configurable> implements Controller<T> {
	protected final HashMap<String, T> store = new HashMap<String, T>();
	protected ConfigurableLoader<T> loader;
	protected ConfigurableNotifier notifier;
	protected final KeyEntityRWLockManager lockManager = new KeyEntityRWLockManager();
	
	@Override
	public void setLoader(ConfigurableLoader<T> loader){
		this.loader = loader;
	}
	
	@Override
	public ConfigurableLoader<T> getLoader(){
		return loader;
	}
	
	public void setNotifier(ConfigurableNotifier notifier){
		this.notifier = notifier;
	}
	
	public ConfigurableNotifier getNotifier(){
		return notifier;
	}
	
	@Override
	public boolean isLoaded(String id){
		id = parseId(id);
		
		boolean isLocked = lockManager.tryReadLock(id);
		try{
			return store.containsKey(id);
		}
		finally{
			if(isLocked){
				lockManager.readUnlock(id);
			}
		}
	}
	
	@Override
	public void reload(String id) {
		id = parseId(id);
		lockManager.writeLock(id);
		store.remove(id);
		lockManager.writeUnlock(id);
	}
	
	@Override
	public void reloadAll() {
		String[] keys = store.keySet().toArray(new String[store.size()]);
		for(String id : keys){
			reload(id);
		}
	}
	
	protected String parseId(String id) {
		return id;
	}
	
	@Override
	public T get(String id) throws ControllerException {
		id = parseId(id);
		boolean isLocked = lockManager.tryReadLock(id);
		T t = null;
		try{
			if(store.containsKey(id)){
				t = store.get(id);
			}
			if(t != null && t.isInited()){
				return t;
			}
		}
		finally{
			if(isLocked){
				lockManager.readUnlock(id);
			}
		}
		
		lockManager.writeLock(id);
			try{
				if(t == null){
					if(store.containsKey(id)){
						t = store.get(id);
					}
					else{
						t = loader.load(id);
						store.put(id, t);
					}
				}
				if(!t.isInited()){
					t.init();
				}
				return t;
			}
			finally{
				lockManager.writeUnlock(id);
			}
	}
	
	@Override
	public void add(T t){
		lockManager.writeLock(t.getId());
		store.put(t.getId(), t);
		lockManager.writeUnlock(t.getId());
	}
	
	@Override
	public List<T> getCachedList(){
		List<T> ls = new ArrayList<T>();
		ls.addAll(store.values());
		return ls;
	}
	
	public void add(String id,T t){
		lockManager.writeLock(t.getId());
		store.put(id, t);
		lockManager.writeUnlock(t.getId());
	}
	
	public void setInitList(List<T> ls){
		for(T t : ls){
			add(t);
		}
	}

}
