#!/bin/bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

set -xe

# Nome do processo a ser iniciado (substitua por seu comando)
processo="java -jar /home/ubuntu/deploy-api/api-pets-0.0.1-SNAPSHOT.jar"

# Iniciando o processo em background e capturando o PID
pid=$(nohup $processo &)

# Salvando o PID na variável de ambiente
export API_PID=$pid

# Verificando se o PID foi salvo corretamente
echo "O PID do processo da API é é: $PID"
