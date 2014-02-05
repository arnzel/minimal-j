package ch.openech.mj.service;


public class Services {

	private static Services services;
	
	public static Services getServices() {
		if (services == null) {
			throw new IllegalStateException("Services has to be initialized");
		}
		return services;
	}

	public static synchronized void setServices(Services services) {
		if (Services.services != null) {
			throw new IllegalStateException("Services cannot be changed");
		}		
		if (services == null) {
			throw new IllegalArgumentException("Services cannot be null");
		}
		Services.services = services;
	}

	
	
}