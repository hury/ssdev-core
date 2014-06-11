package ctd.test.schema;


import ctd.schema.Schema;
import ctd.schema.SchemaController;
import ctd.util.BeanUtils;


public class SchemaItemBeanTester {
	public static void main(String[] args) throws Exception{

		Schema sc = SchemaController.instance().get("ctd.test.schema.SystemUser");
		System.out.println(sc.getItem("userId").isPkey());
		
		System.out.println(BeanUtils.getProperty(sc, "getItem('userId')"));
		
	}
}
