#!/bin/bash

echo "🚀 Starting JobApp in Development Mode"
echo "======================================="

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Start infrastructure services first
echo -e "${BLUE}Starting infrastructure services (Database, Redis, Elasticsearch, RabbitMQ)...${NC}"
docker-compose up -d postgres redis elasticsearch rabbitmq

echo -e "${YELLOW}Waiting for services to be ready...${NC}"
sleep 10

# Check if services are healthy
echo -e "${BLUE}Checking service health...${NC}"
docker-compose ps

echo ""
echo -e "${GREEN}Infrastructure services started!${NC}"
echo ""
echo "You can now run the backend services and frontend locally:"
echo ""
echo "Backend (in separate terminals):"
echo "  cd backend/user-service && mvn spring-boot:run"
echo "  cd backend/job-service && mvn spring-boot:run"
echo "  cd backend/matching-service && mvn spring-boot:run"
echo "  cd backend/resume-service && mvn spring-boot:run"
echo ""
echo "Frontend:"
echo "  cd frontend && npm install && npm run dev"
echo ""
echo "Or start all services with Docker:"
echo "  docker-compose up -d"
echo ""

