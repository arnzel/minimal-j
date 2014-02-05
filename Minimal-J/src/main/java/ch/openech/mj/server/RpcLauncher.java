package ch.openech.mj.server;

import ch.openech.mj.util.StringUtils;

public class RpcLauncher {

	private static String applicationName;

	public static void main(final String[] args) throws Exception {
		applicationName = System.getProperty("MjApplication");
		if (StringUtils.isBlank(applicationName)) {
			System.err.println("Missing MjApplication parameter");
			System.exit(-1);
		}
	}
	
}
