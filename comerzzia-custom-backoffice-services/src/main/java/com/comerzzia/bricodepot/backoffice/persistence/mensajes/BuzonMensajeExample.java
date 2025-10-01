package com.comerzzia.bricodepot.backoffice.persistence.mensajes;

import java.util.ArrayList;
import java.util.List;

public class BuzonMensajeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BuzonMensajeExample() {
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

        public Criteria andUidServicioIsNull() {
            addCriterion("UID_SERVICIO is null");
            return (Criteria) this;
        }

        public Criteria andUidServicioIsNotNull() {
            addCriterion("UID_SERVICIO is not null");
            return (Criteria) this;
        }

        public Criteria andUidServicioEqualTo(String value) {
            addCriterion("UID_SERVICIO =", value, "uidServicio");
            return (Criteria) this;
        }

        public Criteria andUidServicioNotEqualTo(String value) {
            addCriterion("UID_SERVICIO <>", value, "uidServicio");
            return (Criteria) this;
        }

        public Criteria andUidServicioGreaterThan(String value) {
            addCriterion("UID_SERVICIO >", value, "uidServicio");
            return (Criteria) this;
        }

        public Criteria andUidServicioGreaterThanOrEqualTo(String value) {
            addCriterion("UID_SERVICIO >=", value, "uidServicio");
            return (Criteria) this;
        }

        public Criteria andUidServicioLessThan(String value) {
            addCriterion("UID_SERVICIO <", value, "uidServicio");
            return (Criteria) this;
        }

        public Criteria andUidServicioLessThanOrEqualTo(String value) {
            addCriterion("UID_SERVICIO <=", value, "uidServicio");
            return (Criteria) this;
        }

        public Criteria andUidServicioLike(String value) {
            addCriterion("UID_SERVICIO like", value, "uidServicio");
            return (Criteria) this;
        }

        public Criteria andUidServicioNotLike(String value) {
            addCriterion("UID_SERVICIO not like", value, "uidServicio");
            return (Criteria) this;
        }

        public Criteria andUidServicioIn(List<String> values) {
            addCriterion("UID_SERVICIO in", values, "uidServicio");
            return (Criteria) this;
        }

        public Criteria andUidServicioNotIn(List<String> values) {
            addCriterion("UID_SERVICIO not in", values, "uidServicio");
            return (Criteria) this;
        }

        public Criteria andUidServicioBetween(String value1, String value2) {
            addCriterion("UID_SERVICIO between", value1, value2, "uidServicio");
            return (Criteria) this;
        }

        public Criteria andUidServicioNotBetween(String value1, String value2) {
            addCriterion("UID_SERVICIO not between", value1, value2, "uidServicio");
            return (Criteria) this;
        }

        public Criteria andIdBuzonIsNull() {
            addCriterion("ID_BUZON is null");
            return (Criteria) this;
        }

        public Criteria andIdBuzonIsNotNull() {
            addCriterion("ID_BUZON is not null");
            return (Criteria) this;
        }

        public Criteria andIdBuzonEqualTo(Long value) {
            addCriterion("ID_BUZON =", value, "idBuzon");
            return (Criteria) this;
        }

        public Criteria andIdBuzonNotEqualTo(Long value) {
            addCriterion("ID_BUZON <>", value, "idBuzon");
            return (Criteria) this;
        }

        public Criteria andIdBuzonGreaterThan(Long value) {
            addCriterion("ID_BUZON >", value, "idBuzon");
            return (Criteria) this;
        }

        public Criteria andIdBuzonGreaterThanOrEqualTo(Long value) {
            addCriterion("ID_BUZON >=", value, "idBuzon");
            return (Criteria) this;
        }

        public Criteria andIdBuzonLessThan(Long value) {
            addCriterion("ID_BUZON <", value, "idBuzon");
            return (Criteria) this;
        }

        public Criteria andIdBuzonLessThanOrEqualTo(Long value) {
            addCriterion("ID_BUZON <=", value, "idBuzon");
            return (Criteria) this;
        }

        public Criteria andIdBuzonIn(List<Long> values) {
            addCriterion("ID_BUZON in", values, "idBuzon");
            return (Criteria) this;
        }

        public Criteria andIdBuzonNotIn(List<Long> values) {
            addCriterion("ID_BUZON not in", values, "idBuzon");
            return (Criteria) this;
        }

        public Criteria andIdBuzonBetween(Long value1, Long value2) {
            addCriterion("ID_BUZON between", value1, value2, "idBuzon");
            return (Criteria) this;
        }

        public Criteria andIdBuzonNotBetween(Long value1, Long value2) {
            addCriterion("ID_BUZON not between", value1, value2, "idBuzon");
            return (Criteria) this;
        }

        public Criteria andUidMensajeIsNull() {
            addCriterion("UID_MENSAJE is null");
            return (Criteria) this;
        }

        public Criteria andUidMensajeIsNotNull() {
            addCriterion("UID_MENSAJE is not null");
            return (Criteria) this;
        }

        public Criteria andUidMensajeEqualTo(String value) {
            addCriterion("UID_MENSAJE =", value, "uidMensaje");
            return (Criteria) this;
        }

        public Criteria andUidMensajeNotEqualTo(String value) {
            addCriterion("UID_MENSAJE <>", value, "uidMensaje");
            return (Criteria) this;
        }

        public Criteria andUidMensajeGreaterThan(String value) {
            addCriterion("UID_MENSAJE >", value, "uidMensaje");
            return (Criteria) this;
        }

        public Criteria andUidMensajeGreaterThanOrEqualTo(String value) {
            addCriterion("UID_MENSAJE >=", value, "uidMensaje");
            return (Criteria) this;
        }

        public Criteria andUidMensajeLessThan(String value) {
            addCriterion("UID_MENSAJE <", value, "uidMensaje");
            return (Criteria) this;
        }

        public Criteria andUidMensajeLessThanOrEqualTo(String value) {
            addCriterion("UID_MENSAJE <=", value, "uidMensaje");
            return (Criteria) this;
        }

        public Criteria andUidMensajeLike(String value) {
            addCriterion("UID_MENSAJE like", value, "uidMensaje");
            return (Criteria) this;
        }

        public Criteria andUidMensajeNotLike(String value) {
            addCriterion("UID_MENSAJE not like", value, "uidMensaje");
            return (Criteria) this;
        }

        public Criteria andUidMensajeIn(List<String> values) {
            addCriterion("UID_MENSAJE in", values, "uidMensaje");
            return (Criteria) this;
        }

        public Criteria andUidMensajeNotIn(List<String> values) {
            addCriterion("UID_MENSAJE not in", values, "uidMensaje");
            return (Criteria) this;
        }

        public Criteria andUidMensajeBetween(String value1, String value2) {
            addCriterion("UID_MENSAJE between", value1, value2, "uidMensaje");
            return (Criteria) this;
        }

        public Criteria andUidMensajeNotBetween(String value1, String value2) {
            addCriterion("UID_MENSAJE not between", value1, value2, "uidMensaje");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotificacionIsNull() {
            addCriterion("UID_TICKET_NOTIFICACION is null");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotificacionIsNotNull() {
            addCriterion("UID_TICKET_NOTIFICACION is not null");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotificacionEqualTo(String value) {
            addCriterion("UID_TICKET_NOTIFICACION =", value, "uidTicketNotificacion");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotificacionNotEqualTo(String value) {
            addCriterion("UID_TICKET_NOTIFICACION <>", value, "uidTicketNotificacion");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotificacionGreaterThan(String value) {
            addCriterion("UID_TICKET_NOTIFICACION >", value, "uidTicketNotificacion");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotificacionGreaterThanOrEqualTo(String value) {
            addCriterion("UID_TICKET_NOTIFICACION >=", value, "uidTicketNotificacion");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotificacionLessThan(String value) {
            addCriterion("UID_TICKET_NOTIFICACION <", value, "uidTicketNotificacion");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotificacionLessThanOrEqualTo(String value) {
            addCriterion("UID_TICKET_NOTIFICACION <=", value, "uidTicketNotificacion");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotificacionLike(String value) {
            addCriterion("UID_TICKET_NOTIFICACION like", value, "uidTicketNotificacion");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotificacionNotLike(String value) {
            addCriterion("UID_TICKET_NOTIFICACION not like", value, "uidTicketNotificacion");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotificacionIn(List<String> values) {
            addCriterion("UID_TICKET_NOTIFICACION in", values, "uidTicketNotificacion");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotificacionNotIn(List<String> values) {
            addCriterion("UID_TICKET_NOTIFICACION not in", values, "uidTicketNotificacion");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotificacionBetween(String value1, String value2) {
            addCriterion("UID_TICKET_NOTIFICACION between", value1, value2, "uidTicketNotificacion");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotificacionNotBetween(String value1, String value2) {
            addCriterion("UID_TICKET_NOTIFICACION not between", value1, value2, "uidTicketNotificacion");
            return (Criteria) this;
        }

        public Criteria andUidActividadLikeInsensitive(String value) {
            addCriterion("upper(UID_ACTIVIDAD) like", value.toUpperCase(), "uidActividad");
            return (Criteria) this;
        }

        public Criteria andUidServicioLikeInsensitive(String value) {
            addCriterion("upper(UID_SERVICIO) like", value.toUpperCase(), "uidServicio");
            return (Criteria) this;
        }

        public Criteria andUidMensajeLikeInsensitive(String value) {
            addCriterion("upper(UID_MENSAJE) like", value.toUpperCase(), "uidMensaje");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotificacionLikeInsensitive(String value) {
            addCriterion("upper(UID_TICKET_NOTIFICACION) like", value.toUpperCase(), "uidTicketNotificacion");
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