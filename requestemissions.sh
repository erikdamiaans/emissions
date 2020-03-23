#!/bin/bash
TOKEN=$(curl  -X POST  -v --silent -H 'Accept: application/json' -H 'Content-Type: application/json' --data '{"username":"another","password":"anotherpw"}' http://localhost:8085/emissions/login | jq -r .token)
echo "got token $TOKEN";
LOAD_COMMAND="curl -X GET   -H 'Content-Type: application/json' -H 'x-access-token: $TOKEN' 'http://localhost:8085/emissions?department=Police&sourceType=Electric'"
echo "LOAD_COMMAND is $LOAD_COMMAND"
eval $LOAD_COMMAND



