package ctd.dao.support.hibernate;
import ctd.dao.support.AbstractCombineDAO;


public class HibernateSupportDAO<T> extends AbstractCombineDAO<T> {
	
	public HibernateSupportDAO(){
		setReadDAO(new HibernateSupportReadDAO<T>());
		setWriteDAO(new HibernateSupportWriteDAO<T>());
	}
	
}
