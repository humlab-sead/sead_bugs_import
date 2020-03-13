FROM maven:3.6.2-jdk-8 as build

ARG BRANCH=master

RUN apt-get update && apt-get install -y \
  ssh \
  git \
  wget curl unzip

WORKDIR /source

ADD https://api.github.com/repos/humlab-sead/sead_bugs_import/git/refs/heads/$BRANCH version.json

RUN git clone https://github.com/humlab-sead/sead_bugs_import \
     && cd sead_bugs_import \
     && mvn -Dmaven.test.skip=true clean \
     && mvn -Dmaven.test.skip=true package

FROM maven:3.6.2-jdk-8

RUN apt-get update && apt-get install -y \
  wget unzip

LABEL MAINTAINER Roger Mähler <roger dot mahler at umu dot se>

RUN useradd -m bugger

COPY ./docker-entrypoint.sh /usr/bin/docker-entrypoint.sh

RUN chmod +x /usr/bin/docker-entrypoint.sh

USER bugger

WORKDIR /home/bugger
COPY --from=build /source/sead_bugs_import/target/bugs.import-0.1-SNAPSHOT.jar .
RUN mkdir -p /home/bugger/config
COPY ./application.properties.template .

ENTRYPOINT [ "docker-entrypoint.sh" ]

CMD [ ]