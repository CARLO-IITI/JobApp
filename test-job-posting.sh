#!/bin/bash

echo "🧪 Testing Job Posting API..."
echo ""

# Test data
JOB_DATA='{
  "title": "Senior Full Stack Developer",
  "description": "We are looking for an experienced Full Stack Developer to join our team.",
  "companyId": 1,
  "companyName": "Tech Corp",
  "companyDescription": "Leading technology company",
  "location": "San Francisco, CA",
  "jobType": "FULL_TIME",
  "experienceLevel": "SENIOR",
  "minExperience": 5,
  "maxExperience": 10,
  "minSalary": 120000,
  "maxSalary": 180000,
  "currency": "USD",
  "remoteAllowed": true,
  "openings": 2,
  "requiredSkills": ["Java", "React", "Node.js", "PostgreSQL"],
  "responsibilities": "Develop and maintain web applications",
  "qualifications": "5+ years of experience in full stack development",
  "benefits": "Health insurance, 401k, remote work",
  "postedBy": 1
}'

echo "📝 Posting job to API..."
echo ""

# Post job (without authentication for testing)
RESPONSE=$(curl -s -X POST http://localhost:8082/api/jobs \
  -H "Content-Type: application/json" \
  -d "$JOB_DATA" \
  -w "\n%{http_code}")

# Extract HTTP code
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

echo "HTTP Status Code: $HTTP_CODE"
echo ""
echo "Response:"
echo "$BODY" | jq '.' 2>/dev/null || echo "$BODY"
echo ""

if [ "$HTTP_CODE" = "201" ] || [ "$HTTP_CODE" = "200" ]; then
    echo "✅ SUCCESS! Job posted successfully!"
else
    echo "❌ FAILED! Got HTTP $HTTP_CODE"
    echo ""
    echo "Common issues:"
    echo "- 401: Authentication required (need valid JWT token)"
    echo "- 400: Bad request (check data format)"
    echo "- 500: Server error (check backend logs)"
fi

