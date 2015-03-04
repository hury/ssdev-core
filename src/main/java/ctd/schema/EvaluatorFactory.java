package ctd.schema;

import java.util.List;

import org.dom4j.Element;

import ctd.schema.exception.EvaluatorException;
import ctd.schema.support.ExpressionEvaluator;
import ctd.schema.support.SequenceEvaluator;
import ctd.sequence.Sequence;
import ctd.sequence.SequenceManager;
import ctd.sequence.exception.SequenceException;
import ctd.sequence.slice.SliceFactory;
import ctd.util.JSONUtils;

public class EvaluatorFactory {
	
	@SuppressWarnings("unchecked")
	public static ExpressionEvaluator newExpressionEvaluator(String expStr){
		List<Object> exp = (List<Object>)JSONUtils.parse(expStr, List.class);
		ExpressionEvaluator evaluator = new ExpressionEvaluator();
		evaluator.setExp(exp);
		return evaluator;
	}
	
	@SuppressWarnings("unchecked")
	public static Evaluator newInstance(Element setEl) throws EvaluatorException{
		
		String assignType = setEl.attributeValue("type","exp");
		if(assignType.equals("exp")){
			
			List<Object> exp = (List<Object>)JSONUtils.parse(setEl.getTextTrim(), List.class);
			ExpressionEvaluator evaluator = new ExpressionEvaluator();
			evaluator.setExp(exp);
			return evaluator;
		}
		else if(assignType.equals("seq")){
			String entityName = setEl.getDocument().getRootElement().attributeValue("entityName");
			String fid = setEl.getParent().attributeValue("id");
			String sequenceId = entityName + "." + fid;
			
			Sequence seq = new Sequence();
			seq.setId(sequenceId);
			
			List<Element> els = setEl.elements();
			try{
				for(Element el : els){
					seq.addSlice(SliceFactory.newInstance(el));
				}
			}
			catch(SequenceException e){
				throw new EvaluatorException(e.getCode(),e.getMessage());
			}
			
			SequenceManager.instance().addSequence(seq);
			
			SequenceEvaluator evaluator = new SequenceEvaluator();
			evaluator.setSequenceId(sequenceId);
			return evaluator;
		}
		return null;
	}

}
