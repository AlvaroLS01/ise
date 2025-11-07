package com.comerzzia.bimbaylola.pos.persistence.movimientostarjeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CajaMovimientoTarjetaExample {
	
	public static final String ORDER_BY_UID_ACTIVIDAD = "UID_ACTIVIDAD";
	public static final String ORDER_BY_UID_ACTIVIDAD_DESC = "UID_ACTIVIDAD DESC";
	public static final String ORDER_BY_UID_DIARIO_CAJA = "UID_DIARIO_CAJA";
	public static final String ORDER_BY_UID_DIARIO_CAJA_DESC = "UID_DIARIO_CAJA DESC";
	public static final String ORDER_BY_LINEA = "LINEA";
	public static final String ORDER_BY_LINEA_DESC = "LINEA DESC";
	public static final String ORDER_BY_RESPUESTA_TARJETA = "RESPUESTA_TARJETA";
	public static final String ORDER_BY_RESPUESTA_TARJETA_DESC = "RESPUESTA_TARJETA DESC";
	
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CajaMovimientoTarjetaExample() {
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

        public Criteria andLineaIsNull() {
            addCriterion("LINEA is null");
            return (Criteria) this;
        }

        public Criteria andLineaIsNotNull() {
            addCriterion("LINEA is not null");
            return (Criteria) this;
        }

        public Criteria andLineaEqualTo(Integer value) {
            addCriterion("LINEA =", value, "linea");
            return (Criteria) this;
        }

        public Criteria andLineaNotEqualTo(Integer value) {
            addCriterion("LINEA <>", value, "linea");
            return (Criteria) this;
        }

        public Criteria andLineaGreaterThan(Integer value) {
            addCriterion("LINEA >", value, "linea");
            return (Criteria) this;
        }

        public Criteria andLineaGreaterThanOrEqualTo(Integer value) {
            addCriterion("LINEA >=", value, "linea");
            return (Criteria) this;
        }

        public Criteria andLineaLessThan(Integer value) {
            addCriterion("LINEA <", value, "linea");
            return (Criteria) this;
        }

        public Criteria andLineaLessThanOrEqualTo(Integer value) {
            addCriterion("LINEA <=", value, "linea");
            return (Criteria) this;
        }

        public Criteria andLineaIn(List<Integer> values) {
            addCriterion("LINEA in", values, "linea");
            return (Criteria) this;
        }

        public Criteria andLineaNotIn(List<Integer> values) {
            addCriterion("LINEA not in", values, "linea");
            return (Criteria) this;
        }

        public Criteria andLineaBetween(Integer value1, Integer value2) {
            addCriterion("LINEA between", value1, value2, "linea");
            return (Criteria) this;
        }

        public Criteria andLineaNotBetween(Integer value1, Integer value2) {
            addCriterion("LINEA not between", value1, value2, "linea");
            return (Criteria) this;
        }

        public Criteria andBaseIsNull() {
            addCriterion("BASE is null");
            return (Criteria) this;
        }

        public Criteria andBaseIsNotNull() {
            addCriterion("BASE is not null");
            return (Criteria) this;
        }

        public Criteria andBaseEqualTo(BigDecimal value) {
            addCriterion("BASE =", value, "base");
            return (Criteria) this;
        }

        public Criteria andBaseNotEqualTo(BigDecimal value) {
            addCriterion("BASE <>", value, "base");
            return (Criteria) this;
        }

        public Criteria andBaseGreaterThan(BigDecimal value) {
            addCriterion("BASE >", value, "base");
            return (Criteria) this;
        }

        public Criteria andBaseGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("BASE >=", value, "base");
            return (Criteria) this;
        }

        public Criteria andBaseLessThan(BigDecimal value) {
            addCriterion("BASE <", value, "base");
            return (Criteria) this;
        }

        public Criteria andBaseLessThanOrEqualTo(BigDecimal value) {
            addCriterion("BASE <=", value, "base");
            return (Criteria) this;
        }

        public Criteria andBaseIn(List<BigDecimal> values) {
            addCriterion("BASE in", values, "base");
            return (Criteria) this;
        }

        public Criteria andBaseNotIn(List<BigDecimal> values) {
            addCriterion("BASE not in", values, "base");
            return (Criteria) this;
        }

        public Criteria andBaseBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("BASE between", value1, value2, "base");
            return (Criteria) this;
        }

        public Criteria andBaseNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("BASE not between", value1, value2, "base");
            return (Criteria) this;
        }

        public Criteria andImpuestosIsNull() {
            addCriterion("IMPUESTOS is null");
            return (Criteria) this;
        }

        public Criteria andImpuestosIsNotNull() {
            addCriterion("IMPUESTOS is not null");
            return (Criteria) this;
        }

        public Criteria andImpuestosEqualTo(BigDecimal value) {
            addCriterion("IMPUESTOS =", value, "impuestos");
            return (Criteria) this;
        }

        public Criteria andImpuestosNotEqualTo(BigDecimal value) {
            addCriterion("IMPUESTOS <>", value, "impuestos");
            return (Criteria) this;
        }

        public Criteria andImpuestosGreaterThan(BigDecimal value) {
            addCriterion("IMPUESTOS >", value, "impuestos");
            return (Criteria) this;
        }

        public Criteria andImpuestosGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("IMPUESTOS >=", value, "impuestos");
            return (Criteria) this;
        }

        public Criteria andImpuestosLessThan(BigDecimal value) {
            addCriterion("IMPUESTOS <", value, "impuestos");
            return (Criteria) this;
        }

        public Criteria andImpuestosLessThanOrEqualTo(BigDecimal value) {
            addCriterion("IMPUESTOS <=", value, "impuestos");
            return (Criteria) this;
        }

        public Criteria andImpuestosIn(List<BigDecimal> values) {
            addCriterion("IMPUESTOS in", values, "impuestos");
            return (Criteria) this;
        }

        public Criteria andImpuestosNotIn(List<BigDecimal> values) {
            addCriterion("IMPUESTOS not in", values, "impuestos");
            return (Criteria) this;
        }

        public Criteria andImpuestosBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("IMPUESTOS between", value1, value2, "impuestos");
            return (Criteria) this;
        }

        public Criteria andImpuestosNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("IMPUESTOS not between", value1, value2, "impuestos");
            return (Criteria) this;
        }

        public Criteria andUidActividadLikeInsensitive(String value) {
            addCriterion("upper(UID_ACTIVIDAD) like", value.toUpperCase(), "uidActividad");
            return (Criteria) this;
        }

        public Criteria andUidDiarioCajaLikeInsensitive(String value) {
            addCriterion("upper(UID_DIARIO_CAJA) like", value.toUpperCase(), "uidDiarioCaja");
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
            } else {
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