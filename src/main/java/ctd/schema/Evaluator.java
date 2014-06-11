package ctd.schema;

import ctd.schema.exception.EvaluatorException;

public interface Evaluator {
	Object eval() throws EvaluatorException;
}
