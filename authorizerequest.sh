#!/bin/bash
TOKEN=$(curl  -X POST  -v --silent -H 'Accept: application/json' -H 'Content-Type: application/json' --data '{"username":"ester","password":"esterpw"}' http://localhost:8085/emissions/login | jq -r .token)
echo "got token $TOKEN";



