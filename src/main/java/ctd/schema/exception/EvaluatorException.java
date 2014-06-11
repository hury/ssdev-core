package ctd.schema.exception;

import ctd.util.exception.CodedBaseException;

public class EvaluatorException extends CodedBaseException {
	private static final long serialVersionUID = -3712261610790074122L;

	public EvaluatorException(int code) {
		super(code);
	}

	public EvaluatorException(String msg) {
		super(msg);
	}

	public EvaluatorException(int code, String msg) {
		super(code, msg);
	}

	public EvaluatorException(Throwable e) {
		super(e);
	}

	public EvaluatorException(Throwable e, int code) {
		super(e, code);
	}

	public EvaluatorException(Throwable e, String msg) {
		super(e, msg);
	}

	public EvaluatorException(Throwable e, int code, String msg) {
		super(e, code, msg);
	}

}
