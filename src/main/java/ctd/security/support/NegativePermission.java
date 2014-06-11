package ctd.security.support;

import ctd.security.Mode;
import ctd.security.Permission;

public class NegativePermission  extends Permission{
	private static final long serialVersionUID = 4430496464665912990L;

	public NegativePermission(){
		setMode(Mode.NoneAccessMode);
	}
}
