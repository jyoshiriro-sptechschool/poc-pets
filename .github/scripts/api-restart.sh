#!/bin/bash
set -x

kill -9 ${API_PID}

sh api-start.sh