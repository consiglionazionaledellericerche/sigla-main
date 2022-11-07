sigla:
  image: docker.si.cnr.it/##{CONTAINER_ID}##
  volumes:
    - ./cert.p12:/opt/cert.p12
    - ./project-formazione.yml:/opt/project-formazione.yml
  command: java -Xmx1g -Xss512k -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8787 -Dspring.profiles.active=liquibase-cnr,pagopa -Dremote.maven.repo=https://repository.jboss.org/nexus/content/groups/public/,https://maven.repository.redhat.com/earlyaccess/all -DarubaRemoteSignService.url=https://arss.demo.firma-automatica.it/ArubaSignService/ArubaSignService?wsdl -Djava.security.egd=file:/dev/./urandom -jar /opt/sigla-thorntail.jar -s/opt/project-formazione.yml -Skeycloak
  extra_hosts:
    - "sigla-print.test.si.cnr.it:150.146.206.186"
    - "zuul-server.test.si.cnr.it:150.146.206.186"
  labels:
   SERVICE_NAME: "##{SERVICE_NAME}##"
