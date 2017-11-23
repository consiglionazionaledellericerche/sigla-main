sigla:
  image: docker.si.cnr.it/##{CONTAINER_ID}##
  mem_limit: 1024m
  environment:
  - 'RUN_CONF=./standalone-custom.conf'
  - 'SIGLA_CONNECTION_URL=jdbc:oracle:thin:@dbtest.cedrc.cnr.it:1521:SIGLAF'
  - 'SIGLA_CONNECTION_USERNAME=PCIR009'
  - 'SIGLA_CONNECTION_PASSWORD=dbform'
  labels:
   SERVICE_NAME: "##{SERVICE_NAME}##"
