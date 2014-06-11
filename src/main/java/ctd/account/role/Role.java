package ctd.account.role;

import ctd.controller.support.AbstractConfigurable;

public class Role extends AbstractConfigurable {
	private static final long serialVersionUID = -2219302553517602005L;
	private String desc;
	
	public String getDescription() {
		return desc;
	}
	
	public void setDescription(String desc) {
		this.desc = desc;
	}
}
