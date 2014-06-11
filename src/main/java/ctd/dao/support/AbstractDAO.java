package ctd.dao.support;


import java.util.List;
import ctd.controller.exception.ControllerException;
import ctd.dao.DAO;
import ctd.dao.QueryContext;
import ctd.dao.QueryResult;
import ctd.dao.exception.DAOException;
import ctd.schema.Schema;
import ctd.schema.SchemaController;
import ctd.util.BeanUtils;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;

public abstract class AbstractDAO<T> implements DAO<T>{
	protected String id;
	protected String schemaId;
	protected String exprLangName;
	protected Class<T> clz;
	
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
		return schemaId == null ? id : schemaId;
	}
	
	@Override
	public void setExprLangName(String nm){
		exprLangName = nm;
	}
	
	@Override
	public String getExprLangName(){
		return exprLangName;
	}
	
	
	protected Context getContext(){
		Context ctx = ContextUtils.getContext(); 
		if(ctx == null){
			ctx = new Context();
			ContextUtils.setContext(ctx);
		}
		return ctx;
	}
	
	public Schema getSchema() throws DAOException{
		try {
			return SchemaController.instance().get(getSchemaId());
		} 
		catch (ControllerException e) {
			throw new DAOException(e,e.getCode(),e.getMessage());
		}
	}
	
	public void batchSave(List<T> ls) throws DAOException{
		for(T o : ls){
			saveOrUpdate(o);
		}
	}
	
	public final T get(Object id) throws DAOException{
		beforeGet(id);
		T t = processGet(id);
		afterGet(id);
		return t;
	}
	protected void beforeGet(Object id) throws DAOException{}
	protected void afterGet(Object id) throws DAOException{}
	protected abstract T processGet(Object id) throws DAOException;
	
	public final T save(T o) throws DAOException{
		beforeSave(o);
		T t = processSave(o);
		afterSave(o);
		return t;
	}
	
	protected void beforeSave(T o) throws DAOException{}
	protected void afterSave(T o) throws DAOException{}
	protected abstract T processSave(T o) throws DAOException;
	
	public final T update(T o) throws DAOException{
		beforeUpdate(o);
		T t = processUpdate(o);
		afterUpdate(o);
		return t;
	}
	
	protected void beforeUpdate(T o) throws DAOException{}
	protected void afterUpdate(T o) throws DAOException{}
	protected abstract T processUpdate(T o) throws DAOException;
	
	public T saveOrUpdate(T o) throws DAOException{
		Schema sc= getSchema();
		
		String keyName = sc.getKey();
		Object key = null;
		try{
			key = BeanUtils.getProperty(o, keyName);
		}
		catch(Exception e){
			throw new DAOException(e);
		}
		if(sc.isAutoKeyGenerator()){
			if(key == null){
				return save(o);
			}
			else{
				return update(o);
			}
		}
		else{
			if(exist(key)){
				return update(o);
			}
			else{
				return save(o);
			}
		}
	}
	
	@Override
	public final void remove(Object id) throws DAOException{
		beforeRemove(id);
		processRemove(id);
		afterRemove(id);
	}
	protected void beforeRemove(Object id) throws DAOException{}
	protected void afterRemove(Object id) throws DAOException{}
	protected abstract void processRemove(Object id) throws DAOException;
	
	public T loadAndRemove(Object id) throws DAOException{
		T o = get(id);
		beforeLoadAndRemove(o);
		processLoadAndRemove(o);
		afterLoadAndRemove(o);
		return o;
	}
	protected void beforeLoadAndRemove(T o) throws DAOException{}
	protected void afterLoadAndRemove(T o) throws DAOException{}
	protected abstract void processLoadAndRemove(T o) throws DAOException;
	
	public final QueryResult<T> find(List<Object> cnds,QueryContext ctx) throws DAOException{
		beforeFind(cnds,ctx);
		QueryResult<T> result  = processFind(cnds,ctx);
		afterFind(cnds,ctx,result);
		return result;
	}
	protected void beforeFind(List<Object> cnds,QueryContext ctx) throws DAOException{}
	protected void afterFind(List<Object> cnds,QueryContext ctx,QueryResult<T> result) throws DAOException{}
	protected abstract QueryResult<T> processFind(List<Object> cnds,QueryContext ctx) throws DAOException;
	
	
	@Override
	public Class<T> getEntityClass() {
		return clz;
	}
	
	@SuppressWarnings("unchecked")
	public void setEntityClass(Class<?> clz){
		this.clz = (Class<T>) clz;
	}
	
	@Override
	public T createObject() throws DAOException {
		try{
			return clz.newInstance();
		}
		catch(Exception e){
			throw new DAOException(e);
		}
	}

}
