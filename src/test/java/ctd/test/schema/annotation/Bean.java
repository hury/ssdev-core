package ctd.test.schema.annotation;

import ctd.schema.annotation.Expression;

public class Bean {
	private String name;
	private int age;
	
	@Expression("['$','server.date.year']")
	private Integer lastModify;
	
	public void setLastModify(int l){
		this.lastModify = l;
	}
}
