package com.comerzzia.bricodepot.backoffice.persistence.tickets.tipoImpresion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TicketTipoImpresionExample {

	protected String orderByClause;

	protected boolean distinct;

	protected List<Criteria> oredCriteria;

	public TicketTipoImpresionExample() {
		oredCriteria = new ArrayList<>();
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	protected abstract static class GeneratedCriteria {

		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
			super();
			criteria = new ArrayList<>();
		}

		public boolean isValid() {
			return criteria.size() > 0;
		}

		public List<Criterion> getAllCriteria() {
			return criteria;
		}

		public List<Criterion> getCriteria() {
			return criteria;
		}

		protected void addCriterion(String condition) {
			if (condition == null) {
				throw new RuntimeException("Value for condition cannot be null");
			}
			criteria.add(new Criterion(condition));
		}

		protected void addCriterion(String condition, Object value, String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value));
		}

		protected void addCriterion(String condition, Object value1, Object value2, String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		public Criteria andUidActividadIsNull() {
			addCriterion("uid_actividad is null");
			return (Criteria) this;
		}

		public Criteria andUidActividadIsNotNull() {
			addCriterion("uid_actividad is not null");
			return (Criteria) this;
		}

		public Criteria andUidActividadEqualTo(String value) {
			addCriterion("uid_actividad =", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadNotEqualTo(String value) {
			addCriterion("uid_actividad <>", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadGreaterThan(String value) {
			addCriterion("uid_actividad >", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadGreaterThanOrEqualTo(String value) {
			addCriterion("uid_actividad >=", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadLessThan(String value) {
			addCriterion("uid_actividad <", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadLessThanOrEqualTo(String value) {
			addCriterion("uid_actividad <=", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadLike(String value) {
			addCriterion("uid_actividad like", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadNotLike(String value) {
			addCriterion("uid_actividad not like", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadIn(List<String> values) {
			addCriterion("uid_actividad in", values, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadNotIn(List<String> values) {
			addCriterion("uid_actividad not in", values, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadBetween(String value1, String value2) {
			addCriterion("uid_actividad between", value1, value2, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadNotBetween(String value1, String value2) {
			addCriterion("uid_actividad not between", value1, value2, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidTicketIsNull() {
			addCriterion("uid_ticket is null");
			return (Criteria) this;
		}

		public Criteria andUidTicketIsNotNull() {
			addCriterion("uid_ticket is not null");
			return (Criteria) this;
		}

		public Criteria andUidTicketEqualTo(String value) {
			addCriterion("uid_ticket =", value, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketNotEqualTo(String value) {
			addCriterion("uid_ticket <>", value, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketGreaterThan(String value) {
			addCriterion("uid_ticket >", value, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketGreaterThanOrEqualTo(String value) {
			addCriterion("uid_ticket >=", value, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketLessThan(String value) {
			addCriterion("uid_ticket <", value, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketLessThanOrEqualTo(String value) {
			addCriterion("uid_ticket <=", value, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketLike(String value) {
			addCriterion("uid_ticket like", value, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketNotLike(String value) {
			addCriterion("uid_ticket not like", value, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketIn(List<String> values) {
			addCriterion("uid_ticket in", values, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketNotIn(List<String> values) {
			addCriterion("uid_ticket not in", values, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketBetween(String value1, String value2) {
			addCriterion("uid_ticket between", value1, value2, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketNotBetween(String value1, String value2) {
			addCriterion("uid_ticket not between", value1, value2, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andTipoImpresionIsNull() {
			addCriterion("tipo_impresion is null");
			return (Criteria) this;
		}

		public Criteria andTipoImpresionIsNotNull() {
			addCriterion("tipo_impresion is not null");
			return (Criteria) this;
		}

		public Criteria andTipoImpresionEqualTo(String value) {
			addCriterion("tipo_impresion =", value, "tipoImpresion");
			return (Criteria) this;
		}

		public Criteria andTipoImpresionNotEqualTo(String value) {
			addCriterion("tipo_impresion <>", value, "tipoImpresion");
			return (Criteria) this;
		}

		public Criteria andTipoImpresionGreaterThan(String value) {
			addCriterion("tipo_impresion >", value, "tipoImpresion");
			return (Criteria) this;
		}

		public Criteria andTipoImpresionGreaterThanOrEqualTo(String value) {
			addCriterion("tipo_impresion >=", value, "tipoImpresion");
			return (Criteria) this;
		}

		public Criteria andTipoImpresionLessThan(String value) {
			addCriterion("tipo_impresion <", value, "tipoImpresion");
			return (Criteria) this;
		}

		public Criteria andTipoImpresionLessThanOrEqualTo(String value) {
			addCriterion("tipo_impresion <=", value, "tipoImpresion");
			return (Criteria) this;
		}

		public Criteria andTipoImpresionLike(String value) {
			addCriterion("tipo_impresion like", value, "tipoImpresion");
			return (Criteria) this;
		}

		public Criteria andTipoImpresionNotLike(String value) {
			addCriterion("tipo_impresion not like", value, "tipoImpresion");
			return (Criteria) this;
		}

		public Criteria andTipoImpresionIn(List<String> values) {
			addCriterion("tipo_impresion in", values, "tipoImpresion");
			return (Criteria) this;
		}

		public Criteria andTipoImpresionNotIn(List<String> values) {
			addCriterion("tipo_impresion not in", values, "tipoImpresion");
			return (Criteria) this;
		}

		public Criteria andTipoImpresionBetween(String value1, String value2) {
			addCriterion("tipo_impresion between", value1, value2, "tipoImpresion");
			return (Criteria) this;
		}

		public Criteria andTipoImpresionNotBetween(String value1, String value2) {
			addCriterion("tipo_impresion not between", value1, value2, "tipoImpresion");
			return (Criteria) this;
		}

		public Criteria andFechaIsNull() {
			addCriterion("fecha is null");
			return (Criteria) this;
		}

		public Criteria andFechaIsNotNull() {
			addCriterion("fecha is not null");
			return (Criteria) this;
		}

		public Criteria andFechaEqualTo(Date value) {
			addCriterion("fecha =", value, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaNotEqualTo(Date value) {
			addCriterion("fecha <>", value, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaGreaterThan(Date value) {
			addCriterion("fecha >", value, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaGreaterThanOrEqualTo(Date value) {
			addCriterion("fecha >=", value, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaLessThan(Date value) {
			addCriterion("fecha <", value, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaLessThanOrEqualTo(Date value) {
			addCriterion("fecha <=", value, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaIn(List<Date> values) {
			addCriterion("fecha in", values, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaNotIn(List<Date> values) {
			addCriterion("fecha not in", values, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaBetween(Date value1, Date value2) {
			addCriterion("fecha between", value1, value2, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaNotBetween(Date value1, Date value2) {
			addCriterion("fecha not between", value1, value2, "fecha");
			return (Criteria) this;
		}

		public Criteria andUidActividadLikeInsensitive(String value) {
			addCriterion("upper(uid_actividad) like", value.toUpperCase(), "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidTicketLikeInsensitive(String value) {
			addCriterion("upper(uid_ticket) like", value.toUpperCase(), "uidTicket");
			return (Criteria) this;
		}

		public Criteria andTipoImpresionLikeInsensitive(String value) {
			addCriterion("upper(tipo_impresion) like", value.toUpperCase(), "tipoImpresion");
			return (Criteria) this;
		}
	}

	public static class Criteria extends GeneratedCriteria {

		protected Criteria() {
			super();
		}
	}

	public static class Criterion {

		private String condition;

		private Object value;

		private Object secondValue;

		private boolean noValue;

		private boolean singleValue;

		private boolean betweenValue;

		private boolean listValue;

		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion(String condition) {
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			}
			else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
	}
}