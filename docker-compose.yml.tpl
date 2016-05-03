sigla:
  image: docker.si.cnr.it/##{CONTAINER_ID}##
  environment:
  - SERVICE_TAGS=webapp
  - SERVICE_NAME=##{SERVICE_NAME}##
  volumes:
  - ./webapp_logs:/opt/jboss-4.2.2.GA/server/default/log/
