package com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa;

import java.util.ArrayList;
import java.util.List;

public class AvisoInformativoExample{

	public static final String ORDER_BY_UID_ACTIVIDAD = "UID_ACTIVIDAD";

	public static final String ORDER_BY_UID_ACTIVIDAD_DESC = "UID_ACTIVIDAD DESC";

	public static final String ORDER_BY_CODPAIS = "CODPAIS";

	public static final String ORDER_BY_CODPAIS_DESC = "CODPAIS DESC";

	public static final String ORDER_BY_CODIGO = "CODIGO";

	public static final String ORDER_BY_CODIGO_DESC = "CODIGO DESC";

	public static final String ORDER_BY_DESCRIPCION = "DESCRIPCION";

	public static final String ORDER_BY_DESCRIPCION_DESC = "DESCRIPCION DESC";

	public static final String ORDER_BY_FECHA = "FECHA";

	public static final String ORDER_BY_FECHA_DESC = "FECHA DESC";

	public static final String ORDER_BY_TEXTO = "TEXTO";

	public static final String ORDER_BY_TEXTO_DESC = "TEXTO DESC";

	public static final String ORDER_BY_DOCU_INDEPE = "DOCU_INDEPE";

	public static final String ORDER_BY_DOCU_INDEPE_DESC = "DOCU_INDEPE DESC";

	public static final String ORDER_BY_COPIAS = "COPIAS";

	public static final String ORDER_BY_COPIAS_DESC = "COPIAS DESC";

	protected String orderByClause;

	protected boolean distinct;

	protected List<Criteria> oredCriteria;

	public AvisoInformativoExample(){
		oredCriteria = new ArrayList<Criteria>();
	}

	public void setOrderByClause(String orderByClause){
		this.orderByClause = orderByClause;
	}

	public String getOrderByClause(){
		return orderByClause;
	}

	public void setDistinct(boolean distinct){
		this.distinct = distinct;
	}

	public boolean isDistinct(){
		return distinct;
	}

	public List<Criteria> getOredCriteria(){
		return oredCriteria;
	}

	public void or(Criteria criteria){
		oredCriteria.add(criteria);
	}

	public Criteria or(){
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	public Criteria createCriteria(){
		Criteria criteria = createCriteriaInternal();
		if(oredCriteria.size() == 0){
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	protected Criteria createCriteriaInternal(){
		Criteria criteria = new Criteria();
		return criteria;
	}

	public void clear(){
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	protected abstract static class GeneratedCriteria{

		protected List<Criterion> criteria;

		protected GeneratedCriteria(){
			super();
			criteria = new ArrayList<Criterion>();
		}

		public boolean isValid(){
			return criteria.size() > 0;
		}

		public List<Criterion> getAllCriteria(){
			return criteria;
		}

		public List<Criterion> getCriteria(){
			return criteria;
		}

		protected void addCriterion(String condition){
			if(condition != null){
				criteria.add(new Criterion(condition));
			}
		}

		protected void addCriterion(String condition, Object value, String property){
			if(value != null){
				criteria.add(new Criterion(condition, value));
			}
		}

		protected void addCriterion(String condition, Object value1, Object value2, String property){
			if(value1 != null && value2 != null){
				criteria.add(new Criterion(condition, value1, value2));
			}
		}

		public Criteria andUidActividadIsNull(){
			addCriterion("UID_ACTIVIDAD is null");
			return (Criteria) this;
		}

		public Criteria andUidActividadIsNotNull(){
			addCriterion("UID_ACTIVIDAD is not null");
			return (Criteria) this;
		}

		public Criteria andUidActividadEqualTo(String value){
			addCriterion("UID_ACTIVIDAD =", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadNotEqualTo(String value){
			addCriterion("UID_ACTIVIDAD <>", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadGreaterThan(String value){
			addCriterion("UID_ACTIVIDAD >", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadGreaterThanOrEqualTo(String value){
			addCriterion("UID_ACTIVIDAD >=", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadLessThan(String value){
			addCriterion("UID_ACTIVIDAD <", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadLessThanOrEqualTo(String value){
			addCriterion("UID_ACTIVIDAD <=", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadLike(String value){
			addCriterion("UID_ACTIVIDAD like", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadNotLike(String value){
			addCriterion("UID_ACTIVIDAD not like", value, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadIn(List<String> values){
			addCriterion("UID_ACTIVIDAD in", values, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadNotIn(List<String> values){
			addCriterion("UID_ACTIVIDAD not in", values, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadBetween(String value1, String value2){
			addCriterion("UID_ACTIVIDAD between", value1, value2, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andUidActividadNotBetween(String value1, String value2){
			addCriterion("UID_ACTIVIDAD not between", value1, value2, "uidActividad");
			return (Criteria) this;
		}

		public Criteria andCodpaisIsNull(){
			addCriterion("CODPAIS is null");
			return (Criteria) this;
		}

		public Criteria andCodpaisIsNotNull(){
			addCriterion("CODPAIS is not null");
			return (Criteria) this;
		}

		public Criteria andCodpaisEqualTo(String value){
			addCriterion("CODPAIS =", value, "codpais");
			return (Criteria) this;
		}

		public Criteria andCodpaisNotEqualTo(String value){
			addCriterion("CODPAIS <>", value, "codpais");
			return (Criteria) this;
		}

		public Criteria andCodpaisGreaterThan(String value){
			addCriterion("CODPAIS >", value, "codpais");
			return (Criteria) this;
		}

		public Criteria andCodpaisGreaterThanOrEqualTo(String value){
			addCriterion("CODPAIS >=", value, "codpais");
			return (Criteria) this;
		}

		public Criteria andCodpaisLessThan(String value){
			addCriterion("CODPAIS <", value, "codpais");
			return (Criteria) this;
		}

		public Criteria andCodpaisLessThanOrEqualTo(String value){
			addCriterion("CODPAIS <=", value, "codpais");
			return (Criteria) this;
		}

		public Criteria andCodpaisLike(String value){
			addCriterion("CODPAIS like", value, "codpais");
			return (Criteria) this;
		}

		public Criteria andCodpaisNotLike(String value){
			addCriterion("CODPAIS not like", value, "codpais");
			return (Criteria) this;
		}

		public Criteria andCodpaisIn(List<String> values){
			addCriterion("CODPAIS in", values, "codpais");
			return (Criteria) this;
		}

		public Criteria andCodpaisNotIn(List<String> values){
			addCriterion("CODPAIS not in", values, "codpais");
			return (Criteria) this;
		}

		public Criteria andCodpaisBetween(String value1, String value2){
			addCriterion("CODPAIS between", value1, value2, "codpais");
			return (Criteria) this;
		}

		public Criteria andCodpaisNotBetween(String value1, String value2){
			addCriterion("CODPAIS not between", value1, value2, "codpais");
			return (Criteria) this;
		}

		public Criteria andCodigoIsNull(){
			addCriterion("CODIGO is null");
			return (Criteria) this;
		}

		public Criteria andCodigoIsNotNull(){
			addCriterion("CODIGO is not null");
			return (Criteria) this;
		}

		public Criteria andCodigoEqualTo(String value){
			addCriterion("CODIGO =", value, "codigo");
			return (Criteria) this;
		}

		public Criteria andCodigoNotEqualTo(String value){
			addCriterion("CODIGO <>", value, "codigo");
			return (Criteria) this;
		}

		public Criteria andCodigoGreaterThan(String value){
			addCriterion("CODIGO >", value, "codigo");
			return (Criteria) this;
		}

		public Criteria andCodigoGreaterThanOrEqualTo(String value){
			addCriterion("CODIGO >=", value, "codigo");
			return (Criteria) this;
		}

		public Criteria andCodigoLessThan(String value){
			addCriterion("CODIGO <", value, "codigo");
			return (Criteria) this;
		}

		public Criteria andCodigoLessThanOrEqualTo(String value){
			addCriterion("CODIGO <=", value, "codigo");
			return (Criteria) this;
		}

		public Criteria andCodigoLike(String value){
			addCriterion("CODIGO like", value, "codigo");
			return (Criteria) this;
		}

		public Criteria andCodigoNotLike(String value){
			addCriterion("CODIGO not like", value, "codigo");
			return (Criteria) this;
		}

		public Criteria andCodigoIn(List<String> values){
			addCriterion("CODIGO in", values, "codigo");
			return (Criteria) this;
		}

		public Criteria andCodigoNotIn(List<String> values){
			addCriterion("CODIGO not in", values, "codigo");
			return (Criteria) this;
		}

		public Criteria andCodigoBetween(String value1, String value2){
			addCriterion("CODIGO between", value1, value2, "codigo");
			return (Criteria) this;
		}

		public Criteria andCodigoNotBetween(String value1, String value2){
			addCriterion("CODIGO not between", value1, value2, "codigo");
			return (Criteria) this;
		}

		public Criteria andDescripcionIsNull(){
			addCriterion("DESCRIPCION is null");
			return (Criteria) this;
		}

		public Criteria andDescripcionIsNotNull(){
			addCriterion("DESCRIPCION is not null");
			return (Criteria) this;
		}

		public Criteria andDescripcionEqualTo(String value){
			addCriterion("DESCRIPCION =", value, "descripcion");
			return (Criteria) this;
		}

		public Criteria andDescripcionNotEqualTo(String value){
			addCriterion("DESCRIPCION <>", value, "descripcion");
			return (Criteria) this;
		}

		public Criteria andDescripcionGreaterThan(String value){
			addCriterion("DESCRIPCION >", value, "descripcion");
			return (Criteria) this;
		}

		public Criteria andDescripcionGreaterThanOrEqualTo(String value){
			addCriterion("DESCRIPCION >=", value, "descripcion");
			return (Criteria) this;
		}

		public Criteria andDescripcionLessThan(String value){
			addCriterion("DESCRIPCION <", value, "descripcion");
			return (Criteria) this;
		}

		public Criteria andDescripcionLessThanOrEqualTo(String value){
			addCriterion("DESCRIPCION <=", value, "descripcion");
			return (Criteria) this;
		}

		public Criteria andDescripcionLike(String value){
			addCriterion("DESCRIPCION like", value, "descripcion");
			return (Criteria) this;
		}

		public Criteria andDescripcionNotLike(String value){
			addCriterion("DESCRIPCION not like", value, "descripcion");
			return (Criteria) this;
		}

		public Criteria andDescripcionIn(List<String> values){
			addCriterion("DESCRIPCION in", values, "descripcion");
			return (Criteria) this;
		}

		public Criteria andDescripcionNotIn(List<String> values){
			addCriterion("DESCRIPCION not in", values, "descripcion");
			return (Criteria) this;
		}

		public Criteria andDescripcionBetween(String value1, String value2){
			addCriterion("DESCRIPCION between", value1, value2, "descripcion");
			return (Criteria) this;
		}

		public Criteria andDescripcionNotBetween(String value1, String value2){
			addCriterion("DESCRIPCION not between", value1, value2, "descripcion");
			return (Criteria) this;
		}

		public Criteria andFechaIsNull(){
			addCriterion("FECHA is null");
			return (Criteria) this;
		}

		public Criteria andFechaIsNotNull(){
			addCriterion("FECHA is not null");
			return (Criteria) this;
		}

		public Criteria andFechaEqualTo(String value){
			addCriterion("FECHA =", value, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaNotEqualTo(String value){
			addCriterion("FECHA <>", value, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaGreaterThan(String value){
			addCriterion("FECHA >", value, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaGreaterThanOrEqualTo(String value){
			addCriterion("FECHA >=", value, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaLessThan(String value){
			addCriterion("FECHA <", value, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaLessThanOrEqualTo(String value){
			addCriterion("FECHA <=", value, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaLike(String value){
			addCriterion("FECHA like", value, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaNotLike(String value){
			addCriterion("FECHA not like", value, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaIn(List<String> values){
			addCriterion("FECHA in", values, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaNotIn(List<String> values){
			addCriterion("FECHA not in", values, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaBetween(String value1, String value2){
			addCriterion("FECHA between", value1, value2, "fecha");
			return (Criteria) this;
		}

		public Criteria andFechaNotBetween(String value1, String value2){
			addCriterion("FECHA not between", value1, value2, "fecha");
			return (Criteria) this;
		}

		public Criteria andTextoIsNull(){
			addCriterion("TEXTO is null");
			return (Criteria) this;
		}

		public Criteria andTextoIsNotNull(){
			addCriterion("TEXTO is not null");
			return (Criteria) this;
		}

		public Criteria andTextoEqualTo(String value){
			addCriterion("TEXTO =", value, "texto");
			return (Criteria) this;
		}

		public Criteria andTextoNotEqualTo(String value){
			addCriterion("TEXTO <>", value, "texto");
			return (Criteria) this;
		}

		public Criteria andTextoGreaterThan(String value){
			addCriterion("TEXTO >", value, "texto");
			return (Criteria) this;
		}

		public Criteria andTextoGreaterThanOrEqualTo(String value){
			addCriterion("TEXTO >=", value, "texto");
			return (Criteria) this;
		}

		public Criteria andTextoLessThan(String value){
			addCriterion("TEXTO <", value, "texto");
			return (Criteria) this;
		}

		public Criteria andTextoLessThanOrEqualTo(String value){
			addCriterion("TEXTO <=", value, "texto");
			return (Criteria) this;
		}

		public Criteria andTextoLike(String value){
			addCriterion("TEXTO like", value, "texto");
			return (Criteria) this;
		}

		public Criteria andTextoNotLike(String value){
			addCriterion("TEXTO not like", value, "texto");
			return (Criteria) this;
		}

		public Criteria andTextoIn(List<String> values){
			addCriterion("TEXTO in", values, "texto");
			return (Criteria) this;
		}

		public Criteria andTextoNotIn(List<String> values){
			addCriterion("TEXTO not in", values, "texto");
			return (Criteria) this;
		}

		public Criteria andTextoBetween(String value1, String value2){
			addCriterion("TEXTO between", value1, value2, "texto");
			return (Criteria) this;
		}

		public Criteria andTextoNotBetween(String value1, String value2){
			addCriterion("TEXTO not between", value1, value2, "texto");
			return (Criteria) this;
		}

		public Criteria andDocuIndepeIsNull(){
			addCriterion("DOCU_INDEPE is null");
			return (Criteria) this;
		}

		public Criteria andDocuIndepeIsNotNull(){
			addCriterion("DOCU_INDEPE is not null");
			return (Criteria) this;
		}

		public Criteria andDocuIndepeEqualTo(String value){
			addCriterion("DOCU_INDEPE =", value, "docuIndepe");
			return (Criteria) this;
		}

		public Criteria andDocuIndepeNotEqualTo(String value){
			addCriterion("DOCU_INDEPE <>", value, "docuIndepe");
			return (Criteria) this;
		}

		public Criteria andDocuIndepeGreaterThan(String value){
			addCriterion("DOCU_INDEPE >", value, "docuIndepe");
			return (Criteria) this;
		}

		public Criteria andDocuIndepeGreaterThanOrEqualTo(String value){
			addCriterion("DOCU_INDEPE >=", value, "docuIndepe");
			return (Criteria) this;
		}

		public Criteria andDocuIndepeLessThan(String value){
			addCriterion("DOCU_INDEPE <", value, "docuIndepe");
			return (Criteria) this;
		}

		public Criteria andDocuIndepeLessThanOrEqualTo(String value){
			addCriterion("DOCU_INDEPE <=", value, "docuIndepe");
			return (Criteria) this;
		}

		public Criteria andDocuIndepeLike(String value){
			addCriterion("DOCU_INDEPE like", value, "docuIndepe");
			return (Criteria) this;
		}

		public Criteria andDocuIndepeNotLike(String value){
			addCriterion("DOCU_INDEPE not like", value, "docuIndepe");
			return (Criteria) this;
		}

		public Criteria andDocuIndepeIn(List<String> values){
			addCriterion("DOCU_INDEPE in", values, "docuIndepe");
			return (Criteria) this;
		}

		public Criteria andDocuIndepeNotIn(List<String> values){
			addCriterion("DOCU_INDEPE not in", values, "docuIndepe");
			return (Criteria) this;
		}

		public Criteria andDocuIndepeBetween(String value1, String value2){
			addCriterion("DOCU_INDEPE between", value1, value2, "docuIndepe");
			return (Criteria) this;
		}

		public Criteria andDocuIndepeNotBetween(String value1, String value2){
			addCriterion("DOCU_INDEPE not between", value1, value2, "docuIndepe");
			return (Criteria) this;
		}

		public Criteria andCopiasIsNull(){
			addCriterion("COPIAS is null");
			return (Criteria) this;
		}

		public Criteria andCopiasIsNotNull(){
			addCriterion("COPIAS is not null");
			return (Criteria) this;
		}

		public Criteria andCopiasEqualTo(Long value){
			addCriterion("COPIAS =", value, "copias");
			return (Criteria) this;
		}

		public Criteria andCopiasNotEqualTo(Long value){
			addCriterion("COPIAS <>", value, "copias");
			return (Criteria) this;
		}

		public Criteria andCopiasGreaterThan(Long value){
			addCriterion("COPIAS >", value, "copias");
			return (Criteria) this;
		}

		public Criteria andCopiasGreaterThanOrEqualTo(Long value){
			addCriterion("COPIAS >=", value, "copias");
			return (Criteria) this;
		}

		public Criteria andCopiasLessThan(Long value){
			addCriterion("COPIAS <", value, "copias");
			return (Criteria) this;
		}

		public Criteria andCopiasLessThanOrEqualTo(Long value){
			addCriterion("COPIAS <=", value, "copias");
			return (Criteria) this;
		}

		public Criteria andCopiasIn(List<Long> values){
			addCriterion("COPIAS in", values, "copias");
			return (Criteria) this;
		}

		public Criteria andCopiasNotIn(List<Long> values){
			addCriterion("COPIAS not in", values, "copias");
			return (Criteria) this;
		}

		public Criteria andCopiasBetween(Long value1, Long value2){
			addCriterion("COPIAS between", value1, value2, "copias");
			return (Criteria) this;
		}

		public Criteria andCopiasNotBetween(Long value1, Long value2){
			addCriterion("COPIAS not between", value1, value2, "copias");
			return (Criteria) this;
		}

		public Criteria andUidActividadLikeInsensitive(String value){
			addCriterion("upper(UID_ACTIVIDAD) like", value.toUpperCase(), "uidActividad");
			return (Criteria) this;
		}

		public Criteria andCodpaisLikeInsensitive(String value){
			addCriterion("upper(CODPAIS) like", value.toUpperCase(), "codpais");
			return (Criteria) this;
		}

		public Criteria andCodigoLikeInsensitive(String value){
			addCriterion("upper(CODIGO) like", value.toUpperCase(), "codigo");
			return (Criteria) this;
		}

		public Criteria andDescripcionLikeInsensitive(String value){
			addCriterion("upper(DESCRIPCION) like", value.toUpperCase(), "descripcion");
			return (Criteria) this;
		}

		public Criteria andFechaLikeInsensitive(String value){
			addCriterion("upper(FECHA) like", value.toUpperCase(), "fecha");
			return (Criteria) this;
		}

		public Criteria andTextoLikeInsensitive(String value){
			addCriterion("upper(TEXTO) like", value.toUpperCase(), "texto");
			return (Criteria) this;
		}

		public Criteria andDocuIndepeLikeInsensitive(String value){
			addCriterion("upper(DOCU_INDEPE) like", value.toUpperCase(), "docuIndepe");
			return (Criteria) this;
		}
	}

	public static class Criteria extends GeneratedCriteria{

		protected Criteria(){
			super();
		}
	}

	public static class Criterion{

		private String condition;

		private Object value;

		private Object secondValue;

		private boolean noValue;

		private boolean singleValue;

		private boolean betweenValue;

		private boolean listValue;

		private String typeHandler;

		public String getCondition(){
			return condition;
		}

		public Object getValue(){
			return value;
		}

		public Object getSecondValue(){
			return secondValue;
		}

		public boolean isNoValue(){
			return noValue;
		}

		public boolean isSingleValue(){
			return singleValue;
		}

		public boolean isBetweenValue(){
			return betweenValue;
		}

		public boolean isListValue(){
			return listValue;
		}

		public String getTypeHandler(){
			return typeHandler;
		}

		protected Criterion(String condition){
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler){
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if(value instanceof List<?>){
				this.listValue = true;
			}
			else{
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value){
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue, String typeHandler){
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue){
			this(condition, value, secondValue, null);
		}
	}
	
}