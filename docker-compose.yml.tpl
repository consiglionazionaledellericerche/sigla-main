sigla:
  image: docker.si.cnr.it/##{CONTAINER_ID}##
  command: java -Xmx1g -Xss512k -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8787 -Dremote.maven.repo=https://repository.jboss.org/nexus/content/groups/public/ -Djava.security.egd=file:/dev/./urandom -jar /opt/sigla-thorntail.war
  labels:
   SERVICE_NAME: "##{SERVICE_NAME}##"
