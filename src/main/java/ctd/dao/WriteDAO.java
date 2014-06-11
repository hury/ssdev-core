package ctd.dao;

import java.util.HashMap;
import java.util.List;

import ctd.dao.exception.DAOException;

public interface WriteDAO<T> {
	public void setSchemaId(String schemaId);
	public String getSchemaId();
	public void setExprLangName(String nm);
	public String getExprLangName();
	
	public T save(T o) throws DAOException;
	public T update(T o) throws DAOException;
	public T saveOrUpdate(T o) throws DAOException;
	
	public void batchSave(List<T> ls) throws DAOException;
	public int updateByCnds(HashMap<String,Object> map,List<Object> cnds) throws DAOException;
	
	
	
	public void remove(Object id) throws DAOException;
	public T removeAndGet(Object id) throws DAOException;
	public int batchRemove(List<Object> keys) throws DAOException;
	public int removeByCnds(List<Object> cnds) throws DAOException;
}
