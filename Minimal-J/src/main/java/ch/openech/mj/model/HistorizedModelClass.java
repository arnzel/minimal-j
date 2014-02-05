package ch.openech.mj.model;

public class HistorizedModelClass<T> extends ModelClass<T> {

	private final Class<?> identificationClass;

	public HistorizedModelClass(Class<T> clazz, Class<?> identificationClass) {
		super(clazz);
		this.identificationClass = identificationClass;
	}

	public Class<?> getIdentificationClass() {
		return identificationClass;
	}
	
}
