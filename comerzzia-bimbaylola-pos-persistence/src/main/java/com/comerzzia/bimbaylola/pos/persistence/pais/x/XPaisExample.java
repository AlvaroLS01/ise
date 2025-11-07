package com.comerzzia.bimbaylola.pos.persistence.pais.x;

import java.util.ArrayList;
import java.util.List;

public class XPaisExample {
    public static final String ORDER_BY_UID_INSTANCIA = "UID_INSTANCIA";

    public static final String ORDER_BY_UID_INSTANCIA_DESC = "UID_INSTANCIA DESC";

    public static final String ORDER_BY_CODPAIS = "CODPAIS";

    public static final String ORDER_BY_CODPAIS_DESC = "CODPAIS DESC";

    public static final String ORDER_BY_PREFIJO_TELEFONO = "PREFIJO_TELEFONO";

    public static final String ORDER_BY_PREFIJO_TELEFONO_DESC = "PREFIJO_TELEFONO DESC";

    public static final String ORDER_BY_ISO_LENGUAJE = "ISO_LENGUAJE";

    public static final String ORDER_BY_ISO_LENGUAJE_DESC = "ISO_LENGUAJE DESC";

    public static final String ORDER_BY_FILTRAR_CLIENTES_PAIS = "FILTRAR_CLIENTES_PAIS";

    public static final String ORDER_BY_FILTRAR_CLIENTES_PAIS_DESC = "FILTRAR_CLIENTES_PAIS DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public XPaisExample() {
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
        protected List<Criterion> filtrarClientesPaisCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            filtrarClientesPaisCriteria = new ArrayList<Criterion>();
        }

        public List<Criterion> getFiltrarClientesPaisCriteria() {
            return filtrarClientesPaisCriteria;
        }

        protected void addFiltrarClientesPaisCriterion(String condition, Object value, String property) {
            if (value != null) {
                filtrarClientesPaisCriteria.add(new Criterion(condition, value, "com.comerzzia.core.util.mybatis.typehandlers.BooleanStringTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addFiltrarClientesPaisCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                filtrarClientesPaisCriteria.add(new Criterion(condition, value1, value2, "com.comerzzia.core.util.mybatis.typehandlers.BooleanStringTypeHandler"));
                allCriteria = null;
            }
        }

        public boolean isValid() {
            return criteria.size() > 0
                || filtrarClientesPaisCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(filtrarClientesPaisCriteria);
            }
            return allCriteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition != null) {
                criteria.add(new Criterion(condition));
                allCriteria = null;
            }
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value != null) {
                criteria.add(new Criterion(condition, value));
                allCriteria = null;
            }
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 != null && value2 != null) {
                criteria.add(new Criterion(condition, value1, value2));
                allCriteria = null;
            }
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

        public Criteria andCodpaisIsNull() {
            addCriterion("CODPAIS is null");
            return (Criteria) this;
        }

        public Criteria andCodpaisIsNotNull() {
            addCriterion("CODPAIS is not null");
            return (Criteria) this;
        }

        public Criteria andCodpaisEqualTo(String value) {
            addCriterion("CODPAIS =", value, "codpais");
            return (Criteria) this;
        }

        public Criteria andCodpaisNotEqualTo(String value) {
            addCriterion("CODPAIS <>", value, "codpais");
            return (Criteria) this;
        }

        public Criteria andCodpaisGreaterThan(String value) {
            addCriterion("CODPAIS >", value, "codpais");
            return (Criteria) this;
        }

        public Criteria andCodpaisGreaterThanOrEqualTo(String value) {
            addCriterion("CODPAIS >=", value, "codpais");
            return (Criteria) this;
        }

        public Criteria andCodpaisLessThan(String value) {
            addCriterion("CODPAIS <", value, "codpais");
            return (Criteria) this;
        }

        public Criteria andCodpaisLessThanOrEqualTo(String value) {
            addCriterion("CODPAIS <=", value, "codpais");
            return (Criteria) this;
        }

        public Criteria andCodpaisLike(String value) {
            addCriterion("CODPAIS like", value, "codpais");
            return (Criteria) this;
        }

        public Criteria andCodpaisNotLike(String value) {
            addCriterion("CODPAIS not like", value, "codpais");
            return (Criteria) this;
        }

        public Criteria andCodpaisIn(List<String> values) {
            addCriterion("CODPAIS in", values, "codpais");
            return (Criteria) this;
        }

        public Criteria andCodpaisNotIn(List<String> values) {
            addCriterion("CODPAIS not in", values, "codpais");
            return (Criteria) this;
        }

        public Criteria andCodpaisBetween(String value1, String value2) {
            addCriterion("CODPAIS between", value1, value2, "codpais");
            return (Criteria) this;
        }

        public Criteria andCodpaisNotBetween(String value1, String value2) {
            addCriterion("CODPAIS not between", value1, value2, "codpais");
            return (Criteria) this;
        }

        public Criteria andPrefijoTelefonoIsNull() {
            addCriterion("PREFIJO_TELEFONO is null");
            return (Criteria) this;
        }

        public Criteria andPrefijoTelefonoIsNotNull() {
            addCriterion("PREFIJO_TELEFONO is not null");
            return (Criteria) this;
        }

        public Criteria andPrefijoTelefonoEqualTo(Integer value) {
            addCriterion("PREFIJO_TELEFONO =", value, "prefijoTelefono");
            return (Criteria) this;
        }

        public Criteria andPrefijoTelefonoNotEqualTo(Integer value) {
            addCriterion("PREFIJO_TELEFONO <>", value, "prefijoTelefono");
            return (Criteria) this;
        }

        public Criteria andPrefijoTelefonoGreaterThan(Integer value) {
            addCriterion("PREFIJO_TELEFONO >", value, "prefijoTelefono");
            return (Criteria) this;
        }

        public Criteria andPrefijoTelefonoGreaterThanOrEqualTo(Integer value) {
            addCriterion("PREFIJO_TELEFONO >=", value, "prefijoTelefono");
            return (Criteria) this;
        }

        public Criteria andPrefijoTelefonoLessThan(Integer value) {
            addCriterion("PREFIJO_TELEFONO <", value, "prefijoTelefono");
            return (Criteria) this;
        }

        public Criteria andPrefijoTelefonoLessThanOrEqualTo(Integer value) {
            addCriterion("PREFIJO_TELEFONO <=", value, "prefijoTelefono");
            return (Criteria) this;
        }

        public Criteria andPrefijoTelefonoIn(List<Integer> values) {
            addCriterion("PREFIJO_TELEFONO in", values, "prefijoTelefono");
            return (Criteria) this;
        }

        public Criteria andPrefijoTelefonoNotIn(List<Integer> values) {
            addCriterion("PREFIJO_TELEFONO not in", values, "prefijoTelefono");
            return (Criteria) this;
        }

        public Criteria andPrefijoTelefonoBetween(Integer value1, Integer value2) {
            addCriterion("PREFIJO_TELEFONO between", value1, value2, "prefijoTelefono");
            return (Criteria) this;
        }

        public Criteria andPrefijoTelefonoNotBetween(Integer value1, Integer value2) {
            addCriterion("PREFIJO_TELEFONO not between", value1, value2, "prefijoTelefono");
            return (Criteria) this;
        }

        public Criteria andIsoLenguajeIsNull() {
            addCriterion("ISO_LENGUAJE is null");
            return (Criteria) this;
        }

        public Criteria andIsoLenguajeIsNotNull() {
            addCriterion("ISO_LENGUAJE is not null");
            return (Criteria) this;
        }

        public Criteria andIsoLenguajeEqualTo(String value) {
            addCriterion("ISO_LENGUAJE =", value, "isoLenguaje");
            return (Criteria) this;
        }

        public Criteria andIsoLenguajeNotEqualTo(String value) {
            addCriterion("ISO_LENGUAJE <>", value, "isoLenguaje");
            return (Criteria) this;
        }

        public Criteria andIsoLenguajeGreaterThan(String value) {
            addCriterion("ISO_LENGUAJE >", value, "isoLenguaje");
            return (Criteria) this;
        }

        public Criteria andIsoLenguajeGreaterThanOrEqualTo(String value) {
            addCriterion("ISO_LENGUAJE >=", value, "isoLenguaje");
            return (Criteria) this;
        }

        public Criteria andIsoLenguajeLessThan(String value) {
            addCriterion("ISO_LENGUAJE <", value, "isoLenguaje");
            return (Criteria) this;
        }

        public Criteria andIsoLenguajeLessThanOrEqualTo(String value) {
            addCriterion("ISO_LENGUAJE <=", value, "isoLenguaje");
            return (Criteria) this;
        }

        public Criteria andIsoLenguajeLike(String value) {
            addCriterion("ISO_LENGUAJE like", value, "isoLenguaje");
            return (Criteria) this;
        }

        public Criteria andIsoLenguajeNotLike(String value) {
            addCriterion("ISO_LENGUAJE not like", value, "isoLenguaje");
            return (Criteria) this;
        }

        public Criteria andIsoLenguajeIn(List<String> values) {
            addCriterion("ISO_LENGUAJE in", values, "isoLenguaje");
            return (Criteria) this;
        }

        public Criteria andIsoLenguajeNotIn(List<String> values) {
            addCriterion("ISO_LENGUAJE not in", values, "isoLenguaje");
            return (Criteria) this;
        }

        public Criteria andIsoLenguajeBetween(String value1, String value2) {
            addCriterion("ISO_LENGUAJE between", value1, value2, "isoLenguaje");
            return (Criteria) this;
        }

        public Criteria andIsoLenguajeNotBetween(String value1, String value2) {
            addCriterion("ISO_LENGUAJE not between", value1, value2, "isoLenguaje");
            return (Criteria) this;
        }

        public Criteria andFiltrarClientesPaisIsNull() {
            addCriterion("FILTRAR_CLIENTES_PAIS is null");
            return (Criteria) this;
        }

        public Criteria andFiltrarClientesPaisIsNotNull() {
            addCriterion("FILTRAR_CLIENTES_PAIS is not null");
            return (Criteria) this;
        }

        public Criteria andFiltrarClientesPaisEqualTo(Boolean value) {
            addFiltrarClientesPaisCriterion("FILTRAR_CLIENTES_PAIS =", value, "filtrarClientesPais");
            return (Criteria) this;
        }

        public Criteria andFiltrarClientesPaisNotEqualTo(Boolean value) {
            addFiltrarClientesPaisCriterion("FILTRAR_CLIENTES_PAIS <>", value, "filtrarClientesPais");
            return (Criteria) this;
        }

        public Criteria andFiltrarClientesPaisGreaterThan(Boolean value) {
            addFiltrarClientesPaisCriterion("FILTRAR_CLIENTES_PAIS >", value, "filtrarClientesPais");
            return (Criteria) this;
        }

        public Criteria andFiltrarClientesPaisGreaterThanOrEqualTo(Boolean value) {
            addFiltrarClientesPaisCriterion("FILTRAR_CLIENTES_PAIS >=", value, "filtrarClientesPais");
            return (Criteria) this;
        }

        public Criteria andFiltrarClientesPaisLessThan(Boolean value) {
            addFiltrarClientesPaisCriterion("FILTRAR_CLIENTES_PAIS <", value, "filtrarClientesPais");
            return (Criteria) this;
        }

        public Criteria andFiltrarClientesPaisLessThanOrEqualTo(Boolean value) {
            addFiltrarClientesPaisCriterion("FILTRAR_CLIENTES_PAIS <=", value, "filtrarClientesPais");
            return (Criteria) this;
        }

        public Criteria andFiltrarClientesPaisLike(Boolean value) {
            addFiltrarClientesPaisCriterion("FILTRAR_CLIENTES_PAIS like", value, "filtrarClientesPais");
            return (Criteria) this;
        }

        public Criteria andFiltrarClientesPaisNotLike(Boolean value) {
            addFiltrarClientesPaisCriterion("FILTRAR_CLIENTES_PAIS not like", value, "filtrarClientesPais");
            return (Criteria) this;
        }

        public Criteria andFiltrarClientesPaisIn(List<Boolean> values) {
            addFiltrarClientesPaisCriterion("FILTRAR_CLIENTES_PAIS in", values, "filtrarClientesPais");
            return (Criteria) this;
        }

        public Criteria andFiltrarClientesPaisNotIn(List<Boolean> values) {
            addFiltrarClientesPaisCriterion("FILTRAR_CLIENTES_PAIS not in", values, "filtrarClientesPais");
            return (Criteria) this;
        }

        public Criteria andFiltrarClientesPaisBetween(Boolean value1, Boolean value2) {
            addFiltrarClientesPaisCriterion("FILTRAR_CLIENTES_PAIS between", value1, value2, "filtrarClientesPais");
            return (Criteria) this;
        }

        public Criteria andFiltrarClientesPaisNotBetween(Boolean value1, Boolean value2) {
            addFiltrarClientesPaisCriterion("FILTRAR_CLIENTES_PAIS not between", value1, value2, "filtrarClientesPais");
            return (Criteria) this;
        }

        public Criteria andUidInstanciaLikeInsensitive(String value) {
            addCriterion("upper(UID_INSTANCIA) like", value.toUpperCase(), "uidInstancia");
            return (Criteria) this;
        }

        public Criteria andCodpaisLikeInsensitive(String value) {
            addCriterion("upper(CODPAIS) like", value.toUpperCase(), "codpais");
            return (Criteria) this;
        }

        public Criteria andIsoLenguajeLikeInsensitive(String value) {
            addCriterion("upper(ISO_LENGUAJE) like", value.toUpperCase(), "isoLenguaje");
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