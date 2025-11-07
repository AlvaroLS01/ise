package com.comerzzia.bimbaylola.pos.persistence.core.contadores;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ByLContadorExample {
    public static final String ORDER_BY_UID_ACTIVIDAD = "UID_ACTIVIDAD";

    public static final String ORDER_BY_UID_ACTIVIDAD_DESC = "UID_ACTIVIDAD DESC";

    public static final String ORDER_BY_ID_CONTADOR = "ID_CONTADOR";

    public static final String ORDER_BY_ID_CONTADOR_DESC = "ID_CONTADOR DESC";

    public static final String ORDER_BY_DIVISOR1 = "DIVISOR1";

    public static final String ORDER_BY_DIVISOR1_DESC = "DIVISOR1 DESC";

    public static final String ORDER_BY_DIVISOR2 = "DIVISOR2";

    public static final String ORDER_BY_DIVISOR2_DESC = "DIVISOR2 DESC";

    public static final String ORDER_BY_DIVISOR3 = "DIVISOR3";

    public static final String ORDER_BY_DIVISOR3_DESC = "DIVISOR3 DESC";

    public static final String ORDER_BY_VALOR = "VALOR";

    public static final String ORDER_BY_VALOR_DESC = "VALOR DESC";

    public static final String ORDER_BY_ULTIMA_PETICION = "ULTIMA_PETICION";

    public static final String ORDER_BY_ULTIMA_PETICION_DESC = "ULTIMA_PETICION DESC";

    public static final String ORDER_BY_ID_RANGO_ULTIMA_PETICION = "ID_RANGO_ULTIMA_PETICION";

    public static final String ORDER_BY_ID_RANGO_ULTIMA_PETICION_DESC = "ID_RANGO_ULTIMA_PETICION DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ByLContadorExample() {
        oredCriteria = new ArrayList<Criteria>();
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

        public Criteria andIdContadorIsNull() {
            addCriterion("ID_CONTADOR is null");
            return (Criteria) this;
        }

        public Criteria andIdContadorIsNotNull() {
            addCriterion("ID_CONTADOR is not null");
            return (Criteria) this;
        }

        public Criteria andIdContadorEqualTo(String value) {
            addCriterion("ID_CONTADOR =", value, "idContador");
            return (Criteria) this;
        }

        public Criteria andIdContadorNotEqualTo(String value) {
            addCriterion("ID_CONTADOR <>", value, "idContador");
            return (Criteria) this;
        }

        public Criteria andIdContadorGreaterThan(String value) {
            addCriterion("ID_CONTADOR >", value, "idContador");
            return (Criteria) this;
        }

        public Criteria andIdContadorGreaterThanOrEqualTo(String value) {
            addCriterion("ID_CONTADOR >=", value, "idContador");
            return (Criteria) this;
        }

        public Criteria andIdContadorLessThan(String value) {
            addCriterion("ID_CONTADOR <", value, "idContador");
            return (Criteria) this;
        }

        public Criteria andIdContadorLessThanOrEqualTo(String value) {
            addCriterion("ID_CONTADOR <=", value, "idContador");
            return (Criteria) this;
        }

        public Criteria andIdContadorLike(String value) {
            addCriterion("ID_CONTADOR like", value, "idContador");
            return (Criteria) this;
        }

        public Criteria andIdContadorNotLike(String value) {
            addCriterion("ID_CONTADOR not like", value, "idContador");
            return (Criteria) this;
        }

        public Criteria andIdContadorIn(List<String> values) {
            addCriterion("ID_CONTADOR in", values, "idContador");
            return (Criteria) this;
        }

        public Criteria andIdContadorNotIn(List<String> values) {
            addCriterion("ID_CONTADOR not in", values, "idContador");
            return (Criteria) this;
        }

        public Criteria andIdContadorBetween(String value1, String value2) {
            addCriterion("ID_CONTADOR between", value1, value2, "idContador");
            return (Criteria) this;
        }

        public Criteria andIdContadorNotBetween(String value1, String value2) {
            addCriterion("ID_CONTADOR not between", value1, value2, "idContador");
            return (Criteria) this;
        }

        public Criteria andDivisor1IsNull() {
            addCriterion("DIVISOR1 is null");
            return (Criteria) this;
        }

        public Criteria andDivisor1IsNotNull() {
            addCriterion("DIVISOR1 is not null");
            return (Criteria) this;
        }

        public Criteria andDivisor1EqualTo(String value) {
            addCriterion("DIVISOR1 =", value, "divisor1");
            return (Criteria) this;
        }

        public Criteria andDivisor1NotEqualTo(String value) {
            addCriterion("DIVISOR1 <>", value, "divisor1");
            return (Criteria) this;
        }

        public Criteria andDivisor1GreaterThan(String value) {
            addCriterion("DIVISOR1 >", value, "divisor1");
            return (Criteria) this;
        }

        public Criteria andDivisor1GreaterThanOrEqualTo(String value) {
            addCriterion("DIVISOR1 >=", value, "divisor1");
            return (Criteria) this;
        }

        public Criteria andDivisor1LessThan(String value) {
            addCriterion("DIVISOR1 <", value, "divisor1");
            return (Criteria) this;
        }

        public Criteria andDivisor1LessThanOrEqualTo(String value) {
            addCriterion("DIVISOR1 <=", value, "divisor1");
            return (Criteria) this;
        }

        public Criteria andDivisor1Like(String value) {
            addCriterion("DIVISOR1 like", value, "divisor1");
            return (Criteria) this;
        }

        public Criteria andDivisor1NotLike(String value) {
            addCriterion("DIVISOR1 not like", value, "divisor1");
            return (Criteria) this;
        }

        public Criteria andDivisor1In(List<String> values) {
            addCriterion("DIVISOR1 in", values, "divisor1");
            return (Criteria) this;
        }

        public Criteria andDivisor1NotIn(List<String> values) {
            addCriterion("DIVISOR1 not in", values, "divisor1");
            return (Criteria) this;
        }

        public Criteria andDivisor1Between(String value1, String value2) {
            addCriterion("DIVISOR1 between", value1, value2, "divisor1");
            return (Criteria) this;
        }

        public Criteria andDivisor1NotBetween(String value1, String value2) {
            addCriterion("DIVISOR1 not between", value1, value2, "divisor1");
            return (Criteria) this;
        }

        public Criteria andDivisor2IsNull() {
            addCriterion("DIVISOR2 is null");
            return (Criteria) this;
        }

        public Criteria andDivisor2IsNotNull() {
            addCriterion("DIVISOR2 is not null");
            return (Criteria) this;
        }

        public Criteria andDivisor2EqualTo(String value) {
            addCriterion("DIVISOR2 =", value, "divisor2");
            return (Criteria) this;
        }

        public Criteria andDivisor2NotEqualTo(String value) {
            addCriterion("DIVISOR2 <>", value, "divisor2");
            return (Criteria) this;
        }

        public Criteria andDivisor2GreaterThan(String value) {
            addCriterion("DIVISOR2 >", value, "divisor2");
            return (Criteria) this;
        }

        public Criteria andDivisor2GreaterThanOrEqualTo(String value) {
            addCriterion("DIVISOR2 >=", value, "divisor2");
            return (Criteria) this;
        }

        public Criteria andDivisor2LessThan(String value) {
            addCriterion("DIVISOR2 <", value, "divisor2");
            return (Criteria) this;
        }

        public Criteria andDivisor2LessThanOrEqualTo(String value) {
            addCriterion("DIVISOR2 <=", value, "divisor2");
            return (Criteria) this;
        }

        public Criteria andDivisor2Like(String value) {
            addCriterion("DIVISOR2 like", value, "divisor2");
            return (Criteria) this;
        }

        public Criteria andDivisor2NotLike(String value) {
            addCriterion("DIVISOR2 not like", value, "divisor2");
            return (Criteria) this;
        }

        public Criteria andDivisor2In(List<String> values) {
            addCriterion("DIVISOR2 in", values, "divisor2");
            return (Criteria) this;
        }

        public Criteria andDivisor2NotIn(List<String> values) {
            addCriterion("DIVISOR2 not in", values, "divisor2");
            return (Criteria) this;
        }

        public Criteria andDivisor2Between(String value1, String value2) {
            addCriterion("DIVISOR2 between", value1, value2, "divisor2");
            return (Criteria) this;
        }

        public Criteria andDivisor2NotBetween(String value1, String value2) {
            addCriterion("DIVISOR2 not between", value1, value2, "divisor2");
            return (Criteria) this;
        }

        public Criteria andDivisor3IsNull() {
            addCriterion("DIVISOR3 is null");
            return (Criteria) this;
        }

        public Criteria andDivisor3IsNotNull() {
            addCriterion("DIVISOR3 is not null");
            return (Criteria) this;
        }

        public Criteria andDivisor3EqualTo(String value) {
            addCriterion("DIVISOR3 =", value, "divisor3");
            return (Criteria) this;
        }

        public Criteria andDivisor3NotEqualTo(String value) {
            addCriterion("DIVISOR3 <>", value, "divisor3");
            return (Criteria) this;
        }

        public Criteria andDivisor3GreaterThan(String value) {
            addCriterion("DIVISOR3 >", value, "divisor3");
            return (Criteria) this;
        }

        public Criteria andDivisor3GreaterThanOrEqualTo(String value) {
            addCriterion("DIVISOR3 >=", value, "divisor3");
            return (Criteria) this;
        }

        public Criteria andDivisor3LessThan(String value) {
            addCriterion("DIVISOR3 <", value, "divisor3");
            return (Criteria) this;
        }

        public Criteria andDivisor3LessThanOrEqualTo(String value) {
            addCriterion("DIVISOR3 <=", value, "divisor3");
            return (Criteria) this;
        }

        public Criteria andDivisor3Like(String value) {
            addCriterion("DIVISOR3 like", value, "divisor3");
            return (Criteria) this;
        }

        public Criteria andDivisor3NotLike(String value) {
            addCriterion("DIVISOR3 not like", value, "divisor3");
            return (Criteria) this;
        }

        public Criteria andDivisor3In(List<String> values) {
            addCriterion("DIVISOR3 in", values, "divisor3");
            return (Criteria) this;
        }

        public Criteria andDivisor3NotIn(List<String> values) {
            addCriterion("DIVISOR3 not in", values, "divisor3");
            return (Criteria) this;
        }

        public Criteria andDivisor3Between(String value1, String value2) {
            addCriterion("DIVISOR3 between", value1, value2, "divisor3");
            return (Criteria) this;
        }

        public Criteria andDivisor3NotBetween(String value1, String value2) {
            addCriterion("DIVISOR3 not between", value1, value2, "divisor3");
            return (Criteria) this;
        }

        public Criteria andValorIsNull() {
            addCriterion("VALOR is null");
            return (Criteria) this;
        }

        public Criteria andValorIsNotNull() {
            addCriterion("VALOR is not null");
            return (Criteria) this;
        }

        public Criteria andValorEqualTo(Long value) {
            addCriterion("VALOR =", value, "valor");
            return (Criteria) this;
        }

        public Criteria andValorNotEqualTo(Long value) {
            addCriterion("VALOR <>", value, "valor");
            return (Criteria) this;
        }

        public Criteria andValorGreaterThan(Long value) {
            addCriterion("VALOR >", value, "valor");
            return (Criteria) this;
        }

        public Criteria andValorGreaterThanOrEqualTo(Long value) {
            addCriterion("VALOR >=", value, "valor");
            return (Criteria) this;
        }

        public Criteria andValorLessThan(Long value) {
            addCriterion("VALOR <", value, "valor");
            return (Criteria) this;
        }

        public Criteria andValorLessThanOrEqualTo(Long value) {
            addCriterion("VALOR <=", value, "valor");
            return (Criteria) this;
        }

        public Criteria andValorIn(List<Long> values) {
            addCriterion("VALOR in", values, "valor");
            return (Criteria) this;
        }

        public Criteria andValorNotIn(List<Long> values) {
            addCriterion("VALOR not in", values, "valor");
            return (Criteria) this;
        }

        public Criteria andValorBetween(Long value1, Long value2) {
            addCriterion("VALOR between", value1, value2, "valor");
            return (Criteria) this;
        }

        public Criteria andValorNotBetween(Long value1, Long value2) {
            addCriterion("VALOR not between", value1, value2, "valor");
            return (Criteria) this;
        }

        public Criteria andUltimaPeticionIsNull() {
            addCriterion("ULTIMA_PETICION is null");
            return (Criteria) this;
        }

        public Criteria andUltimaPeticionIsNotNull() {
            addCriterion("ULTIMA_PETICION is not null");
            return (Criteria) this;
        }

        public Criteria andUltimaPeticionEqualTo(Date value) {
            addCriterion("ULTIMA_PETICION =", value, "ultimaPeticion");
            return (Criteria) this;
        }

        public Criteria andUltimaPeticionNotEqualTo(Date value) {
            addCriterion("ULTIMA_PETICION <>", value, "ultimaPeticion");
            return (Criteria) this;
        }

        public Criteria andUltimaPeticionGreaterThan(Date value) {
            addCriterion("ULTIMA_PETICION >", value, "ultimaPeticion");
            return (Criteria) this;
        }

        public Criteria andUltimaPeticionGreaterThanOrEqualTo(Date value) {
            addCriterion("ULTIMA_PETICION >=", value, "ultimaPeticion");
            return (Criteria) this;
        }

        public Criteria andUltimaPeticionLessThan(Date value) {
            addCriterion("ULTIMA_PETICION <", value, "ultimaPeticion");
            return (Criteria) this;
        }

        public Criteria andUltimaPeticionLessThanOrEqualTo(Date value) {
            addCriterion("ULTIMA_PETICION <=", value, "ultimaPeticion");
            return (Criteria) this;
        }

        public Criteria andUltimaPeticionIn(List<Date> values) {
            addCriterion("ULTIMA_PETICION in", values, "ultimaPeticion");
            return (Criteria) this;
        }

        public Criteria andUltimaPeticionNotIn(List<Date> values) {
            addCriterion("ULTIMA_PETICION not in", values, "ultimaPeticion");
            return (Criteria) this;
        }

        public Criteria andUltimaPeticionBetween(Date value1, Date value2) {
            addCriterion("ULTIMA_PETICION between", value1, value2, "ultimaPeticion");
            return (Criteria) this;
        }

        public Criteria andUltimaPeticionNotBetween(Date value1, Date value2) {
            addCriterion("ULTIMA_PETICION not between", value1, value2, "ultimaPeticion");
            return (Criteria) this;
        }

        public Criteria andIdRangoUltimaPeticionIsNull() {
            addCriterion("ID_RANGO_ULTIMA_PETICION is null");
            return (Criteria) this;
        }

        public Criteria andIdRangoUltimaPeticionIsNotNull() {
            addCriterion("ID_RANGO_ULTIMA_PETICION is not null");
            return (Criteria) this;
        }

        public Criteria andIdRangoUltimaPeticionEqualTo(String value) {
            addCriterion("ID_RANGO_ULTIMA_PETICION =", value, "idRangoUltimaPeticion");
            return (Criteria) this;
        }

        public Criteria andIdRangoUltimaPeticionNotEqualTo(String value) {
            addCriterion("ID_RANGO_ULTIMA_PETICION <>", value, "idRangoUltimaPeticion");
            return (Criteria) this;
        }

        public Criteria andIdRangoUltimaPeticionGreaterThan(String value) {
            addCriterion("ID_RANGO_ULTIMA_PETICION >", value, "idRangoUltimaPeticion");
            return (Criteria) this;
        }

        public Criteria andIdRangoUltimaPeticionGreaterThanOrEqualTo(String value) {
            addCriterion("ID_RANGO_ULTIMA_PETICION >=", value, "idRangoUltimaPeticion");
            return (Criteria) this;
        }

        public Criteria andIdRangoUltimaPeticionLessThan(String value) {
            addCriterion("ID_RANGO_ULTIMA_PETICION <", value, "idRangoUltimaPeticion");
            return (Criteria) this;
        }

        public Criteria andIdRangoUltimaPeticionLessThanOrEqualTo(String value) {
            addCriterion("ID_RANGO_ULTIMA_PETICION <=", value, "idRangoUltimaPeticion");
            return (Criteria) this;
        }

        public Criteria andIdRangoUltimaPeticionLike(String value) {
            addCriterion("ID_RANGO_ULTIMA_PETICION like", value, "idRangoUltimaPeticion");
            return (Criteria) this;
        }

        public Criteria andIdRangoUltimaPeticionNotLike(String value) {
            addCriterion("ID_RANGO_ULTIMA_PETICION not like", value, "idRangoUltimaPeticion");
            return (Criteria) this;
        }

        public Criteria andIdRangoUltimaPeticionIn(List<String> values) {
            addCriterion("ID_RANGO_ULTIMA_PETICION in", values, "idRangoUltimaPeticion");
            return (Criteria) this;
        }

        public Criteria andIdRangoUltimaPeticionNotIn(List<String> values) {
            addCriterion("ID_RANGO_ULTIMA_PETICION not in", values, "idRangoUltimaPeticion");
            return (Criteria) this;
        }

        public Criteria andIdRangoUltimaPeticionBetween(String value1, String value2) {
            addCriterion("ID_RANGO_ULTIMA_PETICION between", value1, value2, "idRangoUltimaPeticion");
            return (Criteria) this;
        }

        public Criteria andIdRangoUltimaPeticionNotBetween(String value1, String value2) {
            addCriterion("ID_RANGO_ULTIMA_PETICION not between", value1, value2, "idRangoUltimaPeticion");
            return (Criteria) this;
        }

        public Criteria andUidActividadLikeInsensitive(String value) {
            addCriterion("upper(UID_ACTIVIDAD) like", value.toUpperCase(), "uidActividad");
            return (Criteria) this;
        }

        public Criteria andIdContadorLikeInsensitive(String value) {
            addCriterion("upper(ID_CONTADOR) like", value.toUpperCase(), "idContador");
            return (Criteria) this;
        }

        public Criteria andDivisor1LikeInsensitive(String value) {
            addCriterion("upper(DIVISOR1) like", value.toUpperCase(), "divisor1");
            return (Criteria) this;
        }

        public Criteria andDivisor2LikeInsensitive(String value) {
            addCriterion("upper(DIVISOR2) like", value.toUpperCase(), "divisor2");
            return (Criteria) this;
        }

        public Criteria andDivisor3LikeInsensitive(String value) {
            addCriterion("upper(DIVISOR3) like", value.toUpperCase(), "divisor3");
            return (Criteria) this;
        }

        public Criteria andIdRangoUltimaPeticionLikeInsensitive(String value) {
            addCriterion("upper(ID_RANGO_ULTIMA_PETICION) like", value.toUpperCase(), "idRangoUltimaPeticion");
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