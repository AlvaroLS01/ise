package com.comerzzia.bricodepot.inStoreEngine.services.ventas.cajas.cabecera;

import java.util.List;

import com.comerzzia.bricodepot.inStoreEngine.util.CajasConstants;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.model.ventas.cajas.CajaDTO;
import com.comerzzia.model.ventas.cajas.cabecera.CabeceraCaja;
import com.comerzzia.model.ventas.cajas.cabecera.CabeceraCajaExample;
import com.comerzzia.model.ventas.cajas.cabecera.CabeceraCajaKey;
import com.comerzzia.model.ventas.cajas.detalle.DetalleCaja;
import com.comerzzia.model.ventas.cajas.detalle.DetalleCajaExample;
import com.comerzzia.persistencia.ventas.cajas.cabecera.CabeceraCajaMapper;
import com.comerzzia.persistencia.ventas.cajas.detalle.DetalleCajaMapper;
import com.comerzzia.servicios.ventas.cajas.cabecera.CabeceraCajaException;
import com.comerzzia.servicios.ventas.cajas.cabecera.CajaAbiertaNotFoundException;
import com.comerzzia.servicios.ventas.cajas.cabecera.CajaAbiertaUsuarioException;
import com.comerzzia.servicios.ventas.cajas.cabecera.EstadoCajaInvalidoException;
import com.comerzzia.servicios.ventas.cajas.cabecera.ServicioCabeceraCajasImpl;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

public class BricodepotServicioCabeceraCajasImpl extends ServicioCabeceraCajasImpl {

	protected static Logger log = Logger.getLogger(ServicioCabeceraCajasImpl.class);

	protected static BricodepotServicioCabeceraCajasImpl instance;

	public static BricodepotServicioCabeceraCajasImpl get() {
		if (instance == null) {
			instance = new BricodepotServicioCabeceraCajasImpl();
		}
		return instance;
	}

	public static void setCustomInstance(BricodepotServicioCabeceraCajasImpl instance) {
		BricodepotServicioCabeceraCajasImpl.instance = instance;
	}

	@Override
	public CajaDTO recuperarCaja(DatosSesionBean datosSesion, String codalm, String usuario, String codcaja, String codCajaMaster)
	        throws EstadoCajaInvalidoException, CabeceraCajaException, CajaAbiertaNotFoundException {
		SqlSession sqlSession = Database.getSqlSession();
		try {
			log.debug("recuperarCaja() - Recuperando caja para el almacén " + codalm + " y usuario " + usuario);
			mapper = sqlSession.getMapper(CabeceraCajaMapper.class);
			detalleMapper = sqlSession.getMapper(DetalleCajaMapper.class);
			CajaDTO dto = null;
			CabeceraCajaExample exampleCab = new CabeceraCajaExample();
			exampleCab.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andUsuarioEqualTo(usuario).andCodalmEqualTo(codalm).andFechaCierreIsNull();
			List<CabeceraCaja> cabeceras = mapper.selectByExample(exampleCab);

			if (!cabeceras.isEmpty()) {
				CabeceraCaja cabecera = cabeceras.get(0);

				// if (!cabecera.getCodcaja().equals(codCajaMaster) && !cabecera.getCodcaja().equals(codcaja)) {
				if (!cabecera.getCodcaja().equalsIgnoreCase(CajasConstants.PARAM_CODCAJA_APARCADA) && !cabecera.getCodcaja().equals(codcaja)) {
					log.debug("recuperarCaja() - La caja no esta aparcada y se encuentra en la caja " + cabecera.getCodcaja());
					throw new CajaAbiertaUsuarioException(cabecera.getCodcaja());
				}
				else {
					dto = new CajaDTO();
					dto.setCabecera(cabecera);
					DetalleCajaExample exampleDet = new DetalleCajaExample();
					exampleDet.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andUidDiarioCajaEqualTo(cabecera.getUidDiarioCaja());
					List<DetalleCaja> detalles = detalleMapper.selectByExample(exampleDet);
					dto.setDetalles(detalles);
				}
			}
			else {
				log.debug("recuperarCaja() - No se ha encontrado ninguna caja");

			}
			return dto;
		}
		catch (CajaAbiertaUsuarioException e) {
			String msg = "El usuario ya tiene una caja abierta en el terminal " + e.getMessage();
			log.error("recuperarCaja() - " + msg);
			throw new EstadoCajaInvalidoException(msg);
		}
		catch (Exception e) {
			String msg = "Ha ocurrido un error al recuperar la caja " + e.getMessage();
			log.error("recuperarCaja() - " + msg);
			throw new CabeceraCajaException(msg);
		}
		finally {
			sqlSession.close();
		}
	}

	@Override
	public void transferirCaja(IDatosSesion datosSesion, String uidDiarioCaja, CajaDTO cajaDTO, String codCajaMaster)
	        throws CabeceraCajaException, CajaAbiertaUsuarioException, EstadoCajaInvalidoException {
		SqlSession sqlSession = Database.getSqlSession();
		try {
			log.debug("transferirCaja() - Transfiriendo la caja " + cajaDTO.getCabecera().getUidDiarioCaja() + " a la caja " + uidDiarioCaja);
			mapper = sqlSession.getMapper(CabeceraCajaMapper.class);
			detalleMapper = sqlSession.getMapper(DetalleCajaMapper.class);

			CabeceraCajaExample exampleCaja = new CabeceraCajaExample();
			exampleCaja.or().andUidActividadEqualTo(datosSesion.getUidActividad())
					.andCodalmEqualTo(cajaDTO.getCabecera().getCodalm())
					.andUsuarioEqualTo(cajaDTO.getCabecera().getUsuario())
			        .andFechaCierreIsNull();
			List<CabeceraCaja> cajasAbiertas = mapper.selectByExample(exampleCaja);
			if (cajasAbiertas != null && !cajasAbiertas.isEmpty()) {
				if (cajasAbiertas.size() == 1) {
					String codCaja = cajasAbiertas.get(0).getCodcaja();
					if (!codCaja.equals(cajaDTO.getCabecera().getCodcaja())) {
						throw new CajaAbiertaUsuarioException();
					}
				}
				else {
					throw new CajaAbiertaUsuarioException();
				}
			}

			CabeceraCajaKey key = new CabeceraCajaKey();
			key.setUidActividad(datosSesion.getUidActividad());
			key.setUidDiarioCaja(uidDiarioCaja);
			CabeceraCaja cabecera = mapper.selectByPrimaryKey(key);

			if (cabecera != null) {
				if (!(cabecera.getCodcaja().equals(codCajaMaster) || cabecera.getCodcaja().equals(cajaDTO.getCabecera().getCodcaja()))) {
					throw new EstadoCajaInvalidoException();
				}

				cajaDTO.getCabecera().setUidDiarioCaja(uidDiarioCaja);
				//TODO
//				cajaDTO.getCabecera().setCodcaja(null);
				/*
				 * En el caso que tengamos fechaCierre significa que venimos de un cierre de caja. En caso contrario se tratara de una
				 * caja aparcada
				 */
				if (cajaDTO.getCabecera().getFechaCierre() != null) {
					cajaDTO.getCabecera().setCodcaja(cajaDTO.getCabecera().getCodcaja());
				}
				else {
					cajaDTO.getCabecera().setCodcaja(CajasConstants.PARAM_CODCAJA_APARCADA);
				}
				
				DetalleCajaExample example = new DetalleCajaExample();
				example.or().andUidActividadEqualTo(datosSesion.getUidActividad()).andUidDiarioCajaEqualTo(uidDiarioCaja);
				detalleMapper.deleteByExample(example);
				mapper.updateByPrimaryKeySelective(cajaDTO.getCabecera());
			}
			else {
				/* Cuando se haya realizado la apertura de otra caja que no sea la caja master */
				cajaDTO.getCabecera().setCodcaja(CajasConstants.PARAM_CODCAJA_APARCADA);
				mapper.insert(cajaDTO.getCabecera());
			}

			for (DetalleCaja detalle : cajaDTO.getDetalles()) {
				detalle.setUidDiarioCaja(uidDiarioCaja);
				detalleMapper.insert(detalle);
			}

			sqlSession.commit();
		}
		catch (CajaAbiertaUsuarioException e) {
			sqlSession.rollback();
			String msg = "Ya existe una caja abierta para este usuario.";
			log.error("recuperarCaja() - " + msg);
			throw e;
		}
		catch (EstadoCajaInvalidoException e) {
			sqlSession.rollback();
			String msg = "El estado de la caja no permite su transferencia " + e.getMessage();
			log.error("recuperarCaja() - " + msg);
			throw e;
		}
		catch (Exception e) {
			sqlSession.rollback();
			String msg = "Ha ocurrido un error al transferir la caja " + e.getMessage();
			log.error("recuperarCaja() - " + msg);
			throw new CabeceraCajaException(msg);
		}
		finally {
			sqlSession.close();
		}
	}
}
