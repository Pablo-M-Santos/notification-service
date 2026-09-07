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

echo "Node RabbitMQ disponível."

echo "Aguardando aplicação RabbitMQ..."

until rabbitmqctl status >/dev/null 2>&1; do
    sleep 2
done

echo "Aplicação RabbitMQ disponível!"

echo "========================================"
echo "Configurando usuário RabbitMQ..."
echo "========================================"

rabbitmqctl delete_user admin 2>/dev/null || true
rabbitmqctl add_user admin "${RABBITMQ_PASSWORD:-admin}"
rabbitmqctl set_user_tags admin administrator
rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"

echo "Usuário RabbitMQ configurado!"

echo "========================================"
echo "Iniciando Spring Boot..."
echo "========================================"

exec java -jar /app/app.jar