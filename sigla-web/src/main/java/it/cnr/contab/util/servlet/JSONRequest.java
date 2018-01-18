package it.cnr.contab.util.servlet;

import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.List;
import java.util.Optional;

public class JSONRequest extends JSONRESTRequest{
	private Integer activePage;
	private Integer maxItemsPerPage;
	private List<OrderBy> orderBy;
	private List<Clause> clauses;
	
	public JSONRequest() {
		super();
	}
	public Integer getActivePage() {
		return activePage;
	}
	public void setActivePage(Integer activePage) {
		this.activePage = activePage;
	}
	public Integer getMaxItemsPerPage() {
		return maxItemsPerPage;
	}
	public void setMaxItemsPerPage(Integer maxItemsPerPage) {
		this.maxItemsPerPage = maxItemsPerPage;
	}
	public List<OrderBy> getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(List<OrderBy> orderBy) {
		this.orderBy = orderBy;
	}

	public List<Clause> getClauses() {
		return clauses;
	}
	public void setClauses(List<Clause> clauses) {
		this.clauses = clauses;
	}

	public static class OrderBy {
		String name, type;

		public OrderBy() {
			super();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}
	public static class Clause {
		private String condition, fieldName, operator;
		private Object fieldValue;
		
		public Clause() {
			super();
		}
		public String getCondition() {
			return condition;
		}
		public void setCondition(String condition) {
			this.condition = condition;
		}
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		public String getOperator() {
			return operator;
		}
		public void setOperator(String operator) {
			this.operator = operator;
		}
		public Object getFieldValue() {
			return fieldValue;
		}
		public void setFieldValue(Object fieldValue) {
			this.fieldValue = fieldValue;
		}
		
		public int getSQLOperator() {
			if (operator.equals("="))
				return SQLBuilder.EQUALS;
			else if (operator.equals("!=") || operator.equals("<>"))
				return SQLBuilder.NOT_EQUALS;
			else if (operator.equals("<"))
				return SQLBuilder.LESS;
			else if (operator.equals("<="))
				return SQLBuilder.LESS_EQUALS;
			else if (operator.equals(">"))
				return SQLBuilder.GREATER;
			else if (operator.equals(">="))
				return SQLBuilder.GREATER_EQUALS;
			else if (operator.equalsIgnoreCase("like"))
				return SQLBuilder.LIKE;
			else if (operator.equalsIgnoreCase("isnull"))
				return SQLBuilder.ISNULL;
			else if (operator.equalsIgnoreCase("isnotnull"))
				return SQLBuilder.ISNOTNULL;
			return SQLBuilder.EQUALS;
		}

		@Override
		public String toString() {
			return "Clause {" +
					"condition='" + condition + '\'' +
					", fieldName='" + fieldName + '\'' +
					", operator='" + operator + '\'' +
					", fieldValue=" + fieldValue +
					'}';
		}

		public void validate() throws ComponentException{
			if (!Optional.ofNullable(getSQLOperator()).isPresent() || !Optional.ofNullable(getFieldName()).isPresent())
				throw new ComponentException("Cannot add clause " + this);
			if (!(getSQLOperator() == SQLBuilder.ISNULL || getSQLOperator() == SQLBuilder.ISNOTNULL) &&
					!Optional.ofNullable(getFieldValue()).isPresent())
				throw new ComponentException("Cannot add clause " + this);
		}
	}

}
