package ctd.dao;


import java.util.List;
import ctd.dao.exception.DAOException;
import ctd.util.annotation.RpcService;


public interface DAOService {
	
	@RpcService
	public Object save(String daoId,Object o) throws DAOException;
	
	@RpcService
	public Object update(String daoId,Object o) throws DAOException;
	
	@RpcService
	public Object saveOrUpdate(String daoId,Object o) throws DAOException;
	
	@RpcService
	public Object get(String daoId,Object id) throws DAOException;
	
	@RpcService
	public void remove(String daoId,Object id) throws DAOException;
	
	@RpcService
	public Object removeAndGet(String daoId,Object id) throws DAOException;
	
	@RpcService
	public QueryResult<?> find(String daoId,List<Object> cnds,QueryContext ctx) throws DAOException;
	
	
}
