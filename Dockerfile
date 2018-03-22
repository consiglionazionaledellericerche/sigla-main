FROM anapsix/alpine-java:8

WORKDIR /sigla/

COPY sigla/target/SIGLA-swarm.jar sigla.jar

CMD ["java", "-Djava.net.preferIPv4Stack=true", "-jar", "sigla.jar", "-Dspring.profiles.active=CMIS"]

EXPOSE 8080
RUN touch audit.log
RUN chown nobody:nobody audit.log
USER nobody
