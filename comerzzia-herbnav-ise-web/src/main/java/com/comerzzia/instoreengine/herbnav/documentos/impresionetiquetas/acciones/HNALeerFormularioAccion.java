package com.comerzzia.instoreengine.herbnav.documentos.impresionetiquetas.acciones;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.numeros.Numero;
import com.comerzzia.core.util.paginacion.PaginaResultados;
import com.comerzzia.instoreengine.util.AppISE;
import com.comerzzia.instoreengine.web.documentos.impresionetiquetas.acciones.LeerFormularioAccion;
import com.comerzzia.model.general.articulos.generaciondatos.dto.GeneracionDatosArticuloCopiasDTO;
import com.comerzzia.model.general.articulos.generaciondatos.dto.GeneracionDatosArticuloDTO;
import com.comerzzia.model.general.articulos.generaciondatos.dto.GeneracionDatosArticulosDTO;
import com.comerzzia.model.general.clientes.ClienteBean;
import com.comerzzia.model.ventas.tarifas.TarifaBean;
import com.comerzzia.persistencia.general.articulos.ParametrosBuscarArticulosBean;
import com.comerzzia.servicios.general.articulos.generaciondatos.GeneracionDatosArticuloException;
import com.comerzzia.servicios.general.articulos.generaciondatos.ServicioGeneracionDatosArticulosImpl;
import com.comerzzia.servicios.general.clientes.ClienteException;
import com.comerzzia.servicios.general.clientes.ClienteNotFoundException;
import com.comerzzia.servicios.general.clientes.ServicioClientesImpl;
import com.comerzzia.web.base.WebKeys;

public class HNALeerFormularioAccion extends LeerFormularioAccion{

	@Override
	protected void añadirArticulos(HttpServletRequest request) throws ClienteException, ClienteNotFoundException, GeneracionDatosArticuloException, SQLException {
		HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);
		@SuppressWarnings("unchecked")
		Map<GeneracionDatosArticuloDTO, Integer> mapaArticulos = (Map<GeneracionDatosArticuloDTO, Integer>) sesion.getAttribute(ATTRIBUTE_MAPA_ARTICULOS);
    	String codcli = AppISE.getPosConfig().getTienda().getCodCliente();
		ClienteBean clienteTienda = ServicioClientesImpl.get().consultarEnTienda(codcli, datosSesion);
		String codTarifa = clienteTienda.getCodTar();
		if(codTarifa == null) {
			codTarifa = TarifaBean.TARIFA_GENERAL;
		}
    	ParametrosBuscarArticulosBean param = inicializarParams(request);
    	int addedCopies = Numero.desformateaInteger(request.getParameter("quantity"), 1);
    	
    	@SuppressWarnings("deprecation")
		GeneracionDatosArticulosDTO dto = ServicioGeneracionDatosArticulosImpl.get().selectByExample(datosSesion, CLASE_IMPRESION_ETIQUETAS, codTarifa, new Date(), param);
    	for(GeneracionDatosArticuloDTO articuloDTO : dto.getArticulos()) {
    		if(mapaArticulos.containsKey(articuloDTO)) {
    			Integer cantidad = mapaArticulos.get(articuloDTO);
    			Integer copias = cantidad + addedCopies;
    			mapaArticulos.put(articuloDTO, copias);
    			cantidad = 0;
    			addedCopies = 0;
    		}else {
    			mapaArticulos.put(articuloDTO, addedCopies);
    			
    			if(mapaArticulos.get(articuloDTO)<=0) {
        			mapaArticulos.remove(articuloDTO);
        		}
    			
    			break;
    		}
    		if(mapaArticulos.get(articuloDTO)<=0) {
    			mapaArticulos.remove(articuloDTO);
    		}
    			
    	}
    	sesion.setAttribute(ATTRIBUTE_MAPA_ARTICULOS, mapaArticulos);
    	List<GeneracionDatosArticuloCopiasDTO> copias = new ArrayList<GeneracionDatosArticuloCopiasDTO>();
    	for(GeneracionDatosArticuloDTO artDTO : mapaArticulos.keySet()) {
    		GeneracionDatosArticuloCopiasDTO copiasDTO = new GeneracionDatosArticuloCopiasDTO();
    		copiasDTO.setArticuloDTO(artDTO);
    		copiasDTO.setNumCopias(mapaArticulos.get(artDTO));
    		copias.add(copiasDTO);
    	}
    	sesion.setAttribute(ATTRIBUTE_COPIAS, copias);
    	Integer tamañoPagina = null;
    	try {
    		tamañoPagina = Integer.parseInt(request.getParameter("tamanoPagina"));
        } 
        catch(Exception e) {
        	tamañoPagina = 10;
        }
    	List<GeneracionDatosArticuloCopiasDTO> resultados = new ArrayList<GeneracionDatosArticuloCopiasDTO>();

    	 PaginaResultados paginaResultados = new PaginaResultados();
    	 paginaResultados.setNumPagina(1);
    	 paginaResultados.setTamañoPagina(tamañoPagina);
    	 int fromIndex = (paginaResultados.getInicio() - 1);
			int toIndex = (paginaResultados.getInicio() + tamañoPagina - 1);
			if(toIndex > copias.size()) {
				toIndex = copias.size();
			}
		   
			resultados.addAll(copias.subList(fromIndex, toIndex));
			paginaResultados.setPagina(resultados);
		  	paginaResultados.setTotalResultados(copias.size());
		  	
		  	sesion.setAttribute(WebKeys.PAGINA_RESULTADOS, paginaResultados);
	}
}
