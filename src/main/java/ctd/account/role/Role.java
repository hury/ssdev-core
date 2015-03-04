package ctd.account.role;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ctd.controller.support.AbstractConfigurable;

@Entity
@Table(name = "BASE_Role")
@Access(AccessType.PROPERTY)
public class Role extends AbstractConfigurable {
	private static final long serialVersionUID = -2219302553517602005L;
	private String desc;
	
	@Id
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return desc;
	}
	
	public void setDescription(String desc) {
		this.desc = desc;
	}
}
