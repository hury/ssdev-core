package ctd.dao.support.hibernate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import ctd.util.context.Context;
import ctd.util.context.ContextUtils;
import ctd.util.exp.ExpressionContextBean;
import ctd.util.exp.ExpressionProcessor;


public class HqlUtils {
	
	public static Map<String,Object> getExpressionParams(){
		ExpressionContextBean bean = (ExpressionContextBean)ContextUtils.get(Context.EXP_BEAN);
		if(bean == null){
			throw new IllegalStateException("ExpressionContextBean not setup alreary.");
		}
		return  bean.getStatementParameters();
	}

	public static void setupQueryParams(Query q){
		Map<String,Object> parameters = getExpressionParams();
		if(!parameters.isEmpty()){
			Collection<String> names = parameters.keySet();
			for(String nm : names){
				q.setParameter(nm, parameters.get(nm));
			}
		}
	}
	
	public static Query buildSelectQuery(Session ss,String entityName,List<String> fields,String exprLangName,List<?> cnds,String orderBy) throws Exception{
		StringBuilder hql = new StringBuilder("select ");
		hql.append(StringUtils.join(fields.toArray(), ',')).append(" from ").append(entityName);
		String where = null;
		if(cnds != null && !cnds.isEmpty()){
			where = ExpressionProcessor.instance(exprLangName).toString(cnds,true);
		}
		if(where != null){
			hql.append(" where ").append(where);
		}
		if(!StringUtils.isEmpty(orderBy)){
			hql.append(" order by ").append(orderBy);
		}
		Query q = ss.createQuery(hql.toString());
		if(where != null){
			setupQueryParams(q);
		}
		return q;
	}
	
	public static Query buildSelectQuery(StatelessSession ss,String entityName,List<String> fields,String exprLangName,List<?> cnds,String orderBy) throws Exception{
		StringBuilder hql = new StringBuilder("select ");
		hql.append(StringUtils.join(fields.toArray(), ',')).append(" from ").append(entityName);
		String where = null;
		if(cnds != null && !cnds.isEmpty()){
			where = ExpressionProcessor.instance(exprLangName).toString(cnds,true);
		}
		if(where != null){
			hql.append(" where ").append(where);
		}
		if(!StringUtils.isEmpty(orderBy)){
			hql.append(" order by ").append(orderBy);
		}
		Query q = ss.createQuery(hql.toString());
		if(where != null){
			setupQueryParams(q);
		}
		return q;
	}

}
