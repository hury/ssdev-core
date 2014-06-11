package ctd.dao.support;

import java.util.List;
import ctd.controller.exception.ControllerException;
import ctd.dao.DAO;
import ctd.dao.WriteDAO;
import ctd.dao.exception.DAOException;
import ctd.schema.Schema;
import ctd.schema.SchemaController;
import ctd.util.BeanUtils;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;

public abstract class AbstractWriteDAO<T> implements WriteDAO<T>{
	protected String schemaId;
	protected String exprLangName;
	protected DAO<T> parent;

	@Override
	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}
	
	@Override
	public String getSchemaId(){
		if(parent != null){
			return parent.getSchemaId();
		}
		return schemaId;
	}
	
	@Override
	public void setExprLangName(String nm){
		exprLangName = nm;
	}
	
	@Override
	public String getExprLangName(){
		if(parent != null){
			return parent.getExprLangName();
		}
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
		Object keyVal = null;
		try{
			keyVal = BeanUtils.getProperty(o, keyName);
		}
		catch(Exception e){
			throw new DAOException(e);
		}
		if(sc.isAutoKeyGenerator()){
			if(keyVal == null){
				return save(o);
			}
			else{
				return update(o);
			}
		}
		else{
			if(parent.exist(keyVal)){
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
	
	public T removeAndGet(Object id) throws DAOException{
		T o = parent.get(id);
		beforeRemoveAndGet(o);
		processRemoveAndGet(o);
		afterRemoveAndGet(o);
		return o;
	}
	protected void beforeRemoveAndGet(T o) throws DAOException{}
	protected void afterRemoveAndGet(T o) throws DAOException{}
	protected abstract void processRemoveAndGet(T o) throws DAOException;
	
	public void setParent(DAO<T> parent){
		this.parent = parent;
	}
	
}
