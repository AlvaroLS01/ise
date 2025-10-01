package com.comerzzia.bricodepot.backoffice.services.ventas.informetto;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.backoffice.persistence.ventas.informetto.InformeTtoVentas;
import com.comerzzia.bricodepot.backoffice.persistence.ventas.informetto.InformeTtoVentasKey;
import com.comerzzia.bricodepot.backoffice.persistence.ventas.informetto.InformeTtoVentasMapper;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.mybatis.session.SqlSession;

public class ServicioVentasInformeTto {

	protected Logger log = Logger.getLogger(ServicioVentasInformeTto.class);
	private static final String COD_CAJA_COFRE = "80";
	private static final String COD_CAJA_COFRE_RETIRADA = "90";

	public static final String ANTICIPO_ESTADO_LIQUIDADO = "LIQUIDADO";
	protected static ServicioVentasInformeTto instance;

	public static ServicioVentasInformeTto get() {
		if (instance != null) {
			return instance;
		}
		else {
			return new ServicioVentasInformeTto();
		}
	}

	/**
	 * Insertamos por primera vez la venta del informe TTO
	 *
	 * @param ventaTto
	 * @return insertado
	 */
	public boolean insertarInformeTtoVenta(InformeTtoVentas ventaTto, Connection conn) {
		boolean insertado = false;
		InformeTtoVentasMapper mapper = conn.getSqlSession().getMapper(InformeTtoVentasMapper.class);
		try {
			if (consultaExistenciaInformeTToVenta(ventaTto, conn) == null) {
				if (mapper.insertSelective(ventaTto) > 0)
					insertado = true;
			}
		}
		catch (Exception e) {
			log.error("insertarInformeTtoVenta() -  Error insertando el informe de venta TTO", e);
		}
		return insertado;
	}

	/**
	 * Consultamos si existe el InformeTtoVentas
	 *
	 * @param ventaTto
	 * @return InformeTtoVentas
	 */
	private InformeTtoVentas consultaExistenciaInformeTToVenta(InformeTtoVentas ventaTto, Connection conn) {
		try {
			InformeTtoVentasMapper mapper = conn.getSqlSession().getMapper(InformeTtoVentasMapper.class);
			InformeTtoVentasKey key = new InformeTtoVentasKey();
			key.setCodalm(ventaTto.getCodalm());
			key.setCodcaja(ventaTto.getCodcaja());
			key.setFecha(ventaTto.getFecha());
			key.setUidActividad(ventaTto.getUidActividad());
			InformeTtoVentas informeEncontrado = mapper.selectByPrimaryKey(key);
			if (informeEncontrado != null) {
				return informeEncontrado;
			}
		}
		catch (Exception e) {
			log.error("consultaExistenciaInformeTToVenta() -  Error consultando el informe de venta TTO", e);
		}
		return null;
	}
	
	/**
	 * Consultamos si existe el InformeTtoVentas
	 *
	 * @param ventaTto
	 * @return InformeTtoVentas
	 */
	private InformeTtoVentas consultaExistenciaInformeTToVenta(InformeTtoVentas ventaTto, SqlSession sqlSession) {
		try {
			InformeTtoVentasMapper mapper = sqlSession.getMapper(InformeTtoVentasMapper.class);
			InformeTtoVentasKey key = new InformeTtoVentasKey();
			key.setCodalm(ventaTto.getCodalm());
			key.setCodcaja(ventaTto.getCodcaja());
			key.setFecha(ventaTto.getFecha());
			key.setUidActividad(ventaTto.getUidActividad());
			InformeTtoVentas informeEncontrado = mapper.selectByPrimaryKey(key);
			if (informeEncontrado != null) {
				return informeEncontrado;
			}
		}
		catch (Exception e) {
			log.error("consultaExistenciaInformeTToVenta() -  Error consultando el informe de venta TTO", e);
		}
		return null;
	}

	/**
	 * Método para inicializar todos los campos nulos de tipo BigDecimal en un objeto dado.
	 *
	 * @param objeto
	 */
	private void inicializarCamposNulos(Object objeto) {
		try {
			for (Method getter : objeto.getClass().getMethods()) {
				if (getter.getName().startsWith("get") && getter.getReturnType().equals(BigDecimal.class)) {
					BigDecimal valor = (BigDecimal) getter.invoke(objeto);
					if (valor == null) {
						String setterName = getter.getName().replaceFirst("get", "set");
						Method setter = objeto.getClass().getMethod(setterName, BigDecimal.class);
						setter.invoke(objeto, BigDecimal.ZERO);
					}
				}
			}
		}
		catch (Exception e) {
			throw new RuntimeException("Error al inicializar campos nulos", e);
		}
	}

	/**
	 * Actualizamos el informe TTO de venta con los datos correspondientes
	 *
	 * @param uidActividad
	 * @param codalm
	 * @param codcaja
	 * @param fecha
	 * @param connection
	 */
	public void actualizarInformeTtoVenta(String uidActividad, String codalm, String codcaja, String fecha, Connection conn) {
        log.debug("actualizarInformeTtoVenta() - Actualizando datos del informe TTO en base de datos");
        try {

    		InformeTtoVentasMapper mapper = conn.getSqlSession().getMapper(InformeTtoVentasMapper.class);
            InformeTtoVentas informeNuevo = construirInformeTtoVenta(uidActividad, codalm, codcaja, fecha, mapper);

            InformeTtoVentas informeExistente = consultaExistenciaInformeTToVenta(informeNuevo, conn);

            if (informeExistente != null) {
                mapper.deleteByPrimaryKey(informeExistente);
            }
            mapper.insertSelective(informeNuevo);

        } catch (Exception e) {
            log.error("actualizarInformeTtoVenta() - Ha ocurrido un error actualizando el informe TTO de venta: " + e.getMessage(), e);
        }
    }
	
	
	/**
	 * Actualizamos el informe TTO de venta con los datos correspondientes
	 *
	 * @param uidActividad
	 * @param codalm
	 * @param codcaja
	 * @param fecha
	 * @param sqlsession
	 */
	public void actualizarInformeTtoVenta(String uidActividad, String codalm, String codcaja, String fecha, SqlSession sqlSession) {
        log.debug("actualizarInformeTtoVenta() - Actualizando datos del informe TTO en base de datos");
        try {
            InformeTtoVentasMapper mapper = sqlSession.getMapper(InformeTtoVentasMapper.class);
            InformeTtoVentas informeNuevo = construirInformeTtoVenta(uidActividad, codalm, codcaja, fecha, mapper);

            InformeTtoVentas informeExistente = consultaExistenciaInformeTToVenta(informeNuevo, sqlSession);

            if (informeExistente != null) {
                mapper.deleteByPrimaryKey(informeExistente);
            }
            mapper.insertSelective(informeNuevo);

        } catch (Exception e) {
            log.error("actualizarInformeTtoVenta() - Ha ocurrido un error actualizando el informe TTO de venta: " + e.getMessage(), e);
        }
    }
	
	private InformeTtoVentas construirInformeTtoVenta(String uidActividad, String codalm, String codcaja, String fecha, InformeTtoVentasMapper mapper) throws Exception {
		// Consultas para obtener los datos necesarios
		BigDecimal totalVentas = Optional.ofNullable(mapper.consultarTotalVentas(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal cifraVenta = Optional.ofNullable(mapper.consultarCifraVenta(uidActividad, codcaja, codalm, fecha)).orElse(BigDecimal.ZERO);

		// Inicialización de variables
		BigDecimal saldoInicialCofre = BigDecimal.ZERO;
		BigDecimal saldoFinalCofre = BigDecimal.ZERO;
		BigDecimal compraMoneda = BigDecimal.ZERO;
		BigDecimal cambiosEntrada = BigDecimal.ZERO;
		BigDecimal cambiosSalida = BigDecimal.ZERO;
		BigDecimal saldoInicialCofreRetiradas = BigDecimal.ZERO;
		BigDecimal saldoFinalCofreRetiradas = BigDecimal.ZERO;

		// Si la caja es '80', realizamos las consultas y asignaciones correspondientes
		if (StringUtils.equals(codcaja, COD_CAJA_COFRE)) {
			saldoInicialCofre = Optional.ofNullable(mapper.consultarSaldoInicialCofre(uidActividad, fecha, codalm)).orElse(BigDecimal.ZERO);
			saldoFinalCofre = Optional.ofNullable(mapper.consultarSaldoFinalCofre(uidActividad, fecha, codalm)).orElse(BigDecimal.ZERO);
			compraMoneda = Optional.ofNullable(mapper.consultarCompraMoneda(uidActividad, fecha, codalm)).orElse(BigDecimal.ZERO);
			cambiosEntrada = Optional.ofNullable(mapper.consultarCambiosEntrada(uidActividad, fecha, codalm)).orElse(BigDecimal.ZERO);
			cambiosSalida = Optional.ofNullable(mapper.consultarCambiosSalida(uidActividad, fecha, codalm)).orElse(BigDecimal.ZERO);
		}

		// Si la caja es '90', realizamos las consultas y asignaciones correspondientes
		if (StringUtils.equals(codcaja, COD_CAJA_COFRE_RETIRADA)) {
			saldoInicialCofreRetiradas = Optional.ofNullable(mapper.consultarSaldoInicialCofreRetiradas(uidActividad, fecha, codalm)).orElse(BigDecimal.ZERO);
			saldoFinalCofreRetiradas = Optional.ofNullable(mapper.consultarSaldoFinalCofreRetiradas(uidActividad, fecha, codalm)).orElse(BigDecimal.ZERO);
		}

		BigDecimal pagoProveedor = Optional.ofNullable(mapper.consultarPagoProveedor(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal retirada = Optional.ofNullable(mapper.consultarRetirada(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal efectivo = Optional.ofNullable(mapper.consultarEfectivo(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal tarjeta = Optional.ofNullable(mapper.consultarTarjeta(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal tarjetaManual = Optional.ofNullable(mapper.consultarTarjetaManual(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal esFinanciacion = Optional.ofNullable(mapper.consultarESFinanciacion(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal ptFinanciacion = Optional.ofNullable(mapper.consultarPTFinanciacion(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal transferencia = Optional.ofNullable(mapper.consultarTransferencia(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal ecommerce = Optional.ofNullable(mapper.consultarEcommerce(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal valeFlexpoint = Optional.ofNullable(mapper.consultarValeFlexpoint(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal valeComerzzia = Optional.ofNullable(mapper.consultarValeComerzzia(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal giftCard = Optional.ofNullable(mapper.consultarGiftCard(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal tarjetaFidelizado = Optional.ofNullable(mapper.consultarTarjetaFidelizado(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal clienteACuenta = Optional.ofNullable(mapper.consultarClienteACuenta(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal enviadoABanco = Optional.ofNullable(mapper.consultarEnviadoABanco(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal importeIngresadoEnBanco = Optional.ofNullable(mapper.consultarImporteIngresadoEnBanco(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal aportaciones = Optional.ofNullable(mapper.consultarAportaciones(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal desaportaciones = Optional.ofNullable(mapper.consultarDesaportaciones(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal anticiposPagados = Optional.ofNullable(mapper.consultarAnticiposPagados(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);
		BigDecimal anticiposDescontados = Optional.ofNullable(mapper.consultarAnticiposDescontados(uidActividad, fecha, codalm, codcaja)).orElse(BigDecimal.ZERO);

		// Creamos el objeto InformeTtoVentas y asignamos los valores
		InformeTtoVentas ventaTto = new InformeTtoVentas();
		ventaTto.setCodalm(codalm);
		ventaTto.setCodcaja(codcaja);
		ventaTto.setUidActividad(uidActividad);

		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		Date fechaFormateada = formato.parse(fecha);

		ventaTto.setFecha(fechaFormateada);

		InformeTtoVentas informeNuevo = new InformeTtoVentas();
		informeNuevo = new InformeTtoVentas();
		informeNuevo.setCodalm(codalm);
		informeNuevo.setCodcaja(codcaja);
		informeNuevo.setFecha(fechaFormateada);
		informeNuevo.setUidActividad(uidActividad);
		informeNuevo.setKpi(totalVentas);
		informeNuevo.setCifraDeVenta(cifraVenta);

		// Asignamos valores solo si la caja es '80'
		if (StringUtils.equals(codcaja, COD_CAJA_COFRE)) {
			informeNuevo.setSiCofre(saldoInicialCofre);
			informeNuevo.setSfCofre(saldoFinalCofre);
			informeNuevo.setCompraDeMoneda(compraMoneda);
			informeNuevo.setCambiosEntrada(cambiosEntrada);
			informeNuevo.setCambiosSalida(cambiosSalida);
		}
		else {
			// Inicializamos a cero si no es la caja '80'
			informeNuevo.setSiCofre(BigDecimal.ZERO);
			informeNuevo.setSfCofre(BigDecimal.ZERO);
			informeNuevo.setCompraDeMoneda(BigDecimal.ZERO);
			informeNuevo.setCambiosEntrada(BigDecimal.ZERO);
			informeNuevo.setCambiosSalida(BigDecimal.ZERO);
		}

		// Asignamos valores solo si la caja es '90'
		if (StringUtils.equals(codcaja, COD_CAJA_COFRE_RETIRADA)) {
			informeNuevo.setSiCofreRetiradas(saldoInicialCofreRetiradas);
			informeNuevo.setSfCofreRetiradas(saldoFinalCofreRetiradas);
		}
		else {
			// Inicializamos a cero si no es la caja '90'
			informeNuevo.setSiCofreRetiradas(BigDecimal.ZERO);
			informeNuevo.setSfCofreRetiradas(BigDecimal.ZERO);
		}

		// Asignamos los valores comunes a todas las cajas
		informeNuevo.setPagoAProveedor(pagoProveedor);
		informeNuevo.setRetirada(retirada);
		informeNuevo.setEfectivo(efectivo);
		informeNuevo.setTarjeta(tarjeta);
		informeNuevo.setGiftCard(giftCard);
		informeNuevo.setTarjetaFidelizado(tarjetaFidelizado);
		informeNuevo.setTarjetaManual(tarjetaManual);
		informeNuevo.setEsFinanciacion(esFinanciacion);
		informeNuevo.setPtFinanciacion(ptFinanciacion);
		informeNuevo.setTransferencia(transferencia);
		informeNuevo.setEcommerce(ecommerce);
		informeNuevo.setValeComerzzia(valeComerzzia);
		informeNuevo.setValeFlexpoint(valeFlexpoint);
		informeNuevo.setClienteACuenta(clienteACuenta);
		informeNuevo.setEnviadoABanco(enviadoABanco);
		informeNuevo.setImporteIngresadoEnBanco(importeIngresadoEnBanco);
		informeNuevo.setAportaciones(aportaciones);
		informeNuevo.setDesaportaciones(desaportaciones);
		informeNuevo.setAnticiposDescontados(anticiposDescontados);
		informeNuevo.setAnticiposPagados(anticiposPagados);

		// Se inicializan los campos nulos usando el método reflection
		inicializarCamposNulos(informeNuevo);
		return informeNuevo;
	}

}
