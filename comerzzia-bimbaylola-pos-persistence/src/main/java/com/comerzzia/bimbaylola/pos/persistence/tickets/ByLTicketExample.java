package com.comerzzia.bimbaylola.pos.persistence.tickets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ByLTicketExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ByLTicketExample() {
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

        public Criteria customAndUidActividadEqualTo(String value) {
            addCriterion("tick.uid_actividad =", value, "uidActividad");
            return (Criteria) this;
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

        public Criteria andUidTicketIsNull() {
            addCriterion("uid_ticket is null");
            return (Criteria) this;
        }

        public Criteria andUidTicketIsNotNull() {
            addCriterion("uid_ticket is not null");
            return (Criteria) this;
        }

        public Criteria andUidTicketEqualTo(String value) {
            addCriterion("uid_ticket =", value, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotEqualTo(String value) {
            addCriterion("uid_ticket <>", value, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketGreaterThan(String value) {
            addCriterion("uid_ticket >", value, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketGreaterThanOrEqualTo(String value) {
            addCriterion("uid_ticket >=", value, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketLessThan(String value) {
            addCriterion("uid_ticket <", value, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketLessThanOrEqualTo(String value) {
            addCriterion("uid_ticket <=", value, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketLike(String value) {
            addCriterion("uid_ticket like", value, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotLike(String value) {
            addCriterion("uid_ticket not like", value, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketIn(List<String> values) {
            addCriterion("uid_ticket in", values, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotIn(List<String> values) {
            addCriterion("uid_ticket not in", values, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketBetween(String value1, String value2) {
            addCriterion("uid_ticket between", value1, value2, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotBetween(String value1, String value2) {
            addCriterion("uid_ticket not between", value1, value2, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andCodalmIsNull() {
            addCriterion("codalm is null");
            return (Criteria) this;
        }

        public Criteria andCodalmIsNotNull() {
            addCriterion("codalm is not null");
            return (Criteria) this;
        }

        public Criteria andCodalmEqualTo(String value) {
            addCriterion("codalm =", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmNotEqualTo(String value) {
            addCriterion("codalm <>", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmGreaterThan(String value) {
            addCriterion("codalm >", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmGreaterThanOrEqualTo(String value) {
            addCriterion("codalm >=", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmLessThan(String value) {
            addCriterion("codalm <", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmLessThanOrEqualTo(String value) {
            addCriterion("codalm <=", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmLike(String value) {
            addCriterion("codalm like", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmNotLike(String value) {
            addCriterion("codalm not like", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmIn(List<String> values) {
            addCriterion("codalm in", values, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmNotIn(List<String> values) {
            addCriterion("codalm not in", values, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmBetween(String value1, String value2) {
            addCriterion("codalm between", value1, value2, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmNotBetween(String value1, String value2) {
            addCriterion("codalm not between", value1, value2, "codalm");
            return (Criteria) this;
        }

        public Criteria andIdTicketIsNull() {
            addCriterion("id_ticket is null");
            return (Criteria) this;
        }

        public Criteria andIdTicketIsNotNull() {
            addCriterion("id_ticket is not null");
            return (Criteria) this;
        }

        public Criteria andIdTicketEqualTo(Long value) {
            addCriterion("id_ticket =", value, "idTicket");
            return (Criteria) this;
        }
        public Criteria customAndIdTicketEqualTo(Long value) {
            addCriterion("tick.id_ticket =", value, "idTicket");
            return (Criteria) this;
        }

        public Criteria andIdTicketNotEqualTo(Long value) {
            addCriterion("id_ticket <>", value, "idTicket");
            return (Criteria) this;
        }

        public Criteria andIdTicketGreaterThan(Long value) {
            addCriterion("id_ticket >", value, "idTicket");
            return (Criteria) this;
        }

        public Criteria andIdTicketGreaterThanOrEqualTo(Long value) {
            addCriterion("id_ticket >=", value, "idTicket");
            return (Criteria) this;
        }

        public Criteria andIdTicketLessThan(Long value) {
            addCriterion("id_ticket <", value, "idTicket");
            return (Criteria) this;
        }

        public Criteria andIdTicketLessThanOrEqualTo(Long value) {
            addCriterion("id_ticket <=", value, "idTicket");
            return (Criteria) this;
        }

        public Criteria andIdTicketIn(List<Long> values) {
            addCriterion("id_ticket in", values, "idTicket");
            return (Criteria) this;
        }

        public Criteria andIdTicketNotIn(List<Long> values) {
            addCriterion("id_ticket not in", values, "idTicket");
            return (Criteria) this;
        }

        public Criteria andIdTicketBetween(Long value1, Long value2) {
            addCriterion("id_ticket between", value1, value2, "idTicket");
            return (Criteria) this;
        }

        public Criteria andIdTicketNotBetween(Long value1, Long value2) {
            addCriterion("id_ticket not between", value1, value2, "idTicket");
            return (Criteria) this;
        }

        public Criteria andFechaIsNull() {
            addCriterion("fecha is null");
            return (Criteria) this;
        }

        public Criteria andFechaIsNotNull() {
            addCriterion("fecha is not null");
            return (Criteria) this;
        }

        public Criteria andFechaEqualTo(Date value) {
            addCriterion("fecha =", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaNotEqualTo(Date value) {
            addCriterion("fecha <>", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaGreaterThan(Date value) {
            addCriterion("fecha >", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaGreaterThanOrEqualTo(Date value) {
            addCriterion("fecha >=", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaLessThan(Date value) {
            addCriterion("fecha <", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaLessThanOrEqualTo(Date value) {
            addCriterion("fecha <=", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaIn(List<Date> values) {
            addCriterion("fecha in", values, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaNotIn(List<Date> values) {
            addCriterion("fecha not in", values, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaBetween(Date value1, Date value2) {
            addCriterion("fecha between", value1, value2, "fecha");
            return (Criteria) this;
        }
        
        public Criteria customAndFechaBetween(Date value1, Date value2) {
            addCriterion("tick.fecha between", value1, value2, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaNotBetween(Date value1, Date value2) {
            addCriterion("fecha not between", value1, value2, "fecha");
            return (Criteria) this;
        }

        public Criteria andProcesadoIsNull() {
            addCriterion("procesado is null");
            return (Criteria) this;
        }

        public Criteria andProcesadoIsNotNull() {
            addCriterion("procesado is not null");
            return (Criteria) this;
        }

        public Criteria andProcesadoEqualTo(String value) {
            addCriterion("procesado =", value, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoNotEqualTo(String value) {
            addCriterion("procesado <>", value, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoGreaterThan(String value) {
            addCriterion("procesado >", value, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoGreaterThanOrEqualTo(String value) {
            addCriterion("procesado >=", value, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoLessThan(String value) {
            addCriterion("procesado <", value, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoLessThanOrEqualTo(String value) {
            addCriterion("procesado <=", value, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoLike(String value) {
            addCriterion("procesado like", value, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoNotLike(String value) {
            addCriterion("procesado not like", value, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoIn(List<String> values) {
            addCriterion("procesado in", values, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoNotIn(List<String> values) {
            addCriterion("procesado not in", values, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoBetween(String value1, String value2) {
            addCriterion("procesado between", value1, value2, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoNotBetween(String value1, String value2) {
            addCriterion("procesado not between", value1, value2, "procesado");
            return (Criteria) this;
        }

        public Criteria andFechaProcesoIsNull() {
            addCriterion("fecha_proceso is null");
            return (Criteria) this;
        }

        public Criteria andFechaProcesoIsNotNull() {
            addCriterion("fecha_proceso is not null");
            return (Criteria) this;
        }

        public Criteria andFechaProcesoEqualTo(Date value) {
            addCriterion("fecha_proceso =", value, "fechaProceso");
            return (Criteria) this;
        }

        public Criteria andFechaProcesoNotEqualTo(Date value) {
            addCriterion("fecha_proceso <>", value, "fechaProceso");
            return (Criteria) this;
        }

        public Criteria andFechaProcesoGreaterThan(Date value) {
            addCriterion("fecha_proceso >", value, "fechaProceso");
            return (Criteria) this;
        }

        public Criteria andFechaProcesoGreaterThanOrEqualTo(Date value) {
            addCriterion("fecha_proceso >=", value, "fechaProceso");
            return (Criteria) this;
        }

        public Criteria andFechaProcesoLessThan(Date value) {
            addCriterion("fecha_proceso <", value, "fechaProceso");
            return (Criteria) this;
        }

        public Criteria andFechaProcesoLessThanOrEqualTo(Date value) {
            addCriterion("fecha_proceso <=", value, "fechaProceso");
            return (Criteria) this;
        }

        public Criteria andFechaProcesoIn(List<Date> values) {
            addCriterion("fecha_proceso in", values, "fechaProceso");
            return (Criteria) this;
        }

        public Criteria andFechaProcesoNotIn(List<Date> values) {
            addCriterion("fecha_proceso not in", values, "fechaProceso");
            return (Criteria) this;
        }

        public Criteria andFechaProcesoBetween(Date value1, Date value2) {
            addCriterion("fecha_proceso between", value1, value2, "fechaProceso");
            return (Criteria) this;
        }

        public Criteria andFechaProcesoNotBetween(Date value1, Date value2) {
            addCriterion("fecha_proceso not between", value1, value2, "fechaProceso");
            return (Criteria) this;
        }

        public Criteria andMensajeProcesoIsNull() {
            addCriterion("mensaje_proceso is null");
            return (Criteria) this;
        }

        public Criteria andMensajeProcesoIsNotNull() {
            addCriterion("mensaje_proceso is not null");
            return (Criteria) this;
        }

        public Criteria andMensajeProcesoEqualTo(String value) {
            addCriterion("mensaje_proceso =", value, "mensajeProceso");
            return (Criteria) this;
        }

        public Criteria andMensajeProcesoNotEqualTo(String value) {
            addCriterion("mensaje_proceso <>", value, "mensajeProceso");
            return (Criteria) this;
        }

        public Criteria andMensajeProcesoGreaterThan(String value) {
            addCriterion("mensaje_proceso >", value, "mensajeProceso");
            return (Criteria) this;
        }

        public Criteria andMensajeProcesoGreaterThanOrEqualTo(String value) {
            addCriterion("mensaje_proceso >=", value, "mensajeProceso");
            return (Criteria) this;
        }

        public Criteria andMensajeProcesoLessThan(String value) {
            addCriterion("mensaje_proceso <", value, "mensajeProceso");
            return (Criteria) this;
        }

        public Criteria andMensajeProcesoLessThanOrEqualTo(String value) {
            addCriterion("mensaje_proceso <=", value, "mensajeProceso");
            return (Criteria) this;
        }

        public Criteria andMensajeProcesoLike(String value) {
            addCriterion("mensaje_proceso like", value, "mensajeProceso");
            return (Criteria) this;
        }

        public Criteria andMensajeProcesoNotLike(String value) {
            addCriterion("mensaje_proceso not like", value, "mensajeProceso");
            return (Criteria) this;
        }

        public Criteria andMensajeProcesoIn(List<String> values) {
            addCriterion("mensaje_proceso in", values, "mensajeProceso");
            return (Criteria) this;
        }

        public Criteria andMensajeProcesoNotIn(List<String> values) {
            addCriterion("mensaje_proceso not in", values, "mensajeProceso");
            return (Criteria) this;
        }

        public Criteria andMensajeProcesoBetween(String value1, String value2) {
            addCriterion("mensaje_proceso between", value1, value2, "mensajeProceso");
            return (Criteria) this;
        }

        public Criteria andMensajeProcesoNotBetween(String value1, String value2) {
            addCriterion("mensaje_proceso not between", value1, value2, "mensajeProceso");
            return (Criteria) this;
        }

        public Criteria andCodcajaIsNull() {
            addCriterion("codcaja is null");
            return (Criteria) this;
        }

        public Criteria andCodcajaIsNotNull() {
            addCriterion("codcaja is not null");
            return (Criteria) this;
        }

        public Criteria andCodcajaEqualTo(String value) {
            addCriterion("codcaja =", value, "codcaja");
            return (Criteria) this;
        }
        
        public Criteria customAndCodcajaEqualTo(String value) {
            addCriterion("tick.codcaja =", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaNotEqualTo(String value) {
            addCriterion("codcaja <>", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaGreaterThan(String value) {
            addCriterion("codcaja >", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaGreaterThanOrEqualTo(String value) {
            addCriterion("codcaja >=", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaLessThan(String value) {
            addCriterion("codcaja <", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaLessThanOrEqualTo(String value) {
            addCriterion("codcaja <=", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaLike(String value) {
            addCriterion("codcaja like", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaNotLike(String value) {
            addCriterion("codcaja not like", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaIn(List<String> values) {
            addCriterion("codcaja in", values, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaNotIn(List<String> values) {
            addCriterion("codcaja not in", values, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaBetween(String value1, String value2) {
            addCriterion("codcaja between", value1, value2, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaNotBetween(String value1, String value2) {
            addCriterion("codcaja not between", value1, value2, "codcaja");
            return (Criteria) this;
        }

        public Criteria andIdTipoDocumentoIsNull() {
            addCriterion("id_tipo_documento is null");
            return (Criteria) this;
        }

        public Criteria andIdTipoDocumentoIsNotNull() {
            addCriterion("id_tipo_documento is not null");
            return (Criteria) this;
        }

        public Criteria andIdTipoDocumentoEqualTo(Long value) {
            addCriterion("id_tipo_documento =", value, "idTipoDocumento");
            return (Criteria) this;
        }
        public Criteria customAndIdTipoDocumentoEqualTo(Long value) {
            addCriterion("tick.id_tipo_documento =", value, "idTipoDocumento");
            return (Criteria) this;
        }

        public Criteria andIdTipoDocumentoNotEqualTo(Long value) {
            addCriterion("id_tipo_documento <>", value, "idTipoDocumento");
            return (Criteria) this;
        }

        public Criteria andIdTipoDocumentoGreaterThan(Long value) {
            addCriterion("id_tipo_documento >", value, "idTipoDocumento");
            return (Criteria) this;
        }

        public Criteria andIdTipoDocumentoGreaterThanOrEqualTo(Long value) {
            addCriterion("id_tipo_documento >=", value, "idTipoDocumento");
            return (Criteria) this;
        }

        public Criteria andIdTipoDocumentoLessThan(Long value) {
            addCriterion("id_tipo_documento <", value, "idTipoDocumento");
            return (Criteria) this;
        }

        public Criteria andIdTipoDocumentoLessThanOrEqualTo(Long value) {
            addCriterion("id_tipo_documento <=", value, "idTipoDocumento");
            return (Criteria) this;
        }

        public Criteria andIdTipoDocumentoIn(List<Long> values) {
            addCriterion("id_tipo_documento in", values, "idTipoDocumento");
            return (Criteria) this;
        }
        
        public Criteria customAndIdTipoDocumentoIn(List<Long> values) {
            addCriterion("tick.id_tipo_documento in", values, "idTipoDocumento");
            return (Criteria) this;
        }

        public Criteria andIdTipoDocumentoNotIn(List<Long> values) {
            addCriterion("id_tipo_documento not in", values, "idTipoDocumento");
            return (Criteria) this;
        }

        public Criteria andIdTipoDocumentoBetween(Long value1, Long value2) {
            addCriterion("id_tipo_documento between", value1, value2, "idTipoDocumento");
            return (Criteria) this;
        }

        public Criteria andIdTipoDocumentoNotBetween(Long value1, Long value2) {
            addCriterion("id_tipo_documento not between", value1, value2, "idTipoDocumento");
            return (Criteria) this;
        }

        public Criteria andCodTicketIsNull() {
            addCriterion("cod_ticket is null");
            return (Criteria) this;
        }

        public Criteria andCodTicketIsNotNull() {
            addCriterion("cod_ticket is not null");
            return (Criteria) this;
        }

        public Criteria andCodTicketEqualTo(String value) {
            addCriterion("cod_ticket =", value, "codTicket");
            return (Criteria) this;
        }

        public Criteria andCodTicketNotEqualTo(String value) {
            addCriterion("cod_ticket <>", value, "codTicket");
            return (Criteria) this;
        }

        public Criteria andCodTicketGreaterThan(String value) {
            addCriterion("cod_ticket >", value, "codTicket");
            return (Criteria) this;
        }

        public Criteria andCodTicketGreaterThanOrEqualTo(String value) {
            addCriterion("cod_ticket >=", value, "codTicket");
            return (Criteria) this;
        }

        public Criteria andCodTicketLessThan(String value) {
            addCriterion("cod_ticket <", value, "codTicket");
            return (Criteria) this;
        }

        public Criteria andCodTicketLessThanOrEqualTo(String value) {
            addCriterion("cod_ticket <=", value, "codTicket");
            return (Criteria) this;
        }

        public Criteria andCodTicketLike(String value) {
            addCriterion("cod_ticket like", value, "codTicket");
            return (Criteria) this;
        }

        public Criteria andCodTicketNotLike(String value) {
            addCriterion("cod_ticket not like", value, "codTicket");
            return (Criteria) this;
        }

        public Criteria andCodTicketIn(List<String> values) {
            addCriterion("cod_ticket in", values, "codTicket");
            return (Criteria) this;
        }

        public Criteria andCodTicketNotIn(List<String> values) {
            addCriterion("cod_ticket not in", values, "codTicket");
            return (Criteria) this;
        }

        public Criteria andCodTicketBetween(String value1, String value2) {
            addCriterion("cod_ticket between", value1, value2, "codTicket");
            return (Criteria) this;
        }

        public Criteria andCodTicketNotBetween(String value1, String value2) {
            addCriterion("cod_ticket not between", value1, value2, "codTicket");
            return (Criteria) this;
        }

        public Criteria andFirmaIsNull() {
            addCriterion("firma is null");
            return (Criteria) this;
        }

        public Criteria andFirmaIsNotNull() {
            addCriterion("firma is not null");
            return (Criteria) this;
        }

        public Criteria andFirmaEqualTo(String value) {
            addCriterion("firma =", value, "firma");
            return (Criteria) this;
        }

        public Criteria andFirmaNotEqualTo(String value) {
            addCriterion("firma <>", value, "firma");
            return (Criteria) this;
        }

        public Criteria andFirmaGreaterThan(String value) {
            addCriterion("firma >", value, "firma");
            return (Criteria) this;
        }

        public Criteria andFirmaGreaterThanOrEqualTo(String value) {
            addCriterion("firma >=", value, "firma");
            return (Criteria) this;
        }

        public Criteria andFirmaLessThan(String value) {
            addCriterion("firma <", value, "firma");
            return (Criteria) this;
        }

        public Criteria andFirmaLessThanOrEqualTo(String value) {
            addCriterion("firma <=", value, "firma");
            return (Criteria) this;
        }

        public Criteria andFirmaLike(String value) {
            addCriterion("firma like", value, "firma");
            return (Criteria) this;
        }

        public Criteria andFirmaNotLike(String value) {
            addCriterion("firma not like", value, "firma");
            return (Criteria) this;
        }

        public Criteria andFirmaIn(List<String> values) {
            addCriterion("firma in", values, "firma");
            return (Criteria) this;
        }

        public Criteria andFirmaNotIn(List<String> values) {
            addCriterion("firma not in", values, "firma");
            return (Criteria) this;
        }

        public Criteria andFirmaBetween(String value1, String value2) {
            addCriterion("firma between", value1, value2, "firma");
            return (Criteria) this;
        }

        public Criteria andFirmaNotBetween(String value1, String value2) {
            addCriterion("firma not between", value1, value2, "firma");
            return (Criteria) this;
        }

        public Criteria andSerieTicketIsNull() {
            addCriterion("serie_ticket is null");
            return (Criteria) this;
        }

        public Criteria andSerieTicketIsNotNull() {
            addCriterion("serie_ticket is not null");
            return (Criteria) this;
        }

        public Criteria andSerieTicketEqualTo(String value) {
            addCriterion("serie_ticket =", value, "serieTicket");
            return (Criteria) this;
        }

        public Criteria andSerieTicketNotEqualTo(String value) {
            addCriterion("serie_ticket <>", value, "serieTicket");
            return (Criteria) this;
        }

        public Criteria andSerieTicketGreaterThan(String value) {
            addCriterion("serie_ticket >", value, "serieTicket");
            return (Criteria) this;
        }

        public Criteria andSerieTicketGreaterThanOrEqualTo(String value) {
            addCriterion("serie_ticket >=", value, "serieTicket");
            return (Criteria) this;
        }

        public Criteria andSerieTicketLessThan(String value) {
            addCriterion("serie_ticket <", value, "serieTicket");
            return (Criteria) this;
        }

        public Criteria andSerieTicketLessThanOrEqualTo(String value) {
            addCriterion("serie_ticket <=", value, "serieTicket");
            return (Criteria) this;
        }

        public Criteria andSerieTicketLike(String value) {
            addCriterion("serie_ticket like", value, "serieTicket");
            return (Criteria) this;
        }

        public Criteria andSerieTicketNotLike(String value) {
            addCriterion("serie_ticket not like", value, "serieTicket");
            return (Criteria) this;
        }

        public Criteria andSerieTicketIn(List<String> values) {
            addCriterion("serie_ticket in", values, "serieTicket");
            return (Criteria) this;
        }

        public Criteria andSerieTicketNotIn(List<String> values) {
            addCriterion("serie_ticket not in", values, "serieTicket");
            return (Criteria) this;
        }

        public Criteria andSerieTicketBetween(String value1, String value2) {
            addCriterion("serie_ticket between", value1, value2, "serieTicket");
            return (Criteria) this;
        }

        public Criteria andSerieTicketNotBetween(String value1, String value2) {
            addCriterion("serie_ticket not between", value1, value2, "serieTicket");
            return (Criteria) this;
        }

        public Criteria andIdAccionEstadosIsNull() {
            addCriterion("id_accion_estados is null");
            return (Criteria) this;
        }

        public Criteria andIdAccionEstadosIsNotNull() {
            addCriterion("id_accion_estados is not null");
            return (Criteria) this;
        }

        public Criteria andIdAccionEstadosEqualTo(Long value) {
            addCriterion("id_accion_estados =", value, "idAccionEstados");
            return (Criteria) this;
        }

        public Criteria andIdAccionEstadosNotEqualTo(Long value) {
            addCriterion("id_accion_estados <>", value, "idAccionEstados");
            return (Criteria) this;
        }

        public Criteria andIdAccionEstadosGreaterThan(Long value) {
            addCriterion("id_accion_estados >", value, "idAccionEstados");
            return (Criteria) this;
        }

        public Criteria andIdAccionEstadosGreaterThanOrEqualTo(Long value) {
            addCriterion("id_accion_estados >=", value, "idAccionEstados");
            return (Criteria) this;
        }

        public Criteria andIdAccionEstadosLessThan(Long value) {
            addCriterion("id_accion_estados <", value, "idAccionEstados");
            return (Criteria) this;
        }

        public Criteria andIdAccionEstadosLessThanOrEqualTo(Long value) {
            addCriterion("id_accion_estados <=", value, "idAccionEstados");
            return (Criteria) this;
        }

        public Criteria andIdAccionEstadosIn(List<Long> values) {
            addCriterion("id_accion_estados in", values, "idAccionEstados");
            return (Criteria) this;
        }

        public Criteria andIdAccionEstadosNotIn(List<Long> values) {
            addCriterion("id_accion_estados not in", values, "idAccionEstados");
            return (Criteria) this;
        }

        public Criteria andIdAccionEstadosBetween(Long value1, Long value2) {
            addCriterion("id_accion_estados between", value1, value2, "idAccionEstados");
            return (Criteria) this;
        }

        public Criteria andIdAccionEstadosNotBetween(Long value1, Long value2) {
            addCriterion("id_accion_estados not between", value1, value2, "idAccionEstados");
            return (Criteria) this;
        }

        public Criteria andIdEstadoIsNull() {
            addCriterion("id_estado is null");
            return (Criteria) this;
        }

        public Criteria andIdEstadoIsNotNull() {
            addCriterion("id_estado is not null");
            return (Criteria) this;
        }

        public Criteria andIdEstadoEqualTo(Integer value) {
            addCriterion("id_estado =", value, "idEstado");
            return (Criteria) this;
        }

        public Criteria andIdEstadoNotEqualTo(Integer value) {
            addCriterion("id_estado <>", value, "idEstado");
            return (Criteria) this;
        }

        public Criteria andIdEstadoGreaterThan(Integer value) {
            addCriterion("id_estado >", value, "idEstado");
            return (Criteria) this;
        }

        public Criteria andIdEstadoGreaterThanOrEqualTo(Integer value) {
            addCriterion("id_estado >=", value, "idEstado");
            return (Criteria) this;
        }

        public Criteria andIdEstadoLessThan(Integer value) {
            addCriterion("id_estado <", value, "idEstado");
            return (Criteria) this;
        }

        public Criteria andIdEstadoLessThanOrEqualTo(Integer value) {
            addCriterion("id_estado <=", value, "idEstado");
            return (Criteria) this;
        }

        public Criteria andIdEstadoIn(List<Integer> values) {
            addCriterion("id_estado in", values, "idEstado");
            return (Criteria) this;
        }

        public Criteria andIdEstadoNotIn(List<Integer> values) {
            addCriterion("id_estado not in", values, "idEstado");
            return (Criteria) this;
        }

        public Criteria andIdEstadoBetween(Integer value1, Integer value2) {
            addCriterion("id_estado between", value1, value2, "idEstado");
            return (Criteria) this;
        }

        public Criteria andIdEstadoNotBetween(Integer value1, Integer value2) {
            addCriterion("id_estado not between", value1, value2, "idEstado");
            return (Criteria) this;
        }

        public Criteria andUidActividadLikeInsensitive(String value) {
            addCriterion("upper(uid_actividad) like", value.toUpperCase(), "uidActividad");
            return (Criteria) this;
        }

        public Criteria andUidTicketLikeInsensitive(String value) {
            addCriterion("upper(uid_ticket) like", value.toUpperCase(), "uidTicket");
            return (Criteria) this;
        }

        public Criteria andCodalmLikeInsensitive(String value) {
            addCriterion("upper(codalm) like", value.toUpperCase(), "codalm");
            return (Criteria) this;
        }

        public Criteria andProcesadoLikeInsensitive(String value) {
            addCriterion("upper(procesado) like", value.toUpperCase(), "procesado");
            return (Criteria) this;
        }

        public Criteria andMensajeProcesoLikeInsensitive(String value) {
            addCriterion("upper(mensaje_proceso) like", value.toUpperCase(), "mensajeProceso");
            return (Criteria) this;
        }

        public Criteria andCodcajaLikeInsensitive(String value) {
            addCriterion("upper(codcaja) like", value.toUpperCase(), "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodTicketLikeInsensitive(String value) {
            addCriterion("upper(cod_ticket) like", value.toUpperCase(), "codTicket");
            return (Criteria) this;
        }

        public Criteria andFirmaLikeInsensitive(String value) {
            addCriterion("upper(firma) like", value.toUpperCase(), "firma");
            return (Criteria) this;
        }

        public Criteria andSerieTicketLikeInsensitive(String value) {
            addCriterion("upper(serie_ticket) like", value.toUpperCase(), "serieTicket");
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