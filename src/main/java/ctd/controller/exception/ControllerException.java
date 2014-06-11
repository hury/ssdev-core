package ctd.controller.exception;

import ctd.util.exception.CodedBaseException;

public class ControllerException extends CodedBaseException {
	public static final int INSTANCE_NOT_FOUND = 404;
	public static final int IO_ERROR = 409;
	public static final int PARSE_ERROR = 401;
	
	private static final long serialVersionUID = 7336681897520100911L;

	public ControllerException(int code) {
		super(code);
	}

	public ControllerException(String msg) {
		super(msg);
	}

	public ControllerException(int code, String msg) {
		super(code, msg);
	}

	public ControllerException(Throwable e) {
		super(e);
	}

	public ControllerException(Throwable e, int code) {
		super(e, code);
	}

	public ControllerException(Throwable e, String msg) {
		super(e, msg);
	}

	public ControllerException(Throwable e, int code, String msg) {
		super(e, code, msg);
	}
	
	public boolean isInstanceNotFound(){
		return code == INSTANCE_NOT_FOUND;
	}

}
