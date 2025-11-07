package com.comerzzia.bimbaylola.pos.persistence.apartados;

import java.util.ArrayList;
import java.util.List;

public class ByLApartadosCabeceraExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ByLApartadosCabeceraExample() {
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

        public Criteria andUidApartadoIsNull() {
            addCriterion("uid_apartado is null");
            return (Criteria) this;
        }

        public Criteria andUidApartadoIsNotNull() {
            addCriterion("uid_apartado is not null");
            return (Criteria) this;
        }

        public Criteria andUidApartadoEqualTo(String value) {
            addCriterion("uid_apartado =", value, "uidApartado");
            return (Criteria) this;
        }

        public Criteria andUidApartadoNotEqualTo(String value) {
            addCriterion("uid_apartado <>", value, "uidApartado");
            return (Criteria) this;
        }

        public Criteria andUidApartadoGreaterThan(String value) {
            addCriterion("uid_apartado >", value, "uidApartado");
            return (Criteria) this;
        }

        public Criteria andUidApartadoGreaterThanOrEqualTo(String value) {
            addCriterion("uid_apartado >=", value, "uidApartado");
            return (Criteria) this;
        }

        public Criteria andUidApartadoLessThan(String value) {
            addCriterion("uid_apartado <", value, "uidApartado");
            return (Criteria) this;
        }

        public Criteria andUidApartadoLessThanOrEqualTo(String value) {
            addCriterion("uid_apartado <=", value, "uidApartado");
            return (Criteria) this;
        }

        public Criteria andUidApartadoLike(String value) {
            addCriterion("uid_apartado like", value, "uidApartado");
            return (Criteria) this;
        }

        public Criteria andUidApartadoNotLike(String value) {
            addCriterion("uid_apartado not like", value, "uidApartado");
            return (Criteria) this;
        }

        public Criteria andUidApartadoIn(List<String> values) {
            addCriterion("uid_apartado in", values, "uidApartado");
            return (Criteria) this;
        }

        public Criteria andUidApartadoNotIn(List<String> values) {
            addCriterion("uid_apartado not in", values, "uidApartado");
            return (Criteria) this;
        }

        public Criteria andUidApartadoBetween(String value1, String value2) {
            addCriterion("uid_apartado between", value1, value2, "uidApartado");
            return (Criteria) this;
        }

        public Criteria andUidApartadoNotBetween(String value1, String value2) {
            addCriterion("uid_apartado not between", value1, value2, "uidApartado");
            return (Criteria) this;
        }

        public Criteria andCodCajaIsNull() {
            addCriterion("cod_caja is null");
            return (Criteria) this;
        }

        public Criteria andCodCajaIsNotNull() {
            addCriterion("cod_caja is not null");
            return (Criteria) this;
        }

        public Criteria andCodCajaEqualTo(String value) {
            addCriterion("cod_caja =", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaNotEqualTo(String value) {
            addCriterion("cod_caja <>", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaGreaterThan(String value) {
            addCriterion("cod_caja >", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaGreaterThanOrEqualTo(String value) {
            addCriterion("cod_caja >=", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaLessThan(String value) {
            addCriterion("cod_caja <", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaLessThanOrEqualTo(String value) {
            addCriterion("cod_caja <=", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaLike(String value) {
            addCriterion("cod_caja like", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaNotLike(String value) {
            addCriterion("cod_caja not like", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaIn(List<String> values) {
            addCriterion("cod_caja in", values, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaNotIn(List<String> values) {
            addCriterion("cod_caja not in", values, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaBetween(String value1, String value2) {
            addCriterion("cod_caja between", value1, value2, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaNotBetween(String value1, String value2) {
            addCriterion("cod_caja not between", value1, value2, "codCaja");
            return (Criteria) this;
        }

        public Criteria andUidActividadLikeInsensitive(String value) {
            addCriterion("upper(uid_actividad) like", value.toUpperCase(), "uidActividad");
            return (Criteria) this;
        }

        public Criteria andUidApartadoLikeInsensitive(String value) {
            addCriterion("upper(uid_apartado) like", value.toUpperCase(), "uidApartado");
            return (Criteria) this;
        }

        public Criteria andCodCajaLikeInsensitive(String value) {
            addCriterion("upper(cod_caja) like", value.toUpperCase(), "codCaja");
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