sigla:
  image: docker.si.cnr.it/##{CONTAINER_ID}##
  environment:
  - JAVA_OPTS=-Xmx256m
  - SERVICE_TAGS=webapp
  - SERVICE_NAME=##{SERVICE_NAME}##
