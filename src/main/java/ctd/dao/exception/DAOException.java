package ctd.dao.exception;

import ctd.util.exception.CodedBaseException;

public class DAOException  extends CodedBaseException {
	private static final long serialVersionUID = -8481811634176212223L;
	public static int ENTITIY_NOT_FOUND = 404;
	public static int SESSION_ACQUIRE_FALIED = 501;
	public static int SCHEMA_NOT_FOUND = 504;
	public static int VALUE_NEEDED = 505;
	public static int EVAL_FALIED = 506;
	public static int VALIDATE_FALIED = 507;
	public static int DATABASE_ACCESS_FAILED = 510;
	public static int ACCESS_DENIED = 511;
	
	public DAOException(String msg){
		super(msg);
	}
	
	public DAOException(Throwable e){
		super(e);
	}
	
	public DAOException(Throwable e,int code){
		super(e,code);
	}
	
	public DAOException(Throwable e,int code,String msg){
		super(e,code,msg);
	}
	
	public DAOException(int code,String msg) {
		super(code,msg);
	}
	
}
