package ctd.dao;

import java.util.List;

import ctd.dao.exception.DAOException;

public interface ReadDAO<T> {
	public void setSchemaId(String schemaId);
	public String getSchemaId();
	public void setExprLangName(String nm);
	public String getExprLangName();
	
	public T get(Object id) throws DAOException;
	public boolean exist(Object id) throws DAOException;
	public long getCount(List<Object> cnds) throws DAOException;
	public QueryResult<T> find(List<Object> cnds,QueryContext ctx) throws DAOException;
}
