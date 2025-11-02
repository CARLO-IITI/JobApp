#!/bin/bash

echo "🧪 Testing Semantic Matching API..."
echo ""

# Test data
TEST_DATA='{
  "job": {
    "title": "Senior Full Stack Developer",
    "description": "We are looking for an experienced Full Stack Developer with expertise in Java, Spring Boot, React, and modern web technologies. The ideal candidate will have strong backend and frontend skills, experience with microservices architecture, and a passion for building scalable applications.",
    "requiredSkills": ["Java", "Spring Boot", "React", "PostgreSQL", "Docker", "Kubernetes"],
    "experienceLevel": "SENIOR",
    "minExperience": 5,
    "location": "San Francisco, CA",
    "remoteAllowed": true
  },
  "candidate": {
    "headline": "Full Stack Software Engineer",
    "summary": "Experienced software engineer with 6 years in full stack development. Strong expertise in Java, Spring ecosystem, and modern frontend frameworks. Built and deployed multiple microservices applications.",
    "skills": ["Java", "Spring Boot", "React", "Node.js", "PostgreSQL", "MongoDB", "Docker", "AWS"],
    "yearsOfExperience": 6,
    "currentLocation": "San Francisco, CA",
    "openToRemote": true,
    "workExperiences": [
      {
        "jobTitle": "Senior Software Engineer",
        "companyName": "Tech Corp",
        "description": "Led development of microservices architecture using Spring Boot and React",
        "technologies": ["Java", "Spring", "React", "Docker", "Kubernetes"]
      }
    ]
  }
}'

echo "📝 Testing semantic match calculation..."
echo ""

# Call the API
RESPONSE=$(curl -s -X POST http://localhost:8083/api/semantic-matching/calculate-match \
  -H "Content-Type: application/json" \
  -d "$TEST_DATA" \
  -w "\n%{http_code}")

# Extract HTTP code
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

echo "HTTP Status Code: $HTTP_CODE"
echo ""

if [ "$HTTP_CODE" = "200" ]; then
    echo "✅ SUCCESS! Semantic matching is working!"
    echo ""
    echo "Response:"
    echo "$BODY" | jq '.' 2>/dev/null || echo "$BODY"
    echo ""
    
    # Extract match score if available
    MATCH_SCORE=$(echo "$BODY" | jq -r '.data.overallPercentage' 2>/dev/null)
    if [ "$MATCH_SCORE" != "null" ]; then
        echo "🎯 Match Score: $MATCH_SCORE%"
        
        if [ "$MATCH_SCORE" -ge 85 ]; then
            echo "📊 Recommendation: EXCELLENT_MATCH 🌟"
        elif [ "$MATCH_SCORE" -ge 70 ]; then
            echo "📊 Recommendation: STRONG_MATCH ⭐"
        elif [ "$MATCH_SCORE" -ge 55 ]; then
            echo "📊 Recommendation: GOOD_MATCH ✓"
        else
            echo "📊 Recommendation: MODERATE_MATCH"
        fi
    fi
else
    echo "❌ FAILED! Got HTTP $HTTP_CODE"
    echo ""
    echo "Response:"
    echo "$BODY"
    echo ""
    echo "Possible issues:"
    echo "- Matching service not running (check port 8083)"
    echo "- Service still starting up (wait a moment)"
    echo "- Check logs: tail -f backend/matching-service/target/*.log"
fi

echo ""
echo "=========================================="
echo "📚 API Endpoints Available:"
echo "=========================================="
echo "POST /api/semantic-matching/calculate-match"
echo "POST /api/semantic-matching/find-candidates"
echo "POST /api/semantic-matching/find-jobs"
echo "POST /api/semantic-matching/batch-match"
echo "GET  /api/semantic-matching/health"
echo ""
echo "See SEMANTIC_MATCHING_FEATURE.md for full documentation"

