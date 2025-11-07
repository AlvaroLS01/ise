package com.comerzzia.bimbaylola.pos.persistence.impuestos.porcentajes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ByLPorcentajeImpuestoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ByLPorcentajeImpuestoExample() {
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

        public Criteria andIdGrupoImpuestosIsNull() {
            addCriterion("ID_GRUPO_IMPUESTOS is null");
            return (Criteria) this;
        }

        public Criteria andIdGrupoImpuestosIsNotNull() {
            addCriterion("ID_GRUPO_IMPUESTOS is not null");
            return (Criteria) this;
        }

        public Criteria andIdGrupoImpuestosEqualTo(Integer value) {
            addCriterion("ID_GRUPO_IMPUESTOS =", value, "idGrupoImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdGrupoImpuestosNotEqualTo(Integer value) {
            addCriterion("ID_GRUPO_IMPUESTOS <>", value, "idGrupoImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdGrupoImpuestosGreaterThan(Integer value) {
            addCriterion("ID_GRUPO_IMPUESTOS >", value, "idGrupoImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdGrupoImpuestosGreaterThanOrEqualTo(Integer value) {
            addCriterion("ID_GRUPO_IMPUESTOS >=", value, "idGrupoImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdGrupoImpuestosLessThan(Integer value) {
            addCriterion("ID_GRUPO_IMPUESTOS <", value, "idGrupoImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdGrupoImpuestosLessThanOrEqualTo(Integer value) {
            addCriterion("ID_GRUPO_IMPUESTOS <=", value, "idGrupoImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdGrupoImpuestosIn(List<Integer> values) {
            addCriterion("ID_GRUPO_IMPUESTOS in", values, "idGrupoImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdGrupoImpuestosNotIn(List<Integer> values) {
            addCriterion("ID_GRUPO_IMPUESTOS not in", values, "idGrupoImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdGrupoImpuestosBetween(Integer value1, Integer value2) {
            addCriterion("ID_GRUPO_IMPUESTOS between", value1, value2, "idGrupoImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdGrupoImpuestosNotBetween(Integer value1, Integer value2) {
            addCriterion("ID_GRUPO_IMPUESTOS not between", value1, value2, "idGrupoImpuestos");
            return (Criteria) this;
        }

        public Criteria andVigenciaDesdeIsNull() {
            addCriterion("VIGENCIA_DESDE is null");
            return (Criteria) this;
        }

        public Criteria andVigenciaDesdeIsNotNull() {
            addCriterion("VIGENCIA_DESDE is not null");
            return (Criteria) this;
        }

        public Criteria andVigenciaDesdeEqualTo(Date value) {
            addCriterion("VIGENCIA_DESDE =", value, "vigenciaDesde");
            return (Criteria) this;
        }

        public Criteria andVigenciaDesdeNotEqualTo(Date value) {
            addCriterion("VIGENCIA_DESDE <>", value, "vigenciaDesde");
            return (Criteria) this;
        }

        public Criteria andVigenciaDesdeGreaterThan(Date value) {
            addCriterion("VIGENCIA_DESDE >", value, "vigenciaDesde");
            return (Criteria) this;
        }

        public Criteria andVigenciaDesdeGreaterThanOrEqualTo(Date value) {
            addCriterion("VIGENCIA_DESDE >=", value, "vigenciaDesde");
            return (Criteria) this;
        }

        public Criteria andVigenciaDesdeLessThan(Date value) {
            addCriterion("VIGENCIA_DESDE <", value, "vigenciaDesde");
            return (Criteria) this;
        }

        public Criteria andVigenciaDesdeLessThanOrEqualTo(Date value) {
            addCriterion("VIGENCIA_DESDE <=", value, "vigenciaDesde");
            return (Criteria) this;
        }

        public Criteria andVigenciaDesdeIn(List<Date> values) {
            addCriterion("VIGENCIA_DESDE in", values, "vigenciaDesde");
            return (Criteria) this;
        }

        public Criteria andVigenciaDesdeNotIn(List<Date> values) {
            addCriterion("VIGENCIA_DESDE not in", values, "vigenciaDesde");
            return (Criteria) this;
        }

        public Criteria andVigenciaDesdeBetween(Date value1, Date value2) {
            addCriterion("VIGENCIA_DESDE between", value1, value2, "vigenciaDesde");
            return (Criteria) this;
        }

        public Criteria andVigenciaDesdeNotBetween(Date value1, Date value2) {
            addCriterion("VIGENCIA_DESDE not between", value1, value2, "vigenciaDesde");
            return (Criteria) this;
        }

        public Criteria andIdTratImpuestosIsNull() {
            addCriterion("ID_TRAT_IMPUESTOS is null");
            return (Criteria) this;
        }

        public Criteria andIdTratImpuestosIsNotNull() {
            addCriterion("ID_TRAT_IMPUESTOS is not null");
            return (Criteria) this;
        }

        public Criteria andIdTratImpuestosEqualTo(Long value) {
            addCriterion("ID_TRAT_IMPUESTOS =", value, "idTratImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdTratImpuestosNotEqualTo(Long value) {
            addCriterion("ID_TRAT_IMPUESTOS <>", value, "idTratImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdTratImpuestosGreaterThan(Long value) {
            addCriterion("ID_TRAT_IMPUESTOS >", value, "idTratImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdTratImpuestosGreaterThanOrEqualTo(Long value) {
            addCriterion("ID_TRAT_IMPUESTOS >=", value, "idTratImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdTratImpuestosLessThan(Long value) {
            addCriterion("ID_TRAT_IMPUESTOS <", value, "idTratImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdTratImpuestosLessThanOrEqualTo(Long value) {
            addCriterion("ID_TRAT_IMPUESTOS <=", value, "idTratImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdTratImpuestosIn(List<Long> values) {
            addCriterion("ID_TRAT_IMPUESTOS in", values, "idTratImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdTratImpuestosNotIn(List<Long> values) {
            addCriterion("ID_TRAT_IMPUESTOS not in", values, "idTratImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdTratImpuestosBetween(Long value1, Long value2) {
            addCriterion("ID_TRAT_IMPUESTOS between", value1, value2, "idTratImpuestos");
            return (Criteria) this;
        }

        public Criteria andIdTratImpuestosNotBetween(Long value1, Long value2) {
            addCriterion("ID_TRAT_IMPUESTOS not between", value1, value2, "idTratImpuestos");
            return (Criteria) this;
        }

        public Criteria andCodtratimpIsNull() {
            addCriterion("CODTRATIMP is null");
            return (Criteria) this;
        }

        public Criteria andCodtratimpIsNotNull() {
            addCriterion("CODTRATIMP is not null");
            return (Criteria) this;
        }

        public Criteria andCodtratimpEqualTo(String value) {
            addCriterion("CODTRATIMP =", value, "codtratimp");
            return (Criteria) this;
        }

        public Criteria andCodtratimpNotEqualTo(String value) {
            addCriterion("CODTRATIMP <>", value, "codtratimp");
            return (Criteria) this;
        }

        public Criteria andCodtratimpGreaterThan(String value) {
            addCriterion("CODTRATIMP >", value, "codtratimp");
            return (Criteria) this;
        }

        public Criteria andCodtratimpGreaterThanOrEqualTo(String value) {
            addCriterion("CODTRATIMP >=", value, "codtratimp");
            return (Criteria) this;
        }

        public Criteria andCodtratimpLessThan(String value) {
            addCriterion("CODTRATIMP <", value, "codtratimp");
            return (Criteria) this;
        }

        public Criteria andCodtratimpLessThanOrEqualTo(String value) {
            addCriterion("CODTRATIMP <=", value, "codtratimp");
            return (Criteria) this;
        }

        public Criteria andCodtratimpLike(String value) {
            addCriterion("CODTRATIMP like", value, "codtratimp");
            return (Criteria) this;
        }

        public Criteria andCodtratimpNotLike(String value) {
            addCriterion("CODTRATIMP not like", value, "codtratimp");
            return (Criteria) this;
        }

        public Criteria andCodtratimpIn(List<String> values) {
            addCriterion("CODTRATIMP in", values, "codtratimp");
            return (Criteria) this;
        }

        public Criteria andCodtratimpNotIn(List<String> values) {
            addCriterion("CODTRATIMP not in", values, "codtratimp");
            return (Criteria) this;
        }

        public Criteria andCodtratimpBetween(String value1, String value2) {
            addCriterion("CODTRATIMP between", value1, value2, "codtratimp");
            return (Criteria) this;
        }

        public Criteria andCodtratimpNotBetween(String value1, String value2) {
            addCriterion("CODTRATIMP not between", value1, value2, "codtratimp");
            return (Criteria) this;
        }

        public Criteria andDestratimpIsNull() {
            addCriterion("DESTRATIMP is null");
            return (Criteria) this;
        }

        public Criteria andDestratimpIsNotNull() {
            addCriterion("DESTRATIMP is not null");
            return (Criteria) this;
        }

        public Criteria andDestratimpEqualTo(String value) {
            addCriterion("DESTRATIMP =", value, "destratimp");
            return (Criteria) this;
        }

        public Criteria andDestratimpNotEqualTo(String value) {
            addCriterion("DESTRATIMP <>", value, "destratimp");
            return (Criteria) this;
        }

        public Criteria andDestratimpGreaterThan(String value) {
            addCriterion("DESTRATIMP >", value, "destratimp");
            return (Criteria) this;
        }

        public Criteria andDestratimpGreaterThanOrEqualTo(String value) {
            addCriterion("DESTRATIMP >=", value, "destratimp");
            return (Criteria) this;
        }

        public Criteria andDestratimpLessThan(String value) {
            addCriterion("DESTRATIMP <", value, "destratimp");
            return (Criteria) this;
        }

        public Criteria andDestratimpLessThanOrEqualTo(String value) {
            addCriterion("DESTRATIMP <=", value, "destratimp");
            return (Criteria) this;
        }

        public Criteria andDestratimpLike(String value) {
            addCriterion("DESTRATIMP like", value, "destratimp");
            return (Criteria) this;
        }

        public Criteria andDestratimpNotLike(String value) {
            addCriterion("DESTRATIMP not like", value, "destratimp");
            return (Criteria) this;
        }

        public Criteria andDestratimpIn(List<String> values) {
            addCriterion("DESTRATIMP in", values, "destratimp");
            return (Criteria) this;
        }

        public Criteria andDestratimpNotIn(List<String> values) {
            addCriterion("DESTRATIMP not in", values, "destratimp");
            return (Criteria) this;
        }

        public Criteria andDestratimpBetween(String value1, String value2) {
            addCriterion("DESTRATIMP between", value1, value2, "destratimp");
            return (Criteria) this;
        }

        public Criteria andDestratimpNotBetween(String value1, String value2) {
            addCriterion("DESTRATIMP not between", value1, value2, "destratimp");
            return (Criteria) this;
        }

        public Criteria andAplicaRecargoIsNull() {
            addCriterion("APLICA_RECARGO is null");
            return (Criteria) this;
        }

        public Criteria andAplicaRecargoIsNotNull() {
            addCriterion("APLICA_RECARGO is not null");
            return (Criteria) this;
        }

        public Criteria andAplicaRecargoEqualTo(String value) {
            addCriterion("APLICA_RECARGO =", value, "aplicaRecargo");
            return (Criteria) this;
        }

        public Criteria andAplicaRecargoNotEqualTo(String value) {
            addCriterion("APLICA_RECARGO <>", value, "aplicaRecargo");
            return (Criteria) this;
        }

        public Criteria andAplicaRecargoGreaterThan(String value) {
            addCriterion("APLICA_RECARGO >", value, "aplicaRecargo");
            return (Criteria) this;
        }

        public Criteria andAplicaRecargoGreaterThanOrEqualTo(String value) {
            addCriterion("APLICA_RECARGO >=", value, "aplicaRecargo");
            return (Criteria) this;
        }

        public Criteria andAplicaRecargoLessThan(String value) {
            addCriterion("APLICA_RECARGO <", value, "aplicaRecargo");
            return (Criteria) this;
        }

        public Criteria andAplicaRecargoLessThanOrEqualTo(String value) {
            addCriterion("APLICA_RECARGO <=", value, "aplicaRecargo");
            return (Criteria) this;
        }

        public Criteria andAplicaRecargoLike(String value) {
            addCriterion("APLICA_RECARGO like", value, "aplicaRecargo");
            return (Criteria) this;
        }

        public Criteria andAplicaRecargoNotLike(String value) {
            addCriterion("APLICA_RECARGO not like", value, "aplicaRecargo");
            return (Criteria) this;
        }

        public Criteria andAplicaRecargoIn(List<String> values) {
            addCriterion("APLICA_RECARGO in", values, "aplicaRecargo");
            return (Criteria) this;
        }

        public Criteria andAplicaRecargoNotIn(List<String> values) {
            addCriterion("APLICA_RECARGO not in", values, "aplicaRecargo");
            return (Criteria) this;
        }

        public Criteria andAplicaRecargoBetween(String value1, String value2) {
            addCriterion("APLICA_RECARGO between", value1, value2, "aplicaRecargo");
            return (Criteria) this;
        }

        public Criteria andAplicaRecargoNotBetween(String value1, String value2) {
            addCriterion("APLICA_RECARGO not between", value1, value2, "aplicaRecargo");
            return (Criteria) this;
        }

        public Criteria andCodimpIsNull() {
            addCriterion("CODIMP is null");
            return (Criteria) this;
        }

        public Criteria andCodimpIsNotNull() {
            addCriterion("CODIMP is not null");
            return (Criteria) this;
        }

        public Criteria andCodimpEqualTo(String value) {
            addCriterion("CODIMP =", value, "codimp");
            return (Criteria) this;
        }

        public Criteria andCodimpNotEqualTo(String value) {
            addCriterion("CODIMP <>", value, "codimp");
            return (Criteria) this;
        }

        public Criteria andCodimpGreaterThan(String value) {
            addCriterion("CODIMP >", value, "codimp");
            return (Criteria) this;
        }

        public Criteria andCodimpGreaterThanOrEqualTo(String value) {
            addCriterion("CODIMP >=", value, "codimp");
            return (Criteria) this;
        }

        public Criteria andCodimpLessThan(String value) {
            addCriterion("CODIMP <", value, "codimp");
            return (Criteria) this;
        }

        public Criteria andCodimpLessThanOrEqualTo(String value) {
            addCriterion("CODIMP <=", value, "codimp");
            return (Criteria) this;
        }

        public Criteria andCodimpLike(String value) {
            addCriterion("CODIMP like", value, "codimp");
            return (Criteria) this;
        }

        public Criteria andCodimpNotLike(String value) {
            addCriterion("CODIMP not like", value, "codimp");
            return (Criteria) this;
        }

        public Criteria andCodimpIn(List<String> values) {
            addCriterion("CODIMP in", values, "codimp");
            return (Criteria) this;
        }

        public Criteria andCodimpNotIn(List<String> values) {
            addCriterion("CODIMP not in", values, "codimp");
            return (Criteria) this;
        }

        public Criteria andCodimpBetween(String value1, String value2) {
            addCriterion("CODIMP between", value1, value2, "codimp");
            return (Criteria) this;
        }

        public Criteria andCodimpNotBetween(String value1, String value2) {
            addCriterion("CODIMP not between", value1, value2, "codimp");
            return (Criteria) this;
        }

        public Criteria andDesimpIsNull() {
            addCriterion("DESIMP is null");
            return (Criteria) this;
        }

        public Criteria andDesimpIsNotNull() {
            addCriterion("DESIMP is not null");
            return (Criteria) this;
        }

        public Criteria andDesimpEqualTo(String value) {
            addCriterion("DESIMP =", value, "desimp");
            return (Criteria) this;
        }

        public Criteria andDesimpNotEqualTo(String value) {
            addCriterion("DESIMP <>", value, "desimp");
            return (Criteria) this;
        }

        public Criteria andDesimpGreaterThan(String value) {
            addCriterion("DESIMP >", value, "desimp");
            return (Criteria) this;
        }

        public Criteria andDesimpGreaterThanOrEqualTo(String value) {
            addCriterion("DESIMP >=", value, "desimp");
            return (Criteria) this;
        }

        public Criteria andDesimpLessThan(String value) {
            addCriterion("DESIMP <", value, "desimp");
            return (Criteria) this;
        }

        public Criteria andDesimpLessThanOrEqualTo(String value) {
            addCriterion("DESIMP <=", value, "desimp");
            return (Criteria) this;
        }

        public Criteria andDesimpLike(String value) {
            addCriterion("DESIMP like", value, "desimp");
            return (Criteria) this;
        }

        public Criteria andDesimpNotLike(String value) {
            addCriterion("DESIMP not like", value, "desimp");
            return (Criteria) this;
        }

        public Criteria andDesimpIn(List<String> values) {
            addCriterion("DESIMP in", values, "desimp");
            return (Criteria) this;
        }

        public Criteria andDesimpNotIn(List<String> values) {
            addCriterion("DESIMP not in", values, "desimp");
            return (Criteria) this;
        }

        public Criteria andDesimpBetween(String value1, String value2) {
            addCriterion("DESIMP between", value1, value2, "desimp");
            return (Criteria) this;
        }

        public Criteria andDesimpNotBetween(String value1, String value2) {
            addCriterion("DESIMP not between", value1, value2, "desimp");
            return (Criteria) this;
        }

        public Criteria andPorcentajeIsNull() {
            addCriterion("PORCENTAJE is null");
            return (Criteria) this;
        }

        public Criteria andPorcentajeIsNotNull() {
            addCriterion("PORCENTAJE is not null");
            return (Criteria) this;
        }

        public Criteria andPorcentajeEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE =", value, "porcentaje");
            return (Criteria) this;
        }

        public Criteria andPorcentajeNotEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE <>", value, "porcentaje");
            return (Criteria) this;
        }

        public Criteria andPorcentajeGreaterThan(BigDecimal value) {
            addCriterion("PORCENTAJE >", value, "porcentaje");
            return (Criteria) this;
        }

        public Criteria andPorcentajeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE >=", value, "porcentaje");
            return (Criteria) this;
        }

        public Criteria andPorcentajeLessThan(BigDecimal value) {
            addCriterion("PORCENTAJE <", value, "porcentaje");
            return (Criteria) this;
        }

        public Criteria andPorcentajeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE <=", value, "porcentaje");
            return (Criteria) this;
        }

        public Criteria andPorcentajeIn(List<BigDecimal> values) {
            addCriterion("PORCENTAJE in", values, "porcentaje");
            return (Criteria) this;
        }

        public Criteria andPorcentajeNotIn(List<BigDecimal> values) {
            addCriterion("PORCENTAJE not in", values, "porcentaje");
            return (Criteria) this;
        }

        public Criteria andPorcentajeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PORCENTAJE between", value1, value2, "porcentaje");
            return (Criteria) this;
        }

        public Criteria andPorcentajeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PORCENTAJE not between", value1, value2, "porcentaje");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargoIsNull() {
            addCriterion("PORCENTAJE_RECARGO is null");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargoIsNotNull() {
            addCriterion("PORCENTAJE_RECARGO is not null");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargoEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO =", value, "porcentajeRecargo");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargoNotEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO <>", value, "porcentajeRecargo");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargoGreaterThan(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO >", value, "porcentajeRecargo");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargoGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO >=", value, "porcentajeRecargo");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargoLessThan(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO <", value, "porcentajeRecargo");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargoLessThanOrEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO <=", value, "porcentajeRecargo");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargoIn(List<BigDecimal> values) {
            addCriterion("PORCENTAJE_RECARGO in", values, "porcentajeRecargo");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargoNotIn(List<BigDecimal> values) {
            addCriterion("PORCENTAJE_RECARGO not in", values, "porcentajeRecargo");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargoBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PORCENTAJE_RECARGO between", value1, value2, "porcentajeRecargo");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargoNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PORCENTAJE_RECARGO not between", value1, value2, "porcentajeRecargo");
            return (Criteria) this;
        }

        public Criteria andCodimpFiscalIsNull() {
            addCriterion("CODIMP_FISCAL is null");
            return (Criteria) this;
        }

        public Criteria andCodimpFiscalIsNotNull() {
            addCriterion("CODIMP_FISCAL is not null");
            return (Criteria) this;
        }

        public Criteria andCodimpFiscalEqualTo(String value) {
            addCriterion("CODIMP_FISCAL =", value, "codimpFiscal");
            return (Criteria) this;
        }

        public Criteria andCodimpFiscalNotEqualTo(String value) {
            addCriterion("CODIMP_FISCAL <>", value, "codimpFiscal");
            return (Criteria) this;
        }

        public Criteria andCodimpFiscalGreaterThan(String value) {
            addCriterion("CODIMP_FISCAL >", value, "codimpFiscal");
            return (Criteria) this;
        }

        public Criteria andCodimpFiscalGreaterThanOrEqualTo(String value) {
            addCriterion("CODIMP_FISCAL >=", value, "codimpFiscal");
            return (Criteria) this;
        }

        public Criteria andCodimpFiscalLessThan(String value) {
            addCriterion("CODIMP_FISCAL <", value, "codimpFiscal");
            return (Criteria) this;
        }

        public Criteria andCodimpFiscalLessThanOrEqualTo(String value) {
            addCriterion("CODIMP_FISCAL <=", value, "codimpFiscal");
            return (Criteria) this;
        }

        public Criteria andCodimpFiscalLike(String value) {
            addCriterion("CODIMP_FISCAL like", value, "codimpFiscal");
            return (Criteria) this;
        }

        public Criteria andCodimpFiscalNotLike(String value) {
            addCriterion("CODIMP_FISCAL not like", value, "codimpFiscal");
            return (Criteria) this;
        }

        public Criteria andCodimpFiscalIn(List<String> values) {
            addCriterion("CODIMP_FISCAL in", values, "codimpFiscal");
            return (Criteria) this;
        }

        public Criteria andCodimpFiscalNotIn(List<String> values) {
            addCriterion("CODIMP_FISCAL not in", values, "codimpFiscal");
            return (Criteria) this;
        }

        public Criteria andCodimpFiscalBetween(String value1, String value2) {
            addCriterion("CODIMP_FISCAL between", value1, value2, "codimpFiscal");
            return (Criteria) this;
        }

        public Criteria andCodimpFiscalNotBetween(String value1, String value2) {
            addCriterion("CODIMP_FISCAL not between", value1, value2, "codimpFiscal");
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

        public Criteria andPorcentajeRecargo2IsNull() {
            addCriterion("PORCENTAJE_RECARGO2 is null");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo2IsNotNull() {
            addCriterion("PORCENTAJE_RECARGO2 is not null");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo2EqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO2 =", value, "porcentajeRecargo2");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo2NotEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO2 <>", value, "porcentajeRecargo2");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo2GreaterThan(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO2 >", value, "porcentajeRecargo2");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo2GreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO2 >=", value, "porcentajeRecargo2");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo2LessThan(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO2 <", value, "porcentajeRecargo2");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo2LessThanOrEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO2 <=", value, "porcentajeRecargo2");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo2In(List<BigDecimal> values) {
            addCriterion("PORCENTAJE_RECARGO2 in", values, "porcentajeRecargo2");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo2NotIn(List<BigDecimal> values) {
            addCriterion("PORCENTAJE_RECARGO2 not in", values, "porcentajeRecargo2");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo2Between(BigDecimal value1, BigDecimal value2) {
            addCriterion("PORCENTAJE_RECARGO2 between", value1, value2, "porcentajeRecargo2");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo2NotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PORCENTAJE_RECARGO2 not between", value1, value2, "porcentajeRecargo2");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo3IsNull() {
            addCriterion("PORCENTAJE_RECARGO3 is null");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo3IsNotNull() {
            addCriterion("PORCENTAJE_RECARGO3 is not null");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo3EqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO3 =", value, "porcentajeRecargo3");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo3NotEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO3 <>", value, "porcentajeRecargo3");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo3GreaterThan(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO3 >", value, "porcentajeRecargo3");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo3GreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO3 >=", value, "porcentajeRecargo3");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo3LessThan(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO3 <", value, "porcentajeRecargo3");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo3LessThanOrEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO3 <=", value, "porcentajeRecargo3");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo3In(List<BigDecimal> values) {
            addCriterion("PORCENTAJE_RECARGO3 in", values, "porcentajeRecargo3");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo3NotIn(List<BigDecimal> values) {
            addCriterion("PORCENTAJE_RECARGO3 not in", values, "porcentajeRecargo3");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo3Between(BigDecimal value1, BigDecimal value2) {
            addCriterion("PORCENTAJE_RECARGO3 between", value1, value2, "porcentajeRecargo3");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo3NotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PORCENTAJE_RECARGO3 not between", value1, value2, "porcentajeRecargo3");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo4IsNull() {
            addCriterion("PORCENTAJE_RECARGO4 is null");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo4IsNotNull() {
            addCriterion("PORCENTAJE_RECARGO4 is not null");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo4EqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO4 =", value, "porcentajeRecargo4");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo4NotEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO4 <>", value, "porcentajeRecargo4");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo4GreaterThan(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO4 >", value, "porcentajeRecargo4");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo4GreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO4 >=", value, "porcentajeRecargo4");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo4LessThan(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO4 <", value, "porcentajeRecargo4");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo4LessThanOrEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO4 <=", value, "porcentajeRecargo4");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo4In(List<BigDecimal> values) {
            addCriterion("PORCENTAJE_RECARGO4 in", values, "porcentajeRecargo4");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo4NotIn(List<BigDecimal> values) {
            addCriterion("PORCENTAJE_RECARGO4 not in", values, "porcentajeRecargo4");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo4Between(BigDecimal value1, BigDecimal value2) {
            addCriterion("PORCENTAJE_RECARGO4 between", value1, value2, "porcentajeRecargo4");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo4NotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PORCENTAJE_RECARGO4 not between", value1, value2, "porcentajeRecargo4");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo5IsNull() {
            addCriterion("PORCENTAJE_RECARGO5 is null");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo5IsNotNull() {
            addCriterion("PORCENTAJE_RECARGO5 is not null");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo5EqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO5 =", value, "porcentajeRecargo5");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo5NotEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO5 <>", value, "porcentajeRecargo5");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo5GreaterThan(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO5 >", value, "porcentajeRecargo5");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo5GreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO5 >=", value, "porcentajeRecargo5");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo5LessThan(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO5 <", value, "porcentajeRecargo5");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo5LessThanOrEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_RECARGO5 <=", value, "porcentajeRecargo5");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo5In(List<BigDecimal> values) {
            addCriterion("PORCENTAJE_RECARGO5 in", values, "porcentajeRecargo5");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo5NotIn(List<BigDecimal> values) {
            addCriterion("PORCENTAJE_RECARGO5 not in", values, "porcentajeRecargo5");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo5Between(BigDecimal value1, BigDecimal value2) {
            addCriterion("PORCENTAJE_RECARGO5 between", value1, value2, "porcentajeRecargo5");
            return (Criteria) this;
        }

        public Criteria andPorcentajeRecargo5NotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PORCENTAJE_RECARGO5 not between", value1, value2, "porcentajeRecargo5");
            return (Criteria) this;
        }

        public Criteria andUidActividadLikeInsensitive(String value) {
            addCriterion("upper(UID_ACTIVIDAD) like", value.toUpperCase(), "uidActividad");
            return (Criteria) this;
        }

        public Criteria andCodtratimpLikeInsensitive(String value) {
            addCriterion("upper(CODTRATIMP) like", value.toUpperCase(), "codtratimp");
            return (Criteria) this;
        }

        public Criteria andDestratimpLikeInsensitive(String value) {
            addCriterion("upper(DESTRATIMP) like", value.toUpperCase(), "destratimp");
            return (Criteria) this;
        }

        public Criteria andAplicaRecargoLikeInsensitive(String value) {
            addCriterion("upper(APLICA_RECARGO) like", value.toUpperCase(), "aplicaRecargo");
            return (Criteria) this;
        }

        public Criteria andCodimpLikeInsensitive(String value) {
            addCriterion("upper(CODIMP) like", value.toUpperCase(), "codimp");
            return (Criteria) this;
        }

        public Criteria andDesimpLikeInsensitive(String value) {
            addCriterion("upper(DESIMP) like", value.toUpperCase(), "desimp");
            return (Criteria) this;
        }

        public Criteria andCodimpFiscalLikeInsensitive(String value) {
            addCriterion("upper(CODIMP_FISCAL) like", value.toUpperCase(), "codimpFiscal");
            return (Criteria) this;
        }

        public Criteria andCodpaisLikeInsensitive(String value) {
            addCriterion("upper(CODPAIS) like", value.toUpperCase(), "codpais");
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