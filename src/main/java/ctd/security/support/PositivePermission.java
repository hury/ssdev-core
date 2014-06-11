package ctd.security.support;

import ctd.security.Mode;
import ctd.security.Permission;

public class PositivePermission  extends Permission{
	private static final long serialVersionUID = 5806063940172661713L;

	public PositivePermission(){
		setMode(Mode.FullAccessMode);
	}
}
