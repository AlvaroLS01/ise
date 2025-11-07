package com.comerzzia.bimbaylola.pos.persistence.pais.provincias;

import java.util.ArrayList;
import java.util.List;

public class ProvinciasExample{

	public static final String ORDER_BY_UID_INSTANCIA = "UID_INSTANCIA";

	public static final String ORDER_BY_UID_INSTANCIA_DESC = "UID_INSTANCIA DESC";

	public static final String ORDER_BY_CODPROVINCIA = "CODPROVINCIA";

	public static final String ORDER_BY_CODPROVINCIA_DESC = "CODPROVINCIA DESC";

	public static final String ORDER_BY_CODPAIS = "CODPAIS";

	public static final String ORDER_BY_CODPAIS_DESC = "CODPAIS DESC";

	public static final String ORDER_BY_DESPROVINCIA = "DESPROVINCIA";

	public static final String ORDER_BY_DESPROVINCIA_DESC = "DESPROVINCIA DESC";

	protected String orderByClause;

	protected boolean distinct;

	protected List<Criteria> oredCriteria;

	public ProvinciasExample(){
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

		public Criteria andUidInstanciaIsNull(){
			addCriterion("UID_INSTANCIA is null");
			return (Criteria) this;
		}

		public Criteria andUidInstanciaIsNotNull(){
			addCriterion("UID_INSTANCIA is not null");
			return (Criteria) this;
		}

		public Criteria andUidInstanciaEqualTo(String value){
			addCriterion("UID_INSTANCIA =", value, "uidInstancia");
			return (Criteria) this;
		}

		public Criteria andUidInstanciaNotEqualTo(String value){
			addCriterion("UID_INSTANCIA <>", value, "uidInstancia");
			return (Criteria) this;
		}

		public Criteria andUidInstanciaGreaterThan(String value){
			addCriterion("UID_INSTANCIA >", value, "uidInstancia");
			return (Criteria) this;
		}

		public Criteria andUidInstanciaGreaterThanOrEqualTo(String value){
			addCriterion("UID_INSTANCIA >=", value, "uidInstancia");
			return (Criteria) this;
		}

		public Criteria andUidInstanciaLessThan(String value){
			addCriterion("UID_INSTANCIA <", value, "uidInstancia");
			return (Criteria) this;
		}

		public Criteria andUidInstanciaLessThanOrEqualTo(String value){
			addCriterion("UID_INSTANCIA <=", value, "uidInstancia");
			return (Criteria) this;
		}

		public Criteria andUidInstanciaLike(String value){
			addCriterion("UID_INSTANCIA like", value, "uidInstancia");
			return (Criteria) this;
		}

		public Criteria andUidInstanciaNotLike(String value){
			addCriterion("UID_INSTANCIA not like", value, "uidInstancia");
			return (Criteria) this;
		}

		public Criteria andUidInstanciaIn(List<String> values){
			addCriterion("UID_INSTANCIA in", values, "uidInstancia");
			return (Criteria) this;
		}

		public Criteria andUidInstanciaNotIn(List<String> values){
			addCriterion("UID_INSTANCIA not in", values, "uidInstancia");
			return (Criteria) this;
		}

		public Criteria andUidInstanciaBetween(String value1, String value2){
			addCriterion("UID_INSTANCIA between", value1, value2, "uidInstancia");
			return (Criteria) this;
		}

		public Criteria andUidInstanciaNotBetween(String value1, String value2){
			addCriterion("UID_INSTANCIA not between", value1, value2, "uidInstancia");
			return (Criteria) this;
		}

		public Criteria andCodprovinciaIsNull(){
			addCriterion("CODPROVINCIA is null");
			return (Criteria) this;
		}

		public Criteria andCodprovinciaIsNotNull(){
			addCriterion("CODPROVINCIA is not null");
			return (Criteria) this;
		}

		public Criteria andCodprovinciaEqualTo(String value){
			addCriterion("CODPROVINCIA =", value, "codprovincia");
			return (Criteria) this;
		}

		public Criteria andCodprovinciaNotEqualTo(String value){
			addCriterion("CODPROVINCIA <>", value, "codprovincia");
			return (Criteria) this;
		}

		public Criteria andCodprovinciaGreaterThan(String value){
			addCriterion("CODPROVINCIA >", value, "codprovincia");
			return (Criteria) this;
		}

		public Criteria andCodprovinciaGreaterThanOrEqualTo(String value){
			addCriterion("CODPROVINCIA >=", value, "codprovincia");
			return (Criteria) this;
		}

		public Criteria andCodprovinciaLessThan(String value){
			addCriterion("CODPROVINCIA <", value, "codprovincia");
			return (Criteria) this;
		}

		public Criteria andCodprovinciaLessThanOrEqualTo(String value){
			addCriterion("CODPROVINCIA <=", value, "codprovincia");
			return (Criteria) this;
		}

		public Criteria andCodprovinciaLike(String value){
			addCriterion("CODPROVINCIA like", value, "codprovincia");
			return (Criteria) this;
		}

		public Criteria andCodprovinciaNotLike(String value){
			addCriterion("CODPROVINCIA not like", value, "codprovincia");
			return (Criteria) this;
		}

		public Criteria andCodprovinciaIn(List<String> values){
			addCriterion("CODPROVINCIA in", values, "codprovincia");
			return (Criteria) this;
		}

		public Criteria andCodprovinciaNotIn(List<String> values){
			addCriterion("CODPROVINCIA not in", values, "codprovincia");
			return (Criteria) this;
		}

		public Criteria andCodprovinciaBetween(String value1, String value2){
			addCriterion("CODPROVINCIA between", value1, value2, "codprovincia");
			return (Criteria) this;
		}

		public Criteria andCodprovinciaNotBetween(String value1, String value2){
			addCriterion("CODPROVINCIA not between", value1, value2, "codprovincia");
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

		public Criteria andDesprovinciaIsNull(){
			addCriterion("DESPROVINCIA is null");
			return (Criteria) this;
		}

		public Criteria andDesprovinciaIsNotNull(){
			addCriterion("DESPROVINCIA is not null");
			return (Criteria) this;
		}

		public Criteria andDesprovinciaEqualTo(String value){
			addCriterion("DESPROVINCIA =", value, "desprovincia");
			return (Criteria) this;
		}

		public Criteria andDesprovinciaNotEqualTo(String value){
			addCriterion("DESPROVINCIA <>", value, "desprovincia");
			return (Criteria) this;
		}

		public Criteria andDesprovinciaGreaterThan(String value){
			addCriterion("DESPROVINCIA >", value, "desprovincia");
			return (Criteria) this;
		}

		public Criteria andDesprovinciaGreaterThanOrEqualTo(String value){
			addCriterion("DESPROVINCIA >=", value, "desprovincia");
			return (Criteria) this;
		}

		public Criteria andDesprovinciaLessThan(String value){
			addCriterion("DESPROVINCIA <", value, "desprovincia");
			return (Criteria) this;
		}

		public Criteria andDesprovinciaLessThanOrEqualTo(String value){
			addCriterion("DESPROVINCIA <=", value, "desprovincia");
			return (Criteria) this;
		}

		public Criteria andDesprovinciaLike(String value){
			addCriterion("DESPROVINCIA like", value, "desprovincia");
			return (Criteria) this;
		}

		public Criteria andDesprovinciaNotLike(String value){
			addCriterion("DESPROVINCIA not like", value, "desprovincia");
			return (Criteria) this;
		}

		public Criteria andDesprovinciaIn(List<String> values){
			addCriterion("DESPROVINCIA in", values, "desprovincia");
			return (Criteria) this;
		}

		public Criteria andDesprovinciaNotIn(List<String> values){
			addCriterion("DESPROVINCIA not in", values, "desprovincia");
			return (Criteria) this;
		}

		public Criteria andDesprovinciaBetween(String value1, String value2){
			addCriterion("DESPROVINCIA between", value1, value2, "desprovincia");
			return (Criteria) this;
		}

		public Criteria andDesprovinciaNotBetween(String value1, String value2){
			addCriterion("DESPROVINCIA not between", value1, value2, "desprovincia");
			return (Criteria) this;
		}

		public Criteria andUidInstanciaLikeInsensitive(String value){
			addCriterion("upper(UID_INSTANCIA) like", value.toUpperCase(), "uidInstancia");
			return (Criteria) this;
		}

		public Criteria andCodprovinciaLikeInsensitive(String value){
			addCriterion("upper(CODPROVINCIA) like", value.toUpperCase(), "codprovincia");
			return (Criteria) this;
		}

		public Criteria andCodpaisLikeInsensitive(String value){
			addCriterion("upper(CODPAIS) like", value.toUpperCase(), "codpais");
			return (Criteria) this;
		}

		public Criteria andDesprovinciaLikeInsensitive(String value){
			addCriterion("upper(DESPROVINCIA) like", value.toUpperCase(), "desprovincia");
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