package ctd.dao.support.hibernate.template;

public interface HibernateStatelessResultAction<T> extends HibernateStatelessAction {
	void setResult(T val);
	T getResult();
}
