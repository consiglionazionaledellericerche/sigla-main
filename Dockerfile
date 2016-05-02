FROM centos:7

WORKDIR /opt/

RUN curl -L -b "oraclelicense=a" http://download.oracle.com/otn-pub/java/jdk/1.5.0_22/jdk-1_5_0_22-linux-amd64-rpm.bin -O

ADD http://downloads.sourceforge.net/project/jboss/JBoss/JBoss-4.2.2.GA/jboss-4.2.2.GA.zip?use_mirror=netcologne .

RUN chmod +x jdk-1_5_0_22-linux-amd64-rpm.bin

RUN echo y |  ./jdk-1_5_0_22-linux-amd64-rpm.bin

RUN ln -s /usr/java/jdk1.5.0_22/bin/java /usr/bin/

RUN yum install -y unzip

RUN unzip jboss-4.2.2.GA.zip && rm jboss-4.2.2.GA.zip

COPY ./src/main/docker/conf/* /opt/jboss-4.2.2.GA/server/default/conf/
COPY ./src/main/docker/deploy/* /opt/jboss-4.2.2.GA/server/default/deploy/
COPY ./src/main/docker/lib/* /opt/jboss-4.2.2.GA/server/default/lib/

#ADD http://maven.si.cnr.it/service/local/artifact/maven/redirect?r=snapshots&g=it.cnr&a=sigla-ear&v=LATEST&e=ear

ADD  http://maven.si.cnr.it/content/repositories/snapshots/it/cnr/sigla-ear/3.1.60-SNAPSHOT/sigla-ear-3.1.60-20160406.091205-14.ear /opt/jboss-4.2.2.GA/server/default/deploy/SIGLA.ear

EXPOSE 8080

RUN rm jdk-1_5_0_22-linux-amd64-rpm.bin

ENV JAVA_OPTS '-Xms128m -Xmx1g -Xss2m -XX:MaxPermSize=256m'

CMD ["/opt/jboss-4.2.2.GA/bin/run.sh", "-b", "0.0.0.0"]

