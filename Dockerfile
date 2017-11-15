# DOCKER-VERSION 17.10.0-ce
FROM jboss/wildfly:10.0.0.Final
WORKDIR /opt/jboss/wildfly

ENV SIGLA it/cnr/sigla-ear/4.0.1/sigla-ear-4.0.1-wildfly.ear

COPY SIGLA/target/SIGLA.ear standalone/deployments/SIGLA.ear

CMD ["./bin/standalone.sh", "-b", "0.0.0.0", "--debug", "8787", "-bmanagement", "0.0.0.0", "-Dspring.profiles.active=CMIS"]

COPY src/main/docker/standalone/configuration/  standalone/configuration/
COPY src/main/docker/domain/configuration/  domain/configuration/
COPY src/main/docker/modules/system/layers/base/com/informix/jdbc/ modules/system/layers/base/com/informix/jdbc/
COPY src/main/docker/modules/system/layers/base/com/oracle/jdbc/ modules/system/layers/base/com/oracle/jdbc/
COPY src/main/docker/modules/system/layers/base/it/cnr/sigla/configuration/main/ modules/system/layers/base/it/cnr/sigla/configuration/main/

EXPOSE 9990

RUN ./bin/add-user.sh admin admin --silent
