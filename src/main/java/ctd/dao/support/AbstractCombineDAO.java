package ctd.dao.support;

import java.util.HashMap;
import java.util.List;

import org.springframework.cglib.core.ReflectUtils;

import ctd.dao.CombineDAO;

import ctd.dao.QueryContext;
import ctd.dao.QueryResult;
import ctd.dao.ReadDAO;
import ctd.dao.WriteDAO;
import ctd.dao.exception.DAOException;


public abstract class AbstractCombineDAO<T> implements CombineDAO<T>{
	
	protected String id;
	protected String schemaId;
	protected String exprLangName;
	protected Class<T> clz;
	
	private AbstractReadDAO<T> readDao;
	private AbstractWriteDAO<T> writeDao;
	
	@Override
	public void setId(String id){
		this.id = id;
	}
	
	@Override
	public String getId(){
		return id;
	}
	
	
	@Override
	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}
	
	@Override
	public String getSchemaId(){
		return schemaId;
	}
	
	@Override
	public void setExprLangName(String nm){
		exprLangName = nm;
	}
	
	@Override
	public String getExprLangName(){
		return exprLangName;
	}
		
	@Override
	public final T get(Object id) throws DAOException{
		return readDao.get(id);
	}

	
	public final T save(T o) throws DAOException{
		return writeDao.save(o);
	}

	
	public final T update(T o) throws DAOException{
		return writeDao.update(o);
	}
	
	public T saveOrUpdate(T o) throws DAOException{
		return writeDao.saveOrUpdate(o);
	}
	
	@Override
	public final void remove(Object id) throws DAOException{
		writeDao.remove(id);
	}

	
	public T removeAndGet(Object id) throws DAOException{
		return writeDao.removeAndGet(id);
	}
	
	public final QueryResult<T> find(List<Object> cnds,QueryContext ctx) throws DAOException{
		return readDao.find(cnds, ctx);
	}

	@Override
	public Class<T> getEntityClass() {
		return clz;
	}
	
	@SuppressWarnings("unchecked")
	public void setEntityClass(Class<?> clz){
		this.clz = (Class<T>) clz;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T createObject() throws DAOException {
		try{
			return (T) ReflectUtils.newInstance(clz);
		}
		catch(Exception e){
			throw new DAOException(e);
		}
	}

	@Override
	public boolean exist(Object id) throws DAOException {
		return readDao.exist(id);
	}

	@Override
	public long getCount(List<Object> cnds) throws DAOException {
		return readDao.getCount(cnds);
	}

	@Override
	public void batchSave(List<T> ls) throws DAOException{
		for(T o : ls){
			writeDao.saveOrUpdate(o);
		}
	}

	@Override
	public int updateByCnds(HashMap<String, Object> map, List<Object> cnds) throws DAOException {
		return writeDao.updateByCnds(map, cnds);
	}

	@Override
	public int batchRemove(List<Object> keys) throws DAOException {
		return writeDao.batchRemove(keys);
	}

	@Override
	public int removeByCnds(List<Object> cnds) throws DAOException {
		return writeDao.removeByCnds(cnds);
	}

	@Override
	public void setReadDAO(ReadDAO<T> dao) {
		this.readDao = (AbstractReadDAO<T>)dao;
		this.readDao.setParent(this);
	}

	@Override
	public void setWriteDAO(WriteDAO<T> dao) {
		this.writeDao = (AbstractWriteDAO<T>)dao;
		this.writeDao.setParent(this);
	}
	
}
