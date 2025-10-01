package com.comerzzia.bricodepot.backoffice.persistence.usuarios.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsuarioXExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UsuarioXExample() {
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

        public Criteria andUidInstanciaIsNull() {
            addCriterion("UID_INSTANCIA is null");
            return (Criteria) this;
        }

        public Criteria andUidInstanciaIsNotNull() {
            addCriterion("UID_INSTANCIA is not null");
            return (Criteria) this;
        }

        public Criteria andUidInstanciaEqualTo(String value) {
            addCriterion("UID_INSTANCIA =", value, "uidInstancia");
            return (Criteria) this;
        }

        public Criteria andUidInstanciaNotEqualTo(String value) {
            addCriterion("UID_INSTANCIA <>", value, "uidInstancia");
            return (Criteria) this;
        }

        public Criteria andUidInstanciaGreaterThan(String value) {
            addCriterion("UID_INSTANCIA >", value, "uidInstancia");
            return (Criteria) this;
        }

        public Criteria andUidInstanciaGreaterThanOrEqualTo(String value) {
            addCriterion("UID_INSTANCIA >=", value, "uidInstancia");
            return (Criteria) this;
        }

        public Criteria andUidInstanciaLessThan(String value) {
            addCriterion("UID_INSTANCIA <", value, "uidInstancia");
            return (Criteria) this;
        }

        public Criteria andUidInstanciaLessThanOrEqualTo(String value) {
            addCriterion("UID_INSTANCIA <=", value, "uidInstancia");
            return (Criteria) this;
        }

        public Criteria andUidInstanciaLike(String value) {
            addCriterion("UID_INSTANCIA like", value, "uidInstancia");
            return (Criteria) this;
        }

        public Criteria andUidInstanciaNotLike(String value) {
            addCriterion("UID_INSTANCIA not like", value, "uidInstancia");
            return (Criteria) this;
        }

        public Criteria andUidInstanciaIn(List<String> values) {
            addCriterion("UID_INSTANCIA in", values, "uidInstancia");
            return (Criteria) this;
        }

        public Criteria andUidInstanciaNotIn(List<String> values) {
            addCriterion("UID_INSTANCIA not in", values, "uidInstancia");
            return (Criteria) this;
        }

        public Criteria andUidInstanciaBetween(String value1, String value2) {
            addCriterion("UID_INSTANCIA between", value1, value2, "uidInstancia");
            return (Criteria) this;
        }

        public Criteria andUidInstanciaNotBetween(String value1, String value2) {
            addCriterion("UID_INSTANCIA not between", value1, value2, "uidInstancia");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioIsNull() {
            addCriterion("ID_USUARIO is null");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioIsNotNull() {
            addCriterion("ID_USUARIO is not null");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioEqualTo(Long value) {
            addCriterion("ID_USUARIO =", value, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioNotEqualTo(Long value) {
            addCriterion("ID_USUARIO <>", value, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioGreaterThan(Long value) {
            addCriterion("ID_USUARIO >", value, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioGreaterThanOrEqualTo(Long value) {
            addCriterion("ID_USUARIO >=", value, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioLessThan(Long value) {
            addCriterion("ID_USUARIO <", value, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioLessThanOrEqualTo(Long value) {
            addCriterion("ID_USUARIO <=", value, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioIn(List<Long> values) {
            addCriterion("ID_USUARIO in", values, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioNotIn(List<Long> values) {
            addCriterion("ID_USUARIO not in", values, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioBetween(Long value1, Long value2) {
            addCriterion("ID_USUARIO between", value1, value2, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioNotBetween(Long value1, Long value2) {
            addCriterion("ID_USUARIO not between", value1, value2, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andUltimoCambioIsNull() {
            addCriterion("ULTIMO_CAMBIO is null");
            return (Criteria) this;
        }

        public Criteria andUltimoCambioIsNotNull() {
            addCriterion("ULTIMO_CAMBIO is not null");
            return (Criteria) this;
        }

        public Criteria andUltimoCambioEqualTo(Date value) {
            addCriterion("ULTIMO_CAMBIO =", value, "ultimoCambio");
            return (Criteria) this;
        }

        public Criteria andUltimoCambioNotEqualTo(Date value) {
            addCriterion("ULTIMO_CAMBIO <>", value, "ultimoCambio");
            return (Criteria) this;
        }

        public Criteria andUltimoCambioGreaterThan(Date value) {
            addCriterion("ULTIMO_CAMBIO >", value, "ultimoCambio");
            return (Criteria) this;
        }

        public Criteria andUltimoCambioGreaterThanOrEqualTo(Date value) {
            addCriterion("ULTIMO_CAMBIO >=", value, "ultimoCambio");
            return (Criteria) this;
        }

        public Criteria andUltimoCambioLessThan(Date value) {
            addCriterion("ULTIMO_CAMBIO <", value, "ultimoCambio");
            return (Criteria) this;
        }

        public Criteria andUltimoCambioLessThanOrEqualTo(Date value) {
            addCriterion("ULTIMO_CAMBIO <=", value, "ultimoCambio");
            return (Criteria) this;
        }

        public Criteria andUltimoCambioIn(List<Date> values) {
            addCriterion("ULTIMO_CAMBIO in", values, "ultimoCambio");
            return (Criteria) this;
        }

        public Criteria andUltimoCambioNotIn(List<Date> values) {
            addCriterion("ULTIMO_CAMBIO not in", values, "ultimoCambio");
            return (Criteria) this;
        }

        public Criteria andUltimoCambioBetween(Date value1, Date value2) {
            addCriterion("ULTIMO_CAMBIO between", value1, value2, "ultimoCambio");
            return (Criteria) this;
        }

        public Criteria andUltimoCambioNotBetween(Date value1, Date value2) {
            addCriterion("ULTIMO_CAMBIO not between", value1, value2, "ultimoCambio");
            return (Criteria) this;
        }

        public Criteria andIntentosLoginFallidosIsNull() {
            addCriterion("INTENTOS_LOGIN_FALLIDOS is null");
            return (Criteria) this;
        }

        public Criteria andIntentosLoginFallidosIsNotNull() {
            addCriterion("INTENTOS_LOGIN_FALLIDOS is not null");
            return (Criteria) this;
        }

        public Criteria andIntentosLoginFallidosEqualTo(Long value) {
            addCriterion("INTENTOS_LOGIN_FALLIDOS =", value, "intentosLoginFallidos");
            return (Criteria) this;
        }

        public Criteria andIntentosLoginFallidosNotEqualTo(Long value) {
            addCriterion("INTENTOS_LOGIN_FALLIDOS <>", value, "intentosLoginFallidos");
            return (Criteria) this;
        }

        public Criteria andIntentosLoginFallidosGreaterThan(Long value) {
            addCriterion("INTENTOS_LOGIN_FALLIDOS >", value, "intentosLoginFallidos");
            return (Criteria) this;
        }

        public Criteria andIntentosLoginFallidosGreaterThanOrEqualTo(Long value) {
            addCriterion("INTENTOS_LOGIN_FALLIDOS >=", value, "intentosLoginFallidos");
            return (Criteria) this;
        }

        public Criteria andIntentosLoginFallidosLessThan(Long value) {
            addCriterion("INTENTOS_LOGIN_FALLIDOS <", value, "intentosLoginFallidos");
            return (Criteria) this;
        }

        public Criteria andIntentosLoginFallidosLessThanOrEqualTo(Long value) {
            addCriterion("INTENTOS_LOGIN_FALLIDOS <=", value, "intentosLoginFallidos");
            return (Criteria) this;
        }

        public Criteria andIntentosLoginFallidosIn(List<Long> values) {
            addCriterion("INTENTOS_LOGIN_FALLIDOS in", values, "intentosLoginFallidos");
            return (Criteria) this;
        }

        public Criteria andIntentosLoginFallidosNotIn(List<Long> values) {
            addCriterion("INTENTOS_LOGIN_FALLIDOS not in", values, "intentosLoginFallidos");
            return (Criteria) this;
        }

        public Criteria andIntentosLoginFallidosBetween(Long value1, Long value2) {
            addCriterion("INTENTOS_LOGIN_FALLIDOS between", value1, value2, "intentosLoginFallidos");
            return (Criteria) this;
        }

        public Criteria andIntentosLoginFallidosNotBetween(Long value1, Long value2) {
            addCriterion("INTENTOS_LOGIN_FALLIDOS not between", value1, value2, "intentosLoginFallidos");
            return (Criteria) this;
        }

        public Criteria andCambiosClaveIsNull() {
            addCriterion("CAMBIOS_CLAVE is null");
            return (Criteria) this;
        }

        public Criteria andCambiosClaveIsNotNull() {
            addCriterion("CAMBIOS_CLAVE is not null");
            return (Criteria) this;
        }

        public Criteria andCambiosClaveEqualTo(Long value) {
            addCriterion("CAMBIOS_CLAVE =", value, "cambiosClave");
            return (Criteria) this;
        }

        public Criteria andCambiosClaveNotEqualTo(Long value) {
            addCriterion("CAMBIOS_CLAVE <>", value, "cambiosClave");
            return (Criteria) this;
        }

        public Criteria andCambiosClaveGreaterThan(Long value) {
            addCriterion("CAMBIOS_CLAVE >", value, "cambiosClave");
            return (Criteria) this;
        }

        public Criteria andCambiosClaveGreaterThanOrEqualTo(Long value) {
            addCriterion("CAMBIOS_CLAVE >=", value, "cambiosClave");
            return (Criteria) this;
        }

        public Criteria andCambiosClaveLessThan(Long value) {
            addCriterion("CAMBIOS_CLAVE <", value, "cambiosClave");
            return (Criteria) this;
        }

        public Criteria andCambiosClaveLessThanOrEqualTo(Long value) {
            addCriterion("CAMBIOS_CLAVE <=", value, "cambiosClave");
            return (Criteria) this;
        }

        public Criteria andCambiosClaveIn(List<Long> values) {
            addCriterion("CAMBIOS_CLAVE in", values, "cambiosClave");
            return (Criteria) this;
        }

        public Criteria andCambiosClaveNotIn(List<Long> values) {
            addCriterion("CAMBIOS_CLAVE not in", values, "cambiosClave");
            return (Criteria) this;
        }

        public Criteria andCambiosClaveBetween(Long value1, Long value2) {
            addCriterion("CAMBIOS_CLAVE between", value1, value2, "cambiosClave");
            return (Criteria) this;
        }

        public Criteria andCambiosClaveNotBetween(Long value1, Long value2) {
            addCriterion("CAMBIOS_CLAVE not between", value1, value2, "cambiosClave");
            return (Criteria) this;
        }

        public Criteria andBloqueadoIsNull() {
            addCriterion("BLOQUEADO is null");
            return (Criteria) this;
        }

        public Criteria andBloqueadoIsNotNull() {
            addCriterion("BLOQUEADO is not null");
            return (Criteria) this;
        }

        public Criteria andBloqueadoEqualTo(String value) {
            addCriterion("BLOQUEADO =", value, "bloqueado");
            return (Criteria) this;
        }

        public Criteria andBloqueadoNotEqualTo(String value) {
            addCriterion("BLOQUEADO <>", value, "bloqueado");
            return (Criteria) this;
        }

        public Criteria andBloqueadoGreaterThan(String value) {
            addCriterion("BLOQUEADO >", value, "bloqueado");
            return (Criteria) this;
        }

        public Criteria andBloqueadoGreaterThanOrEqualTo(String value) {
            addCriterion("BLOQUEADO >=", value, "bloqueado");
            return (Criteria) this;
        }

        public Criteria andBloqueadoLessThan(String value) {
            addCriterion("BLOQUEADO <", value, "bloqueado");
            return (Criteria) this;
        }

        public Criteria andBloqueadoLessThanOrEqualTo(String value) {
            addCriterion("BLOQUEADO <=", value, "bloqueado");
            return (Criteria) this;
        }

        public Criteria andBloqueadoLike(String value) {
            addCriterion("BLOQUEADO like", value, "bloqueado");
            return (Criteria) this;
        }

        public Criteria andBloqueadoNotLike(String value) {
            addCriterion("BLOQUEADO not like", value, "bloqueado");
            return (Criteria) this;
        }

        public Criteria andBloqueadoIn(List<String> values) {
            addCriterion("BLOQUEADO in", values, "bloqueado");
            return (Criteria) this;
        }

        public Criteria andBloqueadoNotIn(List<String> values) {
            addCriterion("BLOQUEADO not in", values, "bloqueado");
            return (Criteria) this;
        }

        public Criteria andBloqueadoBetween(String value1, String value2) {
            addCriterion("BLOQUEADO between", value1, value2, "bloqueado");
            return (Criteria) this;
        }

        public Criteria andBloqueadoNotBetween(String value1, String value2) {
            addCriterion("BLOQUEADO not between", value1, value2, "bloqueado");
            return (Criteria) this;
        }

        public Criteria andUidInstanciaLikeInsensitive(String value) {
            addCriterion("upper(UID_INSTANCIA) like", value.toUpperCase(), "uidInstancia");
            return (Criteria) this;
        }

        public Criteria andBloqueadoLikeInsensitive(String value) {
            addCriterion("upper(BLOQUEADO) like", value.toUpperCase(), "bloqueado");
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