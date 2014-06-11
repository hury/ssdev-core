package ctd.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class QueryResult<T> implements Serializable{
	private static final long serialVersionUID = 6195741402452890231L;
	private long totalCount;
	private int start;
	private int limit;
	private List<T> records;
	
	private HashMap<String,Object> properties;
	
	public QueryResult(){};
	
	public QueryResult(int start,int limit){
		this.start = start;
		this.limit = limit;
	}
	
	public QueryResult(long totalCount,int start,int limit,List<T> records){
		this.totalCount = totalCount;
		this.start = start;
		this.limit = limit;
		this.records = records;
	}
	
	public void setTotal(long totalCount){
		this.totalCount = totalCount;
	}

	public long getTotal(){
		return totalCount;
	}
	
	public long getStart(){
		return start;
	}
	
	public long getLimit(){
		return limit;
	}
	
	
	public void setStart(int start) {
		this.start = start;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setItems(List<T> records) {
		this.records = records;
	}
	
	public List<T> getItems(){
		return records;
	}
	
	public void setProperty(String nm,Object v){
		if(properties == null){
			properties = new HashMap<String,Object>();
		}
		properties.put(nm, v);
	}
	
	public Object getProperty(String nm){
		if(properties == null){
			return null;
		}
		return properties.get(nm);
	}
	
	public HashMap<String,Object> getProperties(){
		return properties;
	}
	
}
