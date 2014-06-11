package ctd.account.user.loader;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.StatelessSession;

import ctd.account.UserRoleToken;
import ctd.account.user.User;
import ctd.controller.ConfigurableLoader;
import ctd.controller.exception.ControllerException;
import ctd.dao.exception.DAOException;
import ctd.dao.support.hibernate.template.AbstractHibernateStatelessResultAction;
import ctd.dao.support.hibernate.template.HibernateSessionTemplate;
import ctd.dao.support.hibernate.template.HibernateStatelessResultAction;

public class UserLocalLoader implements ConfigurableLoader<User> {
	private static final String UserRolesQueryHQL = "from UserRoleTokenEntity where userId = :id";
	
	@SuppressWarnings("unchecked")
	@Override
	public User load(final String id) throws ControllerException {
		
		
		HibernateStatelessResultAction<User> action = new AbstractHibernateStatelessResultAction<User>(){

			@Override
			public void execute(StatelessSession ss) throws Exception {
				User user = (User) ss.get(User.class, id);
				if(user == null){
					throw new ControllerException(ControllerException.INSTANCE_NOT_FOUND,"user[" + id + "] not found.");
				}
				
				Query q = ss.createQuery(UserRolesQueryHQL);
				q.setString("id", id);
				
				List<UserRoleToken> urs = q.list();
				for(UserRoleToken ur :urs){
					user.addUserRoleToken(ur);
				}
				user.setLastModify(System.currentTimeMillis());
				afterUserLoad(user);
				setResult(user);
			}
			
		};
		
		try{
			HibernateSessionTemplate.instance().execute(action);
			return action.getResult();
		}
		catch(DAOException e){
			throw new ControllerException(e,"load user[" + id + "] falied:"+e.getMessage());
		}
	}

	protected void afterUserLoad(User user) {
		
	}

}
