package ctd.account.role.loader;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import ctd.account.role.Role;
import ctd.controller.ConfigurableLoader;
import ctd.controller.exception.ControllerException;
import ctd.util.AppContextHolder;

public class RoleLocalLoader implements ConfigurableLoader<Role> {
	
	@Override
	public Role load(String id) throws ControllerException {
		SessionFactory sf = AppContextHolder.getBean(AppContextHolder.SESSION_FACTORY,SessionFactory.class);
		Session ss = null;
		try{
			ss = sf.openSession();
			Role role = (Role) ss.get(Role.class, id);
			role.setLastModify(System.currentTimeMillis());
			return role;
		}
		catch(Exception e){
			throw new ControllerException(e,"load user[" + id + "] falied:"+e.getMessage());
		}
		finally{
			if(ss != null && ss.isOpen()){
				ss.close();
			}
		}
	}

}
