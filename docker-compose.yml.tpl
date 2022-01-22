siglang:
  image: docker.si.cnr.it/##{CONTAINER_ID}##
  environment:
    - SIGLA_WILDFLY_URL=http://sigla-main-sso.test.si.cnr.it
  command: java -Xmx512m -Xss512k -Dserver.port=8080 -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8787 -Djava.security.egd=file:/dev/./urandom -jar /opt/sigla-ng.war --spring.profiles.active=prod,keycloak
  labels:
    SERVICE_NAME: "##{SERVICE_NAME}##"
  volumes:
    - ./application-ldap.yml:/application-ldap.yml
