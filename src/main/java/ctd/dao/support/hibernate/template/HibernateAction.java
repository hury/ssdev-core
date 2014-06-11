package ctd.dao.support.hibernate.template;

import org.hibernate.Session;


public interface HibernateAction {
	void execute(Session ss) throws Exception;
}
