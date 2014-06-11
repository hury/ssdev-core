package ctd.dao.support;

import java.util.ArrayList;
import java.util.List;
import ctd.controller.exception.ControllerException;
import ctd.dao.DAO;
import ctd.dao.QueryContext;
import ctd.dao.QueryResult;
import ctd.dao.ReadDAO;
import ctd.dao.exception.DAOException;
import ctd.schema.Schema;
import ctd.schema.SchemaController;
import ctd.schema.SchemaItem;
import ctd.schema.constants.DisplayModes;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;

public abstract class AbstractReadDAO<T> implements ReadDAO<T>{

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
	
	public final T get(Object id) throws DAOException{
		beforeGet(id);
		T t = processGet(id);
		afterGet(id);
		return t;
	}
	protected void beforeGet(Object id) throws DAOException{}
	protected void afterGet(Object id) throws DAOException{}
	protected abstract T processGet(Object id) throws DAOException;
	
	protected List<String> getFields() throws DAOException{
		Schema sc = getSchema();
		List<String> fields = new ArrayList<String>();
		List<SchemaItem> items = sc.getItems();
		for(SchemaItem it : items){
		
			if(it.getDisplayMode() == DisplayModes.NO_LIST_DATA){
				continue;
			}
			fields.add(it.getId());
		}
		return fields;
	}
	
	public final QueryResult<T> find(List<Object> cnds,QueryContext ctx) throws DAOException{
		beforeFind(cnds,ctx);
		if(ctx.getFields() == null){
			ctx.setFields(getFields());
		}
		QueryResult<T> result  = processFind(cnds,ctx);
		afterFind(cnds,ctx,result);
		return result;
	}
	protected void beforeFind(List<Object> cnds,QueryContext ctx) throws DAOException{}
	protected void afterFind(List<Object> cnds,QueryContext ctx,QueryResult<T> result) throws DAOException{}
	protected abstract QueryResult<T> processFind(List<Object> cnds,QueryContext ctx) throws DAOException;
	
	public void setParent(DAO<T> parent){
		this.parent = parent;
	}

}
