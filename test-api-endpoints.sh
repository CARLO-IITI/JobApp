#!/bin/bash

# Test API Endpoints Script
# This script tests the work experience and job posting endpoints

echo "🧪 Testing JobApp API Endpoints..."
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Test 1: Check if User Service is running
echo -e "${BLUE}Test 1: Checking User Service (port 8081)...${NC}"
if curl -s http://localhost:8081/actuator/health > /dev/null 2>&1; then
    echo -e "${GREEN}✓ User Service is running${NC}"
else
    echo -e "${RED}✗ User Service is NOT running${NC}"
    echo "Run: cd backend/user-service && mvn spring-boot:run"
fi
echo ""

# Test 2: Check if Job Service is running
echo -e "${BLUE}Test 2: Checking Job Service (port 8082)...${NC}"
if curl -s http://localhost:8082/actuator/health > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Job Service is running${NC}"
else
    echo -e "${RED}✗ Job Service is NOT running${NC}"
    echo "Run: cd backend/job-service && mvn spring-boot:run"
fi
echo ""

# Test 3: Check if Frontend is running
echo -e "${BLUE}Test 3: Checking Frontend (port 3000)...${NC}"
if curl -s http://localhost:3000 > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Frontend is running${NC}"
else
    echo -e "${RED}✗ Frontend is NOT running${NC}"
    echo "Run: cd frontend && npm run dev"
fi
echo ""

# Test 4: Test Work Experience Endpoint (requires authentication)
echo -e "${BLUE}Test 4: Testing Work Experience API endpoint...${NC}"
if curl -s http://localhost:8081/api/experience/work/1 > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Work Experience endpoint is accessible${NC}"
else
    echo -e "${RED}✗ Work Experience endpoint returned an error${NC}"
fi
echo ""

# Test 5: Test Job Posting Endpoint
echo -e "${BLUE}Test 5: Testing Job Posting API endpoint...${NC}"
if curl -s http://localhost:8082/api/jobs > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Job Posting endpoint is accessible${NC}"
else
    echo -e "${RED}✗ Job Posting endpoint returned an error${NC}"
fi
echo ""

echo "=========================================="
echo "📝 Summary:"
echo "=========================================="
echo "If all services are running, try the following:"
echo ""
echo "1. For Work Experience:"
echo "   - Go to your Profile page"
echo "   - Click 'Add' button under Work Experience"
echo "   - You'll be redirected to Edit Profile page"
echo "   - Click 'Add Experience' button there"
echo "   - Fill in the form and save"
echo ""
echo "2. For Job Posting:"
echo "   - Login as a RECRUITER"
echo "   - Go to 'Post Job' page"
echo "   - Fill in all required fields"
echo "   - Click 'Post Job' button"
echo ""
echo "If you see 500 errors:"
echo "   - Check backend console logs for detailed error messages"
echo "   - Ensure PostgreSQL database is running on port 5432"
echo "   - Check database connection in application.yml"
echo ""

