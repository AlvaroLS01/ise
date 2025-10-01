FROM registry.comerzzia.com/czz-devops/comerzzia/backoffice:4.8.1.230720

RUN rm ${INSTALL_DIR}/wildfly/standalone/deployments/*.war

COPY ./target/*.war "${INSTALL_DIR}/wildfly/standalone/deployments/"
