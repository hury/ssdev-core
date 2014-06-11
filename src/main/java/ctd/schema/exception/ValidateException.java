package ctd.schema.exception;

import ctd.util.exception.CodedBaseException;

public class ValidateException extends CodedBaseException {
	private static final long serialVersionUID = 2069940988017461965L;

	public ValidateException(int code) {
		super(code);
	}

	public ValidateException(String msg) {
		super(msg);
	}

	public ValidateException(int code, String msg) {
		super(code, msg);
	}

	public ValidateException(Throwable e) {
		super(e);
	}

	public ValidateException(Throwable e, int code) {
		super(e, code);
	}

	public ValidateException(Throwable e, String msg) {
		super(e, msg);
	}

	public ValidateException(Throwable e, int code, String msg) {
		super(e, code, msg);
	}

}
