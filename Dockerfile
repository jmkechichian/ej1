# Imagen oficial de WildFly, misma version que el servidor standalone (41.0.0.Final).
# Si este tag fallara al descargar, probar con "41.0.0.Final-jdk17" o "latest".
FROM quay.io/wildfly/wildfly:41.0.0.Final-jdk21

# --- Usuario de aplicacion -------------------------------------------------
# Necesario para que ej1-console pueda invocar el EJB remoto
# (coincide con el usuario "prueba/prueba" del wildfly-config.xml del cliente).
# Opcional si solo se usa la capa web.
USER root
RUN /opt/jboss/wildfly/bin/add-user.sh -a -u prueba -p prueba --silent

# --- Despliegue de la aplicacion ----------------------------------------
# El build de Maven (mvn install) deja el EAR en ear/target/ej1.ear.
# El deployment scanner de WildFly lo despliega solo al arrancar.
COPY ear/target/ej1.ear /opt/jboss/wildfly/standalone/deployments/

# --- Compatibilidad con PaaS tipo OpenShift ----------------------------
# OpenShift ejecuta el contenedor con un UID aleatorio que pertenece al
# grupo 0 (root). Los archivos que agregamos/modificamos deben ser
# legibles y escribibles por ese grupo, si no WildFly no arranca.
RUN chgrp -R 0 /opt/jboss/wildfly/standalone \
 && chmod -R g+rwX /opt/jboss/wildfly/standalone

# Volver a un usuario no-root para 'docker run' local (OpenShift lo ignora
# y usa su propio UID aleatorio).
USER 1000

# HTTP (aplicacion) y consola de administracion
EXPOSE 8080 9990

# -b 0.0.0.0            -> acepta conexiones desde fuera del contenedor
# -bmanagement 0.0.0.0  -> expone tambien el 9990 (irrelevante en la nube)
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
