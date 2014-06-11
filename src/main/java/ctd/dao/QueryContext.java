package ctd.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import ctd.security.support.condition.ConditionActionTypes;

public class QueryContext implements Serializable{
	private static final long serialVersionUID = 6555265307558967183L;
	private List<String> fields;
	private int pageNo = 1;
	private int pageSize = 25;
	private String orderBy;
	private String queryAction = ConditionActionTypes.QUERY;
	private Map<String,Object> params;
	
	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public int getPageNo() {
		return pageNo;
	}
	
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getQueryAction() {
		return queryAction;
	}

	public void setQueryAction(String queryAction) {
		this.queryAction = queryAction;
	}

	public Map<String,Object> getParams() {
		return params;
	}

	public void setParams(Map<String,Object> params) {
		this.params = params;
	}
	
	
}
