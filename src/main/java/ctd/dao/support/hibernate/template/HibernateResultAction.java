package ctd.dao.support.hibernate.template;

public interface HibernateResultAction<T> extends HibernateAction {
	void setResult(T val);
	T getResult();
}
