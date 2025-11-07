package com.comerzzia.bimbaylola.pos.persistence.core.config.configcontadores.rangos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfigContadorRangoExample {
	public static final String ORDER_BY_ID_CONTADOR = "ID_CONTADOR";

    public static final String ORDER_BY_ID_CONTADOR_DESC = "ID_CONTADOR DESC";

    public static final String ORDER_BY_ID_RANGO = "ID_RANGO";

    public static final String ORDER_BY_ID_RANGO_DESC = "ID_RANGO DESC";

    public static final String ORDER_BY_RANGO_DESCRIPCION = "RANGO_DESCRIPCION";

    public static final String ORDER_BY_RANGO_DESCRIPCION_DESC = "RANGO_DESCRIPCION DESC";

    public static final String ORDER_BY_RANGO_INICIO = "RANGO_INICIO";

    public static final String ORDER_BY_RANGO_INICIO_DESC = "RANGO_INICIO DESC";

    public static final String ORDER_BY_RANGO_FIN = "RANGO_FIN";

    public static final String ORDER_BY_RANGO_FIN_DESC = "RANGO_FIN DESC";

    public static final String ORDER_BY_RANGO_AVISO = "RANGO_AVISO";

    public static final String ORDER_BY_RANGO_AVISO_DESC = "RANGO_AVISO DESC";

    public static final String ORDER_BY_RANGO_AVISO_INTERVALO = "RANGO_AVISO_INTERVALO";

    public static final String ORDER_BY_RANGO_AVISO_INTERVALO_DESC = "RANGO_AVISO_INTERVALO DESC";

    public static final String ORDER_BY_RANGO_ULTIMO_AVISO = "RANGO_ULTIMO_AVISO";

    public static final String ORDER_BY_RANGO_ULTIMO_AVISO_DESC = "RANGO_ULTIMO_AVISO DESC";

    public static final String ORDER_BY_RANGO_FECHA_INICIO = "RANGO_FECHA_INICIO";

    public static final String ORDER_BY_RANGO_FECHA_INICIO_DESC = "RANGO_FECHA_INICIO DESC";

    public static final String ORDER_BY_RANGO_FECHA_FIN = "RANGO_FECHA_FIN";

    public static final String ORDER_BY_RANGO_FECHA_FIN_DESC = "RANGO_FECHA_FIN DESC";

    public static final String ORDER_BY_RANGO_FECHA_AVISO = "RANGO_FECHA_AVISO";

    public static final String ORDER_BY_RANGO_FECHA_AVISO_DESC = "RANGO_FECHA_AVISO DESC";

    public static final String ORDER_BY_RANGO_FECHA_AVISO_INTERVALO = "RANGO_FECHA_AVISO_INTERVALO";

    public static final String ORDER_BY_RANGO_FECHA_AVISO_INTERVALO_DESC = "RANGO_FECHA_AVISO_INTERVALO DESC";

    public static final String ORDER_BY_RANGO_FECHA_ULTIMO_AVISO = "RANGO_FECHA_ULTIMO_AVISO";

    public static final String ORDER_BY_RANGO_FECHA_ULTIMO_AVISO_DESC = "RANGO_FECHA_ULTIMO_AVISO DESC";

    public static final String ORDER_BY_CODEMP = "CODEMP";

    public static final String ORDER_BY_CODEMP_DESC = "CODEMP DESC";

    public static final String ORDER_BY_CODALM = "CODALM";

    public static final String ORDER_BY_CODALM_DESC = "CODALM DESC";

    public static final String ORDER_BY_CODCAJA = "CODCAJA";

    public static final String ORDER_BY_CODCAJA_DESC = "CODCAJA DESC";
	
	
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ConfigContadorRangoExample() {
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

        public Criteria andIdRangoIsNull() {
            addCriterion("ID_RANGO is null");
            return (Criteria) this;
        }

        public Criteria andIdRangoIsNotNull() {
            addCriterion("ID_RANGO is not null");
            return (Criteria) this;
        }

        public Criteria andIdRangoEqualTo(String value) {
            addCriterion("ID_RANGO =", value, "idRango");
            return (Criteria) this;
        }

        public Criteria andIdRangoNotEqualTo(String value) {
            addCriterion("ID_RANGO <>", value, "idRango");
            return (Criteria) this;
        }

        public Criteria andIdRangoGreaterThan(String value) {
            addCriterion("ID_RANGO >", value, "idRango");
            return (Criteria) this;
        }

        public Criteria andIdRangoGreaterThanOrEqualTo(String value) {
            addCriterion("ID_RANGO >=", value, "idRango");
            return (Criteria) this;
        }

        public Criteria andIdRangoLessThan(String value) {
            addCriterion("ID_RANGO <", value, "idRango");
            return (Criteria) this;
        }

        public Criteria andIdRangoLessThanOrEqualTo(String value) {
            addCriterion("ID_RANGO <=", value, "idRango");
            return (Criteria) this;
        }

        public Criteria andIdRangoLike(String value) {
            addCriterion("ID_RANGO like", value, "idRango");
            return (Criteria) this;
        }

        public Criteria andIdRangoNotLike(String value) {
            addCriterion("ID_RANGO not like", value, "idRango");
            return (Criteria) this;
        }

        public Criteria andIdRangoIn(List<String> values) {
            addCriterion("ID_RANGO in", values, "idRango");
            return (Criteria) this;
        }

        public Criteria andIdRangoNotIn(List<String> values) {
            addCriterion("ID_RANGO not in", values, "idRango");
            return (Criteria) this;
        }

        public Criteria andIdRangoBetween(String value1, String value2) {
            addCriterion("ID_RANGO between", value1, value2, "idRango");
            return (Criteria) this;
        }

        public Criteria andIdRangoNotBetween(String value1, String value2) {
            addCriterion("ID_RANGO not between", value1, value2, "idRango");
            return (Criteria) this;
        }

        public Criteria andRangoDescripcionIsNull() {
            addCriterion("RANGO_DESCRIPCION is null");
            return (Criteria) this;
        }

        public Criteria andRangoDescripcionIsNotNull() {
            addCriterion("RANGO_DESCRIPCION is not null");
            return (Criteria) this;
        }

        public Criteria andRangoDescripcionEqualTo(String value) {
            addCriterion("RANGO_DESCRIPCION =", value, "rangoDescripcion");
            return (Criteria) this;
        }

        public Criteria andRangoDescripcionNotEqualTo(String value) {
            addCriterion("RANGO_DESCRIPCION <>", value, "rangoDescripcion");
            return (Criteria) this;
        }

        public Criteria andRangoDescripcionGreaterThan(String value) {
            addCriterion("RANGO_DESCRIPCION >", value, "rangoDescripcion");
            return (Criteria) this;
        }

        public Criteria andRangoDescripcionGreaterThanOrEqualTo(String value) {
            addCriterion("RANGO_DESCRIPCION >=", value, "rangoDescripcion");
            return (Criteria) this;
        }

        public Criteria andRangoDescripcionLessThan(String value) {
            addCriterion("RANGO_DESCRIPCION <", value, "rangoDescripcion");
            return (Criteria) this;
        }

        public Criteria andRangoDescripcionLessThanOrEqualTo(String value) {
            addCriterion("RANGO_DESCRIPCION <=", value, "rangoDescripcion");
            return (Criteria) this;
        }

        public Criteria andRangoDescripcionLike(String value) {
            addCriterion("RANGO_DESCRIPCION like", value, "rangoDescripcion");
            return (Criteria) this;
        }

        public Criteria andRangoDescripcionNotLike(String value) {
            addCriterion("RANGO_DESCRIPCION not like", value, "rangoDescripcion");
            return (Criteria) this;
        }

        public Criteria andRangoDescripcionIn(List<String> values) {
            addCriterion("RANGO_DESCRIPCION in", values, "rangoDescripcion");
            return (Criteria) this;
        }

        public Criteria andRangoDescripcionNotIn(List<String> values) {
            addCriterion("RANGO_DESCRIPCION not in", values, "rangoDescripcion");
            return (Criteria) this;
        }

        public Criteria andRangoDescripcionBetween(String value1, String value2) {
            addCriterion("RANGO_DESCRIPCION between", value1, value2, "rangoDescripcion");
            return (Criteria) this;
        }

        public Criteria andRangoDescripcionNotBetween(String value1, String value2) {
            addCriterion("RANGO_DESCRIPCION not between", value1, value2, "rangoDescripcion");
            return (Criteria) this;
        }

        public Criteria andRangoInicioIsNull() {
            addCriterion("RANGO_INICIO is null");
            return (Criteria) this;
        }

        public Criteria andRangoInicioIsNotNull() {
            addCriterion("RANGO_INICIO is not null");
            return (Criteria) this;
        }

        public Criteria andRangoInicioEqualTo(Long value) {
            addCriterion("RANGO_INICIO =", value, "rangoInicio");
            return (Criteria) this;
        }

        public Criteria andRangoInicioNotEqualTo(Long value) {
            addCriterion("RANGO_INICIO <>", value, "rangoInicio");
            return (Criteria) this;
        }

        public Criteria andRangoInicioGreaterThan(Long value) {
            addCriterion("RANGO_INICIO >", value, "rangoInicio");
            return (Criteria) this;
        }

        public Criteria andRangoInicioGreaterThanOrEqualTo(Long value) {
            addCriterion("RANGO_INICIO >=", value, "rangoInicio");
            return (Criteria) this;
        }

        public Criteria andRangoInicioLessThan(Long value) {
            addCriterion("RANGO_INICIO <", value, "rangoInicio");
            return (Criteria) this;
        }

        public Criteria andRangoInicioLessThanOrEqualTo(Long value) {
            addCriterion("RANGO_INICIO <=", value, "rangoInicio");
            return (Criteria) this;
        }

        public Criteria andRangoInicioIn(List<Long> values) {
            addCriterion("RANGO_INICIO in", values, "rangoInicio");
            return (Criteria) this;
        }

        public Criteria andRangoInicioNotIn(List<Long> values) {
            addCriterion("RANGO_INICIO not in", values, "rangoInicio");
            return (Criteria) this;
        }

        public Criteria andRangoInicioBetween(Long value1, Long value2) {
            addCriterion("RANGO_INICIO between", value1, value2, "rangoInicio");
            return (Criteria) this;
        }

        public Criteria andRangoInicioNotBetween(Long value1, Long value2) {
            addCriterion("RANGO_INICIO not between", value1, value2, "rangoInicio");
            return (Criteria) this;
        }

        public Criteria andRangoFinIsNull() {
            addCriterion("RANGO_FIN is null");
            return (Criteria) this;
        }

        public Criteria andRangoFinIsNotNull() {
            addCriterion("RANGO_FIN is not null");
            return (Criteria) this;
        }

        public Criteria andRangoFinEqualTo(Long value) {
            addCriterion("RANGO_FIN =", value, "rangoFin");
            return (Criteria) this;
        }

        public Criteria andRangoFinNotEqualTo(Long value) {
            addCriterion("RANGO_FIN <>", value, "rangoFin");
            return (Criteria) this;
        }

        public Criteria andRangoFinGreaterThan(Long value) {
            addCriterion("RANGO_FIN >", value, "rangoFin");
            return (Criteria) this;
        }

        public Criteria andRangoFinGreaterThanOrEqualTo(Long value) {
            addCriterion("RANGO_FIN >=", value, "rangoFin");
            return (Criteria) this;
        }

        public Criteria andRangoFinLessThan(Long value) {
            addCriterion("RANGO_FIN <", value, "rangoFin");
            return (Criteria) this;
        }

        public Criteria andRangoFinLessThanOrEqualTo(Long value) {
            addCriterion("RANGO_FIN <=", value, "rangoFin");
            return (Criteria) this;
        }

        public Criteria andRangoFinIn(List<Long> values) {
            addCriterion("RANGO_FIN in", values, "rangoFin");
            return (Criteria) this;
        }

        public Criteria andRangoFinNotIn(List<Long> values) {
            addCriterion("RANGO_FIN not in", values, "rangoFin");
            return (Criteria) this;
        }

        public Criteria andRangoFinBetween(Long value1, Long value2) {
            addCriterion("RANGO_FIN between", value1, value2, "rangoFin");
            return (Criteria) this;
        }

        public Criteria andRangoFinNotBetween(Long value1, Long value2) {
            addCriterion("RANGO_FIN not between", value1, value2, "rangoFin");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoIsNull() {
            addCriterion("RANGO_AVISO is null");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoIsNotNull() {
            addCriterion("RANGO_AVISO is not null");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoEqualTo(Long value) {
            addCriterion("RANGO_AVISO =", value, "rangoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoNotEqualTo(Long value) {
            addCriterion("RANGO_AVISO <>", value, "rangoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoGreaterThan(Long value) {
            addCriterion("RANGO_AVISO >", value, "rangoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoGreaterThanOrEqualTo(Long value) {
            addCriterion("RANGO_AVISO >=", value, "rangoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoLessThan(Long value) {
            addCriterion("RANGO_AVISO <", value, "rangoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoLessThanOrEqualTo(Long value) {
            addCriterion("RANGO_AVISO <=", value, "rangoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoIn(List<Long> values) {
            addCriterion("RANGO_AVISO in", values, "rangoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoNotIn(List<Long> values) {
            addCriterion("RANGO_AVISO not in", values, "rangoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoBetween(Long value1, Long value2) {
            addCriterion("RANGO_AVISO between", value1, value2, "rangoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoNotBetween(Long value1, Long value2) {
            addCriterion("RANGO_AVISO not between", value1, value2, "rangoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoIntervaloIsNull() {
            addCriterion("RANGO_AVISO_INTERVALO is null");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoIntervaloIsNotNull() {
            addCriterion("RANGO_AVISO_INTERVALO is not null");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoIntervaloEqualTo(Short value) {
            addCriterion("RANGO_AVISO_INTERVALO =", value, "rangoAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoIntervaloNotEqualTo(Short value) {
            addCriterion("RANGO_AVISO_INTERVALO <>", value, "rangoAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoIntervaloGreaterThan(Short value) {
            addCriterion("RANGO_AVISO_INTERVALO >", value, "rangoAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoIntervaloGreaterThanOrEqualTo(Short value) {
            addCriterion("RANGO_AVISO_INTERVALO >=", value, "rangoAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoIntervaloLessThan(Short value) {
            addCriterion("RANGO_AVISO_INTERVALO <", value, "rangoAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoIntervaloLessThanOrEqualTo(Short value) {
            addCriterion("RANGO_AVISO_INTERVALO <=", value, "rangoAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoIntervaloIn(List<Short> values) {
            addCriterion("RANGO_AVISO_INTERVALO in", values, "rangoAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoIntervaloNotIn(List<Short> values) {
            addCriterion("RANGO_AVISO_INTERVALO not in", values, "rangoAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoIntervaloBetween(Short value1, Short value2) {
            addCriterion("RANGO_AVISO_INTERVALO between", value1, value2, "rangoAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoAvisoIntervaloNotBetween(Short value1, Short value2) {
            addCriterion("RANGO_AVISO_INTERVALO not between", value1, value2, "rangoAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoUltimoAvisoIsNull() {
            addCriterion("RANGO_ULTIMO_AVISO is null");
            return (Criteria) this;
        }

        public Criteria andRangoUltimoAvisoIsNotNull() {
            addCriterion("RANGO_ULTIMO_AVISO is not null");
            return (Criteria) this;
        }

        public Criteria andRangoUltimoAvisoEqualTo(Long value) {
            addCriterion("RANGO_ULTIMO_AVISO =", value, "rangoUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoUltimoAvisoNotEqualTo(Long value) {
            addCriterion("RANGO_ULTIMO_AVISO <>", value, "rangoUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoUltimoAvisoGreaterThan(Long value) {
            addCriterion("RANGO_ULTIMO_AVISO >", value, "rangoUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoUltimoAvisoGreaterThanOrEqualTo(Long value) {
            addCriterion("RANGO_ULTIMO_AVISO >=", value, "rangoUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoUltimoAvisoLessThan(Long value) {
            addCriterion("RANGO_ULTIMO_AVISO <", value, "rangoUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoUltimoAvisoLessThanOrEqualTo(Long value) {
            addCriterion("RANGO_ULTIMO_AVISO <=", value, "rangoUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoUltimoAvisoIn(List<Long> values) {
            addCriterion("RANGO_ULTIMO_AVISO in", values, "rangoUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoUltimoAvisoNotIn(List<Long> values) {
            addCriterion("RANGO_ULTIMO_AVISO not in", values, "rangoUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoUltimoAvisoBetween(Long value1, Long value2) {
            addCriterion("RANGO_ULTIMO_AVISO between", value1, value2, "rangoUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoUltimoAvisoNotBetween(Long value1, Long value2) {
            addCriterion("RANGO_ULTIMO_AVISO not between", value1, value2, "rangoUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaInicioIsNull() {
            addCriterion("RANGO_FECHA_INICIO is null");
            return (Criteria) this;
        }

        public Criteria andRangoFechaInicioIsNotNull() {
            addCriterion("RANGO_FECHA_INICIO is not null");
            return (Criteria) this;
        }

        public Criteria andRangoFechaInicioEqualTo(Date value) {
            addCriterion("RANGO_FECHA_INICIO =", value, "rangoFechaInicio");
            return (Criteria) this;
        }

        public Criteria andRangoFechaInicioNotEqualTo(Date value) {
            addCriterion("RANGO_FECHA_INICIO <>", value, "rangoFechaInicio");
            return (Criteria) this;
        }

        public Criteria andRangoFechaInicioGreaterThan(Date value) {
            addCriterion("RANGO_FECHA_INICIO >", value, "rangoFechaInicio");
            return (Criteria) this;
        }

        public Criteria andRangoFechaInicioGreaterThanOrEqualTo(Date value) {
            addCriterion("RANGO_FECHA_INICIO >=", value, "rangoFechaInicio");
            return (Criteria) this;
        }

        public Criteria andRangoFechaInicioLessThan(Date value) {
            addCriterion("RANGO_FECHA_INICIO <", value, "rangoFechaInicio");
            return (Criteria) this;
        }

        public Criteria andRangoFechaInicioLessThanOrEqualTo(Date value) {
            addCriterion("RANGO_FECHA_INICIO <=", value, "rangoFechaInicio");
            return (Criteria) this;
        }

        public Criteria andRangoFechaInicioIn(List<Date> values) {
            addCriterion("RANGO_FECHA_INICIO in", values, "rangoFechaInicio");
            return (Criteria) this;
        }

        public Criteria andRangoFechaInicioNotIn(List<Date> values) {
            addCriterion("RANGO_FECHA_INICIO not in", values, "rangoFechaInicio");
            return (Criteria) this;
        }

        public Criteria andRangoFechaInicioBetween(Date value1, Date value2) {
            addCriterion("RANGO_FECHA_INICIO between", value1, value2, "rangoFechaInicio");
            return (Criteria) this;
        }

        public Criteria andRangoFechaInicioNotBetween(Date value1, Date value2) {
            addCriterion("RANGO_FECHA_INICIO not between", value1, value2, "rangoFechaInicio");
            return (Criteria) this;
        }

        public Criteria andRangoFechaFinIsNull() {
            addCriterion("RANGO_FECHA_FIN is null");
            return (Criteria) this;
        }

        public Criteria andRangoFechaFinIsNotNull() {
            addCriterion("RANGO_FECHA_FIN is not null");
            return (Criteria) this;
        }

        public Criteria andRangoFechaFinEqualTo(Date value) {
            addCriterion("RANGO_FECHA_FIN =", value, "rangoFechaFin");
            return (Criteria) this;
        }

        public Criteria andRangoFechaFinNotEqualTo(Date value) {
            addCriterion("RANGO_FECHA_FIN <>", value, "rangoFechaFin");
            return (Criteria) this;
        }

        public Criteria andRangoFechaFinGreaterThan(Date value) {
            addCriterion("RANGO_FECHA_FIN >", value, "rangoFechaFin");
            return (Criteria) this;
        }

        public Criteria andRangoFechaFinGreaterThanOrEqualTo(Date value) {
            addCriterion("RANGO_FECHA_FIN >=", value, "rangoFechaFin");
            return (Criteria) this;
        }

        public Criteria andRangoFechaFinLessThan(Date value) {
            addCriterion("RANGO_FECHA_FIN <", value, "rangoFechaFin");
            return (Criteria) this;
        }

        public Criteria andRangoFechaFinLessThanOrEqualTo(Date value) {
            addCriterion("RANGO_FECHA_FIN <=", value, "rangoFechaFin");
            return (Criteria) this;
        }

        public Criteria andRangoFechaFinIn(List<Date> values) {
            addCriterion("RANGO_FECHA_FIN in", values, "rangoFechaFin");
            return (Criteria) this;
        }

        public Criteria andRangoFechaFinNotIn(List<Date> values) {
            addCriterion("RANGO_FECHA_FIN not in", values, "rangoFechaFin");
            return (Criteria) this;
        }

        public Criteria andRangoFechaFinBetween(Date value1, Date value2) {
            addCriterion("RANGO_FECHA_FIN between", value1, value2, "rangoFechaFin");
            return (Criteria) this;
        }

        public Criteria andRangoFechaFinNotBetween(Date value1, Date value2) {
            addCriterion("RANGO_FECHA_FIN not between", value1, value2, "rangoFechaFin");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoIsNull() {
            addCriterion("RANGO_FECHA_AVISO is null");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoIsNotNull() {
            addCriterion("RANGO_FECHA_AVISO is not null");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoEqualTo(Date value) {
            addCriterion("RANGO_FECHA_AVISO =", value, "rangoFechaAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoNotEqualTo(Date value) {
            addCriterion("RANGO_FECHA_AVISO <>", value, "rangoFechaAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoGreaterThan(Date value) {
            addCriterion("RANGO_FECHA_AVISO >", value, "rangoFechaAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoGreaterThanOrEqualTo(Date value) {
            addCriterion("RANGO_FECHA_AVISO >=", value, "rangoFechaAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoLessThan(Date value) {
            addCriterion("RANGO_FECHA_AVISO <", value, "rangoFechaAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoLessThanOrEqualTo(Date value) {
            addCriterion("RANGO_FECHA_AVISO <=", value, "rangoFechaAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoIn(List<Date> values) {
            addCriterion("RANGO_FECHA_AVISO in", values, "rangoFechaAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoNotIn(List<Date> values) {
            addCriterion("RANGO_FECHA_AVISO not in", values, "rangoFechaAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoBetween(Date value1, Date value2) {
            addCriterion("RANGO_FECHA_AVISO between", value1, value2, "rangoFechaAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoNotBetween(Date value1, Date value2) {
            addCriterion("RANGO_FECHA_AVISO not between", value1, value2, "rangoFechaAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoIntervaloIsNull() {
            addCriterion("RANGO_FECHA_AVISO_INTERVALO is null");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoIntervaloIsNotNull() {
            addCriterion("RANGO_FECHA_AVISO_INTERVALO is not null");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoIntervaloEqualTo(Short value) {
            addCriterion("RANGO_FECHA_AVISO_INTERVALO =", value, "rangoFechaAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoIntervaloNotEqualTo(Short value) {
            addCriterion("RANGO_FECHA_AVISO_INTERVALO <>", value, "rangoFechaAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoIntervaloGreaterThan(Short value) {
            addCriterion("RANGO_FECHA_AVISO_INTERVALO >", value, "rangoFechaAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoIntervaloGreaterThanOrEqualTo(Short value) {
            addCriterion("RANGO_FECHA_AVISO_INTERVALO >=", value, "rangoFechaAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoIntervaloLessThan(Short value) {
            addCriterion("RANGO_FECHA_AVISO_INTERVALO <", value, "rangoFechaAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoIntervaloLessThanOrEqualTo(Short value) {
            addCriterion("RANGO_FECHA_AVISO_INTERVALO <=", value, "rangoFechaAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoIntervaloIn(List<Short> values) {
            addCriterion("RANGO_FECHA_AVISO_INTERVALO in", values, "rangoFechaAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoIntervaloNotIn(List<Short> values) {
            addCriterion("RANGO_FECHA_AVISO_INTERVALO not in", values, "rangoFechaAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoIntervaloBetween(Short value1, Short value2) {
            addCriterion("RANGO_FECHA_AVISO_INTERVALO between", value1, value2, "rangoFechaAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoFechaAvisoIntervaloNotBetween(Short value1, Short value2) {
            addCriterion("RANGO_FECHA_AVISO_INTERVALO not between", value1, value2, "rangoFechaAvisoIntervalo");
            return (Criteria) this;
        }

        public Criteria andRangoFechaUltimoAvisoIsNull() {
            addCriterion("RANGO_FECHA_ULTIMO_AVISO is null");
            return (Criteria) this;
        }

        public Criteria andRangoFechaUltimoAvisoIsNotNull() {
            addCriterion("RANGO_FECHA_ULTIMO_AVISO is not null");
            return (Criteria) this;
        }

        public Criteria andRangoFechaUltimoAvisoEqualTo(Date value) {
            addCriterion("RANGO_FECHA_ULTIMO_AVISO =", value, "rangoFechaUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaUltimoAvisoNotEqualTo(Date value) {
            addCriterion("RANGO_FECHA_ULTIMO_AVISO <>", value, "rangoFechaUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaUltimoAvisoGreaterThan(Date value) {
            addCriterion("RANGO_FECHA_ULTIMO_AVISO >", value, "rangoFechaUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaUltimoAvisoGreaterThanOrEqualTo(Date value) {
            addCriterion("RANGO_FECHA_ULTIMO_AVISO >=", value, "rangoFechaUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaUltimoAvisoLessThan(Date value) {
            addCriterion("RANGO_FECHA_ULTIMO_AVISO <", value, "rangoFechaUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaUltimoAvisoLessThanOrEqualTo(Date value) {
            addCriterion("RANGO_FECHA_ULTIMO_AVISO <=", value, "rangoFechaUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaUltimoAvisoIn(List<Date> values) {
            addCriterion("RANGO_FECHA_ULTIMO_AVISO in", values, "rangoFechaUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaUltimoAvisoNotIn(List<Date> values) {
            addCriterion("RANGO_FECHA_ULTIMO_AVISO not in", values, "rangoFechaUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaUltimoAvisoBetween(Date value1, Date value2) {
            addCriterion("RANGO_FECHA_ULTIMO_AVISO between", value1, value2, "rangoFechaUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andRangoFechaUltimoAvisoNotBetween(Date value1, Date value2) {
            addCriterion("RANGO_FECHA_ULTIMO_AVISO not between", value1, value2, "rangoFechaUltimoAviso");
            return (Criteria) this;
        }

        public Criteria andCodempIsNull() {
            addCriterion("CODEMP is null");
            return (Criteria) this;
        }

        public Criteria andCodempIsNotNull() {
            addCriterion("CODEMP is not null");
            return (Criteria) this;
        }

        public Criteria andCodempEqualTo(String value) {
            addCriterion("CODEMP =", value, "codemp");
            return (Criteria) this;
        }

        public Criteria andCodempNotEqualTo(String value) {
            addCriterion("CODEMP <>", value, "codemp");
            return (Criteria) this;
        }

        public Criteria andCodempGreaterThan(String value) {
            addCriterion("CODEMP >", value, "codemp");
            return (Criteria) this;
        }

        public Criteria andCodempGreaterThanOrEqualTo(String value) {
            addCriterion("CODEMP >=", value, "codemp");
            return (Criteria) this;
        }

        public Criteria andCodempLessThan(String value) {
            addCriterion("CODEMP <", value, "codemp");
            return (Criteria) this;
        }

        public Criteria andCodempLessThanOrEqualTo(String value) {
            addCriterion("CODEMP <=", value, "codemp");
            return (Criteria) this;
        }

        public Criteria andCodempLike(String value) {
            addCriterion("CODEMP like", value, "codemp");
            return (Criteria) this;
        }

        public Criteria andCodempNotLike(String value) {
            addCriterion("CODEMP not like", value, "codemp");
            return (Criteria) this;
        }

        public Criteria andCodempIn(List<String> values) {
            addCriterion("CODEMP in", values, "codemp");
            return (Criteria) this;
        }

        public Criteria andCodempNotIn(List<String> values) {
            addCriterion("CODEMP not in", values, "codemp");
            return (Criteria) this;
        }

        public Criteria andCodempBetween(String value1, String value2) {
            addCriterion("CODEMP between", value1, value2, "codemp");
            return (Criteria) this;
        }

        public Criteria andCodempNotBetween(String value1, String value2) {
            addCriterion("CODEMP not between", value1, value2, "codemp");
            return (Criteria) this;
        }

        public Criteria andCodalmIsNull() {
            addCriterion("CODALM is null");
            return (Criteria) this;
        }

        public Criteria andCodalmIsNotNull() {
            addCriterion("CODALM is not null");
            return (Criteria) this;
        }

        public Criteria andCodalmEqualTo(String value) {
            addCriterion("CODALM =", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmNotEqualTo(String value) {
            addCriterion("CODALM <>", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmGreaterThan(String value) {
            addCriterion("CODALM >", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmGreaterThanOrEqualTo(String value) {
            addCriterion("CODALM >=", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmLessThan(String value) {
            addCriterion("CODALM <", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmLessThanOrEqualTo(String value) {
            addCriterion("CODALM <=", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmLike(String value) {
            addCriterion("CODALM like", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmNotLike(String value) {
            addCriterion("CODALM not like", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmIn(List<String> values) {
            addCriterion("CODALM in", values, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmNotIn(List<String> values) {
            addCriterion("CODALM not in", values, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmBetween(String value1, String value2) {
            addCriterion("CODALM between", value1, value2, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmNotBetween(String value1, String value2) {
            addCriterion("CODALM not between", value1, value2, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodcajaIsNull() {
            addCriterion("CODCAJA is null");
            return (Criteria) this;
        }

        public Criteria andCodcajaIsNotNull() {
            addCriterion("CODCAJA is not null");
            return (Criteria) this;
        }

        public Criteria andCodcajaEqualTo(String value) {
            addCriterion("CODCAJA =", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaNotEqualTo(String value) {
            addCriterion("CODCAJA <>", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaGreaterThan(String value) {
            addCriterion("CODCAJA >", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaGreaterThanOrEqualTo(String value) {
            addCriterion("CODCAJA >=", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaLessThan(String value) {
            addCriterion("CODCAJA <", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaLessThanOrEqualTo(String value) {
            addCriterion("CODCAJA <=", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaLike(String value) {
            addCriterion("CODCAJA like", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaNotLike(String value) {
            addCriterion("CODCAJA not like", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaIn(List<String> values) {
            addCriterion("CODCAJA in", values, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaNotIn(List<String> values) {
            addCriterion("CODCAJA not in", values, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaBetween(String value1, String value2) {
            addCriterion("CODCAJA between", value1, value2, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaNotBetween(String value1, String value2) {
            addCriterion("CODCAJA not between", value1, value2, "codcaja");
            return (Criteria) this;
        }

        public Criteria andRangoCoNumResolucionIsNull() {
            addCriterion("RANGO_CO_NUM_RESOLUCION is null");
            return (Criteria) this;
        }

        public Criteria andRangoCoNumResolucionIsNotNull() {
            addCriterion("RANGO_CO_NUM_RESOLUCION is not null");
            return (Criteria) this;
        }

        public Criteria andRangoCoNumResolucionEqualTo(String value) {
            addCriterion("RANGO_CO_NUM_RESOLUCION =", value, "rangoCoNumResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoNumResolucionNotEqualTo(String value) {
            addCriterion("RANGO_CO_NUM_RESOLUCION <>", value, "rangoCoNumResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoNumResolucionGreaterThan(String value) {
            addCriterion("RANGO_CO_NUM_RESOLUCION >", value, "rangoCoNumResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoNumResolucionGreaterThanOrEqualTo(String value) {
            addCriterion("RANGO_CO_NUM_RESOLUCION >=", value, "rangoCoNumResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoNumResolucionLessThan(String value) {
            addCriterion("RANGO_CO_NUM_RESOLUCION <", value, "rangoCoNumResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoNumResolucionLessThanOrEqualTo(String value) {
            addCriterion("RANGO_CO_NUM_RESOLUCION <=", value, "rangoCoNumResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoNumResolucionLike(String value) {
            addCriterion("RANGO_CO_NUM_RESOLUCION like", value, "rangoCoNumResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoNumResolucionNotLike(String value) {
            addCriterion("RANGO_CO_NUM_RESOLUCION not like", value, "rangoCoNumResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoNumResolucionIn(List<String> values) {
            addCriterion("RANGO_CO_NUM_RESOLUCION in", values, "rangoCoNumResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoNumResolucionNotIn(List<String> values) {
            addCriterion("RANGO_CO_NUM_RESOLUCION not in", values, "rangoCoNumResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoNumResolucionBetween(String value1, String value2) {
            addCriterion("RANGO_CO_NUM_RESOLUCION between", value1, value2, "rangoCoNumResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoNumResolucionNotBetween(String value1, String value2) {
            addCriterion("RANGO_CO_NUM_RESOLUCION not between", value1, value2, "rangoCoNumResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoFechaResolucionIsNull() {
            addCriterion("RANGO_CO_FECHA_RESOLUCION is null");
            return (Criteria) this;
        }

        public Criteria andRangoCoFechaResolucionIsNotNull() {
            addCriterion("RANGO_CO_FECHA_RESOLUCION is not null");
            return (Criteria) this;
        }

        public Criteria andRangoCoFechaResolucionEqualTo(Date value) {
            addCriterion("RANGO_CO_FECHA_RESOLUCION =", value, "rangoCoFechaResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoFechaResolucionNotEqualTo(Date value) {
            addCriterion("RANGO_CO_FECHA_RESOLUCION <>", value, "rangoCoFechaResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoFechaResolucionGreaterThan(Date value) {
            addCriterion("RANGO_CO_FECHA_RESOLUCION >", value, "rangoCoFechaResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoFechaResolucionGreaterThanOrEqualTo(Date value) {
            addCriterion("RANGO_CO_FECHA_RESOLUCION >=", value, "rangoCoFechaResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoFechaResolucionLessThan(Date value) {
            addCriterion("RANGO_CO_FECHA_RESOLUCION <", value, "rangoCoFechaResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoFechaResolucionLessThanOrEqualTo(Date value) {
            addCriterion("RANGO_CO_FECHA_RESOLUCION <=", value, "rangoCoFechaResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoFechaResolucionIn(List<Date> values) {
            addCriterion("RANGO_CO_FECHA_RESOLUCION in", values, "rangoCoFechaResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoFechaResolucionNotIn(List<Date> values) {
            addCriterion("RANGO_CO_FECHA_RESOLUCION not in", values, "rangoCoFechaResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoFechaResolucionBetween(Date value1, Date value2) {
            addCriterion("RANGO_CO_FECHA_RESOLUCION between", value1, value2, "rangoCoFechaResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoFechaResolucionNotBetween(Date value1, Date value2) {
            addCriterion("RANGO_CO_FECHA_RESOLUCION not between", value1, value2, "rangoCoFechaResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoClaveTecnicaIsNull() {
            addCriterion("RANGO_CO_CLAVE_TECNICA is null");
            return (Criteria) this;
        }

        public Criteria andRangoCoClaveTecnicaIsNotNull() {
            addCriterion("RANGO_CO_CLAVE_TECNICA is not null");
            return (Criteria) this;
        }

        public Criteria andRangoCoClaveTecnicaEqualTo(String value) {
            addCriterion("RANGO_CO_CLAVE_TECNICA =", value, "rangoCoClaveTecnica");
            return (Criteria) this;
        }

        public Criteria andRangoCoClaveTecnicaNotEqualTo(String value) {
            addCriterion("RANGO_CO_CLAVE_TECNICA <>", value, "rangoCoClaveTecnica");
            return (Criteria) this;
        }

        public Criteria andRangoCoClaveTecnicaGreaterThan(String value) {
            addCriterion("RANGO_CO_CLAVE_TECNICA >", value, "rangoCoClaveTecnica");
            return (Criteria) this;
        }

        public Criteria andRangoCoClaveTecnicaGreaterThanOrEqualTo(String value) {
            addCriterion("RANGO_CO_CLAVE_TECNICA >=", value, "rangoCoClaveTecnica");
            return (Criteria) this;
        }

        public Criteria andRangoCoClaveTecnicaLessThan(String value) {
            addCriterion("RANGO_CO_CLAVE_TECNICA <", value, "rangoCoClaveTecnica");
            return (Criteria) this;
        }

        public Criteria andRangoCoClaveTecnicaLessThanOrEqualTo(String value) {
            addCriterion("RANGO_CO_CLAVE_TECNICA <=", value, "rangoCoClaveTecnica");
            return (Criteria) this;
        }

        public Criteria andRangoCoClaveTecnicaLike(String value) {
            addCriterion("RANGO_CO_CLAVE_TECNICA like", value, "rangoCoClaveTecnica");
            return (Criteria) this;
        }

        public Criteria andRangoCoClaveTecnicaNotLike(String value) {
            addCriterion("RANGO_CO_CLAVE_TECNICA not like", value, "rangoCoClaveTecnica");
            return (Criteria) this;
        }

        public Criteria andRangoCoClaveTecnicaIn(List<String> values) {
            addCriterion("RANGO_CO_CLAVE_TECNICA in", values, "rangoCoClaveTecnica");
            return (Criteria) this;
        }

        public Criteria andRangoCoClaveTecnicaNotIn(List<String> values) {
            addCriterion("RANGO_CO_CLAVE_TECNICA not in", values, "rangoCoClaveTecnica");
            return (Criteria) this;
        }

        public Criteria andRangoCoClaveTecnicaBetween(String value1, String value2) {
            addCriterion("RANGO_CO_CLAVE_TECNICA between", value1, value2, "rangoCoClaveTecnica");
            return (Criteria) this;
        }

        public Criteria andRangoCoClaveTecnicaNotBetween(String value1, String value2) {
            addCriterion("RANGO_CO_CLAVE_TECNICA not between", value1, value2, "rangoCoClaveTecnica");
            return (Criteria) this;
        }

        public Criteria andRangoCoPrefIsNull() {
            addCriterion("RANGO_CO_PREF is null");
            return (Criteria) this;
        }

        public Criteria andRangoCoPrefIsNotNull() {
            addCriterion("RANGO_CO_PREF is not null");
            return (Criteria) this;
        }

        public Criteria andRangoCoPrefEqualTo(String value) {
            addCriterion("RANGO_CO_PREF =", value, "rangoCoPref");
            return (Criteria) this;
        }

        public Criteria andRangoCoPrefNotEqualTo(String value) {
            addCriterion("RANGO_CO_PREF <>", value, "rangoCoPref");
            return (Criteria) this;
        }

        public Criteria andRangoCoPrefGreaterThan(String value) {
            addCriterion("RANGO_CO_PREF >", value, "rangoCoPref");
            return (Criteria) this;
        }

        public Criteria andRangoCoPrefGreaterThanOrEqualTo(String value) {
            addCriterion("RANGO_CO_PREF >=", value, "rangoCoPref");
            return (Criteria) this;
        }

        public Criteria andRangoCoPrefLessThan(String value) {
            addCriterion("RANGO_CO_PREF <", value, "rangoCoPref");
            return (Criteria) this;
        }

        public Criteria andRangoCoPrefLessThanOrEqualTo(String value) {
            addCriterion("RANGO_CO_PREF <=", value, "rangoCoPref");
            return (Criteria) this;
        }

        public Criteria andRangoCoPrefLike(String value) {
            addCriterion("RANGO_CO_PREF like", value, "rangoCoPref");
            return (Criteria) this;
        }

        public Criteria andRangoCoPrefNotLike(String value) {
            addCriterion("RANGO_CO_PREF not like", value, "rangoCoPref");
            return (Criteria) this;
        }

        public Criteria andRangoCoPrefIn(List<String> values) {
            addCriterion("RANGO_CO_PREF in", values, "rangoCoPref");
            return (Criteria) this;
        }

        public Criteria andRangoCoPrefNotIn(List<String> values) {
            addCriterion("RANGO_CO_PREF not in", values, "rangoCoPref");
            return (Criteria) this;
        }

        public Criteria andRangoCoPrefBetween(String value1, String value2) {
            addCriterion("RANGO_CO_PREF between", value1, value2, "rangoCoPref");
            return (Criteria) this;
        }

        public Criteria andRangoCoPrefNotBetween(String value1, String value2) {
            addCriterion("RANGO_CO_PREF not between", value1, value2, "rangoCoPref");
            return (Criteria) this;
        }

        public Criteria andIdContadorLikeInsensitive(String value) {
            addCriterion("upper(ID_CONTADOR) like", value.toUpperCase(), "idContador");
            return (Criteria) this;
        }

        public Criteria andIdRangoLikeInsensitive(String value) {
            addCriterion("upper(ID_RANGO) like", value.toUpperCase(), "idRango");
            return (Criteria) this;
        }

        public Criteria andRangoDescripcionLikeInsensitive(String value) {
            addCriterion("upper(RANGO_DESCRIPCION) like", value.toUpperCase(), "rangoDescripcion");
            return (Criteria) this;
        }

        public Criteria andCodempLikeInsensitive(String value) {
            addCriterion("upper(CODEMP) like", value.toUpperCase(), "codemp");
            return (Criteria) this;
        }

        public Criteria andCodalmLikeInsensitive(String value) {
            addCriterion("upper(CODALM) like", value.toUpperCase(), "codalm");
            return (Criteria) this;
        }

        public Criteria andCodcajaLikeInsensitive(String value) {
            addCriterion("upper(CODCAJA) like", value.toUpperCase(), "codcaja");
            return (Criteria) this;
        }

        public Criteria andRangoCoNumResolucionLikeInsensitive(String value) {
            addCriterion("upper(RANGO_CO_NUM_RESOLUCION) like", value.toUpperCase(), "rangoCoNumResolucion");
            return (Criteria) this;
        }

        public Criteria andRangoCoClaveTecnicaLikeInsensitive(String value) {
            addCriterion("upper(RANGO_CO_CLAVE_TECNICA) like", value.toUpperCase(), "rangoCoClaveTecnica");
            return (Criteria) this;
        }

        public Criteria andRangoCoPrefLikeInsensitive(String value) {
            addCriterion("upper(RANGO_CO_PREF) like", value.toUpperCase(), "rangoCoPref");
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