package ctd.account.user;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import ctd.controller.support.AbstractController;

public class UserController extends AbstractController<User> {
	private static UserController instance;
	private static final int EXPIRES_TIME = 3;
	
	
	public UserController(){
		super();
		if(instance != null){
			this.setInitList(instance.getCachedList());
		}
		instance = this;
	}
	
	@Override
	protected void createCacheStore(){
		store = CacheBuilder.newBuilder().expireAfterAccess(EXPIRES_TIME, TimeUnit.HOURS).build(createCacheLoader());
	}
	
	public static UserController instance() {
		if(instance == null){
			instance = new UserController();
		}
		return instance;
	}
	
}
