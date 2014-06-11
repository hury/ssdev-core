package ctd.dao.support.service;


import java.util.ArrayList;
import java.util.List;
import ctd.account.UserRoleToken;
import ctd.controller.exception.ControllerException;
import ctd.dao.DAO;
import ctd.dao.DAOFactory;
import ctd.dao.DAOService;
import ctd.dao.QueryContext;
import ctd.dao.QueryResult;
import ctd.dao.exception.DAOException;
import ctd.schema.Schema;
import ctd.schema.SchemaController;
import ctd.security.Permission;
import ctd.security.support.condition.FilterCondition;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;
import ctd.util.converter.ConversionUtils;

@SuppressWarnings("unchecked")
public class AbstractDAOService implements DAOService {

	@Override
	public Object save(String daoId,Object o) throws DAOException{
		DAO<Object> dao = (DAO<Object>) DAOFactory.getDao(daoId);
		Schema sc = getSchema(dao.getSchemaId());
		Permission p = sc.lookupPremission();
		if(!p.getMode().isCreatable()){
			throw new DAOException(DAOException.ACCESS_DENIED,"entity[" + sc.getId() + "] can't create for user[" + UserRoleToken.getCurrent().getUserId() + "]@role[" + UserRoleToken.getCurrent().getRoleId() +"]");
		}
		ContextUtils.put(Context.ENTITY_CONTEXT, o);
		return dao.save(convert(dao.getEntityClass(),o));
	}
	
	@Override
	public Object update(String daoId,Object o) throws DAOException{
		DAO<Object> dao = (DAO<Object>) DAOFactory.getDao(daoId);
		Schema sc = getSchema(dao.getSchemaId());
		Permission p = sc.lookupPremission();
		if(!p.getMode().isUpdatable()){
			throw new DAOException(DAOException.ACCESS_DENIED,"entity[" + sc.getId() + "] can't update for user[" + UserRoleToken.getCurrent().getUserId() + "]@role[" + UserRoleToken.getCurrent().getRoleId() +"]");
		}
		ContextUtils.put(Context.ENTITY_CONTEXT, o);
		return dao.update(convert(dao.getEntityClass(),o));
	}
	
	@Override
	public Object saveOrUpdate(String daoId,Object o) throws DAOException{
		DAO<Object> dao = (DAO<Object>) DAOFactory.getDao(daoId);
		Schema sc = getSchema(dao.getSchemaId());
		Permission p = sc.lookupPremission();
		if(!(p.getMode().isUpdatable() && p.getMode().isCreatable())){
			throw new DAOException(DAOException.ACCESS_DENIED,"entity[" + sc.getId() + "] can't saveOrUpdate for user[" + UserRoleToken.getCurrent().getUserId() + "]@role[" + UserRoleToken.getCurrent().getRoleId() +"]");
		}
		ContextUtils.put(Context.ENTITY_CONTEXT, o);
		return dao.saveOrUpdate(convert(dao.getEntityClass(),o));
	}
	
	@Override
	public Object get(String daoId,Object id) throws DAOException{
		DAO<Object> dao = (DAO<Object>) DAOFactory.getDao(daoId);
		Schema sc = getSchema(dao.getSchemaId());
		Permission p = sc.lookupPremission();
		if(!(p.getMode().isAccessible())){
			throw new DAOException(DAOException.ACCESS_DENIED,"entity[" + sc.getId() + "] can't load for user[" + UserRoleToken.getCurrent().getUserId() + "]@role[" + UserRoleToken.getCurrent().getRoleId() +"]");
		}
		return dao.get(id);
	}
	
	@Override
	public void remove(String daoId,Object id) throws DAOException{
		DAO<Object> dao = (DAO<Object>) DAOFactory.getDao(daoId);
		Schema sc = getSchema(dao.getSchemaId());
		Permission p = sc.lookupPremission();
		if(!(p.getMode().isRemovable())){
			throw new DAOException(DAOException.ACCESS_DENIED,"entity[" + sc.getId() + "] can't remove for user[" + UserRoleToken.getCurrent().getUserId() + "]@role[" + UserRoleToken.getCurrent().getRoleId() +"]");
		}
		dao.remove(id);
	}
	
	@Override
	public Object removeAndGet(String daoId,Object id) throws DAOException{
		DAO<Object> dao = (DAO<Object>) DAOFactory.getDao(daoId);
		Schema sc = getSchema(dao.getSchemaId());
		Permission p = sc.lookupPremission();
		if(!(p.getMode().isRemovable())){
			throw new DAOException(DAOException.ACCESS_DENIED,"entity[" + sc.getId() + "] can't remove for user[" + UserRoleToken.getCurrent().getUserId() + "@" + UserRoleToken.getCurrent().getRoleId() +"]");
		}
		return dao.removeAndGet(id);
	}
	
	@Override
	public QueryResult<?> find(String daoId,List<Object> cnds,QueryContext ctx) throws DAOException{
		DAO<Object> dao = (DAO<Object>) DAOFactory.getDao(daoId);
		Schema sc = getSchema(dao.getSchemaId());
		
		Permission p = sc.lookupPremission();
		if(!(p.getMode().isAccessible())){
			throw new DAOException(DAOException.ACCESS_DENIED,"entity[" + sc.getId() + "] can't load for user[" + UserRoleToken.getCurrent().getUserId() + "@" + UserRoleToken.getCurrent().getRoleId() +"]");
		}
		
		List<Object> queryCnd = null;
		List<Object> roleCnd = null;
		
		FilterCondition cd = (FilterCondition)sc.lookupCondition(ctx.getQueryAction());
		if(cd !=null){
			roleCnd = (List<Object>)cd.getDefine();
		}
		
		if(cnds == null || cnds.isEmpty()){
			if(roleCnd != null && !roleCnd.isEmpty()){
				queryCnd = roleCnd;
			}
		}
		else{
			if(roleCnd == null || roleCnd.isEmpty()){
				queryCnd = cnds;
			}
			else{
				if(cnds.get(0).equals("and")){
					cnds.add(roleCnd);
					queryCnd = cnds;
				}
				else{
					queryCnd = new ArrayList<Object>();
					queryCnd.add("and");
					queryCnd.add(cnds);
					queryCnd.add(roleCnd);
				}
			}
		}
		
		return dao.find(queryCnd, ctx);
	}
	
	private Object convert(Class<?> entityClass,Object source) throws DAOException{
		return ConversionUtils.convert(source, entityClass);
	}
	
	public Schema getSchema(String id) throws DAOException{
		try {
			return SchemaController.instance().get(id);
		} 
		catch (ControllerException e) {
			throw new DAOException(e,e.getCode(),e.getMessage());
		}
	}
}
