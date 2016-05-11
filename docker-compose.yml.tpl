sigla:
  image: docker.si.cnr.it/##{CONTAINER_ID}##
  mem_limit: 512m
  environment:
  - SERVICE_TAGS=webapp
  - SERVICE_NAME=##{SERVICE_NAME}##
