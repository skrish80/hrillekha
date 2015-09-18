package com.techlords.crown.helpers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

public final class QueryBuilder {
	public final List<String> queryStack = new ArrayList<String>();

	public void addQuery(String argument, Object value) {
		addQuery(argument, value, false);
	}

	public boolean isEmpty() {
		return queryStack.isEmpty();
	}

	public void addQuery(String argument, Object value, boolean notEquals) {
		if (value == null) {
			return;
		}
		if (!queryStack.isEmpty()) {
			queryStack.add(" AND ");
		}
		String condition = (notEquals) ? " <> " : " = ";
		queryStack.add(argument + condition + value);
	}

	public void addORLikeQuery(Object value, String... arguments) {
		if (value == null) {
			return;
		}
		if (arguments.length == 0) {
			return;
		}
		if (!queryStack.isEmpty()) {
			queryStack.add(" AND ");
		}
		queryStack.add(arguments[0] + " LIKE '" + value + "%'");
		for (int i = 1; i < arguments.length; i++) {
			String arg = arguments[i];
			queryStack.add(" OR " + arg + " LIKE '" + value + "%'");
		}
	}

	public void addLikeQuery(String argument, Object value) {
		if (value == null) {
			return;
		}
		if (!queryStack.isEmpty()) {
			queryStack.add(" AND ");
		}
		queryStack.add(argument + " LIKE '" + value + "%'");
	}

	public void addGroupByClause(Object... values) {
		queryStack.add(" GROUP BY "
				+ StringUtils.arrayToCommaDelimitedString(values));
	}

	public void addJoinQuery(String argument, Object value,
			String joinTableName, String joinColumn) {
		if (value == null) {
			return;
		}
		if (!queryStack.isEmpty()) {
			queryStack.add(" AND ");
		}
		queryStack.add(argument + " IN (SELECT " + argument + " FROM "
				+ joinTableName + " WHERE " + joinColumn + " = " + value + ")");
	}

	public void addJoinInQuery(String argument, String joinTableName,
			String joinColumn, String additionalConstraint, Object... values) {
		if (values == null) {
			return;
		}
		if (!queryStack.isEmpty()) {
			queryStack.add(" AND ");
		}
		queryStack.add(argument + " IN (SELECT " + argument + " FROM "
				+ joinTableName + " WHERE " + joinColumn + " IN ("
				+ StringUtils.arrayToCommaDelimitedString(values) + ")");
		if(additionalConstraint != null && additionalConstraint.trim().length() > 0) {
			queryStack.add(" " + additionalConstraint);
		}
		queryStack.add(")");
	}

	public void addInQuery(String argument, Object... values) {
		if (values == null) {
			return;
		}
		if (!queryStack.isEmpty()) {
			queryStack.add(" AND ");
		}
		if (values.length > 0) {
			queryStack.add(argument + " IN ("
					+ StringUtils.arrayToCommaDelimitedString(values) + ")");
		}
	}

	public void addNotInQuery(String argument, Object... values) {
		if (values == null) {
			return;
		}
		if (!queryStack.isEmpty()) {
			queryStack.add(" AND ");
		}
		queryStack.add(argument + " NOT IN ("
				+ StringUtils.arrayToCommaDelimitedString(values) + ")");
	}

	public String getQuery() {
		final StringBuilder builder = new StringBuilder();
		for (final String query : queryStack) {
			builder.append(query);
		}
		return builder.toString();
	}

	/**
	 * Always call this before getQuery()
	 */
	public void addWhereClause() {
		if (!queryStack.isEmpty()) {
			queryStack.add(0, " WHERE ");
		}
	}
}
