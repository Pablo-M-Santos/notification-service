#!/bin/bash

set -e

echo "========================================"
echo "Iniciando RabbitMQ..."
echo "========================================"

rabbitmq-server -detached

echo "Aguardando RabbitMQ..."

until rabbitmq-diagnostics -q ping; do
    sleep 2
done

echo "RabbitMQ iniciado com sucesso!"

echo "========================================"
echo "Iniciando Spring Boot..."
echo "========================================"

exec java -jar /app/app.jar