#!/bin/bash
echo "Testing direct registration..."
curl -v -X OPTIONS http://localhost:8081/api/auth/register \
  -H "Origin: http://localhost:3000" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: content-type" 2>&1 | grep -E "HTTP|Access-Control"
