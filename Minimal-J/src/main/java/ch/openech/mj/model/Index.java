package ch.openech.mj.model;



public class Index<T> {

	public  static enum INDEX_TYPE {
		FULLTEXT, UNIQUE, REFERENCE;
	}
	
	private final INDEX_TYPE type;
	private final Object[] keys;
	
	public Index(INDEX_TYPE type, Object... keys) {
		this.type = type;
		this.keys = keys;
	}

	
}
