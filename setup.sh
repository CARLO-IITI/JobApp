#!/bin/bash

echo "🚀 Setting up JobApp - Intelligent Job Matching Platform"
echo "=========================================================="

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "${YELLOW}Docker is not installed. Please install Docker first.${NC}"
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo -e "${YELLOW}Docker Compose is not installed. Please install Docker Compose first.${NC}"
    exit 1
fi

echo -e "${BLUE}Starting setup...${NC}"

# Create necessary directories
echo -e "${BLUE}Creating directories...${NC}"
mkdir -p ml-models
mkdir -p uploads
touch ml-models/.gitkeep
touch uploads/.gitkeep

# Create .env file if it doesn't exist
if [ ! -f .env ]; then
    echo -e "${BLUE}Creating .env file...${NC}"
    cat > .env << EOF
# Database Configuration
DB_HOST=postgres
DB_PORT=5432
DB_NAME=jobapp
DB_USER=postgres
DB_PASSWORD=postgres123

# JWT Configuration
JWT_SECRET=mySecretKeyForJobAppPlatformThatIsLongEnoughForHS512Algorithm
JWT_EXPIRATION=86400000

# Services URLs
USER_SERVICE_URL=http://user-service:8081
JOB_SERVICE_URL=http://job-service:8082
MATCHING_SERVICE_URL=http://matching-service:8083
RESUME_SERVICE_URL=http://resume-service:8084

# Elasticsearch
ELASTICSEARCH_URL=http://elasticsearch:9200

# Redis
REDIS_HOST=redis
REDIS_PORT=6379

# RabbitMQ
RABBITMQ_HOST=rabbitmq
RABBITMQ_PORT=5672

# Email Configuration (Optional)
EMAIL_USERNAME=
EMAIL_PASSWORD=

# Google Calendar API (Optional)
GOOGLE_CALENDAR_API_KEY=

# SendGrid API (Optional)
SENDGRID_API_KEY=
EOF
    echo -e "${GREEN}✓ .env file created${NC}"
else
    echo -e "${YELLOW}.env file already exists, skipping...${NC}"
fi

echo ""
echo -e "${GREEN}✓ Setup completed successfully!${NC}"
echo ""
echo "Next steps:"
echo "1. Review and update the .env file with your configurations"
echo "2. Run 'docker-compose up -d' to start all services"
echo "3. Access the application at http://localhost:3000"
echo ""
echo "API Documentation will be available at:"
echo "  - Swagger UI: http://localhost:8080/swagger-ui.html"
echo ""
echo "Service Ports:"
echo "  - Frontend: http://localhost:3000"
echo "  - API Gateway: http://localhost:8080"
echo "  - User Service: http://localhost:8081"
echo "  - Job Service: http://localhost:8082"
echo "  - Matching Service: http://localhost:8083"
echo "  - Resume Service: http://localhost:8084"
echo "  - PostgreSQL: localhost:5432"
echo "  - Elasticsearch: http://localhost:9200"
echo "  - RabbitMQ Management: http://localhost:15672"
echo ""
echo "Happy coding! 🎉"

