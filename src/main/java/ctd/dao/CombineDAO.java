package ctd.dao;

public interface CombineDAO<T> extends DAO<T>{
	void setReadDAO(ReadDAO<T> dao);
	void setWriteDAO(WriteDAO<T> dao);
}
