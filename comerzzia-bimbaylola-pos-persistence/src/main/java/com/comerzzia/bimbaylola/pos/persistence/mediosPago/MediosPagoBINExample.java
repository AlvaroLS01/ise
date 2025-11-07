package com.comerzzia.bimbaylola.pos.persistence.mediosPago;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MediosPagoBINExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MediosPagoBINExample() {
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

        public Criteria andCodmedpagIsNull() {
            addCriterion("CODMEDPAG is null");
            return (Criteria) this;
        }

        public Criteria andCodmedpagIsNotNull() {
            addCriterion("CODMEDPAG is not null");
            return (Criteria) this;
        }

        public Criteria andCodmedpagEqualTo(String value) {
            addCriterion("CODMEDPAG =", value, "codmedpag");
            return (Criteria) this;
        }

        public Criteria andCodmedpagNotEqualTo(String value) {
            addCriterion("CODMEDPAG <>", value, "codmedpag");
            return (Criteria) this;
        }

        public Criteria andCodmedpagGreaterThan(String value) {
            addCriterion("CODMEDPAG >", value, "codmedpag");
            return (Criteria) this;
        }

        public Criteria andCodmedpagGreaterThanOrEqualTo(String value) {
            addCriterion("CODMEDPAG >=", value, "codmedpag");
            return (Criteria) this;
        }

        public Criteria andCodmedpagLessThan(String value) {
            addCriterion("CODMEDPAG <", value, "codmedpag");
            return (Criteria) this;
        }

        public Criteria andCodmedpagLessThanOrEqualTo(String value) {
            addCriterion("CODMEDPAG <=", value, "codmedpag");
            return (Criteria) this;
        }

        public Criteria andCodmedpagLike(String value) {
            addCriterion("CODMEDPAG like", value, "codmedpag");
            return (Criteria) this;
        }

        public Criteria andCodmedpagNotLike(String value) {
            addCriterion("CODMEDPAG not like", value, "codmedpag");
            return (Criteria) this;
        }

        public Criteria andCodmedpagIn(List<String> values) {
            addCriterion("CODMEDPAG in", values, "codmedpag");
            return (Criteria) this;
        }

        public Criteria andCodmedpagNotIn(List<String> values) {
            addCriterion("CODMEDPAG not in", values, "codmedpag");
            return (Criteria) this;
        }

        public Criteria andCodmedpagBetween(String value1, String value2) {
            addCriterion("CODMEDPAG between", value1, value2, "codmedpag");
            return (Criteria) this;
        }

        public Criteria andCodmedpagNotBetween(String value1, String value2) {
            addCriterion("CODMEDPAG not between", value1, value2, "codmedpag");
            return (Criteria) this;
        }

        public Criteria andOrdenIsNull() {
            addCriterion("ORDEN is null");
            return (Criteria) this;
        }

        public Criteria andOrdenIsNotNull() {
            addCriterion("ORDEN is not null");
            return (Criteria) this;
        }

        public Criteria andOrdenEqualTo(String value) {
            addCriterion("ORDEN =", value, "orden");
            return (Criteria) this;
        }

        public Criteria andOrdenNotEqualTo(String value) {
            addCriterion("ORDEN <>", value, "orden");
            return (Criteria) this;
        }

        public Criteria andOrdenGreaterThan(String value) {
            addCriterion("ORDEN >", value, "orden");
            return (Criteria) this;
        }

        public Criteria andOrdenGreaterThanOrEqualTo(String value) {
            addCriterion("ORDEN >=", value, "orden");
            return (Criteria) this;
        }

        public Criteria andOrdenLessThan(String value) {
            addCriterion("ORDEN <", value, "orden");
            return (Criteria) this;
        }

        public Criteria andOrdenLessThanOrEqualTo(String value) {
            addCriterion("ORDEN <=", value, "orden");
            return (Criteria) this;
        }

        public Criteria andOrdenLike(String value) {
            addCriterion("ORDEN like", value, "orden");
            return (Criteria) this;
        }

        public Criteria andOrdenNotLike(String value) {
            addCriterion("ORDEN not like", value, "orden");
            return (Criteria) this;
        }

        public Criteria andOrdenIn(List<String> values) {
            addCriterion("ORDEN in", values, "orden");
            return (Criteria) this;
        }

        public Criteria andOrdenNotIn(List<String> values) {
            addCriterion("ORDEN not in", values, "orden");
            return (Criteria) this;
        }

        public Criteria andOrdenBetween(String value1, String value2) {
            addCriterion("ORDEN between", value1, value2, "orden");
            return (Criteria) this;
        }

        public Criteria andOrdenNotBetween(String value1, String value2) {
            addCriterion("ORDEN not between", value1, value2, "orden");
            return (Criteria) this;
        }

        public Criteria andFechaInicioIsNull() {
            addCriterion("FECHA_INICIO is null");
            return (Criteria) this;
        }

        public Criteria andFechaInicioIsNotNull() {
            addCriterion("FECHA_INICIO is not null");
            return (Criteria) this;
        }

        public Criteria andFechaInicioEqualTo(Date value) {
            addCriterion("FECHA_INICIO =", value, "fechaInicio");
            return (Criteria) this;
        }

        public Criteria andFechaInicioNotEqualTo(Date value) {
            addCriterion("FECHA_INICIO <>", value, "fechaInicio");
            return (Criteria) this;
        }

        public Criteria andFechaInicioGreaterThan(Date value) {
            addCriterion("FECHA_INICIO >", value, "fechaInicio");
            return (Criteria) this;
        }

        public Criteria andFechaInicioGreaterThanOrEqualTo(Date value) {
            addCriterion("FECHA_INICIO >=", value, "fechaInicio");
            return (Criteria) this;
        }

        public Criteria andFechaInicioLessThan(Date value) {
            addCriterion("FECHA_INICIO <", value, "fechaInicio");
            return (Criteria) this;
        }

        public Criteria andFechaInicioLessThanOrEqualTo(Date value) {
            addCriterion("FECHA_INICIO <=", value, "fechaInicio");
            return (Criteria) this;
        }

        public Criteria andFechaInicioIn(List<Date> values) {
            addCriterion("FECHA_INICIO in", values, "fechaInicio");
            return (Criteria) this;
        }

        public Criteria andFechaInicioNotIn(List<Date> values) {
            addCriterion("FECHA_INICIO not in", values, "fechaInicio");
            return (Criteria) this;
        }

        public Criteria andFechaInicioBetween(Date value1, Date value2) {
            addCriterion("FECHA_INICIO between", value1, value2, "fechaInicio");
            return (Criteria) this;
        }

        public Criteria andFechaInicioNotBetween(Date value1, Date value2) {
            addCriterion("FECHA_INICIO not between", value1, value2, "fechaInicio");
            return (Criteria) this;
        }

        public Criteria andFechaFinIsNull() {
            addCriterion("FECHA_FIN is null");
            return (Criteria) this;
        }

        public Criteria andFechaFinIsNotNull() {
            addCriterion("FECHA_FIN is not null");
            return (Criteria) this;
        }

        public Criteria andFechaFinEqualTo(Date value) {
            addCriterion("FECHA_FIN =", value, "fechaFin");
            return (Criteria) this;
        }

        public Criteria andFechaFinNotEqualTo(Date value) {
            addCriterion("FECHA_FIN <>", value, "fechaFin");
            return (Criteria) this;
        }

        public Criteria andFechaFinGreaterThan(Date value) {
            addCriterion("FECHA_FIN >", value, "fechaFin");
            return (Criteria) this;
        }

        public Criteria andFechaFinGreaterThanOrEqualTo(Date value) {
            addCriterion("FECHA_FIN >=", value, "fechaFin");
            return (Criteria) this;
        }

        public Criteria andFechaFinLessThan(Date value) {
            addCriterion("FECHA_FIN <", value, "fechaFin");
            return (Criteria) this;
        }

        public Criteria andFechaFinLessThanOrEqualTo(Date value) {
            addCriterion("FECHA_FIN <=", value, "fechaFin");
            return (Criteria) this;
        }

        public Criteria andFechaFinIn(List<Date> values) {
            addCriterion("FECHA_FIN in", values, "fechaFin");
            return (Criteria) this;
        }

        public Criteria andFechaFinNotIn(List<Date> values) {
            addCriterion("FECHA_FIN not in", values, "fechaFin");
            return (Criteria) this;
        }

        public Criteria andFechaFinBetween(Date value1, Date value2) {
            addCriterion("FECHA_FIN between", value1, value2, "fechaFin");
            return (Criteria) this;
        }

        public Criteria andFechaFinNotBetween(Date value1, Date value2) {
            addCriterion("FECHA_FIN not between", value1, value2, "fechaFin");
            return (Criteria) this;
        }

        public Criteria andIdD365IsNull() {
            addCriterion("ID_D365 is null");
            return (Criteria) this;
        }

        public Criteria andIdD365IsNotNull() {
            addCriterion("ID_D365 is not null");
            return (Criteria) this;
        }

        public Criteria andIdD365EqualTo(String value) {
            addCriterion("ID_D365 =", value, "idD365");
            return (Criteria) this;
        }

        public Criteria andIdD365NotEqualTo(String value) {
            addCriterion("ID_D365 <>", value, "idD365");
            return (Criteria) this;
        }

        public Criteria andIdD365GreaterThan(String value) {
            addCriterion("ID_D365 >", value, "idD365");
            return (Criteria) this;
        }

        public Criteria andIdD365GreaterThanOrEqualTo(String value) {
            addCriterion("ID_D365 >=", value, "idD365");
            return (Criteria) this;
        }

        public Criteria andIdD365LessThan(String value) {
            addCriterion("ID_D365 <", value, "idD365");
            return (Criteria) this;
        }

        public Criteria andIdD365LessThanOrEqualTo(String value) {
            addCriterion("ID_D365 <=", value, "idD365");
            return (Criteria) this;
        }

        public Criteria andIdD365Like(String value) {
            addCriterion("ID_D365 like", value, "idD365");
            return (Criteria) this;
        }

        public Criteria andIdD365NotLike(String value) {
            addCriterion("ID_D365 not like", value, "idD365");
            return (Criteria) this;
        }

        public Criteria andIdD365In(List<String> values) {
            addCriterion("ID_D365 in", values, "idD365");
            return (Criteria) this;
        }

        public Criteria andIdD365NotIn(List<String> values) {
            addCriterion("ID_D365 not in", values, "idD365");
            return (Criteria) this;
        }

        public Criteria andIdD365Between(String value1, String value2) {
            addCriterion("ID_D365 between", value1, value2, "idD365");
            return (Criteria) this;
        }

        public Criteria andIdD365NotBetween(String value1, String value2) {
            addCriterion("ID_D365 not between", value1, value2, "idD365");
            return (Criteria) this;
        }

        public Criteria andIdD365AbonoIsNull() {
            addCriterion("ID_D365_ABONO is null");
            return (Criteria) this;
        }

        public Criteria andIdD365AbonoIsNotNull() {
            addCriterion("ID_D365_ABONO is not null");
            return (Criteria) this;
        }

        public Criteria andIdD365AbonoEqualTo(String value) {
            addCriterion("ID_D365_ABONO =", value, "idD365Abono");
            return (Criteria) this;
        }

        public Criteria andIdD365AbonoNotEqualTo(String value) {
            addCriterion("ID_D365_ABONO <>", value, "idD365Abono");
            return (Criteria) this;
        }

        public Criteria andIdD365AbonoGreaterThan(String value) {
            addCriterion("ID_D365_ABONO >", value, "idD365Abono");
            return (Criteria) this;
        }

        public Criteria andIdD365AbonoGreaterThanOrEqualTo(String value) {
            addCriterion("ID_D365_ABONO >=", value, "idD365Abono");
            return (Criteria) this;
        }

        public Criteria andIdD365AbonoLessThan(String value) {
            addCriterion("ID_D365_ABONO <", value, "idD365Abono");
            return (Criteria) this;
        }

        public Criteria andIdD365AbonoLessThanOrEqualTo(String value) {
            addCriterion("ID_D365_ABONO <=", value, "idD365Abono");
            return (Criteria) this;
        }

        public Criteria andIdD365AbonoLike(String value) {
            addCriterion("ID_D365_ABONO like", value, "idD365Abono");
            return (Criteria) this;
        }

        public Criteria andIdD365AbonoNotLike(String value) {
            addCriterion("ID_D365_ABONO not like", value, "idD365Abono");
            return (Criteria) this;
        }

        public Criteria andIdD365AbonoIn(List<String> values) {
            addCriterion("ID_D365_ABONO in", values, "idD365Abono");
            return (Criteria) this;
        }

        public Criteria andIdD365AbonoNotIn(List<String> values) {
            addCriterion("ID_D365_ABONO not in", values, "idD365Abono");
            return (Criteria) this;
        }

        public Criteria andIdD365AbonoBetween(String value1, String value2) {
            addCriterion("ID_D365_ABONO between", value1, value2, "idD365Abono");
            return (Criteria) this;
        }

        public Criteria andIdD365AbonoNotBetween(String value1, String value2) {
            addCriterion("ID_D365_ABONO not between", value1, value2, "idD365Abono");
            return (Criteria) this;
        }

        public Criteria andUidActividadLikeInsensitive(String value) {
            addCriterion("upper(UID_ACTIVIDAD) like", value.toUpperCase(), "uidActividad");
            return (Criteria) this;
        }

        public Criteria andCodmedpagLikeInsensitive(String value) {
            addCriterion("upper(CODMEDPAG) like", value.toUpperCase(), "codmedpag");
            return (Criteria) this;
        }

        public Criteria andOrdenLikeInsensitive(String value) {
            addCriterion("upper(ORDEN) like", value.toUpperCase(), "orden");
            return (Criteria) this;
        }

        public Criteria andIdD365LikeInsensitive(String value) {
            addCriterion("upper(ID_D365) like", value.toUpperCase(), "idD365");
            return (Criteria) this;
        }

        public Criteria andIdD365AbonoLikeInsensitive(String value) {
            addCriterion("upper(ID_D365_ABONO) like", value.toUpperCase(), "idD365Abono");
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