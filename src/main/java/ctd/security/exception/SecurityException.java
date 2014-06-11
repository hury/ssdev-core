package ctd.security.exception;

import ctd.util.exception.CodedBaseException;

public class SecurityException extends CodedBaseException {
	private static final long serialVersionUID = -8481811634176212223L;
	public static int NOT_LOGON = 403;
	
	public SecurityException(int code) {
		super(code);
	}

	public SecurityException(String msg) {
		super(msg);
	}

	public SecurityException(int code, String msg) {
		super(code, msg);
	}

	public SecurityException(Throwable e) {
		super(e);
	}

	public SecurityException(Throwable e, int code) {
		super(e, code);
	}

	public SecurityException(Throwable e, String msg) {
		super(e, msg);
	}

	public SecurityException(Throwable e, int code, String msg) {
		super(e, code, msg);
	}

}
