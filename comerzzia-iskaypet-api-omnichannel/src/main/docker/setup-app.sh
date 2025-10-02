#!/bin/bash

echo "setup-app: Configuring environment ...."

if [ -e "${INSTALL_DIR}/conf/outstore" ]; then
    echo "setup-app: Creando configuración de Comerzzia-OutStore"

    ls -1 "${INSTALL_DIR}/conf/outstore" | while read CONF_FILE
    do
        echo "setup-app: Archivo ${CONF_FILE}"
        cat "${INSTALL_DIR}/conf/outstore/${CONF_FILE}" > "${INSTALL_DIR}/${CONF_FILE}"
    done
fi

echo "setup-app: Configuring database vars"

# Reemplazar la variable de la base de datos por defecto
sed -i 's#@@COMERZZIA_DB_URL@@#'$COMERZZIA_DB_URL'#;
s#@@COMERZZIA_DB_DRIVER@@#'$COMERZZIA_DB_DRIVER'#;
s#@@COMERZZIA_DB_USER@@#'$COMERZZIA_DB_USER'#;
s#@@COMERZZIA_DB_PASS@@#'$COMERZZIA_DB_PASS'#;
s#@@COMERZZIA_DB_CLASS@@#'$COMERZZIA_DB_CLASS'#;' \
"${INSTALL_DIR}/comerzzia.xml"
