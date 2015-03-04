package ctd.account;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import ctd.controller.exception.ControllerException;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;

public abstract class UserRoleToken implements Serializable{
	private static final long serialVersionUID = 6987538198590285584L;

	public abstract Integer getId();
	
	public abstract String getDisplayName();

	public abstract String getUserId();
	
	public abstract String getUserName();
	
	public abstract Integer getUserAvatar();

	public abstract String getRoleName();
	
	public abstract String getRoleId();
	
	public abstract String getTenantId();
	
	public abstract String getTenantId(boolean verify) throws ControllerException;
	
	public abstract String getTenantName();
	
	public abstract String getManageUnit();
	
	public abstract String getManageUnitName();

	public abstract Date getLastLoginTime();

	public abstract String getLastIPAddress();

	public abstract void setLastIPAddress(String lastIPAddress);

	public abstract Object getProperty(String nm);
	
	public abstract Object getProperty(String nm,boolean inherit);
	
	public abstract <T> T getProperty(String nm,Class<T> targetType);
	
	public abstract <T> T getProperty(String nm,Class<T> targetType,boolean inherit);
	
	public abstract Map<String,Object> getProperties();
	
	public static UserRoleToken getCurrent(){
		UserRoleToken ur = ContextUtils.get(Context.USER_ROLE_TOKEN,UserRoleToken.class);
		return ur;
	}
	
}
