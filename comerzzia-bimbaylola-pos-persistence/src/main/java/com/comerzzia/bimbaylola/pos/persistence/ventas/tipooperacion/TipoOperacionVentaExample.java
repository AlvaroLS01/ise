package com.comerzzia.bimbaylola.pos.persistence.ventas.tipooperacion;

import java.util.ArrayList;
import java.util.List;

public class TipoOperacionVentaExample {

	protected String orderByClause;

	protected boolean distinct;

	protected List<Criteria> oredCriteria;

	public TipoOperacionVentaExample() {
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
			addCriterion("UID_ACTIVIDAD is null");
			return (Criteria) this;
		}

		public Criteria andUidActividadIsNotNull() {
			addCriterion("UID_ACTIVIDAD is not null");
			return (Criteria) this;
		}

		public Criteria andUidActividadEqualTo(String value) {
			addCriterion("UID_ACTIVIDAD =", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadNotEqualTo(String value) {
			addCriterion("UID_ACTIVIDAD <>", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadGreaterThan(String value) {
			addCriterion("UID_ACTIVIDAD >", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadGreaterThanOrEqualTo(String value) {
			addCriterion("UID_ACTIVIDAD >=", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadLessThan(String value) {
			addCriterion("UID_ACTIVIDAD <", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadLessThanOrEqualTo(String value) {
			addCriterion("UID_ACTIVIDAD <=", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadLike(String value) {
			addCriterion("UID_ACTIVIDAD like", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadNotLike(String value) {
			addCriterion("UID_ACTIVIDAD not like", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadIn(List<String> values) {
			addCriterion("UID_ACTIVIDAD in", values, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadNotIn(List<String> values) {
			addCriterion("UID_ACTIVIDAD not in", values, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadBetween(String value1, String value2) {
			addCriterion("UID_ACTIVIDAD between", value1, value2, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadNotBetween(String value1, String value2) {
			addCriterion("UID_ACTIVIDAD not between", value1, value2, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidTicketIsNull() {
			addCriterion("UID_TICKET is null");
			return (Criteria) this;
		}

		public Criteria andUidTicketIsNotNull() {
			addCriterion("UID_TICKET is not null");
			return (Criteria) this;
		}

		public Criteria andUidTicketEqualTo(String value) {
			addCriterion("UID_TICKET =", value, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketNotEqualTo(String value) {
			addCriterion("UID_TICKET <>", value, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketGreaterThan(String value) {
			addCriterion("UID_TICKET >", value, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketGreaterThanOrEqualTo(String value) {
			addCriterion("UID_TICKET >=", value, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketLessThan(String value) {
			addCriterion("UID_TICKET <", value, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketLessThanOrEqualTo(String value) {
			addCriterion("UID_TICKET <=", value, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketLike(String value) {
			addCriterion("UID_TICKET like", value, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketNotLike(String value) {
			addCriterion("UID_TICKET not like", value, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketIn(List<String> values) {
			addCriterion("UID_TICKET in", values, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketNotIn(List<String> values) {
			addCriterion("UID_TICKET not in", values, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketBetween(String value1, String value2) {
			addCriterion("UID_TICKET between", value1, value2, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidTicketNotBetween(String value1, String value2) {
			addCriterion("UID_TICKET not between", value1, value2, "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaIsNull() {
			addCriterion("UID_DIARIO_CAJA is null");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaIsNotNull() {
			addCriterion("UID_DIARIO_CAJA is not null");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaEqualTo(String value) {
			addCriterion("UID_DIARIO_CAJA =", value, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaNotEqualTo(String value) {
			addCriterion("UID_DIARIO_CAJA <>", value, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaGreaterThan(String value) {
			addCriterion("UID_DIARIO_CAJA >", value, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaGreaterThanOrEqualTo(String value) {
			addCriterion("UID_DIARIO_CAJA >=", value, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaLessThan(String value) {
			addCriterion("UID_DIARIO_CAJA <", value, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaLessThanOrEqualTo(String value) {
			addCriterion("UID_DIARIO_CAJA <=", value, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaLike(String value) {
			addCriterion("UID_DIARIO_CAJA like", value, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaNotLike(String value) {
			addCriterion("UID_DIARIO_CAJA not like", value, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaIn(List<String> values) {
			addCriterion("UID_DIARIO_CAJA in", values, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaNotIn(List<String> values) {
			addCriterion("UID_DIARIO_CAJA not in", values, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaBetween(String value1, String value2) {
			addCriterion("UID_DIARIO_CAJA between", value1, value2, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaNotBetween(String value1, String value2) {
			addCriterion("UID_DIARIO_CAJA not between", value1, value2, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andTipoDocumentoIsNull() {
			addCriterion("TIPO_DOCUMENTO is null");
			return (Criteria) this;
		}

		public Criteria andTipoDocumentoIsNotNull() {
			addCriterion("TIPO_DOCUMENTO is not null");
			return (Criteria) this;
		}

		public Criteria andTipoDocumentoEqualTo(String value) {
			addCriterion("TIPO_DOCUMENTO =", value, "tipoDocumento");
			return (Criteria) this;
		}

		public Criteria andTipoDocumentoNotEqualTo(String value) {
			addCriterion("TIPO_DOCUMENTO <>", value, "tipoDocumento");
			return (Criteria) this;
		}

		public Criteria andTipoDocumentoGreaterThan(String value) {
			addCriterion("TIPO_DOCUMENTO >", value, "tipoDocumento");
			return (Criteria) this;
		}

		public Criteria andTipoDocumentoGreaterThanOrEqualTo(String value) {
			addCriterion("TIPO_DOCUMENTO >=", value, "tipoDocumento");
			return (Criteria) this;
		}

		public Criteria andTipoDocumentoLessThan(String value) {
			addCriterion("TIPO_DOCUMENTO <", value, "tipoDocumento");
			return (Criteria) this;
		}

		public Criteria andTipoDocumentoLessThanOrEqualTo(String value) {
			addCriterion("TIPO_DOCUMENTO <=", value, "tipoDocumento");
			return (Criteria) this;
		}

		public Criteria andTipoDocumentoLike(String value) {
			addCriterion("TIPO_DOCUMENTO like", value, "tipoDocumento");
			return (Criteria) this;
		}

		public Criteria andTipoDocumentoNotLike(String value) {
			addCriterion("TIPO_DOCUMENTO not like", value, "tipoDocumento");
			return (Criteria) this;
		}

		public Criteria andTipoDocumentoIn(List<String> values) {
			addCriterion("TIPO_DOCUMENTO in", values, "tipoDocumento");
			return (Criteria) this;
		}

		public Criteria andTipoDocumentoNotIn(List<String> values) {
			addCriterion("TIPO_DOCUMENTO not in", values, "tipoDocumento");
			return (Criteria) this;
		}

		public Criteria andTipoDocumentoBetween(String value1, String value2) {
			addCriterion("TIPO_DOCUMENTO between", value1, value2, "tipoDocumento");
			return (Criteria) this;
		}

		public Criteria andTipoDocumentoNotBetween(String value1, String value2) {
			addCriterion("TIPO_DOCUMENTO not between", value1, value2, "tipoDocumento");
			return (Criteria) this;
		}

		public Criteria andSignoDocumentoIsNull() {
			addCriterion("SIGNO_DOCUMENTO is null");
			return (Criteria) this;
		}

		public Criteria andSignoDocumentoIsNotNull() {
			addCriterion("SIGNO_DOCUMENTO is not null");
			return (Criteria) this;
		}

		public Criteria andSignoDocumentoEqualTo(String value) {
			addCriterion("SIGNO_DOCUMENTO =", value, "signoDocumento");
			return (Criteria) this;
		}

		public Criteria andSignoDocumentoNotEqualTo(String value) {
			addCriterion("SIGNO_DOCUMENTO <>", value, "signoDocumento");
			return (Criteria) this;
		}

		public Criteria andSignoDocumentoGreaterThan(String value) {
			addCriterion("SIGNO_DOCUMENTO >", value, "signoDocumento");
			return (Criteria) this;
		}

		public Criteria andSignoDocumentoGreaterThanOrEqualTo(String value) {
			addCriterion("SIGNO_DOCUMENTO >=", value, "signoDocumento");
			return (Criteria) this;
		}

		public Criteria andSignoDocumentoLessThan(String value) {
			addCriterion("SIGNO_DOCUMENTO <", value, "signoDocumento");
			return (Criteria) this;
		}

		public Criteria andSignoDocumentoLessThanOrEqualTo(String value) {
			addCriterion("SIGNO_DOCUMENTO <=", value, "signoDocumento");
			return (Criteria) this;
		}

		public Criteria andSignoDocumentoLike(String value) {
			addCriterion("SIGNO_DOCUMENTO like", value, "signoDocumento");
			return (Criteria) this;
		}

		public Criteria andSignoDocumentoNotLike(String value) {
			addCriterion("SIGNO_DOCUMENTO not like", value, "signoDocumento");
			return (Criteria) this;
		}

		public Criteria andSignoDocumentoIn(List<String> values) {
			addCriterion("SIGNO_DOCUMENTO in", values, "signoDocumento");
			return (Criteria) this;
		}

		public Criteria andSignoDocumentoNotIn(List<String> values) {
			addCriterion("SIGNO_DOCUMENTO not in", values, "signoDocumento");
			return (Criteria) this;
		}

		public Criteria andSignoDocumentoBetween(String value1, String value2) {
			addCriterion("SIGNO_DOCUMENTO between", value1, value2, "signoDocumento");
			return (Criteria) this;
		}

		public Criteria andSignoDocumentoNotBetween(String value1, String value2) {
			addCriterion("SIGNO_DOCUMENTO not between", value1, value2, "signoDocumento");
			return (Criteria) this;
		}

		public Criteria andIdTipoDocumentoIsNull() {
			addCriterion("ID_TIPO_DOCUMENTO is null");
			return (Criteria) this;
		}

		public Criteria andIdTipoDocumentoIsNotNull() {
			addCriterion("ID_TIPO_DOCUMENTO is not null");
			return (Criteria) this;
		}

		public Criteria andIdTipoDocumentoEqualTo(Long value) {
			addCriterion("ID_TIPO_DOCUMENTO =", value, "idTipoDocumento");
			return (Criteria) this;
		}

		public Criteria andIdTipoDocumentoNotEqualTo(Long value) {
			addCriterion("ID_TIPO_DOCUMENTO <>", value, "idTipoDocumento");
			return (Criteria) this;
		}

		public Criteria andIdTipoDocumentoGreaterThan(Long value) {
			addCriterion("ID_TIPO_DOCUMENTO >", value, "idTipoDocumento");
			return (Criteria) this;
		}

		public Criteria andIdTipoDocumentoGreaterThanOrEqualTo(Long value) {
			addCriterion("ID_TIPO_DOCUMENTO >=", value, "idTipoDocumento");
			return (Criteria) this;
		}

		public Criteria andIdTipoDocumentoLessThan(Long value) {
			addCriterion("ID_TIPO_DOCUMENTO <", value, "idTipoDocumento");
			return (Criteria) this;
		}

		public Criteria andIdTipoDocumentoLessThanOrEqualTo(Long value) {
			addCriterion("ID_TIPO_DOCUMENTO <=", value, "idTipoDocumento");
			return (Criteria) this;
		}

		public Criteria andIdTipoDocumentoIn(List<Long> values) {
			addCriterion("ID_TIPO_DOCUMENTO in", values, "idTipoDocumento");
			return (Criteria) this;
		}

		public Criteria andIdTipoDocumentoNotIn(List<Long> values) {
			addCriterion("ID_TIPO_DOCUMENTO not in", values, "idTipoDocumento");
			return (Criteria) this;
		}

		public Criteria andIdTipoDocumentoBetween(Long value1, Long value2) {
			addCriterion("ID_TIPO_DOCUMENTO between", value1, value2, "idTipoDocumento");
			return (Criteria) this;
		}

		public Criteria andIdTipoDocumentoNotBetween(Long value1, Long value2) {
			addCriterion("ID_TIPO_DOCUMENTO not between", value1, value2, "idTipoDocumento");
			return (Criteria) this;
		}

		public Criteria andUidActividadLikeInsensitive(String value) {
			addCriterion("upper(UID_ACTIVIDAD) like", value.toUpperCase(), "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidTicketLikeInsensitive(String value) {
			addCriterion("upper(UID_TICKET) like", value.toUpperCase(), "uidTicket");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaLikeInsensitive(String value) {
			addCriterion("upper(UID_DIARIO_CAJA) like", value.toUpperCase(), "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andTipoDocumentoLikeInsensitive(String value) {
			addCriterion("upper(TIPO_DOCUMENTO) like", value.toUpperCase(), "tipoDocumento");
			return (Criteria) this;
		}

		public Criteria andSignoDocumentoLikeInsensitive(String value) {
			addCriterion("upper(SIGNO_DOCUMENTO) like", value.toUpperCase(), "signoDocumento");
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