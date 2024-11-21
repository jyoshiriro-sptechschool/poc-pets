#!/bin/bash
set -x

kill -9 `cat api_pid.txt`

set -e

sh /home/ubuntu/deploy-api/api-start.sh
