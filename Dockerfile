FROM java:8

WORKDIR /opt/

ENV WILDFLY_VERSION 10.0.0.Final

ENV WILDFLY wildfly-${WILDFLY_VERSION}

ENV SIGLA it/cnr/sigla-ear/4.0.1/sigla-ear-4.0.1-wildfly.ear

ADD http://download.jboss.org/wildfly/${WILDFLY_VERSION}/${WILDFLY}.tar.gz .

RUN tar xvf ${WILDFLY}.tar.gz && rm ${WILDFLY}.tar.gz

ADD http://maven.si.cnr.it/content/repositories/releases/${SIGLA} ${WILDFLY}/standalone/deployments/SIGLA.ear

WORKDIR ${WILDFLY}

CMD ["./bin/standalone.sh", "-b", "0.0.0.0"]

COPY src/main/docker/standalone/configuration/  standalone/configuration/
COPY src/main/docker/modules/system/layers/base/com/informix/jdbc/ modules/system/layers/base/com/informix/jdbc/
COPY src/main/docker/modules/system/layers/base/com/oracle/jdbc/ modules/system/layers/base/com/oracle/jdbc/

EXPOSE 8080
EXPOSE 9999

