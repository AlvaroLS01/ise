package com.comerzzia.bimbaylola.pos.persistence.reservas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReservasPagoGiftCardExample{

	public static final String ORDER_BY_UID_ACTIVIDAD = "UID_ACTIVIDAD";

	public static final String ORDER_BY_UID_ACTIVIDAD_DESC = "UID_ACTIVIDAD DESC";

	public static final String ORDER_BY_ID_CLIE_ALBARAN = "ID_CLIE_ALBARAN";

	public static final String ORDER_BY_ID_CLIE_ALBARAN_DESC = "ID_CLIE_ALBARAN DESC";

	public static final String ORDER_BY_LINEA = "LINEA";

	public static final String ORDER_BY_LINEA_DESC = "LINEA DESC";

	public static final String ORDER_BY_NUMERO_TARJETA = "NUMERO_TARJETA";

	public static final String ORDER_BY_NUMERO_TARJETA_DESC = "NUMERO_TARJETA DESC";

	public static final String ORDER_BY_SALDO = "SALDO";

	public static final String ORDER_BY_SALDO_DESC = "SALDO DESC";

	public static final String ORDER_BY_SALDO_PROVISIONAL = "SALDO_PROVISIONAL";

	public static final String ORDER_BY_SALDO_PROVISIONAL_DESC = "SALDO_PROVISIONAL DESC";

	public static final String ORDER_BY_UID_TRANSACCION = "UID_TRANSACCION";

	public static final String ORDER_BY_UID_TRANSACCION_DESC = "UID_TRANSACCION DESC";

	public static final String ORDER_BY_IMPORTE_PAGO = "IMPORTE_PAGO";

	public static final String ORDER_BY_IMPORTE_PAGO_DESC = "IMPORTE_PAGO DESC";

	protected String orderByClause;

	protected boolean distinct;

	protected List<Criteria> oredCriteria;

	public ReservasPagoGiftCardExample(){
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

		public Criteria andIdClieAlbaranIsNull(){
			addCriterion("ID_CLIE_ALBARAN is null");
			return (Criteria) this;
		}

		public Criteria andIdClieAlbaranIsNotNull(){
			addCriterion("ID_CLIE_ALBARAN is not null");
			return (Criteria) this;
		}

		public Criteria andIdClieAlbaranEqualTo(Long value){
			addCriterion("ID_CLIE_ALBARAN =", value, "idClieAlbaran");
			return (Criteria) this;
		}

		public Criteria andIdClieAlbaranNotEqualTo(Long value){
			addCriterion("ID_CLIE_ALBARAN <>", value, "idClieAlbaran");
			return (Criteria) this;
		}

		public Criteria andIdClieAlbaranGreaterThan(Long value){
			addCriterion("ID_CLIE_ALBARAN >", value, "idClieAlbaran");
			return (Criteria) this;
		}

		public Criteria andIdClieAlbaranGreaterThanOrEqualTo(Long value){
			addCriterion("ID_CLIE_ALBARAN >=", value, "idClieAlbaran");
			return (Criteria) this;
		}

		public Criteria andIdClieAlbaranLessThan(Long value){
			addCriterion("ID_CLIE_ALBARAN <", value, "idClieAlbaran");
			return (Criteria) this;
		}

		public Criteria andIdClieAlbaranLessThanOrEqualTo(Long value){
			addCriterion("ID_CLIE_ALBARAN <=", value, "idClieAlbaran");
			return (Criteria) this;
		}

		public Criteria andIdClieAlbaranIn(List<Long> values){
			addCriterion("ID_CLIE_ALBARAN in", values, "idClieAlbaran");
			return (Criteria) this;
		}

		public Criteria andIdClieAlbaranNotIn(List<Long> values){
			addCriterion("ID_CLIE_ALBARAN not in", values, "idClieAlbaran");
			return (Criteria) this;
		}

		public Criteria andIdClieAlbaranBetween(Long value1, Long value2){
			addCriterion("ID_CLIE_ALBARAN between", value1, value2, "idClieAlbaran");
			return (Criteria) this;
		}

		public Criteria andIdClieAlbaranNotBetween(Long value1, Long value2){
			addCriterion("ID_CLIE_ALBARAN not between", value1, value2, "idClieAlbaran");
			return (Criteria) this;
		}

		public Criteria andLineaIsNull(){
			addCriterion("LINEA is null");
			return (Criteria) this;
		}

		public Criteria andLineaIsNotNull(){
			addCriterion("LINEA is not null");
			return (Criteria) this;
		}

		public Criteria andLineaEqualTo(Integer value){
			addCriterion("LINEA =", value, "linea");
			return (Criteria) this;
		}

		public Criteria andLineaNotEqualTo(Integer value){
			addCriterion("LINEA <>", value, "linea");
			return (Criteria) this;
		}

		public Criteria andLineaGreaterThan(Integer value){
			addCriterion("LINEA >", value, "linea");
			return (Criteria) this;
		}

		public Criteria andLineaGreaterThanOrEqualTo(Integer value){
			addCriterion("LINEA >=", value, "linea");
			return (Criteria) this;
		}

		public Criteria andLineaLessThan(Integer value){
			addCriterion("LINEA <", value, "linea");
			return (Criteria) this;
		}

		public Criteria andLineaLessThanOrEqualTo(Integer value){
			addCriterion("LINEA <=", value, "linea");
			return (Criteria) this;
		}

		public Criteria andLineaIn(List<Integer> values){
			addCriterion("LINEA in", values, "linea");
			return (Criteria) this;
		}

		public Criteria andLineaNotIn(List<Integer> values){
			addCriterion("LINEA not in", values, "linea");
			return (Criteria) this;
		}

		public Criteria andLineaBetween(Integer value1, Integer value2){
			addCriterion("LINEA between", value1, value2, "linea");
			return (Criteria) this;
		}

		public Criteria andLineaNotBetween(Integer value1, Integer value2){
			addCriterion("LINEA not between", value1, value2, "linea");
			return (Criteria) this;
		}

		public Criteria andNumeroTarjetaIsNull(){
			addCriterion("NUMERO_TARJETA is null");
			return (Criteria) this;
		}

		public Criteria andNumeroTarjetaIsNotNull(){
			addCriterion("NUMERO_TARJETA is not null");
			return (Criteria) this;
		}

		public Criteria andNumeroTarjetaEqualTo(String value){
			addCriterion("NUMERO_TARJETA =", value, "numeroTarjeta");
			return (Criteria) this;
		}

		public Criteria andNumeroTarjetaNotEqualTo(String value){
			addCriterion("NUMERO_TARJETA <>", value, "numeroTarjeta");
			return (Criteria) this;
		}

		public Criteria andNumeroTarjetaGreaterThan(String value){
			addCriterion("NUMERO_TARJETA >", value, "numeroTarjeta");
			return (Criteria) this;
		}

		public Criteria andNumeroTarjetaGreaterThanOrEqualTo(String value){
			addCriterion("NUMERO_TARJETA >=", value, "numeroTarjeta");
			return (Criteria) this;
		}

		public Criteria andNumeroTarjetaLessThan(String value){
			addCriterion("NUMERO_TARJETA <", value, "numeroTarjeta");
			return (Criteria) this;
		}

		public Criteria andNumeroTarjetaLessThanOrEqualTo(String value){
			addCriterion("NUMERO_TARJETA <=", value, "numeroTarjeta");
			return (Criteria) this;
		}

		public Criteria andNumeroTarjetaLike(String value){
			addCriterion("NUMERO_TARJETA like", value, "numeroTarjeta");
			return (Criteria) this;
		}

		public Criteria andNumeroTarjetaNotLike(String value){
			addCriterion("NUMERO_TARJETA not like", value, "numeroTarjeta");
			return (Criteria) this;
		}

		public Criteria andNumeroTarjetaIn(List<String> values){
			addCriterion("NUMERO_TARJETA in", values, "numeroTarjeta");
			return (Criteria) this;
		}

		public Criteria andNumeroTarjetaNotIn(List<String> values){
			addCriterion("NUMERO_TARJETA not in", values, "numeroTarjeta");
			return (Criteria) this;
		}

		public Criteria andNumeroTarjetaBetween(String value1, String value2){
			addCriterion("NUMERO_TARJETA between", value1, value2, "numeroTarjeta");
			return (Criteria) this;
		}

		public Criteria andNumeroTarjetaNotBetween(String value1, String value2){
			addCriterion("NUMERO_TARJETA not between", value1, value2, "numeroTarjeta");
			return (Criteria) this;
		}

		public Criteria andSaldoIsNull(){
			addCriterion("SALDO is null");
			return (Criteria) this;
		}

		public Criteria andSaldoIsNotNull(){
			addCriterion("SALDO is not null");
			return (Criteria) this;
		}

		public Criteria andSaldoEqualTo(BigDecimal value){
			addCriterion("SALDO =", value, "saldo");
			return (Criteria) this;
		}

		public Criteria andSaldoNotEqualTo(BigDecimal value){
			addCriterion("SALDO <>", value, "saldo");
			return (Criteria) this;
		}

		public Criteria andSaldoGreaterThan(BigDecimal value){
			addCriterion("SALDO >", value, "saldo");
			return (Criteria) this;
		}

		public Criteria andSaldoGreaterThanOrEqualTo(BigDecimal value){
			addCriterion("SALDO >=", value, "saldo");
			return (Criteria) this;
		}

		public Criteria andSaldoLessThan(BigDecimal value){
			addCriterion("SALDO <", value, "saldo");
			return (Criteria) this;
		}

		public Criteria andSaldoLessThanOrEqualTo(BigDecimal value){
			addCriterion("SALDO <=", value, "saldo");
			return (Criteria) this;
		}

		public Criteria andSaldoIn(List<BigDecimal> values){
			addCriterion("SALDO in", values, "saldo");
			return (Criteria) this;
		}

		public Criteria andSaldoNotIn(List<BigDecimal> values){
			addCriterion("SALDO not in", values, "saldo");
			return (Criteria) this;
		}

		public Criteria andSaldoBetween(BigDecimal value1, BigDecimal value2){
			addCriterion("SALDO between", value1, value2, "saldo");
			return (Criteria) this;
		}

		public Criteria andSaldoNotBetween(BigDecimal value1, BigDecimal value2){
			addCriterion("SALDO not between", value1, value2, "saldo");
			return (Criteria) this;
		}

		public Criteria andSaldoProvisionalIsNull(){
			addCriterion("SALDO_PROVISIONAL is null");
			return (Criteria) this;
		}

		public Criteria andSaldoProvisionalIsNotNull(){
			addCriterion("SALDO_PROVISIONAL is not null");
			return (Criteria) this;
		}

		public Criteria andSaldoProvisionalEqualTo(BigDecimal value){
			addCriterion("SALDO_PROVISIONAL =", value, "saldoProvisional");
			return (Criteria) this;
		}

		public Criteria andSaldoProvisionalNotEqualTo(BigDecimal value){
			addCriterion("SALDO_PROVISIONAL <>", value, "saldoProvisional");
			return (Criteria) this;
		}

		public Criteria andSaldoProvisionalGreaterThan(BigDecimal value){
			addCriterion("SALDO_PROVISIONAL >", value, "saldoProvisional");
			return (Criteria) this;
		}

		public Criteria andSaldoProvisionalGreaterThanOrEqualTo(BigDecimal value){
			addCriterion("SALDO_PROVISIONAL >=", value, "saldoProvisional");
			return (Criteria) this;
		}

		public Criteria andSaldoProvisionalLessThan(BigDecimal value){
			addCriterion("SALDO_PROVISIONAL <", value, "saldoProvisional");
			return (Criteria) this;
		}

		public Criteria andSaldoProvisionalLessThanOrEqualTo(BigDecimal value){
			addCriterion("SALDO_PROVISIONAL <=", value, "saldoProvisional");
			return (Criteria) this;
		}

		public Criteria andSaldoProvisionalIn(List<BigDecimal> values){
			addCriterion("SALDO_PROVISIONAL in", values, "saldoProvisional");
			return (Criteria) this;
		}

		public Criteria andSaldoProvisionalNotIn(List<BigDecimal> values){
			addCriterion("SALDO_PROVISIONAL not in", values, "saldoProvisional");
			return (Criteria) this;
		}

		public Criteria andSaldoProvisionalBetween(BigDecimal value1, BigDecimal value2){
			addCriterion("SALDO_PROVISIONAL between", value1, value2, "saldoProvisional");
			return (Criteria) this;
		}

		public Criteria andSaldoProvisionalNotBetween(BigDecimal value1, BigDecimal value2){
			addCriterion("SALDO_PROVISIONAL not between", value1, value2, "saldoProvisional");
			return (Criteria) this;
		}

		public Criteria andUidTransaccionIsNull(){
			addCriterion("UID_TRANSACCION is null");
			return (Criteria) this;
		}

		public Criteria andUidTransaccionIsNotNull(){
			addCriterion("UID_TRANSACCION is not null");
			return (Criteria) this;
		}

		public Criteria andUidTransaccionEqualTo(String value){
			addCriterion("UID_TRANSACCION =", value, "uidTransaccion");
			return (Criteria) this;
		}

		public Criteria andUidTransaccionNotEqualTo(String value){
			addCriterion("UID_TRANSACCION <>", value, "uidTransaccion");
			return (Criteria) this;
		}

		public Criteria andUidTransaccionGreaterThan(String value){
			addCriterion("UID_TRANSACCION >", value, "uidTransaccion");
			return (Criteria) this;
		}

		public Criteria andUidTransaccionGreaterThanOrEqualTo(String value){
			addCriterion("UID_TRANSACCION >=", value, "uidTransaccion");
			return (Criteria) this;
		}

		public Criteria andUidTransaccionLessThan(String value){
			addCriterion("UID_TRANSACCION <", value, "uidTransaccion");
			return (Criteria) this;
		}

		public Criteria andUidTransaccionLessThanOrEqualTo(String value){
			addCriterion("UID_TRANSACCION <=", value, "uidTransaccion");
			return (Criteria) this;
		}

		public Criteria andUidTransaccionLike(String value){
			addCriterion("UID_TRANSACCION like", value, "uidTransaccion");
			return (Criteria) this;
		}

		public Criteria andUidTransaccionNotLike(String value){
			addCriterion("UID_TRANSACCION not like", value, "uidTransaccion");
			return (Criteria) this;
		}

		public Criteria andUidTransaccionIn(List<String> values){
			addCriterion("UID_TRANSACCION in", values, "uidTransaccion");
			return (Criteria) this;
		}

		public Criteria andUidTransaccionNotIn(List<String> values){
			addCriterion("UID_TRANSACCION not in", values, "uidTransaccion");
			return (Criteria) this;
		}

		public Criteria andUidTransaccionBetween(String value1, String value2){
			addCriterion("UID_TRANSACCION between", value1, value2, "uidTransaccion");
			return (Criteria) this;
		}

		public Criteria andUidTransaccionNotBetween(String value1, String value2){
			addCriterion("UID_TRANSACCION not between", value1, value2, "uidTransaccion");
			return (Criteria) this;
		}

		public Criteria andImportePagoIsNull(){
			addCriterion("IMPORTE_PAGO is null");
			return (Criteria) this;
		}

		public Criteria andImportePagoIsNotNull(){
			addCriterion("IMPORTE_PAGO is not null");
			return (Criteria) this;
		}

		public Criteria andImportePagoEqualTo(BigDecimal value){
			addCriterion("IMPORTE_PAGO =", value, "importePago");
			return (Criteria) this;
		}

		public Criteria andImportePagoNotEqualTo(BigDecimal value){
			addCriterion("IMPORTE_PAGO <>", value, "importePago");
			return (Criteria) this;
		}

		public Criteria andImportePagoGreaterThan(BigDecimal value){
			addCriterion("IMPORTE_PAGO >", value, "importePago");
			return (Criteria) this;
		}

		public Criteria andImportePagoGreaterThanOrEqualTo(BigDecimal value){
			addCriterion("IMPORTE_PAGO >=", value, "importePago");
			return (Criteria) this;
		}

		public Criteria andImportePagoLessThan(BigDecimal value){
			addCriterion("IMPORTE_PAGO <", value, "importePago");
			return (Criteria) this;
		}

		public Criteria andImportePagoLessThanOrEqualTo(BigDecimal value){
			addCriterion("IMPORTE_PAGO <=", value, "importePago");
			return (Criteria) this;
		}

		public Criteria andImportePagoIn(List<BigDecimal> values){
			addCriterion("IMPORTE_PAGO in", values, "importePago");
			return (Criteria) this;
		}

		public Criteria andImportePagoNotIn(List<BigDecimal> values){
			addCriterion("IMPORTE_PAGO not in", values, "importePago");
			return (Criteria) this;
		}

		public Criteria andImportePagoBetween(BigDecimal value1, BigDecimal value2){
			addCriterion("IMPORTE_PAGO between", value1, value2, "importePago");
			return (Criteria) this;
		}

		public Criteria andImportePagoNotBetween(BigDecimal value1, BigDecimal value2){
			addCriterion("IMPORTE_PAGO not between", value1, value2, "importePago");
			return (Criteria) this;
		}

		public Criteria andUidActividadLikeInsensitive(String value){
			addCriterion("upper(UID_ACTIVIDAD) like", value.toUpperCase(), "uidActividad");
			return (Criteria) this;
		}

		public Criteria andNumeroTarjetaLikeInsensitive(String value){
			addCriterion("upper(NUMERO_TARJETA) like", value.toUpperCase(), "numeroTarjeta");
			return (Criteria) this;
		}

		public Criteria andUidTransaccionLikeInsensitive(String value){
			addCriterion("upper(UID_TRANSACCION) like", value.toUpperCase(), "uidTransaccion");
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