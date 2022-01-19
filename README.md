docker run --name hivemq4 -p 8080:8080 -p 1883:1883 -p 8000:8000 -p 8001:8001 hivemq/hivemq4

docker run --link hivemq4 -it hivemq/mqtt-cli shell
pub -t a -m "Hello man!"

docker run -it hivemq/mqtt-cli shell

connect --host test.mosquitto.org --port 1883

subscribe --topic presence --stay --showTopics --jsonOutput
subscribe --topic presence --showTopics --jsonOutput

subscribe --topic hungtest-reply --stay --showTopics --jsonOutput
publish --topic presence --message "Good day man!"

echo 'FCigpm8JItLLD3lMtXEDNW-DlKcOmOHttX_xf86UoAONJrHfVQGYrftxz0oYSP6Zag_qCIGEDH3HEB5r-1_UVw' | base64 -w0

echo 'FCigpm8JItLLD3lMtXEDNW-DlKcOmOHttX_xf86UoAONJrHfVQGYrftxz0oYSP6Zag_qCIGEDH3HEB5r-1_UVw' | base64 -w0 | sed -e 's/+/-/g' -e 's/\//_/g' -e 's/=//g'


RkNpZ3BtOEpJdExMRDNsTXRYRUROVy1EbEtjT21PSHR0WF94Zjg2VW9BT05KckhmVlFHWXJmdHh6MG9ZU1A2WmFnX3FDSUdFREgzSEVCNXItMV9VVncK

RkNpZ3BtOEpJdExMRDNsTXRYRUROVy1EbEtjT21PSHR0WF94Zjg2VW9BT05KckhmVlFHWXJmdHh6MG9ZU1A2WmFnX3FDSUdFREgzSEVCNXItMV9VVncK

kubectl create secret generic eab-secret --from-literal \
  secret=RkNpZ3BtOEpJdExMRDNsTXRYRUROVy1EbEtjT21PSHR0WF94Zjg2VW9BT05KckhmVlFHWXJmdHh6MG9ZU1A2WmFnX3FDSUdFREgzSEVCNXItMV9VVncK

echo 'FCigpm8JItLLD3lMtXEDNW-DlKcOmOHttX_xf86UoAONJrHfVQGYrftxz0oYSP6Zag_qCIGEDH3HEB5r-1_UVw' | base64 -w0 | base64 -d

kubectl create secret generic eab-secret --from-literal secret="FCigpm8JItLLD3lMtXEDNW-DlKcOmOHttX_xf86UoAONJrHfVQGYrftxz0oYSP6Zag_qCIGEDH3HEB5r-1_UVw"

curl https://baltocdn.com/helm/signing.asc | sudo apt-key add -
sudo apt-get install apt-transport-https --yes
echo "deb https://baltocdn.com/helm/stable/debian/ all main" | sudo tee /etc/apt/sources.list.d/helm-stable-debian.list
sudo apt-get update
sudo apt-get install helm

helm install haproxy-ingress haproxy-ingress/haproxy-ingress\
  --create-namespace --namespace haproxy-ingress\
  --version 0.13.4\
  -f haproxy-ingress-values.yaml

kubectl --namespace default create deployment echoserver --image k8s.gcr.io/echoserver:1.3
kubectl --namespace default expose deployment echoserver --port=8080

kubectl --namespace default create ingress echoserver\
  --annotation kubernetes.io/ingress.class=haproxy\
  --rule="192.168.142.72.nip.io/*=echoserver:8080"

curl -k http://192.168.142.72 -H "host: 192.168.142.72.nip.io"

docker run -d --name=solace-standard\
  -p 8080:8080 -p 55555:55555 \
  -p 1883:1883 -p 8000:8000 -p 8:443
  --shm-size=1g \
  --env username_admin_globalaccesslevel=admin \
  --env username_admin_password=admin \
  solace/solace-pubsub-standard
