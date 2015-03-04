package ctd.account.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ctd.account.UserRoleToken;
import ctd.controller.support.AbstractConfigurable;

@Entity
@Table(name = "BASE_User")
@Access(AccessType.PROPERTY)
public class User extends AbstractConfigurable{
	private static final long serialVersionUID = 3175037043404273987L;
	private Map<Integer,UserRoleToken> roles = new ConcurrentHashMap<>();
	private Integer avatarFileId;
	private String password;
	private String status;
	private String email;
	private Date createDt;
	
	@Id
	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Integer getAvatarFileId() {
		return avatarFileId;
	}

	public void setAvatarFileId(Integer avatarFileId) {
		this.avatarFileId = avatarFileId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean validatePassword(String pwd) {
		return password.equals(pwd);
	}
	
	public void addUserRoleToken(UserRoleToken ur){
		roles.put(ur.getId(),ur);
	}
	
	public void addUserRoleTokens(List<UserRoleToken> ls){
		for(UserRoleToken ur : ls){
			roles.put(ur.getId(),ur);
		}
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
	
	public List<UserRoleToken> findUserRoleTokenByRoleId(String roleId){
		Collection<UserRoleToken> tokens = roles.values();
		List<UserRoleToken> rs = new ArrayList<UserRoleToken>();
		for(UserRoleToken ur : tokens){
			if(ur.getRoleId().equals(roleId)){
				rs.add(ur);
			}
		}
		return rs;
	}
	
	@Transient
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
