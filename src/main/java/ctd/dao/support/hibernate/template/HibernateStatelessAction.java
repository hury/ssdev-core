package ctd.dao.support.hibernate.template;

import org.hibernate.StatelessSession;


public interface HibernateStatelessAction {
	void execute(StatelessSession ss) throws Exception;
}
