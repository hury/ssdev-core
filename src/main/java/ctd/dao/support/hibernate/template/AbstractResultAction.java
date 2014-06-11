package ctd.dao.support.hibernate.template;


public abstract class AbstractResultAction<T>{
	private T val;

	public void setResult(T val) {
		this.val = val;
	}

	public T getResult() {
		return val;
	}

}
