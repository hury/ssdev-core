package ctd.dao.support.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Query;
import org.hibernate.StatelessSession;
import ctd.dao.exception.DAOException;
import ctd.dao.support.AbstractWriteDAO;
import ctd.dao.support.hibernate.template.AbstractHibernateStatelessResultAction;
import ctd.dao.support.hibernate.template.HibernateSessionTemplate;
import ctd.dao.support.hibernate.template.HibernateStatelessAction;
import ctd.dao.support.hibernate.template.HibernateStatelessResultAction;
import ctd.schema.Schema;
import ctd.schema.SchemaItem;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;
import ctd.util.exp.ExpressionContextBean;
import ctd.util.exp.ExpressionProcessor;
import ctd.util.exp.exception.ExprException;

public class HibernateSupportWriteDAO<T> extends AbstractWriteDAO<T> {
	
	
	@Override
	public T processSave(final T o) throws DAOException {
		
		if(o == null){
			throw new DAOException(DAOException.VALUE_NEEDED,"entity[" + schemaId + "] can not save null object");
		}
		
		HibernateSessionTemplate.instance().execute(new HibernateStatelessAction() {
			
			@Override
			public void execute(StatelessSession ss) throws Exception {
				Schema sc = getSchema();
				String keyName = sc.getKey();
				Serializable key = (Serializable) BeanUtils.getProperty(o, keyName);

				if(!sc.isAutoKeyGenerator() && key == null && !sc.getItem(keyName).isEvalValue()){
					throw new DAOException(DAOException.VALUE_NEEDED,"entity[" + schemaId + "] key value must not be null.");
				}
			
				ContextUtils.put(Context.ENTITY_CONTEXT, o);
				List<SchemaItem> items = sc.getItems();
				for(SchemaItem it : items){
					String id = it.getId();
					Object v = null;
					if(it.isEvalValue()){
						v = it.eval();
					}
					else{
						v = BeanUtils.getProperty(o,id);
					}
					Boolean isRequired = it.isRequired();
					if(v == null && (isRequired != null && isRequired == true)){
						throw new DAOException(DAOException.VALUE_NEEDED,"entity[" + schemaId + "]item[" + id +"] is required.");
					}
					BeanUtils.setProperty(o, id, it.toPersistValue(v));
				}
				
				Object keyValue = ss.insert(sc.getEntityName(), o);
				if(sc.isAutoKeyGenerator()){
					BeanUtils.setProperty(o,keyName,keyValue);
				}
			}
		});
		return o;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T processUpdate(final T o) throws DAOException {
		
		HibernateStatelessResultAction<T> action = new AbstractHibernateStatelessResultAction<T>(){
			@Override
			public void execute(StatelessSession ss) throws Exception {
				Schema sc = getSchema();
				String keyName = sc.getKey();
				Serializable key = (Serializable) BeanUtils.getProperty(o, keyName);
				if(key == null){
					throw new DAOException(DAOException.VALUE_NEEDED,"entity[" + schemaId + "] key value must not be null for update.");
				}
				String entityName = sc.getEntityName();
				T org = (T)ss.get(entityName, key);
				if(org == null){
					throw new DAOException(DAOException.ENTITIY_NOT_FOUND,"entity[" + schemaId + "][" + key+ "] not exist");
				}
				ContextUtils.put(Context.ENTITY_CONTEXT, org);
				List<SchemaItem> items = sc.getItems();
				for(SchemaItem it : items){
					if(!it.isUpdatable()){
						continue;
					}
					String id = it.getId();
					Object v = null;
					if(it.isEvalValue()){
						try {
							v = it.eval();
							
						} 
						catch (ExprException e) {
							throw new DAOException(e);
						}
					}
					else{
						v = BeanUtils.getProperty(o, id);
					}
					BeanUtils.setProperty(org, id, it.toPersistValue(v));
				}
				ss.update(org);
				setResult(org);
			}
		};
		
		HibernateSessionTemplate.instance().execute(action);
		
		return action.getResult();
	}
	
	
	@Override
	public int updateByCnds(HashMap<String, Object> map,List<Object> cnds) throws DAOException {
		return 0;
	}



	@Override
	public void processRemove(final Object id) throws DAOException {
		if(id == null){
			throw new DAOException(DAOException.VALUE_NEEDED,"entity[" + schemaId + "] key value must not be null.");
		}
		
		HibernateSessionTemplate.instance().execute(new HibernateStatelessAction() {

			@Override
			public void execute(StatelessSession ss) throws Exception {
				Schema sc = getSchema();
				String keyName = sc.getKey();
				StringBuilder hql = new StringBuilder("delete from ");
				hql.append(sc.getEntityName()).append(" where ").append(keyName).append("=:").append(keyName);
				Query q = ss.createQuery(hql.toString());
				q.setParameter(keyName, id);
				q.executeUpdate();
			}
			
		});
		
	}

	@Override
	public int batchRemove(List<Object> keys) throws DAOException {
		List<Object> cnds = new ArrayList<Object>();
		cnds.add("in");
		cnds.add(keys);
		return removeByCnds(cnds);
	}

	@Override
	public int removeByCnds(final List<Object> cnds) throws DAOException {
		if(cnds == null || cnds.isEmpty()){
			throw new IllegalStateException("cnds is empty.");
		}
		
		HibernateStatelessResultAction<Integer> action = new AbstractHibernateStatelessResultAction<Integer>(){

			@Override
			public void execute(StatelessSession ss) throws Exception {
				Schema sc = getSchema();
				
				ExpressionContextBean bean = null;
				if(ContextUtils.hasKey(Context.EXP_BEAN)){
					bean = ContextUtils.get(Context.EXP_BEAN, ExpressionContextBean.class);
					bean.clearPatameters();
				}
				else{
					bean = new ExpressionContextBean();
					ContextUtils.put(Context.EXP_BEAN, bean);
				}
				bean.setForPreparedStatement(true);
				String where = ExpressionProcessor.instance(getExprLangName()).toString(cnds);
				StringBuffer hql = new StringBuffer("delete from ");
				hql.append(sc.getEntityName()).append(" where ").append(where);
				Query q = ss.createQuery(hql.toString());
				Map<String,Object> parameters =  bean.getStatementParameters();
				Collection<String> names = parameters.keySet();
				for(String nm : names){
					q.setParameter(nm, parameters.get(nm));
				}
				setResult(q.executeUpdate());
			}
			
		};
		HibernateSessionTemplate.instance().execute(action);
		
		return action.getResult();
	}

	@Override
	protected void processRemoveAndGet(final T o) throws DAOException {
		if(o == null){
			return;
		}
		
		HibernateSessionTemplate.instance().execute(new HibernateStatelessAction() {
			@Override
			public void execute(StatelessSession ss) throws Exception {
				Schema sc = getSchema();
				ss.delete(sc.getEntityName(), o);
			}
			
		});
	}
	
}
