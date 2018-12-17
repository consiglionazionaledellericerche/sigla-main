# DOCKER-VERSION 17.10.0-ce
FROM anapsix/alpine-java:jdk8
MAINTAINER Marco Spasiano <marco.spasiano@cnr.it>

COPY target/sigla-swarm.jar /opt/sigla-swarm.jar

EXPOSE 8080
EXPOSE 8787

# https://spring.io/guides/gs/spring-boot-docker/#_containerize_it
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/opt/sigla-swarm.jar"]
