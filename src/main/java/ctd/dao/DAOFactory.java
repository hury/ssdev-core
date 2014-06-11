package ctd.dao;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import ctd.dao.support.hibernate.HibernateSupportDAO;

public class DAOFactory {
	private static final ConcurrentHashMap<String, DAO<?>> store = new ConcurrentHashMap<String, DAO<?>>();
	private static Class<?> defaultDaoClass = HibernateSupportDAO.class;
	
	
	public void setDefaultDaoClass(String className){
		try {
			defaultDaoClass =  Class.forName(className);
		} 
		catch (ClassNotFoundException e) {
			throw new IllegalStateException("defaultDaoClass[" + className + "] not found");
		}
	}
	
	public void setDaoList(List<DAO<?>> ls){
		for(DAO<?> dao : ls){
			registerDao(dao);
		}
	}
	
	public static void registerDao(DAO<?> dao){
		store.putIfAbsent(dao.getId(), dao);
	}
	
	public static DAO<?> getDao(String id){
		if(store.containsKey(id)){
			return store.get(id);
		}
		else{
			if(defaultDaoClass != null){
				try {
					DAO<?> newOne = (DAO<?>) defaultDaoClass.newInstance();
					newOne.setEntityClass(HashMap.class);
					newOne.setId(id);
					newOne.setSchemaId(id);
					registerDao(newOne);
					return newOne;
				} 
				catch (Exception e) {
					throw new IllegalStateException(e);
				} 
			}
			else{
				throw new IllegalStateException("dao[" + id + "] not found,no default daoClass exsit.");
			}
		}
		
	}
	
	
}
