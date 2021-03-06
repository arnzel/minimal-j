package org.minimalj.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.frontend.Frontend;
import org.minimalj.transaction.PersistenceTransaction;
import org.minimalj.transaction.Role;
import org.minimalj.transaction.Transaction;

public class Subject implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Serializable token;
	
	private final List<String> roles = new ArrayList<>();

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Serializable getToken() {
		return token;
	}
	
	public void setToken(Serializable token) {
		this.token = token;
	}
	
	public List<String> getRoles() {
		return roles;
	}
	
	public boolean isValid() {
		return token != null;
	}

	public static boolean hasRoleFor(Transaction<?> transaction) {
		Role role = getRole(transaction);
		boolean noRoleNeeded = role == null;
		return noRoleNeeded || hasRole(role.value());
	}
	
	public static boolean hasRole(String... roleNames) {
		Subject subject = getSubject();
		if (subject != null) {
			for (String roleName : roleNames) {
				if (subject.roles.contains(roleName)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static Role getRole(Transaction<?> transaction) {
		boolean isPersistenceTransaction = transaction instanceof PersistenceTransaction;
		if (isPersistenceTransaction) {
			PersistenceTransaction<?> persistenceTransaction = (PersistenceTransaction<?>) transaction;
			Role role = findRoleByType(transaction.getClass(), persistenceTransaction.getClass());
			if (role == null) {
				role = findRoleByType(persistenceTransaction.getEntityClazz(), persistenceTransaction.getClass());
			}
			return role;
		} else {
			return findRoleByType(transaction.getClass(), Transaction.class);
		}
	}
	
	private static Role findRoleByType(Class<?> clazz, @SuppressWarnings("rawtypes") Class<? extends Transaction> transactionClass) {
		Role[] roles = clazz.getAnnotationsByType(Role.class);
		Role role = findRoleByType(roles, transactionClass);
		if (role != null) {
			return role;
		}
		roles = clazz.getPackage().getAnnotationsByType(Role.class);
		role = findRoleByType(roles, transactionClass);
		return role;
	}
	
	private static Role findRoleByType(Role[] roles, @SuppressWarnings("rawtypes") Class<? extends Transaction> transactionClass) {
		for (Role role : roles) {
			if (role.transaction() == transactionClass) {
				return role;
			}
		}
		// TODO respect class hierachy when retrieving needed role for a transaction
		// the following lines only go by order of the roles not by the hierachy
//		for (Role role : roles) {
//			if (role.transactionClass().isAssignableFrom(transactionClass)) {
//				return role;
//			}
//		}
		
		// check for the Transaction.class as this is the default in the annotation
		for (Role role : roles) {
			if (role.transaction() == Transaction.class) {
				return role;
			}
		}		
		return null;
	}
	
	public static Subject getSubject() {
		if (Frontend.isAvailable()) {
			return Frontend.getInstance().getSubject();
		} else {
			return Authorization.getInstance().getSubject();
		}
	}

}
