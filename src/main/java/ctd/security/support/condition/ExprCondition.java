package ctd.security.support.condition;

import java.util.List;

import ctd.security.Condition;
import ctd.util.JSONUtils;
import ctd.util.exception.CodedBaseException;
import ctd.util.exp.ExpressionProcessor;

public class ExprCondition extends Condition {
	private static final long serialVersionUID = 2700768346285919954L;
	protected List<Object> expr;
	
	public ExprCondition(){
		
	};
	
	public ExprCondition(String exprStr){
		setExpr(exprStr);
	}
	
	@SuppressWarnings("unchecked")
	public void setExpr(String exprStr){
		expr = JSONUtils.parse(exprStr, List.class);
	}
	
	public Object getDefine(){
		return expr;
	}
	
	public Object run(String lang) throws CodedBaseException{
		return ExpressionProcessor.instance(lang).run(expr);
	}
	
	public Object run() throws CodedBaseException{
		return ExpressionProcessor.instance().run(expr);
	}
}
