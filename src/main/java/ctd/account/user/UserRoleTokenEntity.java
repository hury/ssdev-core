package ctd.account.user;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ctd.account.AccountCenter;
import ctd.account.UserRoleToken;
import ctd.account.tenant.TenantController;
import ctd.controller.exception.ControllerException;
import ctd.util.converter.ConversionUtils;

@Entity
@Table(name = "BASE_UserRoles")
@Access(AccessType.PROPERTY)
public class UserRoleTokenEntity extends UserRoleToken{
	private static final long serialVersionUID = 258173847713519333L;
	protected Map<String,Object> properties;
	
	private Integer id;
	private String userId;
	private String roleId;
	private String tenantId;
	private String manageUnit;
	private Date lastLoginTime;
	private String lastIPAddress;
	private String lastUserAgent;
	
	@Id
	public Integer getId() {
		return id;
	}
	
	@Transient
	public String getDisplayName(){
		StringBuilder sb = new StringBuilder(getTenantName());
		String manageUnitId = getManageUnit();
		sb.append("-");
		if(!manageUnitId.equals(tenantId)){
			sb.append(getManageUnitName()).append("-");
		}
		sb.append(getRoleName());
		return sb.toString();
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}
	
	@Transient
	public String getUserName(){
		try {
			return AccountCenter.getUser(userId).getName();
		} 
		catch (ControllerException e) {
			return null;
		}
	}
	
	@Transient
	public Integer getUserAvatar(){
		try {
			return AccountCenter.getUser(userId).getAvatarFileId();
		} 
		catch (ControllerException e) {
			return 0;
		}
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Transient
	public String getRoleName(){
		try {
			return AccountCenter.getRole(roleId).getName();
		} 
		catch (ControllerException e) {
			return null;
		}
	}
	
	public void setRoleId(String roleId){
		this.roleId = roleId;
	}
	
	public String getRoleId() {
		return roleId;
	}
	
	public String getTenantId(){
		return tenantId;
	}
	
	public void setTenantId(String tenantId){
		this.tenantId = tenantId;
	}
	
	public String getTenantId(boolean verify) throws ControllerException{
		if(!verify){
			return getTenantId();
		}
		String tenantId = getTenantId();
		TenantController.instance().get(tenantId);
		return tenantId;
	}
	
	@Transient
	public String getTenantName(){
		String tenantId = getTenantId();
		try {
			return TenantController.instance().get(tenantId).getName();
		} 
		catch (ControllerException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("invaild tenantId:" + tenantId);
		}
	}
	
	public void setManageUnit(String manageUnit){
		this.manageUnit = manageUnit;
	}
	
	public String getManageUnit() {
		return manageUnit;
	}
	
	@Transient
	public String getManageUnitName(){
		try {
			return AccountCenter.getManageUnit(manageUnit).getName();
		} 
		catch (ControllerException e) {
			return null;
		}
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastIPAddress() {
		return lastIPAddress;
	}

	public void setLastIPAddress(String lastIPAddress) {
		this.lastIPAddress = lastIPAddress;
	}
	
	public String getLastUserAgent() {
		return lastUserAgent;
	}

	public void setLastUserAgent(String lastUserAgent) {
		this.lastUserAgent = lastUserAgent;
	}

	public void setProperty(String nm,Object v){
		if(properties == null){
			properties = new ConcurrentHashMap<String,Object>();
		}
		properties.put(nm, v);
	}
	
	public Object getProperty(String nm){
		
		return getProperty(nm,false);
	}
	
	
	
	public Object getProperty(String nm,boolean inherit){
		Object val = null;
		if(properties == null){
			return val;
		}
		val = properties.get(nm);
		if(!inherit){
			return val;
		}
		if(val == null){
			try {
				val = AccountCenter.getUser(userId).getProperty(nm);
			} 
			catch (ControllerException e) {}
		}
		if(val == null){
			try {
				val = AccountCenter.getRole(roleId).getProperty(nm);
			} 
			catch (ControllerException e) {}
		}
		if(val == null){
			try {
				val = AccountCenter.getManageUnit(manageUnit).getProperty(nm);
			} 
			catch (ControllerException e) {}
		}
		if(val == null){
			try {
				val = TenantController.instance().get(getTenantId()).getProperty(nm);
			} 
			catch (ControllerException e) {}
		}
		return val;
	}
	
	public <T> T getProperty(String nm,Class<T> targetType){
		return ConversionUtils.convert(getProperty(nm), targetType);
	}
	
	public <T> T getProperty(String nm,Class<T> targetType,boolean inherit){
		return ConversionUtils.convert(getProperty(nm,inherit), targetType);
	}
	
	public int hashCode(){
		return id.hashCode();
	}
	
	public boolean equals(Object o){
		if(o == null || o.getClass().equals(this.getClass())){
			return false;
		}
		if(((UserRoleTokenEntity)o).hashCode() == hashCode()){
			return true;
		}
		return false;
	}

	@Override
	@Transient
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	
}
