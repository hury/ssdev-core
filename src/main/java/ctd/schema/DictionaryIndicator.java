package ctd.schema;

import java.io.Serializable;

import ctd.util.StringValueParser;

public class DictionaryIndicator implements Serializable {
	private static final long serialVersionUID = 2542057660051407081L;
	private String id;
	private String render;
	private String parentKey;
	private Integer slice;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getRender() {
		return render;
	}
	
	public void setRender(String render) {
		this.render = render;
	}
	
	public String getParentKey() {
		return StringValueParser.parse(parentKey, String.class);
	}
	
	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}
	
	public Integer getSlice() {
		return slice;
	}
	
	public void setSlice(Integer slice) {
		this.slice = slice;
	}
	
}
