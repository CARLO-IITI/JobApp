#!/bin/bash
echo "Full registration test with detailed output..."
curl -v -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -H "Origin: http://localhost:3000" \
  -d '{
    "email": "realtest@example.com",
    "password": "password123",
    "fullName": "Real Test User",
    "phone": "+1234567890",
    "role": "CANDIDATE"
  }' 2>&1

