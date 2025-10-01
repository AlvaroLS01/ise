package com.comerzzia.bricodepot.backoffice.persistence.facturas;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BricodepotFacturasExample {

	protected String orderByClause;
	protected String filtroByCodart;

	protected boolean distinct;

	protected List<Criteria> oredCriteria;

	public BricodepotFacturasExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	public String getOrderByClause() {
		return orderByClause;
	}
	
	public void getFiltroByCodart(String value) {
		this.filtroByCodart = value;
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
			criteria = new ArrayList<Criterion>();
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
			if (condition != null) {
				criteria.add(new Criterion(condition));
			}
		}

		protected void addCriterion(String condition, Object value, String property) {
			if (value != null) {
				criteria.add(new Criterion(condition, value));
			}
		}

		protected void addCriterion(String condition, Object value1, Object value2, String property) {
			if (value1 != null && value2 != null) {
				criteria.add(new Criterion(condition, value1, value2));
			}
		}

		public Criteria andUidActividadEqualTo(String value) {
			addCriterion("A.uid_actividad =", value, "uidActividad");
			return (Criteria) this;
		}
		
		public Criteria andUidActividadLikeInsensitive(String value) {
			addCriterion("upper(A.uid_actividad) like", value.toUpperCase(), "uidActividad");
			return (Criteria) this;
		}

		public Criteria andCodalmLikeInsensitive(String value) {
			addCriterion("upper(A.codalm) like", value.toUpperCase(), "codalm");
			return (Criteria) this;
		}

		public Criteria andCodcajaLikeInsensitive(String value) {
			addCriterion("upper(A.codcaja) like", value.toUpperCase(), "codcaja");
			return (Criteria) this;
		}

		public Criteria andIdTipoDocEqualTo(Long value) {
			addCriterion("A.id_tipo_documento =", value, "idTipoDoc");
			return (Criteria) this;
		}

		public Criteria andReferenciaClienteLikeInsensitive(String value) {
			addCriterion("upper(A.referencia_cliente) like", value.toUpperCase(), "referenciaCliente");
			return (Criteria) this;
		}

		public Criteria andCifLikeInsensitive(String value) {
			addCriterion("upper(A.cif) like", value.toUpperCase(), "cif");
			return (Criteria) this;
		}

		public Criteria andCodartLikeInsensitive(String value) {
			addCriterion("upper(B.codart) like", value.toUpperCase(), "codart");
			return (Criteria) this;
		}

		public Criteria andDescliLikeInsensitive(String value) {
			addCriterion("upper(A.descli) like", value.toUpperCase(), "descli");
			return (Criteria) this;
		}

		public Criteria andTotalEqualTo(Double double1) {
			addCriterion("A.total =", double1, "total");
			return (Criteria) this;
		}

		public Criteria andDesartLikeInsensitive(String value) {
			addCriterion("upper(B.desart) like", value.toUpperCase(), "desart");
			return (Criteria) this;
		}

		public Criteria andFechaGreaterThanOrEqualTo(Date value) {
			addCriterion("A.fecha >=", value, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaLessThanOrEqualTo(Date value) {
			addCriterion("A.fecha <=", value, "fecha");
			return (Criteria) this;
		}

		public Criteria andUsuarioLikeInsensitive(String value) {
			addCriterion("upper(A.usuario) like", value.toUpperCase(), "usuario");
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
