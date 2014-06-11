package ctd.security;

import java.io.Serializable;

import ctd.util.converter.ConversionUtils;
import ctd.util.exception.CodedBaseException;

public abstract class Condition implements Serializable{
	private static final long serialVersionUID = -4471912895041205561L;
	protected String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public <T> T run(Class<T> t) throws CodedBaseException{
		return ConversionUtils.convert(run(), t);
	}
	
	public abstract Object run() throws CodedBaseException;
	
	public abstract Object getDefine();
	
}
