#!/bin/sh
http=https
url=api.test.studywithme.ovh
//endpoint=$http://$url
endpoint=http://localhost:8080
echo "curl -i -H \"Content-Type: application/json\" -X GET $endpoint/groups"
curl -i -H "Content-Type: application/json" -X GET $endpoint/groups

echo "curl -i -H \"Content-Type: application/json\" -X GET $endpoint/groups/1"
curl -i -H "Content-Type: application/json" -X GET $endpoint/groups/1
