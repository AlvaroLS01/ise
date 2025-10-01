package com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ErrorInterfazExample {
    public static final String ORDER_BY_UID_ACTIVIDAD = "UID_ACTIVIDAD";

    public static final String ORDER_BY_UID_ACTIVIDAD_DESC = "UID_ACTIVIDAD DESC";

    public static final String ORDER_BY_UID_ERROR = "UID_ERROR";

    public static final String ORDER_BY_UID_ERROR_DESC = "UID_ERROR DESC";

    public static final String ORDER_BY_ID_CLASE = "ID_CLASE";

    public static final String ORDER_BY_ID_CLASE_DESC = "ID_CLASE DESC";

    public static final String ORDER_BY_ID_OBJETO = "ID_OBJETO";

    public static final String ORDER_BY_ID_OBJETO_DESC = "ID_OBJETO DESC";

    public static final String ORDER_BY_FECHA_INICIO = "FECHA_INICIO";

    public static final String ORDER_BY_FECHA_INICIO_DESC = "FECHA_INICIO DESC";

    public static final String ORDER_BY_ULTIMO_ERROR = "ULTIMO_ERROR";

    public static final String ORDER_BY_ULTIMO_ERROR_DESC = "ULTIMO_ERROR DESC";

    public static final String ORDER_BY_ULTIMO_DOCUMENTO = "ULTIMO_DOCUMENTO";

    public static final String ORDER_BY_ULTIMO_DOCUMENTO_DESC = "ULTIMO_DOCUMENTO DESC";

    public static final String ORDER_BY_ULTIMO_MENSAJE = "ULTIMO_MENSAJE";

    public static final String ORDER_BY_ULTIMO_MENSAJE_DESC = "ULTIMO_MENSAJE DESC";

    public static final String ORDER_BY_FECHA_FIN = "FECHA_FIN";

    public static final String ORDER_BY_FECHA_FIN_DESC = "FECHA_FIN DESC";

    public static final String ORDER_BY_TIENDAS_IMPLICADAS = "TIENDAS_IMPLICADAS";

    public static final String ORDER_BY_TIENDAS_IMPLICADAS_DESC = "TIENDAS_IMPLICADAS DESC";

    public static final String ORDER_BY_ULTIMO_MENSAJE_TRAZA = "ULTIMO_MENSAJE_TRAZA";

    public static final String ORDER_BY_ULTIMO_MENSAJE_TRAZA_DESC = "ULTIMO_MENSAJE_TRAZA DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ErrorInterfazExample() {
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

        public Criteria andUidErrorIsNull() {
            addCriterion("UID_ERROR is null");
            return (Criteria) this;
        }

        public Criteria andUidErrorIsNotNull() {
            addCriterion("UID_ERROR is not null");
            return (Criteria) this;
        }

        public Criteria andUidErrorEqualTo(String value) {
            addCriterion("UID_ERROR =", value, "uidError");
            return (Criteria) this;
        }

        public Criteria andUidErrorNotEqualTo(String value) {
            addCriterion("UID_ERROR <>", value, "uidError");
            return (Criteria) this;
        }

        public Criteria andUidErrorGreaterThan(String value) {
            addCriterion("UID_ERROR >", value, "uidError");
            return (Criteria) this;
        }

        public Criteria andUidErrorGreaterThanOrEqualTo(String value) {
            addCriterion("UID_ERROR >=", value, "uidError");
            return (Criteria) this;
        }

        public Criteria andUidErrorLessThan(String value) {
            addCriterion("UID_ERROR <", value, "uidError");
            return (Criteria) this;
        }

        public Criteria andUidErrorLessThanOrEqualTo(String value) {
            addCriterion("UID_ERROR <=", value, "uidError");
            return (Criteria) this;
        }

        public Criteria andUidErrorLike(String value) {
            addCriterion("UID_ERROR like", value, "uidError");
            return (Criteria) this;
        }

        public Criteria andUidErrorNotLike(String value) {
            addCriterion("UID_ERROR not like", value, "uidError");
            return (Criteria) this;
        }

        public Criteria andUidErrorIn(List<String> values) {
            addCriterion("UID_ERROR in", values, "uidError");
            return (Criteria) this;
        }

        public Criteria andUidErrorNotIn(List<String> values) {
            addCriterion("UID_ERROR not in", values, "uidError");
            return (Criteria) this;
        }

        public Criteria andUidErrorBetween(String value1, String value2) {
            addCriterion("UID_ERROR between", value1, value2, "uidError");
            return (Criteria) this;
        }

        public Criteria andUidErrorNotBetween(String value1, String value2) {
            addCriterion("UID_ERROR not between", value1, value2, "uidError");
            return (Criteria) this;
        }

        public Criteria andIdClaseIsNull() {
            addCriterion("ID_CLASE is null");
            return (Criteria) this;
        }

        public Criteria andIdClaseIsNotNull() {
            addCriterion("ID_CLASE is not null");
            return (Criteria) this;
        }

        public Criteria andIdClaseEqualTo(String value) {
            addCriterion("ID_CLASE =", value, "idClase");
            return (Criteria) this;
        }

        public Criteria andIdClaseNotEqualTo(String value) {
            addCriterion("ID_CLASE <>", value, "idClase");
            return (Criteria) this;
        }

        public Criteria andIdClaseGreaterThan(String value) {
            addCriterion("ID_CLASE >", value, "idClase");
            return (Criteria) this;
        }

        public Criteria andIdClaseGreaterThanOrEqualTo(String value) {
            addCriterion("ID_CLASE >=", value, "idClase");
            return (Criteria) this;
        }

        public Criteria andIdClaseLessThan(String value) {
            addCriterion("ID_CLASE <", value, "idClase");
            return (Criteria) this;
        }

        public Criteria andIdClaseLessThanOrEqualTo(String value) {
            addCriterion("ID_CLASE <=", value, "idClase");
            return (Criteria) this;
        }

        public Criteria andIdClaseLike(String value) {
            addCriterion("ID_CLASE like", value, "idClase");
            return (Criteria) this;
        }

        public Criteria andIdClaseNotLike(String value) {
            addCriterion("ID_CLASE not like", value, "idClase");
            return (Criteria) this;
        }

        public Criteria andIdClaseIn(List<String> values) {
            addCriterion("ID_CLASE in", values, "idClase");
            return (Criteria) this;
        }

        public Criteria andIdClaseNotIn(List<String> values) {
            addCriterion("ID_CLASE not in", values, "idClase");
            return (Criteria) this;
        }

        public Criteria andIdClaseBetween(String value1, String value2) {
            addCriterion("ID_CLASE between", value1, value2, "idClase");
            return (Criteria) this;
        }

        public Criteria andIdClaseNotBetween(String value1, String value2) {
            addCriterion("ID_CLASE not between", value1, value2, "idClase");
            return (Criteria) this;
        }

        public Criteria andIdObjetoIsNull() {
            addCriterion("ID_OBJETO is null");
            return (Criteria) this;
        }

        public Criteria andIdObjetoIsNotNull() {
            addCriterion("ID_OBJETO is not null");
            return (Criteria) this;
        }

        public Criteria andIdObjetoEqualTo(String value) {
            addCriterion("ID_OBJETO =", value, "idObjeto");
            return (Criteria) this;
        }

        public Criteria andIdObjetoNotEqualTo(String value) {
            addCriterion("ID_OBJETO <>", value, "idObjeto");
            return (Criteria) this;
        }

        public Criteria andIdObjetoGreaterThan(String value) {
            addCriterion("ID_OBJETO >", value, "idObjeto");
            return (Criteria) this;
        }

        public Criteria andIdObjetoGreaterThanOrEqualTo(String value) {
            addCriterion("ID_OBJETO >=", value, "idObjeto");
            return (Criteria) this;
        }

        public Criteria andIdObjetoLessThan(String value) {
            addCriterion("ID_OBJETO <", value, "idObjeto");
            return (Criteria) this;
        }

        public Criteria andIdObjetoLessThanOrEqualTo(String value) {
            addCriterion("ID_OBJETO <=", value, "idObjeto");
            return (Criteria) this;
        }

        public Criteria andIdObjetoLike(String value) {
            addCriterion("ID_OBJETO like", value, "idObjeto");
            return (Criteria) this;
        }

        public Criteria andIdObjetoNotLike(String value) {
            addCriterion("ID_OBJETO not like", value, "idObjeto");
            return (Criteria) this;
        }

        public Criteria andIdObjetoIn(List<String> values) {
            addCriterion("ID_OBJETO in", values, "idObjeto");
            return (Criteria) this;
        }

        public Criteria andIdObjetoNotIn(List<String> values) {
            addCriterion("ID_OBJETO not in", values, "idObjeto");
            return (Criteria) this;
        }

        public Criteria andIdObjetoBetween(String value1, String value2) {
            addCriterion("ID_OBJETO between", value1, value2, "idObjeto");
            return (Criteria) this;
        }

        public Criteria andIdObjetoNotBetween(String value1, String value2) {
            addCriterion("ID_OBJETO not between", value1, value2, "idObjeto");
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

        public Criteria andUltimoErrorIsNull() {
            addCriterion("ULTIMO_ERROR is null");
            return (Criteria) this;
        }

        public Criteria andUltimoErrorIsNotNull() {
            addCriterion("ULTIMO_ERROR is not null");
            return (Criteria) this;
        }

        public Criteria andUltimoErrorEqualTo(Date value) {
            addCriterion("ULTIMO_ERROR =", value, "ultimoError");
            return (Criteria) this;
        }

        public Criteria andUltimoErrorNotEqualTo(Date value) {
            addCriterion("ULTIMO_ERROR <>", value, "ultimoError");
            return (Criteria) this;
        }

        public Criteria andUltimoErrorGreaterThan(Date value) {
            addCriterion("ULTIMO_ERROR >", value, "ultimoError");
            return (Criteria) this;
        }

        public Criteria andUltimoErrorGreaterThanOrEqualTo(Date value) {
            addCriterion("ULTIMO_ERROR >=", value, "ultimoError");
            return (Criteria) this;
        }

        public Criteria andUltimoErrorLessThan(Date value) {
            addCriterion("ULTIMO_ERROR <", value, "ultimoError");
            return (Criteria) this;
        }

        public Criteria andUltimoErrorLessThanOrEqualTo(Date value) {
            addCriterion("ULTIMO_ERROR <=", value, "ultimoError");
            return (Criteria) this;
        }

        public Criteria andUltimoErrorIn(List<Date> values) {
            addCriterion("ULTIMO_ERROR in", values, "ultimoError");
            return (Criteria) this;
        }

        public Criteria andUltimoErrorNotIn(List<Date> values) {
            addCriterion("ULTIMO_ERROR not in", values, "ultimoError");
            return (Criteria) this;
        }

        public Criteria andUltimoErrorBetween(Date value1, Date value2) {
            addCriterion("ULTIMO_ERROR between", value1, value2, "ultimoError");
            return (Criteria) this;
        }

        public Criteria andUltimoErrorNotBetween(Date value1, Date value2) {
            addCriterion("ULTIMO_ERROR not between", value1, value2, "ultimoError");
            return (Criteria) this;
        }

        public Criteria andUltimoDocumentoIsNull() {
            addCriterion("ULTIMO_DOCUMENTO is null");
            return (Criteria) this;
        }

        public Criteria andUltimoDocumentoIsNotNull() {
            addCriterion("ULTIMO_DOCUMENTO is not null");
            return (Criteria) this;
        }

        public Criteria andUltimoDocumentoEqualTo(String value) {
            addCriterion("ULTIMO_DOCUMENTO =", value, "ultimoDocumento");
            return (Criteria) this;
        }

        public Criteria andUltimoDocumentoNotEqualTo(String value) {
            addCriterion("ULTIMO_DOCUMENTO <>", value, "ultimoDocumento");
            return (Criteria) this;
        }

        public Criteria andUltimoDocumentoGreaterThan(String value) {
            addCriterion("ULTIMO_DOCUMENTO >", value, "ultimoDocumento");
            return (Criteria) this;
        }

        public Criteria andUltimoDocumentoGreaterThanOrEqualTo(String value) {
            addCriterion("ULTIMO_DOCUMENTO >=", value, "ultimoDocumento");
            return (Criteria) this;
        }

        public Criteria andUltimoDocumentoLessThan(String value) {
            addCriterion("ULTIMO_DOCUMENTO <", value, "ultimoDocumento");
            return (Criteria) this;
        }

        public Criteria andUltimoDocumentoLessThanOrEqualTo(String value) {
            addCriterion("ULTIMO_DOCUMENTO <=", value, "ultimoDocumento");
            return (Criteria) this;
        }

        public Criteria andUltimoDocumentoLike(String value) {
            addCriterion("ULTIMO_DOCUMENTO like", value, "ultimoDocumento");
            return (Criteria) this;
        }

        public Criteria andUltimoDocumentoNotLike(String value) {
            addCriterion("ULTIMO_DOCUMENTO not like", value, "ultimoDocumento");
            return (Criteria) this;
        }

        public Criteria andUltimoDocumentoIn(List<String> values) {
            addCriterion("ULTIMO_DOCUMENTO in", values, "ultimoDocumento");
            return (Criteria) this;
        }

        public Criteria andUltimoDocumentoNotIn(List<String> values) {
            addCriterion("ULTIMO_DOCUMENTO not in", values, "ultimoDocumento");
            return (Criteria) this;
        }

        public Criteria andUltimoDocumentoBetween(String value1, String value2) {
            addCriterion("ULTIMO_DOCUMENTO between", value1, value2, "ultimoDocumento");
            return (Criteria) this;
        }

        public Criteria andUltimoDocumentoNotBetween(String value1, String value2) {
            addCriterion("ULTIMO_DOCUMENTO not between", value1, value2, "ultimoDocumento");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeIsNull() {
            addCriterion("ULTIMO_MENSAJE is null");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeIsNotNull() {
            addCriterion("ULTIMO_MENSAJE is not null");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeEqualTo(String value) {
            addCriterion("ULTIMO_MENSAJE =", value, "ultimoMensaje");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeNotEqualTo(String value) {
            addCriterion("ULTIMO_MENSAJE <>", value, "ultimoMensaje");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeGreaterThan(String value) {
            addCriterion("ULTIMO_MENSAJE >", value, "ultimoMensaje");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeGreaterThanOrEqualTo(String value) {
            addCriterion("ULTIMO_MENSAJE >=", value, "ultimoMensaje");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeLessThan(String value) {
            addCriterion("ULTIMO_MENSAJE <", value, "ultimoMensaje");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeLessThanOrEqualTo(String value) {
            addCriterion("ULTIMO_MENSAJE <=", value, "ultimoMensaje");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeLike(String value) {
            addCriterion("ULTIMO_MENSAJE like", value, "ultimoMensaje");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeNotLike(String value) {
            addCriterion("ULTIMO_MENSAJE not like", value, "ultimoMensaje");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeIn(List<String> values) {
            addCriterion("ULTIMO_MENSAJE in", values, "ultimoMensaje");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeNotIn(List<String> values) {
            addCriterion("ULTIMO_MENSAJE not in", values, "ultimoMensaje");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeBetween(String value1, String value2) {
            addCriterion("ULTIMO_MENSAJE between", value1, value2, "ultimoMensaje");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeNotBetween(String value1, String value2) {
            addCriterion("ULTIMO_MENSAJE not between", value1, value2, "ultimoMensaje");
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

        public Criteria andTiendasImplicadasIsNull() {
            addCriterion("TIENDAS_IMPLICADAS is null");
            return (Criteria) this;
        }

        public Criteria andTiendasImplicadasIsNotNull() {
            addCriterion("TIENDAS_IMPLICADAS is not null");
            return (Criteria) this;
        }

        public Criteria andTiendasImplicadasEqualTo(Short value) {
            addCriterion("TIENDAS_IMPLICADAS =", value, "tiendasImplicadas");
            return (Criteria) this;
        }

        public Criteria andTiendasImplicadasNotEqualTo(Short value) {
            addCriterion("TIENDAS_IMPLICADAS <>", value, "tiendasImplicadas");
            return (Criteria) this;
        }

        public Criteria andTiendasImplicadasGreaterThan(Short value) {
            addCriterion("TIENDAS_IMPLICADAS >", value, "tiendasImplicadas");
            return (Criteria) this;
        }

        public Criteria andTiendasImplicadasGreaterThanOrEqualTo(Short value) {
            addCriterion("TIENDAS_IMPLICADAS >=", value, "tiendasImplicadas");
            return (Criteria) this;
        }

        public Criteria andTiendasImplicadasLessThan(Short value) {
            addCriterion("TIENDAS_IMPLICADAS <", value, "tiendasImplicadas");
            return (Criteria) this;
        }

        public Criteria andTiendasImplicadasLessThanOrEqualTo(Short value) {
            addCriterion("TIENDAS_IMPLICADAS <=", value, "tiendasImplicadas");
            return (Criteria) this;
        }

        public Criteria andTiendasImplicadasIn(List<Short> values) {
            addCriterion("TIENDAS_IMPLICADAS in", values, "tiendasImplicadas");
            return (Criteria) this;
        }

        public Criteria andTiendasImplicadasNotIn(List<Short> values) {
            addCriterion("TIENDAS_IMPLICADAS not in", values, "tiendasImplicadas");
            return (Criteria) this;
        }

        public Criteria andTiendasImplicadasBetween(Short value1, Short value2) {
            addCriterion("TIENDAS_IMPLICADAS between", value1, value2, "tiendasImplicadas");
            return (Criteria) this;
        }

        public Criteria andTiendasImplicadasNotBetween(Short value1, Short value2) {
            addCriterion("TIENDAS_IMPLICADAS not between", value1, value2, "tiendasImplicadas");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeTrazaIsNull() {
            addCriterion("ULTIMO_MENSAJE_TRAZA is null");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeTrazaIsNotNull() {
            addCriterion("ULTIMO_MENSAJE_TRAZA is not null");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeTrazaEqualTo(String value) {
            addCriterion("ULTIMO_MENSAJE_TRAZA =", value, "ultimoMensajeTraza");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeTrazaNotEqualTo(String value) {
            addCriterion("ULTIMO_MENSAJE_TRAZA <>", value, "ultimoMensajeTraza");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeTrazaGreaterThan(String value) {
            addCriterion("ULTIMO_MENSAJE_TRAZA >", value, "ultimoMensajeTraza");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeTrazaGreaterThanOrEqualTo(String value) {
            addCriterion("ULTIMO_MENSAJE_TRAZA >=", value, "ultimoMensajeTraza");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeTrazaLessThan(String value) {
            addCriterion("ULTIMO_MENSAJE_TRAZA <", value, "ultimoMensajeTraza");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeTrazaLessThanOrEqualTo(String value) {
            addCriterion("ULTIMO_MENSAJE_TRAZA <=", value, "ultimoMensajeTraza");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeTrazaLike(String value) {
            addCriterion("ULTIMO_MENSAJE_TRAZA like", value, "ultimoMensajeTraza");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeTrazaNotLike(String value) {
            addCriterion("ULTIMO_MENSAJE_TRAZA not like", value, "ultimoMensajeTraza");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeTrazaIn(List<String> values) {
            addCriterion("ULTIMO_MENSAJE_TRAZA in", values, "ultimoMensajeTraza");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeTrazaNotIn(List<String> values) {
            addCriterion("ULTIMO_MENSAJE_TRAZA not in", values, "ultimoMensajeTraza");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeTrazaBetween(String value1, String value2) {
            addCriterion("ULTIMO_MENSAJE_TRAZA between", value1, value2, "ultimoMensajeTraza");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeTrazaNotBetween(String value1, String value2) {
            addCriterion("ULTIMO_MENSAJE_TRAZA not between", value1, value2, "ultimoMensajeTraza");
            return (Criteria) this;
        }

        public Criteria andUidActividadLikeInsensitive(String value) {
            addCriterion("upper(UID_ACTIVIDAD) like", value.toUpperCase(), "uidActividad");
            return (Criteria) this;
        }

        public Criteria andUidErrorLikeInsensitive(String value) {
            addCriterion("upper(UID_ERROR) like", value.toUpperCase(), "uidError");
            return (Criteria) this;
        }

        public Criteria andIdClaseLikeInsensitive(String value) {
            addCriterion("upper(ID_CLASE) like", value.toUpperCase(), "idClase");
            return (Criteria) this;
        }

        public Criteria andIdObjetoLikeInsensitive(String value) {
            addCriterion("upper(ID_OBJETO) like", value.toUpperCase(), "idObjeto");
            return (Criteria) this;
        }

        public Criteria andUltimoDocumentoLikeInsensitive(String value) {
            addCriterion("upper(ULTIMO_DOCUMENTO) like", value.toUpperCase(), "ultimoDocumento");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeLikeInsensitive(String value) {
            addCriterion("upper(ULTIMO_MENSAJE) like", value.toUpperCase(), "ultimoMensaje");
            return (Criteria) this;
        }

        public Criteria andUltimoMensajeTrazaLikeInsensitive(String value) {
            addCriterion("upper(ULTIMO_MENSAJE_TRAZA) like", value.toUpperCase(), "ultimoMensajeTraza");
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