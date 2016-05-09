#!/bin/bash
echo $PWD

echo "Deploying Discovery-Service"
java -jar /home/ubuntu/zoo/discovery-service.jar > discover-service.log &

sleep 30

echo "Deploying History-Services"
java -jar /home/ubuntu/zoo/history-services.jar > history-services.log &

sleep 30

echo "Deploying Provider-Services"
java -jar /home/ubuntu/zoo/provider-services.jar > provider-services.log &

sleep 30

echo "Deploying User-Services"
java -jar /home/ubuntu/zoo/user-services.jar > user-servioes.log &

sleep 30

echo "Deploying Front-Web"
java -jar /home/ubuntu/zoo/web-front.jar > web-front.log &

echo "All apps up and running..."
