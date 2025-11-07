package com.comerzzia.bimbaylola.pos.persistence.core.config.configcontadores;

import java.util.ArrayList;
import java.util.List;

public class ByLConfigContadorExample {
    public static final String ORDER_BY_ID_CONTADOR = "ID_CONTADOR";

    public static final String ORDER_BY_ID_CONTADOR_DESC = "ID_CONTADOR DESC";

    public static final String ORDER_BY_DESCRIPCION = "DESCRIPCION";

    public static final String ORDER_BY_DESCRIPCION_DESC = "DESCRIPCION DESC";

    public static final String ORDER_BY_MASCARA_DIVISOR1 = "MASCARA_DIVISOR1";

    public static final String ORDER_BY_MASCARA_DIVISOR1_DESC = "MASCARA_DIVISOR1 DESC";

    public static final String ORDER_BY_MASCARA_DIVISOR2 = "MASCARA_DIVISOR2";

    public static final String ORDER_BY_MASCARA_DIVISOR2_DESC = "MASCARA_DIVISOR2 DESC";

    public static final String ORDER_BY_MASCARA_DIVISOR3 = "MASCARA_DIVISOR3";

    public static final String ORDER_BY_MASCARA_DIVISOR3_DESC = "MASCARA_DIVISOR3 DESC";

    public static final String ORDER_BY_LONGITUD_MAXIMA = "LONGITUD_MAXIMA";

    public static final String ORDER_BY_LONGITUD_MAXIMA_DESC = "LONGITUD_MAXIMA DESC";

    public static final String ORDER_BY_DIRECCION_RELLENO = "DIRECCION_RELLENO";

    public static final String ORDER_BY_DIRECCION_RELLENO_DESC = "DIRECCION_RELLENO DESC";

    public static final String ORDER_BY_CARACTER_RELLENO = "CARACTER_RELLENO";

    public static final String ORDER_BY_CARACTER_RELLENO_DESC = "CARACTER_RELLENO DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ByLConfigContadorExample() {
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

        public Criteria andDescripcionIsNull() {
            addCriterion("DESCRIPCION is null");
            return (Criteria) this;
        }

        public Criteria andDescripcionIsNotNull() {
            addCriterion("DESCRIPCION is not null");
            return (Criteria) this;
        }

        public Criteria andDescripcionEqualTo(String value) {
            addCriterion("DESCRIPCION =", value, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionNotEqualTo(String value) {
            addCriterion("DESCRIPCION <>", value, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionGreaterThan(String value) {
            addCriterion("DESCRIPCION >", value, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionGreaterThanOrEqualTo(String value) {
            addCriterion("DESCRIPCION >=", value, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionLessThan(String value) {
            addCriterion("DESCRIPCION <", value, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionLessThanOrEqualTo(String value) {
            addCriterion("DESCRIPCION <=", value, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionLike(String value) {
            addCriterion("DESCRIPCION like", value, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionNotLike(String value) {
            addCriterion("DESCRIPCION not like", value, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionIn(List<String> values) {
            addCriterion("DESCRIPCION in", values, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionNotIn(List<String> values) {
            addCriterion("DESCRIPCION not in", values, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionBetween(String value1, String value2) {
            addCriterion("DESCRIPCION between", value1, value2, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionNotBetween(String value1, String value2) {
            addCriterion("DESCRIPCION not between", value1, value2, "descripcion");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor1IsNull() {
            addCriterion("MASCARA_DIVISOR1 is null");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor1IsNotNull() {
            addCriterion("MASCARA_DIVISOR1 is not null");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor1EqualTo(String value) {
            addCriterion("MASCARA_DIVISOR1 =", value, "mascaraDivisor1");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor1NotEqualTo(String value) {
            addCriterion("MASCARA_DIVISOR1 <>", value, "mascaraDivisor1");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor1GreaterThan(String value) {
            addCriterion("MASCARA_DIVISOR1 >", value, "mascaraDivisor1");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor1GreaterThanOrEqualTo(String value) {
            addCriterion("MASCARA_DIVISOR1 >=", value, "mascaraDivisor1");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor1LessThan(String value) {
            addCriterion("MASCARA_DIVISOR1 <", value, "mascaraDivisor1");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor1LessThanOrEqualTo(String value) {
            addCriterion("MASCARA_DIVISOR1 <=", value, "mascaraDivisor1");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor1Like(String value) {
            addCriterion("MASCARA_DIVISOR1 like", value, "mascaraDivisor1");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor1NotLike(String value) {
            addCriterion("MASCARA_DIVISOR1 not like", value, "mascaraDivisor1");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor1In(List<String> values) {
            addCriterion("MASCARA_DIVISOR1 in", values, "mascaraDivisor1");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor1NotIn(List<String> values) {
            addCriterion("MASCARA_DIVISOR1 not in", values, "mascaraDivisor1");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor1Between(String value1, String value2) {
            addCriterion("MASCARA_DIVISOR1 between", value1, value2, "mascaraDivisor1");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor1NotBetween(String value1, String value2) {
            addCriterion("MASCARA_DIVISOR1 not between", value1, value2, "mascaraDivisor1");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor2IsNull() {
            addCriterion("MASCARA_DIVISOR2 is null");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor2IsNotNull() {
            addCriterion("MASCARA_DIVISOR2 is not null");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor2EqualTo(String value) {
            addCriterion("MASCARA_DIVISOR2 =", value, "mascaraDivisor2");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor2NotEqualTo(String value) {
            addCriterion("MASCARA_DIVISOR2 <>", value, "mascaraDivisor2");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor2GreaterThan(String value) {
            addCriterion("MASCARA_DIVISOR2 >", value, "mascaraDivisor2");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor2GreaterThanOrEqualTo(String value) {
            addCriterion("MASCARA_DIVISOR2 >=", value, "mascaraDivisor2");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor2LessThan(String value) {
            addCriterion("MASCARA_DIVISOR2 <", value, "mascaraDivisor2");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor2LessThanOrEqualTo(String value) {
            addCriterion("MASCARA_DIVISOR2 <=", value, "mascaraDivisor2");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor2Like(String value) {
            addCriterion("MASCARA_DIVISOR2 like", value, "mascaraDivisor2");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor2NotLike(String value) {
            addCriterion("MASCARA_DIVISOR2 not like", value, "mascaraDivisor2");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor2In(List<String> values) {
            addCriterion("MASCARA_DIVISOR2 in", values, "mascaraDivisor2");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor2NotIn(List<String> values) {
            addCriterion("MASCARA_DIVISOR2 not in", values, "mascaraDivisor2");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor2Between(String value1, String value2) {
            addCriterion("MASCARA_DIVISOR2 between", value1, value2, "mascaraDivisor2");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor2NotBetween(String value1, String value2) {
            addCriterion("MASCARA_DIVISOR2 not between", value1, value2, "mascaraDivisor2");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor3IsNull() {
            addCriterion("MASCARA_DIVISOR3 is null");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor3IsNotNull() {
            addCriterion("MASCARA_DIVISOR3 is not null");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor3EqualTo(String value) {
            addCriterion("MASCARA_DIVISOR3 =", value, "mascaraDivisor3");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor3NotEqualTo(String value) {
            addCriterion("MASCARA_DIVISOR3 <>", value, "mascaraDivisor3");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor3GreaterThan(String value) {
            addCriterion("MASCARA_DIVISOR3 >", value, "mascaraDivisor3");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor3GreaterThanOrEqualTo(String value) {
            addCriterion("MASCARA_DIVISOR3 >=", value, "mascaraDivisor3");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor3LessThan(String value) {
            addCriterion("MASCARA_DIVISOR3 <", value, "mascaraDivisor3");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor3LessThanOrEqualTo(String value) {
            addCriterion("MASCARA_DIVISOR3 <=", value, "mascaraDivisor3");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor3Like(String value) {
            addCriterion("MASCARA_DIVISOR3 like", value, "mascaraDivisor3");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor3NotLike(String value) {
            addCriterion("MASCARA_DIVISOR3 not like", value, "mascaraDivisor3");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor3In(List<String> values) {
            addCriterion("MASCARA_DIVISOR3 in", values, "mascaraDivisor3");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor3NotIn(List<String> values) {
            addCriterion("MASCARA_DIVISOR3 not in", values, "mascaraDivisor3");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor3Between(String value1, String value2) {
            addCriterion("MASCARA_DIVISOR3 between", value1, value2, "mascaraDivisor3");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor3NotBetween(String value1, String value2) {
            addCriterion("MASCARA_DIVISOR3 not between", value1, value2, "mascaraDivisor3");
            return (Criteria) this;
        }

        public Criteria andLongitudMaximaIsNull() {
            addCriterion("LONGITUD_MAXIMA is null");
            return (Criteria) this;
        }

        public Criteria andLongitudMaximaIsNotNull() {
            addCriterion("LONGITUD_MAXIMA is not null");
            return (Criteria) this;
        }

        public Criteria andLongitudMaximaEqualTo(Short value) {
            addCriterion("LONGITUD_MAXIMA =", value, "longitudMaxima");
            return (Criteria) this;
        }

        public Criteria andLongitudMaximaNotEqualTo(Short value) {
            addCriterion("LONGITUD_MAXIMA <>", value, "longitudMaxima");
            return (Criteria) this;
        }

        public Criteria andLongitudMaximaGreaterThan(Short value) {
            addCriterion("LONGITUD_MAXIMA >", value, "longitudMaxima");
            return (Criteria) this;
        }

        public Criteria andLongitudMaximaGreaterThanOrEqualTo(Short value) {
            addCriterion("LONGITUD_MAXIMA >=", value, "longitudMaxima");
            return (Criteria) this;
        }

        public Criteria andLongitudMaximaLessThan(Short value) {
            addCriterion("LONGITUD_MAXIMA <", value, "longitudMaxima");
            return (Criteria) this;
        }

        public Criteria andLongitudMaximaLessThanOrEqualTo(Short value) {
            addCriterion("LONGITUD_MAXIMA <=", value, "longitudMaxima");
            return (Criteria) this;
        }

        public Criteria andLongitudMaximaIn(List<Short> values) {
            addCriterion("LONGITUD_MAXIMA in", values, "longitudMaxima");
            return (Criteria) this;
        }

        public Criteria andLongitudMaximaNotIn(List<Short> values) {
            addCriterion("LONGITUD_MAXIMA not in", values, "longitudMaxima");
            return (Criteria) this;
        }

        public Criteria andLongitudMaximaBetween(Short value1, Short value2) {
            addCriterion("LONGITUD_MAXIMA between", value1, value2, "longitudMaxima");
            return (Criteria) this;
        }

        public Criteria andLongitudMaximaNotBetween(Short value1, Short value2) {
            addCriterion("LONGITUD_MAXIMA not between", value1, value2, "longitudMaxima");
            return (Criteria) this;
        }

        public Criteria andDireccionRellenoIsNull() {
            addCriterion("DIRECCION_RELLENO is null");
            return (Criteria) this;
        }

        public Criteria andDireccionRellenoIsNotNull() {
            addCriterion("DIRECCION_RELLENO is not null");
            return (Criteria) this;
        }

        public Criteria andDireccionRellenoEqualTo(String value) {
            addCriterion("DIRECCION_RELLENO =", value, "direccionRelleno");
            return (Criteria) this;
        }

        public Criteria andDireccionRellenoNotEqualTo(String value) {
            addCriterion("DIRECCION_RELLENO <>", value, "direccionRelleno");
            return (Criteria) this;
        }

        public Criteria andDireccionRellenoGreaterThan(String value) {
            addCriterion("DIRECCION_RELLENO >", value, "direccionRelleno");
            return (Criteria) this;
        }

        public Criteria andDireccionRellenoGreaterThanOrEqualTo(String value) {
            addCriterion("DIRECCION_RELLENO >=", value, "direccionRelleno");
            return (Criteria) this;
        }

        public Criteria andDireccionRellenoLessThan(String value) {
            addCriterion("DIRECCION_RELLENO <", value, "direccionRelleno");
            return (Criteria) this;
        }

        public Criteria andDireccionRellenoLessThanOrEqualTo(String value) {
            addCriterion("DIRECCION_RELLENO <=", value, "direccionRelleno");
            return (Criteria) this;
        }

        public Criteria andDireccionRellenoLike(String value) {
            addCriterion("DIRECCION_RELLENO like", value, "direccionRelleno");
            return (Criteria) this;
        }

        public Criteria andDireccionRellenoNotLike(String value) {
            addCriterion("DIRECCION_RELLENO not like", value, "direccionRelleno");
            return (Criteria) this;
        }

        public Criteria andDireccionRellenoIn(List<String> values) {
            addCriterion("DIRECCION_RELLENO in", values, "direccionRelleno");
            return (Criteria) this;
        }

        public Criteria andDireccionRellenoNotIn(List<String> values) {
            addCriterion("DIRECCION_RELLENO not in", values, "direccionRelleno");
            return (Criteria) this;
        }

        public Criteria andDireccionRellenoBetween(String value1, String value2) {
            addCriterion("DIRECCION_RELLENO between", value1, value2, "direccionRelleno");
            return (Criteria) this;
        }

        public Criteria andDireccionRellenoNotBetween(String value1, String value2) {
            addCriterion("DIRECCION_RELLENO not between", value1, value2, "direccionRelleno");
            return (Criteria) this;
        }

        public Criteria andCaracterRellenoIsNull() {
            addCriterion("CARACTER_RELLENO is null");
            return (Criteria) this;
        }

        public Criteria andCaracterRellenoIsNotNull() {
            addCriterion("CARACTER_RELLENO is not null");
            return (Criteria) this;
        }

        public Criteria andCaracterRellenoEqualTo(String value) {
            addCriterion("CARACTER_RELLENO =", value, "caracterRelleno");
            return (Criteria) this;
        }

        public Criteria andCaracterRellenoNotEqualTo(String value) {
            addCriterion("CARACTER_RELLENO <>", value, "caracterRelleno");
            return (Criteria) this;
        }

        public Criteria andCaracterRellenoGreaterThan(String value) {
            addCriterion("CARACTER_RELLENO >", value, "caracterRelleno");
            return (Criteria) this;
        }

        public Criteria andCaracterRellenoGreaterThanOrEqualTo(String value) {
            addCriterion("CARACTER_RELLENO >=", value, "caracterRelleno");
            return (Criteria) this;
        }

        public Criteria andCaracterRellenoLessThan(String value) {
            addCriterion("CARACTER_RELLENO <", value, "caracterRelleno");
            return (Criteria) this;
        }

        public Criteria andCaracterRellenoLessThanOrEqualTo(String value) {
            addCriterion("CARACTER_RELLENO <=", value, "caracterRelleno");
            return (Criteria) this;
        }

        public Criteria andCaracterRellenoLike(String value) {
            addCriterion("CARACTER_RELLENO like", value, "caracterRelleno");
            return (Criteria) this;
        }

        public Criteria andCaracterRellenoNotLike(String value) {
            addCriterion("CARACTER_RELLENO not like", value, "caracterRelleno");
            return (Criteria) this;
        }

        public Criteria andCaracterRellenoIn(List<String> values) {
            addCriterion("CARACTER_RELLENO in", values, "caracterRelleno");
            return (Criteria) this;
        }

        public Criteria andCaracterRellenoNotIn(List<String> values) {
            addCriterion("CARACTER_RELLENO not in", values, "caracterRelleno");
            return (Criteria) this;
        }

        public Criteria andCaracterRellenoBetween(String value1, String value2) {
            addCriterion("CARACTER_RELLENO between", value1, value2, "caracterRelleno");
            return (Criteria) this;
        }

        public Criteria andCaracterRellenoNotBetween(String value1, String value2) {
            addCriterion("CARACTER_RELLENO not between", value1, value2, "caracterRelleno");
            return (Criteria) this;
        }

        public Criteria andIdContadorLikeInsensitive(String value) {
            addCriterion("upper(ID_CONTADOR) like", value.toUpperCase(), "idContador");
            return (Criteria) this;
        }

        public Criteria andDescripcionLikeInsensitive(String value) {
            addCriterion("upper(DESCRIPCION) like", value.toUpperCase(), "descripcion");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor1LikeInsensitive(String value) {
            addCriterion("upper(MASCARA_DIVISOR1) like", value.toUpperCase(), "mascaraDivisor1");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor2LikeInsensitive(String value) {
            addCriterion("upper(MASCARA_DIVISOR2) like", value.toUpperCase(), "mascaraDivisor2");
            return (Criteria) this;
        }

        public Criteria andMascaraDivisor3LikeInsensitive(String value) {
            addCriterion("upper(MASCARA_DIVISOR3) like", value.toUpperCase(), "mascaraDivisor3");
            return (Criteria) this;
        }

        public Criteria andDireccionRellenoLikeInsensitive(String value) {
            addCriterion("upper(DIRECCION_RELLENO) like", value.toUpperCase(), "direccionRelleno");
            return (Criteria) this;
        }

        public Criteria andCaracterRellenoLikeInsensitive(String value) {
            addCriterion("upper(CARACTER_RELLENO) like", value.toUpperCase(), "caracterRelleno");
            return (Criteria) this;
        }
        
        public Criteria andCodTipoDocumentoEqualTo(String value) {
            addCriterion("T.CODTIPODOCUMENTO =", value, "codTipoDocumento");
            return (Criteria) this;
        }
        
        public Criteria andCodPaisEqualTo(String value) {
            addCriterion("T.CODPAIS =", value, "codpais");
            return (Criteria) this;
        }
        
        public Criteria andIdContadorJoinedEqualTo(String value) {
            addCriterion("A.ID_CONTADOR =", value, "idContador");
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