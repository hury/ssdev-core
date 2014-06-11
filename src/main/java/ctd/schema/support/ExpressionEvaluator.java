package ctd.schema.support;

import java.util.List;

import ctd.schema.Evaluator;
import ctd.schema.exception.EvaluatorException;
import ctd.util.exp.ExpressionProcessor;
import ctd.util.exp.exception.ExprException;

public class ExpressionEvaluator implements Evaluator {
	
	private List<Object> exp;
	
	@Override
	public Object eval() throws EvaluatorException{
		
		try {
			return ExpressionProcessor.instance().run(exp);
		} 
		catch (ExprException e) {
			throw new EvaluatorException(e.getCode(),e.getMessage());
		}
	}

	public List<Object> getExp() {
		return exp;
	}

	public void setExp(List<Object> exp) {
		this.exp = exp;
	}


}
