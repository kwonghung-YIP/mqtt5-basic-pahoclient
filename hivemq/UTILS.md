keytool -genkey -alias mydomain -keyalg RSA -keystore keystore.jks -keysize 2048

del keystore.jks
keytool -genkey -keystore keystore.jks -alias hivemq -keyalg RSA -keysize 2048 -keypass changeit -storepass changeit -ext BC:"ca:true"

keytool -exportcert -keystore keystore.jks -alias hivemq -keypass changeit -storepass changeit -rfc -file hivemq-server-cert.pem

docker run --link hivemq4 -it -v //c/projects/mqtt-client/hivemq-server-cert.pem:/cafile.pem hivemq/mqtt-cli shell
pub -t a -m "Hello man!"

docker run -it hivemq/mqtt-cli shell

connect --host test.mosquitto.org --port 1883

subscribe --topic presence --stay --showTopics --jsonOutput
subscribe --topic presence --showTopics --jsonOutput

subscribe --topic hungtest-reply --stay --showTopics --jsonOutput
publish --topic presence --message "Good day man!"

con -ws -h hivemq4 -p 8000
con -ws -h hivemq4 -p 8001 -s --cafile /cafile.pem --capath /

docker run -it hivemq/mqtt-cli con -v -h 1c3b21dd0317425d916b4ed67fcd4309.s1.eu.hivemq.cloud -p 8883 -u user1 -pw hm93bqCj6xR562U

docker run -it hivemq/mqtt-cli shell
con -h 192.168.43.217 -p 1883 -u user1 -pw hm93bqCj6xR562U
pub -t public/odds/20211001ST/ra/0/win/4 -m "ABC"