package ctd.dictionary.service;

import java.io.Serializable;
import java.util.List;

import ctd.dictionary.DictionaryItem;

public class DictionarySliceRecordSet implements Serializable{
	private static final long serialVersionUID = 1516348719801279804L;
	private String dicId;
	private long lastModify;
	private List<DictionaryItem> items;
	private int start;
	private int limit;
	private int total;

	public String getDicId() {
		return dicId;
	}

	public void setDicId(String dicId) {
		this.dicId = dicId;
	}

	public long getLastModify() {
		return lastModify;
	}

	public void setLastModify(long lastModify) {
		this.lastModify = lastModify;
	}

	public List<DictionaryItem> getItems() {
		return items;
	}

	public void setItems(List<DictionaryItem> items) {
		this.items = items;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
