package org.minimalj.transaction.persistence;

import java.io.Serializable;

import org.minimalj.backend.Backend;
import org.minimalj.transaction.Transaction;

public class StatementTransaction implements Transaction<Serializable> {
	private static final long serialVersionUID = 1L;

	private final String queryName;
	private final Serializable[] parameters;
	
	public StatementTransaction(String queryName, Serializable... parameters) {
		this.queryName = queryName;
		this.parameters = parameters;
	}

	@Override
	public Serializable execute(Backend backend) {
		Serializable result = backend.executeStatement(queryName, parameters);
		return result;
	}

}