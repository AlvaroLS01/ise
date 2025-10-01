package com.comerzzia.bricodepot.backoffice.persistence.tickets.codigopostal;

import java.util.ArrayList;
import java.util.List;

public class CodigoPostalBeanExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CodigoPostalBeanExample() {
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

        public Criteria andIdClieAlbaranIsNull() {
            addCriterion("ID_CLIE_ALBARAN is null");
            return (Criteria) this;
        }

        public Criteria andIdClieAlbaranIsNotNull() {
            addCriterion("ID_CLIE_ALBARAN is not null");
            return (Criteria) this;
        }

        public Criteria andIdClieAlbaranEqualTo(Long value) {
            addCriterion("ID_CLIE_ALBARAN =", value, "idClieAlbaran");
            return (Criteria) this;
        }

        public Criteria andIdClieAlbaranNotEqualTo(Long value) {
            addCriterion("ID_CLIE_ALBARAN <>", value, "idClieAlbaran");
            return (Criteria) this;
        }

        public Criteria andIdClieAlbaranGreaterThan(Long value) {
            addCriterion("ID_CLIE_ALBARAN >", value, "idClieAlbaran");
            return (Criteria) this;
        }

        public Criteria andIdClieAlbaranGreaterThanOrEqualTo(Long value) {
            addCriterion("ID_CLIE_ALBARAN >=", value, "idClieAlbaran");
            return (Criteria) this;
        }

        public Criteria andIdClieAlbaranLessThan(Long value) {
            addCriterion("ID_CLIE_ALBARAN <", value, "idClieAlbaran");
            return (Criteria) this;
        }

        public Criteria andIdClieAlbaranLessThanOrEqualTo(Long value) {
            addCriterion("ID_CLIE_ALBARAN <=", value, "idClieAlbaran");
            return (Criteria) this;
        }

        public Criteria andIdClieAlbaranIn(List<Long> values) {
            addCriterion("ID_CLIE_ALBARAN in", values, "idClieAlbaran");
            return (Criteria) this;
        }

        public Criteria andIdClieAlbaranNotIn(List<Long> values) {
            addCriterion("ID_CLIE_ALBARAN not in", values, "idClieAlbaran");
            return (Criteria) this;
        }

        public Criteria andIdClieAlbaranBetween(Long value1, Long value2) {
            addCriterion("ID_CLIE_ALBARAN between", value1, value2, "idClieAlbaran");
            return (Criteria) this;
        }

        public Criteria andIdClieAlbaranNotBetween(Long value1, Long value2) {
            addCriterion("ID_CLIE_ALBARAN not between", value1, value2, "idClieAlbaran");
            return (Criteria) this;
        }

        public Criteria andCodigoPostalIsNull() {
            addCriterion("CODIGO_POSTAL is null");
            return (Criteria) this;
        }

        public Criteria andCodigoPostalIsNotNull() {
            addCriterion("CODIGO_POSTAL is not null");
            return (Criteria) this;
        }

        public Criteria andCodigoPostalEqualTo(String value) {
            addCriterion("CODIGO_POSTAL =", value, "codigoPostal");
            return (Criteria) this;
        }

        public Criteria andCodigoPostalNotEqualTo(String value) {
            addCriterion("CODIGO_POSTAL <>", value, "codigoPostal");
            return (Criteria) this;
        }

        public Criteria andCodigoPostalGreaterThan(String value) {
            addCriterion("CODIGO_POSTAL >", value, "codigoPostal");
            return (Criteria) this;
        }

        public Criteria andCodigoPostalGreaterThanOrEqualTo(String value) {
            addCriterion("CODIGO_POSTAL >=", value, "codigoPostal");
            return (Criteria) this;
        }

        public Criteria andCodigoPostalLessThan(String value) {
            addCriterion("CODIGO_POSTAL <", value, "codigoPostal");
            return (Criteria) this;
        }

        public Criteria andCodigoPostalLessThanOrEqualTo(String value) {
            addCriterion("CODIGO_POSTAL <=", value, "codigoPostal");
            return (Criteria) this;
        }

        public Criteria andCodigoPostalLike(String value) {
            addCriterion("CODIGO_POSTAL like", value, "codigoPostal");
            return (Criteria) this;
        }

        public Criteria andCodigoPostalNotLike(String value) {
            addCriterion("CODIGO_POSTAL not like", value, "codigoPostal");
            return (Criteria) this;
        }

        public Criteria andCodigoPostalIn(List<String> values) {
            addCriterion("CODIGO_POSTAL in", values, "codigoPostal");
            return (Criteria) this;
        }

        public Criteria andCodigoPostalNotIn(List<String> values) {
            addCriterion("CODIGO_POSTAL not in", values, "codigoPostal");
            return (Criteria) this;
        }

        public Criteria andCodigoPostalBetween(String value1, String value2) {
            addCriterion("CODIGO_POSTAL between", value1, value2, "codigoPostal");
            return (Criteria) this;
        }

        public Criteria andCodigoPostalNotBetween(String value1, String value2) {
            addCriterion("CODIGO_POSTAL not between", value1, value2, "codigoPostal");
            return (Criteria) this;
        }

        public Criteria andUidActividadLikeInsensitive(String value) {
            addCriterion("upper(UID_ACTIVIDAD) like", value.toUpperCase(), "uidActividad");
            return (Criteria) this;
        }

        public Criteria andCodigoPostalLikeInsensitive(String value) {
            addCriterion("upper(CODIGO_POSTAL) like", value.toUpperCase(), "codigoPostal");
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