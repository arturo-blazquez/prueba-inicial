FROM jenkins

USER root

RUN cd /usr/local/bin
RUN curl -SLO https://download.docker.com/linux/static/stable/x86_64/docker-18.03.1-ce.tgz
RUN	tar xzvf docker-18.03.1-ce.tgz --strip 1 -C /usr/local/bin docker/docker
RUN rm docker-18.03.1-ce.tgz

RUN curl -sL https://deb.nodesource.com/setup_10.x | bash -
RUN apt-get update
RUN apt-get install -y apparmor \
  git \
  nodejs \
  npm

RUN npm link @angular/cli && npm install -g @angular/cli && npm -v && ng -v
