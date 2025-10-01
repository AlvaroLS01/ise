package com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventoAuditoriaBeanExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public EventoAuditoriaBeanExample() {
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
        protected List<Criterion> procedeCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
            procedeCriteria = new ArrayList<>();
        }

        public List<Criterion> getProcedeCriteria() {
            return procedeCriteria;
        }

        protected void addProcedeCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            procedeCriteria.add(new Criterion(condition, value, "com.comerzzia.core.util.mybatis.typehandlers.BooleanStringTypeHandler"));
            allCriteria = null;
        }

        protected void addProcedeCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            procedeCriteria.add(new Criterion(condition, value1, value2, "com.comerzzia.core.util.mybatis.typehandlers.BooleanStringTypeHandler"));
            allCriteria = null;
        }

        public boolean isValid() {
            return criteria.size() > 0
                || procedeCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(procedeCriteria);
            }
            return allCriteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
            allCriteria = null;
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
            allCriteria = null;
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
            allCriteria = null;
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

        public Criteria andUidTicketAuditoriaIsNull() {
            addCriterion("UID_TICKET_AUDITORIA is null");
            return (Criteria) this;
        }

        public Criteria andUidTicketAuditoriaIsNotNull() {
            addCriterion("UID_TICKET_AUDITORIA is not null");
            return (Criteria) this;
        }

        public Criteria andUidTicketAuditoriaEqualTo(String value) {
            addCriterion("UID_TICKET_AUDITORIA =", value, "uidTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andUidTicketAuditoriaNotEqualTo(String value) {
            addCriterion("UID_TICKET_AUDITORIA <>", value, "uidTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andUidTicketAuditoriaGreaterThan(String value) {
            addCriterion("UID_TICKET_AUDITORIA >", value, "uidTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andUidTicketAuditoriaGreaterThanOrEqualTo(String value) {
            addCriterion("UID_TICKET_AUDITORIA >=", value, "uidTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andUidTicketAuditoriaLessThan(String value) {
            addCriterion("UID_TICKET_AUDITORIA <", value, "uidTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andUidTicketAuditoriaLessThanOrEqualTo(String value) {
            addCriterion("UID_TICKET_AUDITORIA <=", value, "uidTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andUidTicketAuditoriaLike(String value) {
            addCriterion("UID_TICKET_AUDITORIA like", value, "uidTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andUidTicketAuditoriaNotLike(String value) {
            addCriterion("UID_TICKET_AUDITORIA not like", value, "uidTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andUidTicketAuditoriaIn(List<String> values) {
            addCriterion("UID_TICKET_AUDITORIA in", values, "uidTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andUidTicketAuditoriaNotIn(List<String> values) {
            addCriterion("UID_TICKET_AUDITORIA not in", values, "uidTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andUidTicketAuditoriaBetween(String value1, String value2) {
            addCriterion("UID_TICKET_AUDITORIA between", value1, value2, "uidTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andUidTicketAuditoriaNotBetween(String value1, String value2) {
            addCriterion("UID_TICKET_AUDITORIA not between", value1, value2, "uidTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andIdTicketAuditoriaIsNull() {
            addCriterion("ID_TICKET_AUDITORIA is null");
            return (Criteria) this;
        }

        public Criteria andIdTicketAuditoriaIsNotNull() {
            addCriterion("ID_TICKET_AUDITORIA is not null");
            return (Criteria) this;
        }

        public Criteria andIdTicketAuditoriaEqualTo(Long value) {
            addCriterion("ID_TICKET_AUDITORIA =", value, "idTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andIdTicketAuditoriaNotEqualTo(Long value) {
            addCriterion("ID_TICKET_AUDITORIA <>", value, "idTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andIdTicketAuditoriaGreaterThan(Long value) {
            addCriterion("ID_TICKET_AUDITORIA >", value, "idTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andIdTicketAuditoriaGreaterThanOrEqualTo(Long value) {
            addCriterion("ID_TICKET_AUDITORIA >=", value, "idTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andIdTicketAuditoriaLessThan(Long value) {
            addCriterion("ID_TICKET_AUDITORIA <", value, "idTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andIdTicketAuditoriaLessThanOrEqualTo(Long value) {
            addCriterion("ID_TICKET_AUDITORIA <=", value, "idTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andIdTicketAuditoriaIn(List<Long> values) {
            addCriterion("ID_TICKET_AUDITORIA in", values, "idTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andIdTicketAuditoriaNotIn(List<Long> values) {
            addCriterion("ID_TICKET_AUDITORIA not in", values, "idTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andIdTicketAuditoriaBetween(Long value1, Long value2) {
            addCriterion("ID_TICKET_AUDITORIA between", value1, value2, "idTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andIdTicketAuditoriaNotBetween(Long value1, Long value2) {
            addCriterion("ID_TICKET_AUDITORIA not between", value1, value2, "idTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andCodigoAlmacenIsNull() {
            addCriterion("CODALM is null");
            return (Criteria) this;
        }

        public Criteria andCodigoAlmacenIsNotNull() {
            addCriterion("CODALM is not null");
            return (Criteria) this;
        }

        public Criteria andCodigoAlmacenEqualTo(String value) {
            addCriterion("CODALM =", value, "codigoAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodigoAlmacenNotEqualTo(String value) {
            addCriterion("CODALM <>", value, "codigoAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodigoAlmacenGreaterThan(String value) {
            addCriterion("CODALM >", value, "codigoAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodigoAlmacenGreaterThanOrEqualTo(String value) {
            addCriterion("CODALM >=", value, "codigoAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodigoAlmacenLessThan(String value) {
            addCriterion("CODALM <", value, "codigoAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodigoAlmacenLessThanOrEqualTo(String value) {
            addCriterion("CODALM <=", value, "codigoAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodigoAlmacenLike(String value) {
            addCriterion("CODALM like", value, "codigoAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodigoAlmacenNotLike(String value) {
            addCriterion("CODALM not like", value, "codigoAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodigoAlmacenIn(List<String> values) {
            addCriterion("CODALM in", values, "codigoAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodigoAlmacenNotIn(List<String> values) {
            addCriterion("CODALM not in", values, "codigoAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodigoAlmacenBetween(String value1, String value2) {
            addCriterion("CODALM between", value1, value2, "codigoAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodigoAlmacenNotBetween(String value1, String value2) {
            addCriterion("CODALM not between", value1, value2, "codigoAlmacen");
            return (Criteria) this;
        }

        public Criteria andFechaIsNull() {
            addCriterion("FECHA is null");
            return (Criteria) this;
        }

        public Criteria andFechaIsNotNull() {
            addCriterion("FECHA is not null");
            return (Criteria) this;
        }

        public Criteria andFechaEqualTo(Date value) {
            addCriterion("FECHA =", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaNotEqualTo(Date value) {
            addCriterion("FECHA <>", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaGreaterThan(Date value) {
            addCriterion("FECHA >", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaGreaterThanOrEqualTo(Date value) {
            addCriterion("FECHA >=", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaLessThan(Date value) {
            addCriterion("FECHA <", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaLessThanOrEqualTo(Date value) {
            addCriterion("FECHA <=", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaIn(List<Date> values) {
            addCriterion("FECHA in", values, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaNotIn(List<Date> values) {
            addCriterion("FECHA not in", values, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaBetween(Date value1, Date value2) {
            addCriterion("FECHA between", value1, value2, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaNotBetween(Date value1, Date value2) {
            addCriterion("FECHA not between", value1, value2, "fecha");
            return (Criteria) this;
        }

        public Criteria andTipoEventoIsNull() {
            addCriterion("TIPO_EVENTO is null");
            return (Criteria) this;
        }

        public Criteria andTipoEventoIsNotNull() {
            addCriterion("TIPO_EVENTO is not null");
            return (Criteria) this;
        }

        public Criteria andTipoEventoEqualTo(String value) {
            addCriterion("TIPO_EVENTO =", value, "tipoEvento");
            return (Criteria) this;
        }

        public Criteria andTipoEventoNotEqualTo(String value) {
            addCriterion("TIPO_EVENTO <>", value, "tipoEvento");
            return (Criteria) this;
        }

        public Criteria andTipoEventoGreaterThan(String value) {
            addCriterion("TIPO_EVENTO >", value, "tipoEvento");
            return (Criteria) this;
        }

        public Criteria andTipoEventoGreaterThanOrEqualTo(String value) {
            addCriterion("TIPO_EVENTO >=", value, "tipoEvento");
            return (Criteria) this;
        }

        public Criteria andTipoEventoLessThan(String value) {
            addCriterion("TIPO_EVENTO <", value, "tipoEvento");
            return (Criteria) this;
        }

        public Criteria andTipoEventoLessThanOrEqualTo(String value) {
            addCriterion("TIPO_EVENTO <=", value, "tipoEvento");
            return (Criteria) this;
        }

        public Criteria andTipoEventoLike(String value) {
            addCriterion("TIPO_EVENTO like", value, "tipoEvento");
            return (Criteria) this;
        }

        public Criteria andTipoEventoNotLike(String value) {
            addCriterion("TIPO_EVENTO not like", value, "tipoEvento");
            return (Criteria) this;
        }

        public Criteria andTipoEventoIn(List<String> values) {
            addCriterion("TIPO_EVENTO in", values, "tipoEvento");
            return (Criteria) this;
        }

        public Criteria andTipoEventoNotIn(List<String> values) {
            addCriterion("TIPO_EVENTO not in", values, "tipoEvento");
            return (Criteria) this;
        }

        public Criteria andTipoEventoBetween(String value1, String value2) {
            addCriterion("TIPO_EVENTO between", value1, value2, "tipoEvento");
            return (Criteria) this;
        }

        public Criteria andTipoEventoNotBetween(String value1, String value2) {
            addCriterion("TIPO_EVENTO not between", value1, value2, "tipoEvento");
            return (Criteria) this;
        }

        public Criteria andDescripcionEventoIsNull() {
            addCriterion("DESCRIPCION_EVENTO is null");
            return (Criteria) this;
        }

        public Criteria andDescripcionEventoIsNotNull() {
            addCriterion("DESCRIPCION_EVENTO is not null");
            return (Criteria) this;
        }

        public Criteria andDescripcionEventoEqualTo(String value) {
            addCriterion("DESCRIPCION_EVENTO =", value, "descripcionEvento");
            return (Criteria) this;
        }

        public Criteria andDescripcionEventoNotEqualTo(String value) {
            addCriterion("DESCRIPCION_EVENTO <>", value, "descripcionEvento");
            return (Criteria) this;
        }

        public Criteria andDescripcionEventoGreaterThan(String value) {
            addCriterion("DESCRIPCION_EVENTO >", value, "descripcionEvento");
            return (Criteria) this;
        }

        public Criteria andDescripcionEventoGreaterThanOrEqualTo(String value) {
            addCriterion("DESCRIPCION_EVENTO >=", value, "descripcionEvento");
            return (Criteria) this;
        }

        public Criteria andDescripcionEventoLessThan(String value) {
            addCriterion("DESCRIPCION_EVENTO <", value, "descripcionEvento");
            return (Criteria) this;
        }

        public Criteria andDescripcionEventoLessThanOrEqualTo(String value) {
            addCriterion("DESCRIPCION_EVENTO <=", value, "descripcionEvento");
            return (Criteria) this;
        }

        public Criteria andDescripcionEventoLike(String value) {
            addCriterion("DESCRIPCION_EVENTO like", value, "descripcionEvento");
            return (Criteria) this;
        }

        public Criteria andDescripcionEventoNotLike(String value) {
            addCriterion("DESCRIPCION_EVENTO not like", value, "descripcionEvento");
            return (Criteria) this;
        }

        public Criteria andDescripcionEventoIn(List<String> values) {
            addCriterion("DESCRIPCION_EVENTO in", values, "descripcionEvento");
            return (Criteria) this;
        }

        public Criteria andDescripcionEventoNotIn(List<String> values) {
            addCriterion("DESCRIPCION_EVENTO not in", values, "descripcionEvento");
            return (Criteria) this;
        }

        public Criteria andDescripcionEventoBetween(String value1, String value2) {
            addCriterion("DESCRIPCION_EVENTO between", value1, value2, "descripcionEvento");
            return (Criteria) this;
        }

        public Criteria andDescripcionEventoNotBetween(String value1, String value2) {
            addCriterion("DESCRIPCION_EVENTO not between", value1, value2, "descripcionEvento");
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

        public Criteria andNombreUsuarioIsNull() {
            addCriterion("DES_USUARIO is null");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioIsNotNull() {
            addCriterion("DES_USUARIO is not null");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioEqualTo(String value) {
            addCriterion("DES_USUARIO =", value, "nombreUsuario");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioNotEqualTo(String value) {
            addCriterion("DES_USUARIO <>", value, "nombreUsuario");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioGreaterThan(String value) {
            addCriterion("DES_USUARIO >", value, "nombreUsuario");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioGreaterThanOrEqualTo(String value) {
            addCriterion("DES_USUARIO >=", value, "nombreUsuario");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioLessThan(String value) {
            addCriterion("DES_USUARIO <", value, "nombreUsuario");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioLessThanOrEqualTo(String value) {
            addCriterion("DES_USUARIO <=", value, "nombreUsuario");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioLike(String value) {
            addCriterion("DES_USUARIO like", value, "nombreUsuario");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioNotLike(String value) {
            addCriterion("DES_USUARIO not like", value, "nombreUsuario");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioIn(List<String> values) {
            addCriterion("DES_USUARIO in", values, "nombreUsuario");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioNotIn(List<String> values) {
            addCriterion("DES_USUARIO not in", values, "nombreUsuario");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioBetween(String value1, String value2) {
            addCriterion("DES_USUARIO between", value1, value2, "nombreUsuario");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioNotBetween(String value1, String value2) {
            addCriterion("DES_USUARIO not between", value1, value2, "nombreUsuario");
            return (Criteria) this;
        }

        public Criteria andProcedeIsNull() {
            addCriterion("PROCEDE is null");
            return (Criteria) this;
        }

        public Criteria andProcedeIsNotNull() {
            addCriterion("PROCEDE is not null");
            return (Criteria) this;
        }

        public Criteria andProcedeEqualTo(Boolean value) {
            addProcedeCriterion("PROCEDE =", value, "procede");
            return (Criteria) this;
        }

        public Criteria andProcedeNotEqualTo(Boolean value) {
            addProcedeCriterion("PROCEDE <>", value, "procede");
            return (Criteria) this;
        }

        public Criteria andProcedeGreaterThan(Boolean value) {
            addProcedeCriterion("PROCEDE >", value, "procede");
            return (Criteria) this;
        }

        public Criteria andProcedeGreaterThanOrEqualTo(Boolean value) {
            addProcedeCriterion("PROCEDE >=", value, "procede");
            return (Criteria) this;
        }

        public Criteria andProcedeLessThan(Boolean value) {
            addProcedeCriterion("PROCEDE <", value, "procede");
            return (Criteria) this;
        }

        public Criteria andProcedeLessThanOrEqualTo(Boolean value) {
            addProcedeCriterion("PROCEDE <=", value, "procede");
            return (Criteria) this;
        }

        public Criteria andProcedeLike(Boolean value) {
            addProcedeCriterion("PROCEDE like", value, "procede");
            return (Criteria) this;
        }

        public Criteria andProcedeNotLike(Boolean value) {
            addProcedeCriterion("PROCEDE not like", value, "procede");
            return (Criteria) this;
        }

        public Criteria andProcedeIn(List<Boolean> values) {
            addProcedeCriterion("PROCEDE in", values, "procede");
            return (Criteria) this;
        }

        public Criteria andProcedeNotIn(List<Boolean> values) {
            addProcedeCriterion("PROCEDE not in", values, "procede");
            return (Criteria) this;
        }

        public Criteria andProcedeBetween(Boolean value1, Boolean value2) {
            addProcedeCriterion("PROCEDE between", value1, value2, "procede");
            return (Criteria) this;
        }

        public Criteria andProcedeNotBetween(Boolean value1, Boolean value2) {
            addProcedeCriterion("PROCEDE not between", value1, value2, "procede");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioSupervisorIsNull() {
            addCriterion("ID_USUARIO_SUPERVISOR is null");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioSupervisorIsNotNull() {
            addCriterion("ID_USUARIO_SUPERVISOR is not null");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioSupervisorEqualTo(Long value) {
            addCriterion("ID_USUARIO_SUPERVISOR =", value, "idUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioSupervisorNotEqualTo(Long value) {
            addCriterion("ID_USUARIO_SUPERVISOR <>", value, "idUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioSupervisorGreaterThan(Long value) {
            addCriterion("ID_USUARIO_SUPERVISOR >", value, "idUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioSupervisorGreaterThanOrEqualTo(Long value) {
            addCriterion("ID_USUARIO_SUPERVISOR >=", value, "idUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioSupervisorLessThan(Long value) {
            addCriterion("ID_USUARIO_SUPERVISOR <", value, "idUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioSupervisorLessThanOrEqualTo(Long value) {
            addCriterion("ID_USUARIO_SUPERVISOR <=", value, "idUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioSupervisorIn(List<Long> values) {
            addCriterion("ID_USUARIO_SUPERVISOR in", values, "idUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioSupervisorNotIn(List<Long> values) {
            addCriterion("ID_USUARIO_SUPERVISOR not in", values, "idUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioSupervisorBetween(Long value1, Long value2) {
            addCriterion("ID_USUARIO_SUPERVISOR between", value1, value2, "idUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioSupervisorNotBetween(Long value1, Long value2) {
            addCriterion("ID_USUARIO_SUPERVISOR not between", value1, value2, "idUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioSupervisorIsNull() {
            addCriterion("DES_USUARIO_SUPERVISOR is null");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioSupervisorIsNotNull() {
            addCriterion("DES_USUARIO_SUPERVISOR is not null");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioSupervisorEqualTo(String value) {
            addCriterion("DES_USUARIO_SUPERVISOR =", value, "nombreUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioSupervisorNotEqualTo(String value) {
            addCriterion("DES_USUARIO_SUPERVISOR <>", value, "nombreUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioSupervisorGreaterThan(String value) {
            addCriterion("DES_USUARIO_SUPERVISOR >", value, "nombreUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioSupervisorGreaterThanOrEqualTo(String value) {
            addCriterion("DES_USUARIO_SUPERVISOR >=", value, "nombreUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioSupervisorLessThan(String value) {
            addCriterion("DES_USUARIO_SUPERVISOR <", value, "nombreUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioSupervisorLessThanOrEqualTo(String value) {
            addCriterion("DES_USUARIO_SUPERVISOR <=", value, "nombreUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioSupervisorLike(String value) {
            addCriterion("DES_USUARIO_SUPERVISOR like", value, "nombreUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioSupervisorNotLike(String value) {
            addCriterion("DES_USUARIO_SUPERVISOR not like", value, "nombreUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioSupervisorIn(List<String> values) {
            addCriterion("DES_USUARIO_SUPERVISOR in", values, "nombreUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioSupervisorNotIn(List<String> values) {
            addCriterion("DES_USUARIO_SUPERVISOR not in", values, "nombreUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioSupervisorBetween(String value1, String value2) {
            addCriterion("DES_USUARIO_SUPERVISOR between", value1, value2, "nombreUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioSupervisorNotBetween(String value1, String value2) {
            addCriterion("DES_USUARIO_SUPERVISOR not between", value1, value2, "nombreUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andUidTicketVentaIsNull() {
            addCriterion("UID_TICKET_VENTA is null");
            return (Criteria) this;
        }

        public Criteria andUidTicketVentaIsNotNull() {
            addCriterion("UID_TICKET_VENTA is not null");
            return (Criteria) this;
        }

        public Criteria andUidTicketVentaEqualTo(String value) {
            addCriterion("UID_TICKET_VENTA =", value, "uidTicketVenta");
            return (Criteria) this;
        }

        public Criteria andUidTicketVentaNotEqualTo(String value) {
            addCriterion("UID_TICKET_VENTA <>", value, "uidTicketVenta");
            return (Criteria) this;
        }

        public Criteria andUidTicketVentaGreaterThan(String value) {
            addCriterion("UID_TICKET_VENTA >", value, "uidTicketVenta");
            return (Criteria) this;
        }

        public Criteria andUidTicketVentaGreaterThanOrEqualTo(String value) {
            addCriterion("UID_TICKET_VENTA >=", value, "uidTicketVenta");
            return (Criteria) this;
        }

        public Criteria andUidTicketVentaLessThan(String value) {
            addCriterion("UID_TICKET_VENTA <", value, "uidTicketVenta");
            return (Criteria) this;
        }

        public Criteria andUidTicketVentaLessThanOrEqualTo(String value) {
            addCriterion("UID_TICKET_VENTA <=", value, "uidTicketVenta");
            return (Criteria) this;
        }

        public Criteria andUidTicketVentaLike(String value) {
            addCriterion("UID_TICKET_VENTA like", value, "uidTicketVenta");
            return (Criteria) this;
        }

        public Criteria andUidTicketVentaNotLike(String value) {
            addCriterion("UID_TICKET_VENTA not like", value, "uidTicketVenta");
            return (Criteria) this;
        }

        public Criteria andUidTicketVentaIn(List<String> values) {
            addCriterion("UID_TICKET_VENTA in", values, "uidTicketVenta");
            return (Criteria) this;
        }

        public Criteria andUidTicketVentaNotIn(List<String> values) {
            addCriterion("UID_TICKET_VENTA not in", values, "uidTicketVenta");
            return (Criteria) this;
        }

        public Criteria andUidTicketVentaBetween(String value1, String value2) {
            addCriterion("UID_TICKET_VENTA between", value1, value2, "uidTicketVenta");
            return (Criteria) this;
        }

        public Criteria andUidTicketVentaNotBetween(String value1, String value2) {
            addCriterion("UID_TICKET_VENTA not between", value1, value2, "uidTicketVenta");
            return (Criteria) this;
        }

        public Criteria andIdTicketVentaIsNull() {
            addCriterion("ID_TICKET_VENTA is null");
            return (Criteria) this;
        }

        public Criteria andIdTicketVentaIsNotNull() {
            addCriterion("ID_TICKET_VENTA is not null");
            return (Criteria) this;
        }

        public Criteria andIdTicketVentaEqualTo(Long value) {
            addCriterion("ID_TICKET_VENTA =", value, "idTicketVenta");
            return (Criteria) this;
        }

        public Criteria andIdTicketVentaNotEqualTo(Long value) {
            addCriterion("ID_TICKET_VENTA <>", value, "idTicketVenta");
            return (Criteria) this;
        }

        public Criteria andIdTicketVentaGreaterThan(Long value) {
            addCriterion("ID_TICKET_VENTA >", value, "idTicketVenta");
            return (Criteria) this;
        }

        public Criteria andIdTicketVentaGreaterThanOrEqualTo(Long value) {
            addCriterion("ID_TICKET_VENTA >=", value, "idTicketVenta");
            return (Criteria) this;
        }

        public Criteria andIdTicketVentaLessThan(Long value) {
            addCriterion("ID_TICKET_VENTA <", value, "idTicketVenta");
            return (Criteria) this;
        }

        public Criteria andIdTicketVentaLessThanOrEqualTo(Long value) {
            addCriterion("ID_TICKET_VENTA <=", value, "idTicketVenta");
            return (Criteria) this;
        }

        public Criteria andIdTicketVentaIn(List<Long> values) {
            addCriterion("ID_TICKET_VENTA in", values, "idTicketVenta");
            return (Criteria) this;
        }

        public Criteria andIdTicketVentaNotIn(List<Long> values) {
            addCriterion("ID_TICKET_VENTA not in", values, "idTicketVenta");
            return (Criteria) this;
        }

        public Criteria andIdTicketVentaBetween(Long value1, Long value2) {
            addCriterion("ID_TICKET_VENTA between", value1, value2, "idTicketVenta");
            return (Criteria) this;
        }

        public Criteria andIdTicketVentaNotBetween(Long value1, Long value2) {
            addCriterion("ID_TICKET_VENTA not between", value1, value2, "idTicketVenta");
            return (Criteria) this;
        }

        public Criteria andCodigoArticuloIsNull() {
            addCriterion("CODART is null");
            return (Criteria) this;
        }

        public Criteria andCodigoArticuloIsNotNull() {
            addCriterion("CODART is not null");
            return (Criteria) this;
        }

        public Criteria andCodigoArticuloEqualTo(String value) {
            addCriterion("CODART =", value, "codigoArticulo");
            return (Criteria) this;
        }

        public Criteria andCodigoArticuloNotEqualTo(String value) {
            addCriterion("CODART <>", value, "codigoArticulo");
            return (Criteria) this;
        }

        public Criteria andCodigoArticuloGreaterThan(String value) {
            addCriterion("CODART >", value, "codigoArticulo");
            return (Criteria) this;
        }

        public Criteria andCodigoArticuloGreaterThanOrEqualTo(String value) {
            addCriterion("CODART >=", value, "codigoArticulo");
            return (Criteria) this;
        }

        public Criteria andCodigoArticuloLessThan(String value) {
            addCriterion("CODART <", value, "codigoArticulo");
            return (Criteria) this;
        }

        public Criteria andCodigoArticuloLessThanOrEqualTo(String value) {
            addCriterion("CODART <=", value, "codigoArticulo");
            return (Criteria) this;
        }

        public Criteria andCodigoArticuloLike(String value) {
            addCriterion("CODART like", value, "codigoArticulo");
            return (Criteria) this;
        }

        public Criteria andCodigoArticuloNotLike(String value) {
            addCriterion("CODART not like", value, "codigoArticulo");
            return (Criteria) this;
        }

        public Criteria andCodigoArticuloIn(List<String> values) {
            addCriterion("CODART in", values, "codigoArticulo");
            return (Criteria) this;
        }

        public Criteria andCodigoArticuloNotIn(List<String> values) {
            addCriterion("CODART not in", values, "codigoArticulo");
            return (Criteria) this;
        }

        public Criteria andCodigoArticuloBetween(String value1, String value2) {
            addCriterion("CODART between", value1, value2, "codigoArticulo");
            return (Criteria) this;
        }

        public Criteria andCodigoArticuloNotBetween(String value1, String value2) {
            addCriterion("CODART not between", value1, value2, "codigoArticulo");
            return (Criteria) this;
        }

        public Criteria andDescripcionArticuloIsNull() {
            addCriterion("DESART is null");
            return (Criteria) this;
        }

        public Criteria andDescripcionArticuloIsNotNull() {
            addCriterion("DESART is not null");
            return (Criteria) this;
        }

        public Criteria andDescripcionArticuloEqualTo(String value) {
            addCriterion("DESART =", value, "descripcionArticulo");
            return (Criteria) this;
        }

        public Criteria andDescripcionArticuloNotEqualTo(String value) {
            addCriterion("DESART <>", value, "descripcionArticulo");
            return (Criteria) this;
        }

        public Criteria andDescripcionArticuloGreaterThan(String value) {
            addCriterion("DESART >", value, "descripcionArticulo");
            return (Criteria) this;
        }

        public Criteria andDescripcionArticuloGreaterThanOrEqualTo(String value) {
            addCriterion("DESART >=", value, "descripcionArticulo");
            return (Criteria) this;
        }

        public Criteria andDescripcionArticuloLessThan(String value) {
            addCriterion("DESART <", value, "descripcionArticulo");
            return (Criteria) this;
        }

        public Criteria andDescripcionArticuloLessThanOrEqualTo(String value) {
            addCriterion("DESART <=", value, "descripcionArticulo");
            return (Criteria) this;
        }

        public Criteria andDescripcionArticuloLike(String value) {
            addCriterion("DESART like", value, "descripcionArticulo");
            return (Criteria) this;
        }

        public Criteria andDescripcionArticuloNotLike(String value) {
            addCriterion("DESART not like", value, "descripcionArticulo");
            return (Criteria) this;
        }

        public Criteria andDescripcionArticuloIn(List<String> values) {
            addCriterion("DESART in", values, "descripcionArticulo");
            return (Criteria) this;
        }

        public Criteria andDescripcionArticuloNotIn(List<String> values) {
            addCriterion("DESART not in", values, "descripcionArticulo");
            return (Criteria) this;
        }

        public Criteria andDescripcionArticuloBetween(String value1, String value2) {
            addCriterion("DESART between", value1, value2, "descripcionArticulo");
            return (Criteria) this;
        }

        public Criteria andDescripcionArticuloNotBetween(String value1, String value2) {
            addCriterion("DESART not between", value1, value2, "descripcionArticulo");
            return (Criteria) this;
        }

        public Criteria andCantidadArticuloIsNull() {
            addCriterion("CANART is null");
            return (Criteria) this;
        }

        public Criteria andCantidadArticuloIsNotNull() {
            addCriterion("CANART is not null");
            return (Criteria) this;
        }

        public Criteria andCantidadArticuloEqualTo(BigDecimal value) {
            addCriterion("CANART =", value, "cantidadArticulo");
            return (Criteria) this;
        }

        public Criteria andCantidadArticuloNotEqualTo(BigDecimal value) {
            addCriterion("CANART <>", value, "cantidadArticulo");
            return (Criteria) this;
        }

        public Criteria andCantidadArticuloGreaterThan(BigDecimal value) {
            addCriterion("CANART >", value, "cantidadArticulo");
            return (Criteria) this;
        }

        public Criteria andCantidadArticuloGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("CANART >=", value, "cantidadArticulo");
            return (Criteria) this;
        }

        public Criteria andCantidadArticuloLessThan(BigDecimal value) {
            addCriterion("CANART <", value, "cantidadArticulo");
            return (Criteria) this;
        }

        public Criteria andCantidadArticuloLessThanOrEqualTo(BigDecimal value) {
            addCriterion("CANART <=", value, "cantidadArticulo");
            return (Criteria) this;
        }

        public Criteria andCantidadArticuloIn(List<BigDecimal> values) {
            addCriterion("CANART in", values, "cantidadArticulo");
            return (Criteria) this;
        }

        public Criteria andCantidadArticuloNotIn(List<BigDecimal> values) {
            addCriterion("CANART not in", values, "cantidadArticulo");
            return (Criteria) this;
        }

        public Criteria andCantidadArticuloBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("CANART between", value1, value2, "cantidadArticulo");
            return (Criteria) this;
        }

        public Criteria andCantidadArticuloNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("CANART not between", value1, value2, "cantidadArticulo");
            return (Criteria) this;
        }

        public Criteria andPrecioOriginalIsNull() {
            addCriterion("PRECIO_ORIGINAL is null");
            return (Criteria) this;
        }

        public Criteria andPrecioOriginalIsNotNull() {
            addCriterion("PRECIO_ORIGINAL is not null");
            return (Criteria) this;
        }

        public Criteria andPrecioOriginalEqualTo(BigDecimal value) {
            addCriterion("PRECIO_ORIGINAL =", value, "precioOriginal");
            return (Criteria) this;
        }

        public Criteria andPrecioOriginalNotEqualTo(BigDecimal value) {
            addCriterion("PRECIO_ORIGINAL <>", value, "precioOriginal");
            return (Criteria) this;
        }

        public Criteria andPrecioOriginalGreaterThan(BigDecimal value) {
            addCriterion("PRECIO_ORIGINAL >", value, "precioOriginal");
            return (Criteria) this;
        }

        public Criteria andPrecioOriginalGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("PRECIO_ORIGINAL >=", value, "precioOriginal");
            return (Criteria) this;
        }

        public Criteria andPrecioOriginalLessThan(BigDecimal value) {
            addCriterion("PRECIO_ORIGINAL <", value, "precioOriginal");
            return (Criteria) this;
        }

        public Criteria andPrecioOriginalLessThanOrEqualTo(BigDecimal value) {
            addCriterion("PRECIO_ORIGINAL <=", value, "precioOriginal");
            return (Criteria) this;
        }

        public Criteria andPrecioOriginalIn(List<BigDecimal> values) {
            addCriterion("PRECIO_ORIGINAL in", values, "precioOriginal");
            return (Criteria) this;
        }

        public Criteria andPrecioOriginalNotIn(List<BigDecimal> values) {
            addCriterion("PRECIO_ORIGINAL not in", values, "precioOriginal");
            return (Criteria) this;
        }

        public Criteria andPrecioOriginalBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PRECIO_ORIGINAL between", value1, value2, "precioOriginal");
            return (Criteria) this;
        }

        public Criteria andPrecioOriginalNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PRECIO_ORIGINAL not between", value1, value2, "precioOriginal");
            return (Criteria) this;
        }

        public Criteria andPrecioAplicadoIsNull() {
            addCriterion("PRECIO_APLICADO is null");
            return (Criteria) this;
        }

        public Criteria andPrecioAplicadoIsNotNull() {
            addCriterion("PRECIO_APLICADO is not null");
            return (Criteria) this;
        }

        public Criteria andPrecioAplicadoEqualTo(BigDecimal value) {
            addCriterion("PRECIO_APLICADO =", value, "precioAplicado");
            return (Criteria) this;
        }

        public Criteria andPrecioAplicadoNotEqualTo(BigDecimal value) {
            addCriterion("PRECIO_APLICADO <>", value, "precioAplicado");
            return (Criteria) this;
        }

        public Criteria andPrecioAplicadoGreaterThan(BigDecimal value) {
            addCriterion("PRECIO_APLICADO >", value, "precioAplicado");
            return (Criteria) this;
        }

        public Criteria andPrecioAplicadoGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("PRECIO_APLICADO >=", value, "precioAplicado");
            return (Criteria) this;
        }

        public Criteria andPrecioAplicadoLessThan(BigDecimal value) {
            addCriterion("PRECIO_APLICADO <", value, "precioAplicado");
            return (Criteria) this;
        }

        public Criteria andPrecioAplicadoLessThanOrEqualTo(BigDecimal value) {
            addCriterion("PRECIO_APLICADO <=", value, "precioAplicado");
            return (Criteria) this;
        }

        public Criteria andPrecioAplicadoIn(List<BigDecimal> values) {
            addCriterion("PRECIO_APLICADO in", values, "precioAplicado");
            return (Criteria) this;
        }

        public Criteria andPrecioAplicadoNotIn(List<BigDecimal> values) {
            addCriterion("PRECIO_APLICADO not in", values, "precioAplicado");
            return (Criteria) this;
        }

        public Criteria andPrecioAplicadoBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PRECIO_APLICADO between", value1, value2, "precioAplicado");
            return (Criteria) this;
        }

        public Criteria andPrecioAplicadoNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PRECIO_APLICADO not between", value1, value2, "precioAplicado");
            return (Criteria) this;
        }

        public Criteria andLineaReferenciaIsNull() {
            addCriterion("LINEA_REFERENCIA is null");
            return (Criteria) this;
        }

        public Criteria andLineaReferenciaIsNotNull() {
            addCriterion("LINEA_REFERENCIA is not null");
            return (Criteria) this;
        }

        public Criteria andLineaReferenciaEqualTo(Integer value) {
            addCriterion("LINEA_REFERENCIA =", value, "lineaReferencia");
            return (Criteria) this;
        }

        public Criteria andLineaReferenciaNotEqualTo(Integer value) {
            addCriterion("LINEA_REFERENCIA <>", value, "lineaReferencia");
            return (Criteria) this;
        }

        public Criteria andLineaReferenciaGreaterThan(Integer value) {
            addCriterion("LINEA_REFERENCIA >", value, "lineaReferencia");
            return (Criteria) this;
        }

        public Criteria andLineaReferenciaGreaterThanOrEqualTo(Integer value) {
            addCriterion("LINEA_REFERENCIA >=", value, "lineaReferencia");
            return (Criteria) this;
        }

        public Criteria andLineaReferenciaLessThan(Integer value) {
            addCriterion("LINEA_REFERENCIA <", value, "lineaReferencia");
            return (Criteria) this;
        }

        public Criteria andLineaReferenciaLessThanOrEqualTo(Integer value) {
            addCriterion("LINEA_REFERENCIA <=", value, "lineaReferencia");
            return (Criteria) this;
        }

        public Criteria andLineaReferenciaIn(List<Integer> values) {
            addCriterion("LINEA_REFERENCIA in", values, "lineaReferencia");
            return (Criteria) this;
        }

        public Criteria andLineaReferenciaNotIn(List<Integer> values) {
            addCriterion("LINEA_REFERENCIA not in", values, "lineaReferencia");
            return (Criteria) this;
        }

        public Criteria andLineaReferenciaBetween(Integer value1, Integer value2) {
            addCriterion("LINEA_REFERENCIA between", value1, value2, "lineaReferencia");
            return (Criteria) this;
        }

        public Criteria andLineaReferenciaNotBetween(Integer value1, Integer value2) {
            addCriterion("LINEA_REFERENCIA not between", value1, value2, "lineaReferencia");
            return (Criteria) this;
        }

        public Criteria andUidInstanciaLikeInsensitive(String value) {
            addCriterion("upper(UID_INSTANCIA) like", value.toUpperCase(), "uidInstancia");
            return (Criteria) this;
        }

        public Criteria andUidActividadLikeInsensitive(String value) {
            addCriterion("upper(UID_ACTIVIDAD) like", value.toUpperCase(), "uidActividad");
            return (Criteria) this;
        }

        public Criteria andUidTicketAuditoriaLikeInsensitive(String value) {
            addCriterion("upper(UID_TICKET_AUDITORIA) like", value.toUpperCase(), "uidTicketAuditoria");
            return (Criteria) this;
        }

        public Criteria andCodigoAlmacenLikeInsensitive(String value) {
            addCriterion("upper(CODALM) like", value.toUpperCase(), "codigoAlmacen");
            return (Criteria) this;
        }

        public Criteria andTipoEventoLikeInsensitive(String value) {
            addCriterion("upper(TIPO_EVENTO) like", value.toUpperCase(), "tipoEvento");
            return (Criteria) this;
        }

        public Criteria andDescripcionEventoLikeInsensitive(String value) {
            addCriterion("upper(DESCRIPCION_EVENTO) like", value.toUpperCase(), "descripcionEvento");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioLikeInsensitive(String value) {
            addCriterion("upper(DES_USUARIO) like", value.toUpperCase(), "nombreUsuario");
            return (Criteria) this;
        }

        public Criteria andNombreUsuarioSupervisorLikeInsensitive(String value) {
            addCriterion("upper(DES_USUARIO_SUPERVISOR) like", value.toUpperCase(), "nombreUsuarioSupervisor");
            return (Criteria) this;
        }

        public Criteria andUidTicketVentaLikeInsensitive(String value) {
            addCriterion("upper(UID_TICKET_VENTA) like", value.toUpperCase(), "uidTicketVenta");
            return (Criteria) this;
        }

        public Criteria andCodigoArticuloLikeInsensitive(String value) {
            addCriterion("upper(CODART) like", value.toUpperCase(), "codigoArticulo");
            return (Criteria) this;
        }

        public Criteria andDescripcionArticuloLikeInsensitive(String value) {
            addCriterion("upper(DESART) like", value.toUpperCase(), "descripcionArticulo");
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