package ctd.schema.support;

import ctd.schema.Evaluator;
import ctd.schema.exception.EvaluatorException;
import ctd.sequence.SequenceManager;
import ctd.sequence.exception.SequenceException;

public class SequenceEvaluator implements Evaluator {

	private String sequenceId;
	
	
	@Override
	public Object eval() throws EvaluatorException {
		try {
			return SequenceManager.instance().getNextSequenceValue(sequenceId);
		}
		catch (SequenceException e) {
			throw new EvaluatorException(e.getCode(),e.getMessage());
		}
	}

	public String getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
		
	}

}
