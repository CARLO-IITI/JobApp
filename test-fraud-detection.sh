#!/bin/bash

echo "🛡️ Testing AI Fraud Detection System"
echo "===================================="
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo "Test 1: GENUINE Profile (Should score HIGH)"
echo "─────────────────────────────────────────────"
curl -s -X POST http://localhost:8083/api/matching/verify-authenticity \
  -H "Content-Type: application/json" \
  -d '{
    "skills": ["Java", "Spring", "PostgreSQL", "Docker", "Git"],
    "yearsOfExperience": 5,
    "education": "Bachelor of Science in Computer Science",
    "summary": "Built 3 microservices at TechCorp, improving response time by 40%. 5 years experience in backend development.",
    "githubUrl": "https://github.com/johndoe",
    "linkedinUrl": "https://linkedin.com/in/johndoe",
    "portfolioUrl": "https://johndoe.dev"
  }' | python3 -m json.tool 2>/dev/null

echo ""
echo ""
echo "Test 2: FRAUDULENT Profile (Should score LOW)"
echo "─────────────────────────────────────────────"
curl -s -X POST http://localhost:8083/api/matching/verify-authenticity \
  -H "Content-Type: application/json" \
  -d '{
    "skills": ["Java", "Python", "JavaScript", "C++", "Go", "Rust", "React", "Angular", "Vue", "Svelte", "Spring", "Django", "Flask", "Express", "Docker", "Kubernetes", "AWS", "Azure", "GCP", "Machine Learning", "Deep Learning", "Blockchain", "AI", "DevOps", "Microservices"],
    "yearsOfExperience": 1,
    "education": "PhD in Computer Science",
    "summary": "I am an expert ninja rockstar guru master developer with extensive experience in numerous technologies at various companies",
    "githubUrl": "",
    "linkedinUrl": "",
    "portfolioUrl": ""
  }' | python3 -m json.tool 2>/dev/null

echo ""
echo ""
echo "Test 3: MODERATE Profile (Some red flags)"
echo "─────────────────────────────────────────────"
curl -s -X POST http://localhost:8083/api/matching/verify-authenticity \
  -H "Content-Type: application/json" \
  -d '{
    "skills": ["React", "Node.js", "MongoDB", "JavaScript", "HTML", "CSS", "Express", "Git", "Docker", "Python", "Django", "PostgreSQL"],
    "yearsOfExperience": 3,
    "education": "Bachelor in Computer Science",
    "summary": "Full stack developer with experience in web development",
    "githubUrl": "https://github.com/user",
    "linkedinUrl": "",
    "portfolioUrl": ""
  }' | python3 -m json.tool 2>/dev/null

echo ""
echo "═══════════════════════════════════════"
echo "✅ Fraud Detection Tests Complete!"
echo "═══════════════════════════════════════"

