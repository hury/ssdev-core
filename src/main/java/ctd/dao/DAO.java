package ctd.dao;

import ctd.dao.exception.DAOException;



public interface  DAO<T> extends ReadDAO<T>,WriteDAO<T>{
	
	public String getId();
	public void setId(String id);
	public String getExprLangName();
	public void setExprLangName(String nm);
	public Class<T> getEntityClass();
	public void setEntityClass(Class<?> clz);
	public T createObject() throws DAOException;
	
}
