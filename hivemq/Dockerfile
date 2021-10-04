FROM alpine:edge as openssl
RUN apk add --verbose --update openssl && mkdir /certs
COPY selfsign.conf /certs
RUN openssl req -verbose -x509 -newkey rsa:2048 -noenc -config /certs/selfsign.conf \
  -keyout /certs/selfsign.key -out /certs/selfsign.crt
RUN openssl pkcs12 -export -out /certs/selfsign.p12 -passout pass:changeit \
  --name hivemq -inkey /certs/selfsign.key -in /certs/selfsign.crt


FROM openjdk:17-oracle as openjdk
RUN java -version && mkdir /certs
COPY --from=openssl /certs/selfsign.p12 /certs/selfsign.p12
RUN keytool -importkeystore -destkeystore /certs/selfsign.jks -deststorepass changeit -destkeypass changeit -srckeystore /certs/selfsign.p12 -srcstoretype PKCS12 -srcstorepass changeit


FROM hivemq/hivemq4:4.7.1
ARG HIVEMQ_HOME=/opt/hivemq-4.7.1
EXPOSE 8080/TCP 1883/TCP 8000/TCP 8001/TCP
COPY --from=openjdk /certs/selfsign.jks ${HIVEMQ_HOME}/conf/selfsign.jks
ADD conf/config.xml ${HIVEMQ_HOME}/conf/config.xml
ADD extensions/hivemq-file-rbac-extension/ ${HIVEMQ_HOME}/extensions/hivemq-file-rbac-extension/
ADD extensions/hivemq-mqtt-message-log-extension/ ${HIVEMQ_HOME}/extensions/hivemq-mqtt-message-log-extension/
