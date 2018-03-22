version: '2'

services:
  sigla:
    image: docker.si.cnr.it/##{CONTAINER_ID}##
    mem_limit: 2048m
    network_mode: bridge
    labels:
      SERVICE_NAME: "##{SERVICE_NAME}##"
