package ctd.account.user;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ctd.account.UserRoleToken;
import ctd.controller.support.AbstractConfigurable;

public class User extends AbstractConfigurable{
	private static final long serialVersionUID = 3175037043404273987L;
	private Map<Integer,UserRoleToken> roles = new ConcurrentHashMap<>();
	private String password;
	private String status;
	private Date createDt;

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean validatePassword(String pwd) {
		return password.equals(pwd);
	}
	
	public void addUserRoleToken(UserRoleToken ur){
		roles.put(ur.getId(),ur);
	}
	
	public void removeUserRoleToken(int id){
		roles.remove(id);
	}
	
	public boolean hasUserRoleToken(UserRoleToken ur){
		return roles.containsValue(ur);
	}
	
	public boolean hasUserRoleToken(Integer urId){
		return roles.containsKey(urId);
	}
	
	public UserRoleToken getUserRoleToken(int urId){
		return roles.get(urId);
	}
	
	public Collection<UserRoleToken> getUserRoleTokens(){
		return roles.values();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
}
