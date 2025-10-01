package com.comerzzia.bricodepot.backoffice.persistence.datosMovimiento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatosMovimientoExample {

	protected String orderByClause;

	protected boolean distinct;

	protected List<Criteria> oredCriteria;

	public DatosMovimientoExample() {
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

		public Criteria andUidDiarioCajaIsNull() {
			addCriterion("uid_diario_caja is null");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaIsNotNull() {
			addCriterion("uid_diario_caja is not null");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaEqualTo(String value) {
			addCriterion("uid_diario_caja =", value, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaNotEqualTo(String value) {
			addCriterion("uid_diario_caja <>", value, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaGreaterThan(String value) {
			addCriterion("uid_diario_caja >", value, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaGreaterThanOrEqualTo(String value) {
			addCriterion("uid_diario_caja >=", value, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaLessThan(String value) {
			addCriterion("uid_diario_caja <", value, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaLessThanOrEqualTo(String value) {
			addCriterion("uid_diario_caja <=", value, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaLike(String value) {
			addCriterion("uid_diario_caja like", value, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaNotLike(String value) {
			addCriterion("uid_diario_caja not like", value, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaIn(List<String> values) {
			addCriterion("uid_diario_caja in", values, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaNotIn(List<String> values) {
			addCriterion("uid_diario_caja not in", values, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaBetween(String value1, String value2) {
			addCriterion("uid_diario_caja between", value1, value2, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andUidDiarioCajaNotBetween(String value1, String value2) {
			addCriterion("uid_diario_caja not between", value1, value2, "uidDiarioCaja");
			return (Criteria) this;
		}

		public Criteria andCodalmIsNull() {
			addCriterion("codalm is null");
			return (Criteria) this;
		}

		public Criteria andCodalmIsNotNull() {
			addCriterion("codalm is not null");
			return (Criteria) this;
		}

		public Criteria andCodalmEqualTo(String value) {
			addCriterion("codalm =", value, "codalm");
			return (Criteria) this;
		}

		public Criteria andCodalmNotEqualTo(String value) {
			addCriterion("codalm <>", value, "codalm");
			return (Criteria) this;
		}

		public Criteria andCodalmGreaterThan(String value) {
			addCriterion("codalm >", value, "codalm");
			return (Criteria) this;
		}

		public Criteria andCodalmGreaterThanOrEqualTo(String value) {
			addCriterion("codalm >=", value, "codalm");
			return (Criteria) this;
		}

		public Criteria andCodalmLessThan(String value) {
			addCriterion("codalm <", value, "codalm");
			return (Criteria) this;
		}

		public Criteria andCodalmLessThanOrEqualTo(String value) {
			addCriterion("codalm <=", value, "codalm");
			return (Criteria) this;
		}

		public Criteria andCodalmLike(String value) {
			addCriterion("codalm like", value, "codalm");
			return (Criteria) this;
		}

		public Criteria andCodalmNotLike(String value) {
			addCriterion("codalm not like", value, "codalm");
			return (Criteria) this;
		}

		public Criteria andCodalmIn(List<String> values) {
			addCriterion("codalm in", values, "codalm");
			return (Criteria) this;
		}

		public Criteria andCodalmNotIn(List<String> values) {
			addCriterion("codalm not in", values, "codalm");
			return (Criteria) this;
		}

		public Criteria andCodalmBetween(String value1, String value2) {
			addCriterion("codalm between", value1, value2, "codalm");
			return (Criteria) this;
		}

		public Criteria andCodalmNotBetween(String value1, String value2) {
			addCriterion("codalm not between", value1, value2, "codalm");
			return (Criteria) this;
		}

		public Criteria andCodcajaIsNull() {
			addCriterion("codcaja is null");
			return (Criteria) this;
		}

		public Criteria andCodcajaIsNotNull() {
			addCriterion("codcaja is not null");
			return (Criteria) this;
		}

		public Criteria andCodcajaEqualTo(String value) {
			addCriterion("codcaja =", value, "codcaja");
			return (Criteria) this;
		}

		public Criteria andCodcajaNotEqualTo(String value) {
			addCriterion("codcaja <>", value, "codcaja");
			return (Criteria) this;
		}

		public Criteria andCodcajaGreaterThan(String value) {
			addCriterion("codcaja >", value, "codcaja");
			return (Criteria) this;
		}

		public Criteria andCodcajaGreaterThanOrEqualTo(String value) {
			addCriterion("codcaja >=", value, "codcaja");
			return (Criteria) this;
		}

		public Criteria andCodcajaLessThan(String value) {
			addCriterion("codcaja <", value, "codcaja");
			return (Criteria) this;
		}

		public Criteria andCodcajaLessThanOrEqualTo(String value) {
			addCriterion("codcaja <=", value, "codcaja");
			return (Criteria) this;
		}

		public Criteria andCodcajaLike(String value) {
			addCriterion("codcaja like", value, "codcaja");
			return (Criteria) this;
		}

		public Criteria andCodcajaNotLike(String value) {
			addCriterion("codcaja not like", value, "codcaja");
			return (Criteria) this;
		}

		public Criteria andCodcajaIn(List<String> values) {
			addCriterion("codcaja in", values, "codcaja");
			return (Criteria) this;
		}

		public Criteria andCodcajaNotIn(List<String> values) {
			addCriterion("codcaja not in", values, "codcaja");
			return (Criteria) this;
		}

		public Criteria andCodcajaBetween(String value1, String value2) {
			addCriterion("codcaja between", value1, value2, "codcaja");
			return (Criteria) this;
		}

		public Criteria andCodcajaNotBetween(String value1, String value2) {
			addCriterion("codcaja not between", value1, value2, "codcaja");
			return (Criteria) this;
		}

		public Criteria andFechaAperturaIsNull() {
			addCriterion("fecha_apertura is null");
			return (Criteria) this;
		}

		public Criteria andFechaAperturaIsNotNull() {
			addCriterion("fecha_apertura is not null");
			return (Criteria) this;
		}

		public Criteria andFechaAperturaEqualTo(Date value) {
			addCriterion("fecha_apertura =", value, "fechaApertura");
			return (Criteria) this;
		}

		public Criteria andFechaAperturaNotEqualTo(Date value) {
			addCriterion("fecha_apertura <>", value, "fechaApertura");
			return (Criteria) this;
		}

		public Criteria andFechaAperturaGreaterThan(Date value) {
			addCriterion("fecha_apertura >", value, "fechaApertura");
			return (Criteria) this;
		}

		public Criteria andFechaAperturaGreaterThanOrEqualTo(Date value) {
			addCriterion("fecha_apertura >=", value, "fechaApertura");
			return (Criteria) this;
		}

		public Criteria andFechaAperturaLessThan(Date value) {
			addCriterion("fecha_apertura <", value, "fechaApertura");
			return (Criteria) this;
		}

		public Criteria andFechaAperturaLessThanOrEqualTo(Date value) {
			addCriterion("fecha_apertura <=", value, "fechaApertura");
			return (Criteria) this;
		}

		public Criteria andFechaAperturaIn(List<Date> values) {
			addCriterion("fecha_apertura in", values, "fechaApertura");
			return (Criteria) this;
		}

		public Criteria andFechaAperturaNotIn(List<Date> values) {
			addCriterion("fecha_apertura not in", values, "fechaApertura");
			return (Criteria) this;
		}

		public Criteria andFechaAperturaBetween(Date value1, Date value2) {
			addCriterion("fecha_apertura between", value1, value2, "fechaApertura");
			return (Criteria) this;
		}

		public Criteria andFechaAperturaNotBetween(Date value1, Date value2) {
			addCriterion("fecha_apertura not between", value1, value2, "fechaApertura");
			return (Criteria) this;
		}

		public Criteria andFechaCierreIsNull() {
			addCriterion("fecha_cierre is null");
			return (Criteria) this;
		}

		public Criteria andFechaCierreIsNotNull() {
			addCriterion("fecha_cierre is not null");
			return (Criteria) this;
		}

		public Criteria andFechaCierreEqualTo(Date value) {
			addCriterion("fecha_cierre =", value, "fechaCierre");
			return (Criteria) this;
		}

		public Criteria andFechaCierreNotEqualTo(Date value) {
			addCriterion("fecha_cierre <>", value, "fechaCierre");
			return (Criteria) this;
		}

		public Criteria andFechaCierreGreaterThan(Date value) {
			addCriterion("fecha_cierre >", value, "fechaCierre");
			return (Criteria) this;
		}

		public Criteria andFechaCierreGreaterThanOrEqualTo(Date value) {
			addCriterion("fecha_cierre >=", value, "fechaCierre");
			return (Criteria) this;
		}

		public Criteria andFechaCierreLessThan(Date value) {
			addCriterion("fecha_cierre <", value, "fechaCierre");
			return (Criteria) this;
		}

		public Criteria andFechaCierreLessThanOrEqualTo(Date value) {
			addCriterion("fecha_cierre <=", value, "fechaCierre");
			return (Criteria) this;
		}

		public Criteria andFechaCierreIn(List<Date> values) {
			addCriterion("fecha_cierre in", values, "fechaCierre");
			return (Criteria) this;
		}

		public Criteria andFechaCierreNotIn(List<Date> values) {
			addCriterion("fecha_cierre not in", values, "fechaCierre");
			return (Criteria) this;
		}

		public Criteria andFechaCierreBetween(Date value1, Date value2) {
			addCriterion("fecha_cierre between", value1, value2, "fechaCierre");
			return (Criteria) this;
		}

		public Criteria andFechaCierreNotBetween(Date value1, Date value2) {
			addCriterion("fecha_cierre not between", value1, value2, "fechaCierre");
			return (Criteria) this;
		}

		public Criteria andFechaEnvioIsNull() {
			addCriterion("fecha_envio is null");
			return (Criteria) this;
		}

		public Criteria andFechaEnvioIsNotNull() {
			addCriterion("fecha_envio is not null");
			return (Criteria) this;
		}

		public Criteria andFechaEnvioEqualTo(Date value) {
			addCriterion("fecha_envio =", value, "fechaEnvio");
			return (Criteria) this;
		}

		public Criteria andFechaEnvioNotEqualTo(Date value) {
			addCriterion("fecha_envio <>", value, "fechaEnvio");
			return (Criteria) this;
		}

		public Criteria andFechaEnvioGreaterThan(Date value) {
			addCriterion("fecha_envio >", value, "fechaEnvio");
			return (Criteria) this;
		}

		public Criteria andFechaEnvioGreaterThanOrEqualTo(Date value) {
			addCriterion("fecha_envio >=", value, "fechaEnvio");
			return (Criteria) this;
		}

		public Criteria andFechaEnvioLessThan(Date value) {
			addCriterion("fecha_envio <", value, "fechaEnvio");
			return (Criteria) this;
		}

		public Criteria andFechaEnvioLessThanOrEqualTo(Date value) {
			addCriterion("fecha_envio <=", value, "fechaEnvio");
			return (Criteria) this;
		}

		public Criteria andFechaEnvioIn(List<Date> values) {
			addCriterion("fecha_envio in", values, "fechaEnvio");
			return (Criteria) this;
		}

		public Criteria andFechaEnvioNotIn(List<Date> values) {
			addCriterion("fecha_envio not in", values, "fechaEnvio");
			return (Criteria) this;
		}

		public Criteria andFechaEnvioBetween(Date value1, Date value2) {
			addCriterion("fecha_envio between", value1, value2, "fechaEnvio");
			return (Criteria) this;
		}

		public Criteria andFechaEnvioNotBetween(Date value1, Date value2) {
			addCriterion("fecha_envio not between", value1, value2, "fechaEnvio");
			return (Criteria) this;
		}

		public Criteria andUsuarioIsNull() {
			addCriterion("usuario is null");
			return (Criteria) this;
		}

		public Criteria andUsuarioIsNotNull() {
			addCriterion("usuario is not null");
			return (Criteria) this;
		}

		public Criteria andUsuarioEqualTo(String value) {
			addCriterion("usuario =", value, "usuario");
			return (Criteria) this;
		}

		public Criteria andUsuarioNotEqualTo(String value) {
			addCriterion("usuario <>", value, "usuario");
			return (Criteria) this;
		}

		public Criteria andUsuarioGreaterThan(String value) {
			addCriterion("usuario >", value, "usuario");
			return (Criteria) this;
		}

		public Criteria andUsuarioGreaterThanOrEqualTo(String value) {
			addCriterion("usuario >=", value, "usuario");
			return (Criteria) this;
		}

		public Criteria andUsuarioLessThan(String value) {
			addCriterion("usuario <", value, "usuario");
			return (Criteria) this;
		}

		public Criteria andUsuarioLessThanOrEqualTo(String value) {
			addCriterion("usuario <=", value, "usuario");
			return (Criteria) this;
		}

		public Criteria andUsuarioLike(String value) {
			addCriterion("usuario like", value, "usuario");
			return (Criteria) this;
		}

		public Criteria andUsuarioNotLike(String value) {
			addCriterion("usuario not like", value, "usuario");
			return (Criteria) this;
		}

		public Criteria andUsuarioIn(List<String> values) {
			addCriterion("usuario in", values, "usuario");
			return (Criteria) this;
		}

		public Criteria andUsuarioNotIn(List<String> values) {
			addCriterion("usuario not in", values, "usuario");
			return (Criteria) this;
		}

		public Criteria andUsuarioBetween(String value1, String value2) {
			addCriterion("usuario between", value1, value2, "usuario");
			return (Criteria) this;
		}

		public Criteria andUsuarioNotBetween(String value1, String value2) {
			addCriterion("usuario not between", value1, value2, "usuario");
			return (Criteria) this;
		}

		public Criteria andUsuarioCierreIsNull() {
			addCriterion("usuario_cierre is null");
			return (Criteria) this;
		}

		public Criteria andUsuarioCierreIsNotNull() {
			addCriterion("usuario_cierre is not null");
			return (Criteria) this;
		}

		public Criteria andUsuarioCierreEqualTo(String value) {
			addCriterion("usuario_cierre =", value, "usuarioCierre");
			return (Criteria) this;
		}

		public Criteria andUsuarioCierreNotEqualTo(String value) {
			addCriterion("usuario_cierre <>", value, "usuarioCierre");
			return (Criteria) this;
		}

		public Criteria andUsuarioCierreGreaterThan(String value) {
			addCriterion("usuario_cierre >", value, "usuarioCierre");
			return (Criteria) this;
		}

		public Criteria andUsuarioCierreGreaterThanOrEqualTo(String value) {
			addCriterion("usuario_cierre >=", value, "usuarioCierre");
			return (Criteria) this;
		}

		public Criteria andUsuarioCierreLessThan(String value) {
			addCriterion("usuario_cierre <", value, "usuarioCierre");
			return (Criteria) this;
		}

		public Criteria andUsuarioCierreLessThanOrEqualTo(String value) {
			addCriterion("usuario_cierre <=", value, "usuarioCierre");
			return (Criteria) this;
		}

		public Criteria andUsuarioCierreLike(String value) {
			addCriterion("usuario_cierre like", value, "usuarioCierre");
			return (Criteria) this;
		}

		public Criteria andUsuarioCierreNotLike(String value) {
			addCriterion("usuario_cierre not like", value, "usuarioCierre");
			return (Criteria) this;
		}

		public Criteria andUsuarioCierreIn(List<String> values) {
			addCriterion("usuario_cierre in", values, "usuarioCierre");
			return (Criteria) this;
		}

		public Criteria andUsuarioCierreNotIn(List<String> values) {
			addCriterion("usuario_cierre not in", values, "usuarioCierre");
			return (Criteria) this;
		}

		public Criteria andUsuarioCierreBetween(String value1, String value2) {
			addCriterion("usuario_cierre between", value1, value2, "usuarioCierre");
			return (Criteria) this;
		}

		public Criteria andUsuarioCierreNotBetween(String value1, String value2) {
			addCriterion("usuario_cierre not between", value1, value2, "usuarioCierre");
			return (Criteria) this;
		}

		public Criteria andFechaContableIsNull() {
			addCriterion("fecha_contable is null");
			return (Criteria) this;
		}

		public Criteria andFechaContableIsNotNull() {
			addCriterion("fecha_contable is not null");
			return (Criteria) this;
		}

		public Criteria andFechaContableEqualTo(Date value) {
			addCriterion("fecha_contable =", value, "fechaContable");
			return (Criteria) this;
		}

		public Criteria andFechaContableNotEqualTo(Date value) {
			addCriterion("fecha_contable <>", value, "fechaContable");
			return (Criteria) this;
		}

		public Criteria andFechaContableGreaterThan(Date value) {
			addCriterion("fecha_contable >", value, "fechaContable");
			return (Criteria) this;
		}

		public Criteria andFechaContableGreaterThanOrEqualTo(Date value) {
			addCriterion("fecha_contable >=", value, "fechaContable");
			return (Criteria) this;
		}

		public Criteria andFechaContableLessThan(Date value) {
			addCriterion("fecha_contable <", value, "fechaContable");
			return (Criteria) this;
		}

		public Criteria andFechaContableLessThanOrEqualTo(Date value) {
			addCriterion("fecha_contable <=", value, "fechaContable");
			return (Criteria) this;
		}

		public Criteria andFechaContableIn(List<Date> values) {
			addCriterion("fecha_contable in", values, "fechaContable");
			return (Criteria) this;
		}

		public Criteria andFechaContableNotIn(List<Date> values) {
			addCriterion("fecha_contable not in", values, "fechaContable");
			return (Criteria) this;
		}

		public Criteria andFechaContableBetween(Date value1, Date value2) {
			addCriterion("fecha_contable between", value1, value2, "fechaContable");
			return (Criteria) this;
		}

		public Criteria andFechaContableNotBetween(Date value1, Date value2) {
			addCriterion("fecha_contable not between", value1, value2, "fechaContable");
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