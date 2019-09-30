FROM maven:3.6.2-jdk-8

ARG USER_HOME_DIR="/home/developer"

RUN apt-get update && apt-get install -y \
  ssh \
  git \
  wget curl unzip \
  && useradd -m developer

ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

WORKDIR ${USER_HOME_DIR}

COPY ./.mvn .
COPY ./config .
COPY ./configuration .
COPY ./pom.xml .
COPY ./.classpath .

RUN git clone https://github.com/humlab-sead/sead_bugs_import
#  \
#     && cd sead_bugs_import \
#     && mvn -Dmaven.test.skip=true clean \
#     && mvn -Dmaven.test.skip=true package

COPY ./config .

#RUN javac Main.java

#CMD ["java", "Main"]

