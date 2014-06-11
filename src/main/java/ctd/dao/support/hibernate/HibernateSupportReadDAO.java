package ctd.dao.support.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import ctd.dao.QueryContext;
import ctd.dao.QueryResult;
import ctd.dao.exception.DAOException;
import ctd.dao.support.AbstractReadDAO;
import ctd.dao.support.hibernate.template.AbstractHibernateResultAction;
import ctd.dao.support.hibernate.template.AbstractHibernateStatelessResultAction;
import ctd.dao.support.hibernate.template.HibernateResultAction;
import ctd.dao.support.hibernate.template.HibernateSessionTemplate;
import ctd.dao.support.hibernate.template.HibernateStatelessAction;
import ctd.dao.support.hibernate.template.HibernateStatelessResultAction;
import ctd.schema.Schema;
import ctd.schema.SchemaItem;
import ctd.util.exp.ExpressionProcessor;


public class HibernateSupportReadDAO<T> extends AbstractReadDAO<T> {
	
	
	@Override
	public boolean exist(final Object id) throws DAOException {
		if(id == null){
			throw new DAOException(DAOException.VALUE_NEEDED,"entity[" + schemaId + "] key value must not be null.");
		}
		
		HibernateStatelessResultAction<Long> action = new AbstractHibernateStatelessResultAction<Long>(){

			@Override
			public void execute(StatelessSession ss) throws Exception {
				Schema sc = getSchema();
				String keyName = sc.getKey();
				StringBuilder hql = new StringBuilder("select count(1) from ");
				hql.append(sc.getEntityName()).append(" where ").append(keyName).append("=:").append(keyName);
				Query q = ss.createQuery(hql.toString());
				q.setParameter(keyName, id);
				long totalCount = ((Long)q.iterate().next()).longValue();
				setResult(totalCount);
			}
			
		};
		HibernateSessionTemplate.instance().execute(action);
		
		return action.getResult() > 0;
	
	}

	@Override
	public long getCount(final List<Object> cnds) throws DAOException {
		
		HibernateResultAction<Long> action = new AbstractHibernateResultAction<Long>(){

			@Override
			public void execute(Session ss) throws Exception {
				Schema sc = getSchema();
				StringBuilder hql = new StringBuilder("select count(*) from ");
				hql.append(sc.getEntityName());
				String where = null;
				if(cnds != null){
					where = ExpressionProcessor.instance(getExprLangName()).toString(cnds,true);
				}
				if(where != null){
					hql.append(" where ").append(where);
				}
				
				Query q = ss.createQuery(hql.toString());
				
				if(where != null){
					HqlUtils.setupQueryParams(q);
				}
				long totalCount = ((Long)q.iterate().next()).longValue();
				setResult(totalCount);
			}
			
		};
		HibernateSessionTemplate.instance().execute(action);
		
		return action.getResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public QueryResult<T> processFind(final List<Object> cnds,final QueryContext ctx) throws DAOException {
		
		final QueryResult<T> qr = new QueryResult<T>();
				
		HibernateSessionTemplate.instance().execute(new HibernateStatelessAction() {
			
			@Override
			public void execute(StatelessSession ss) throws Exception {
				int pageNo = ctx.getPageNo();
				int pageSize = ctx.getPageSize();
				int start = (pageNo - 1) * pageSize;
				long totalCount = getCount(cnds);
				
				qr.setStart(start);
				qr.setLimit(pageSize);
				qr.setTotal(totalCount);
				
				if(totalCount == 0){
					return;
				}
				Schema sc = getSchema();
				List<String> fields = ctx.getFields();
				String orderBy = ctx.getOrderBy();
				if(orderBy == null){
					orderBy = sc.getSortInfo();
				}
				
				Query q = HqlUtils.buildSelectQuery(ss, sc.getEntityName(), fields, getExprLangName(), cnds, orderBy);
				
				List<Object[]> rs = q.list();
				List<T> resultRs = new ArrayList<T>((int)totalCount);
				
				qr.setItems(resultRs);
				
				int fieldCount = fields.size();
				for(Object[] r : rs){
					T t = parent.createObject();
					if(t instanceof Map){
						Map<String,Object> map = (Map<String,Object>)t;
						for(int i = 0; i < fieldCount; i ++){
							String nm = fields.get(i);
							Object v = r[i];
							map.put(nm, v);
							SchemaItem it = sc.getItem(nm);
							if(it.isCodedValue()){
								String displayFn = nm + "Text";
								if(!sc.hasItem(displayFn)){
									map.put(nm + "Text", it.toDisplayValue(v));
								}
							}
						}
					}
					else{
						for(int i = 0; i < fieldCount; i ++){
							String nm = fields.get(i);
							Object v = r[i];
							BeanUtils.setProperty(t, nm, v);

							SchemaItem it = sc.getItem(nm);
							if(it.isCodedValue()){
								String displayFn = nm + "Text";
								if(!sc.hasItem(displayFn)){
									BeanUtils.setProperty(t, displayFn, it.toDisplayValue(v));
								}
							}
						}
					}
					resultRs.add(t);
				}
				
			}
		});
		return 	qr;
	}


	@SuppressWarnings("unchecked")
	@Override
	protected T processGet(final Object id) throws DAOException {
		if(id == null){
			throw new DAOException(DAOException.VALUE_NEEDED,"entity[" + schemaId + "] key value must not be null.");
		}
		
		HibernateStatelessResultAction<T> action = new AbstractHibernateStatelessResultAction<T>(){

			@Override
			public void execute(StatelessSession ss) throws Exception {
				Schema sc = getSchema();
				
				T t = (T)ss.get(sc.getEntityName(), (Serializable)id);
				if(t == null){
					return;
				}
				List<SchemaItem> items = sc.getItems();
				for(SchemaItem it : items){
					String nm = it.getId();
					Object v =  BeanUtils.getProperty(t, nm);
					if(it.isCodedValue()){
						String displayFn = nm + "Text";
						if(!sc.hasItem(displayFn)){
							BeanUtils.setProperty(t, displayFn, it.toDisplayValue(v));
						}
					}
				}
				setResult(t);
				
			}
			
		};
		HibernateSessionTemplate.instance().execute(action);
		return action.getResult();
	}

	
}
