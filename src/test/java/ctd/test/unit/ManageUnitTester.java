package ctd.test.unit;

import ctd.account.tenant.ManageUnit;
import ctd.account.tenant.Tenant;
import ctd.util.JSONUtils;

public class ManageUnitTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Tenant tenant = new Tenant();
		tenant.setId("root");
		tenant.setName("机构");
		
		ManageUnit unit1 = new ManageUnit();
		unit1.setId("unit1");
		unit1.setName("部门1");
		tenant.appendChild(unit1);
		
		
		ManageUnit unit2 = new ManageUnit();
		unit2.setId("unit2");
		unit2.setName("部门2");
		tenant.appendChild(unit2);
		
		System.out.println(JSONUtils.toString(tenant));

	}

}
