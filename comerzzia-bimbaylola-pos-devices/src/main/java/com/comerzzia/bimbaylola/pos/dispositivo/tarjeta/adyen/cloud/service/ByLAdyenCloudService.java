package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.adyen.cloud.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.adyen.Client;
import com.adyen.model.terminal.TerminalAPIRequest;
import com.adyen.model.terminal.TerminalAPIResponse;
import com.adyen.service.TerminalCloudAPI;
import com.comerzzia.pos.dispositivo.tarjeta.adyen.cloud.service.AdyenCloudService;
import com.comerzzia.pos.dispositivo.tarjeta.adyen.exception.AdyenException;
import com.comerzzia.pos.util.i18n.I18N;
import com.google.gson.Gson;

@Component
@Primary
public class ByLAdyenCloudService extends AdyenCloudService {

	private Logger log = Logger.getLogger(ByLAdyenCloudService.class);
        private String terminalApiEndpoint;

        public void setTerminalApiEndpoint(String terminalApiEndpoint) {
                this.terminalApiEndpoint = terminalApiEndpoint;
        }

        @Override
        public TerminalAPIResponse sendRequest(Client client, String tipoMensaje, TerminalAPIRequest peticion) throws AdyenException {
                if (client == null || client.getConfig() == null) {
                        throw new AdyenException(I18N.getTexto("No se ha inicializado el cliente de Adyen para Terminal API"));
                }

                if (StringUtils.isNotBlank(terminalApiEndpoint)) {
                        client.getConfig().setTerminalApiCloudEndpoint(terminalApiEndpoint);
                }
                log.info("Cloud/sendRequest() - Terminal API endpoint configurado: " + client.getConfig().getTerminalApiCloudEndpoint());
                TerminalCloudAPI terminalCloudApi = new TerminalCloudAPI(client);
                /* The words must be in lowercase and the first syllable in uppercase to be able to send it for testing */
		Gson gson = new Gson();
		String jsonRequest = gson.toJson(peticion);
		client.setTimeouts(10000, 150000);
		log.debug("Cloud/sendRequest() - " + I18N.getTexto("Petición de tipo " + tipoMensaje + " enviada - ") + jsonRequest);

		TerminalAPIResponse terminalAPIResponse = null;
		try {
			terminalAPIResponse = terminalCloudApi.sync(peticion);
		}
		catch (Exception e) {
			String mensajeError = I18N.getTexto("Error en la comunicación, reinténtelo y si persiste el problema, realice operación en modo STANDALONE y avise a Soporte. Gracias");
			log.error("Cloud/sendRequest() - " + mensajeError + " - " + e.getMessage(), e);
			throw new AdyenException(mensajeError, e);
		}

		if (terminalAPIResponse != null) {
			String jsonResponse = gson.toJson(terminalAPIResponse);
			log.debug("Cloud/sendRequest() - " + I18N.getTexto("Petición de tipo " + tipoMensaje + " recibida - ") + jsonResponse);
		}

		return terminalAPIResponse;
	}
}
