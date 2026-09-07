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

rabbitmqctl delete_user admin 2>/dev/null || true
rabbitmqctl add_user admin admin
rabbitmqctl set_user_tags admin administrator
rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"

echo "Usuário RabbitMQ configurado!"

echo "========================================"
echo "Iniciando Spring Boot..."
echo "========================================"

exec java -jar /app/app.jar