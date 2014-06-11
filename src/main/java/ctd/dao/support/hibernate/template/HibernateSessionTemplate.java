package ctd.dao.support.hibernate.template;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import ctd.dao.exception.DAOException;
import ctd.util.AppContextHolder;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;
import ctd.util.exp.exception.ExprException;

public class HibernateSessionTemplate {
	private SessionFactory factory;
	private static HibernateSessionTemplate instance;
	
	public HibernateSessionTemplate(){
		instance = this;
	}
	
	public static HibernateSessionTemplate instance(){
		if(instance == null){
			instance = new HibernateSessionTemplate();
			instance.setFactory(AppContextHolder.getBean(AppContextHolder.SESSION_FACTORY,SessionFactory.class));
		}
		return instance;
	}
	
	public void execute(HibernateAction action) throws DAOException{
		Session ss = null;
		boolean isContainerSession = false;
		try{
			if(ContextUtils.hasKey(Context.DB_SESSION)){
				ss = ContextUtils.get(Context.DB_SESSION,Session.class);
				isContainerSession = true;
			}
			else{
				ss = factory.openSession();
			}
			action.execute(ss);
		}
		catch(HibernateException e){
			throw new DAOException(e,DAOException.DATABASE_ACCESS_FAILED);
		}
		catch (ExprException e) {
			throw new DAOException(e,DAOException.EVAL_FALIED);
		}
		catch(Exception e){
			throw new DAOException(e);
		}
		finally{
			if(!isContainerSession && ss != null && ss.isOpen()){
				ss.close();
			}
		}
	}
	
	public void executeTrans(HibernateAction action) throws DAOException{
		Session ss = null;
		Transaction trans = null;
		try{
			ss = factory.openSession();
			trans = ss.beginTransaction();
			trans.begin();
			action.execute(ss);
			trans.commit();
		}
		catch(HibernateException e){
			if(trans != null){
				trans.rollback();
			}
			throw new DAOException(e,DAOException.DATABASE_ACCESS_FAILED);
		}
		catch (ExprException e) {
			throw new DAOException(e,DAOException.EVAL_FALIED);
		}
		catch(Exception e){
			if(trans != null){
				trans.rollback();
			}
			throw new DAOException(e);
		}
		finally{
			if(ss != null && ss.isOpen()){
				ss.close();
			}
		}
	}
	
	public void execute(HibernateStatelessAction action) throws DAOException{
		StatelessSession ss = null;
		boolean isContainerSession = false;
		try{
			if(ContextUtils.hasKey(Context.DB_SESSION)){
				Object containerSession = ContextUtils.get(Context.DB_SESSION);
				if(containerSession instanceof StatelessSession){
					ss = (StatelessSession)containerSession;
					isContainerSession = true;
				}
			}
			if(ss == null){
				ss = factory.openStatelessSession();
			}
			action.execute(ss);
		}
		catch(HibernateException e){
			throw new DAOException(e,DAOException.DATABASE_ACCESS_FAILED);
		}
		catch(Exception e){
			e.printStackTrace();
			throw new DAOException(e);
		}
		finally{
			if(!isContainerSession && ss != null){
				ss.close();
			}
		}
	}
	
	public void executeTrans(HibernateStatelessAction action) throws DAOException{
		StatelessSession ss = null;
		Transaction trans = null;
		try{
			ss = factory.openStatelessSession();
			trans = ss.beginTransaction();
			trans.begin();
			action.execute(ss);
			trans.commit();
		}
		catch(HibernateException e){
			if(trans != null){
				trans.rollback();
			}
			throw new DAOException(e,DAOException.DATABASE_ACCESS_FAILED);
		}
		catch(Exception e){
			if(trans != null){
				trans.rollback();
			}
			throw new DAOException(e);
		}
		finally{
			if(ss != null){
				ss.close();
			}
		}
	}
	
	public SessionFactory getFactory() {
		return factory;
	}
	
	public void setFactory(SessionFactory factory) {
		this.factory = factory;
	}

}
