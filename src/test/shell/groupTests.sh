#!/bin/sh
http=https
url=api.test.studywithme.ovh
endpoint=$http://$url
echo "curl -i -H \"Content-Type: application/json\" -X GET $endpoint/groups"
curl -i -H "Content-Type: application/json" -X GET $endpoint/groups
