package ctd.schema;

import java.util.Comparator;

import com.google.common.primitives.Ints;

public class SchemaItemComparator implements Comparator<SchemaItem> {

	@Override
	public int compare(SchemaItem i1, SchemaItem i2) {
		return Ints.compare(i1.getIndex(),  i2.getIndex());
	}

}
