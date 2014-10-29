package org.minimalj.backend.db;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Reference;

public class H {

	public static final H H_ = Keys.of(H.class);
	
	public H() {
		// needed for reflection constructor
	}

	public Object id;

	@Size(20)
	public String name;
	
	@Reference
	public G g;
	
	public final I i = new I();
}
