#!/bin/bash
set -x

kill -9 ${API_PID}

sh /home/ubuntu/deploy-api/api-start.sh