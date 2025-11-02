#!/bin/bash

echo "🚀 Starting JobApp Locally (No Docker)"
echo "========================================"

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Get the script directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo -e "${BLUE}Building Common Library...${NC}"
cd "$SCRIPT_DIR/backend/common"
mvn clean install -DskipTests -q
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Common library built${NC}"
else
    echo -e "${YELLOW}⚠ Common library build had issues, continuing...${NC}"
fi

echo ""
echo -e "${BLUE}Starting Backend Services...${NC}"
echo "This will open new terminal windows for each service."
echo ""

# Function to start a service in a new terminal
start_service() {
    local service_name=$1
    local service_dir=$2
    local port=$3
    
    echo -e "${BLUE}Starting $service_name on port $port...${NC}"
    
    # For macOS
    osascript <<EOF
tell application "Terminal"
    do script "cd '$SCRIPT_DIR/backend/$service_dir' && echo '🚀 Starting $service_name...' && mvn spring-boot:run"
end tell
EOF
    
    sleep 2
}

# Start all backend services
start_service "User Service" "user-service" "8081"
start_service "Job Service" "job-service" "8082"
start_service "Matching Service" "matching-service" "8083"
start_service "Resume Service" "resume-service" "8084"

echo ""
echo -e "${YELLOW}Waiting for backend services to start (30 seconds)...${NC}"
sleep 30

echo ""
echo -e "${BLUE}Starting Frontend...${NC}"

# Start frontend in new terminal
osascript <<EOF
tell application "Terminal"
    do script "cd '$SCRIPT_DIR/frontend' && echo '🎨 Installing frontend dependencies...' && npm install && echo '🚀 Starting frontend...' && npm run dev"
end tell
EOF

echo ""
echo -e "${GREEN}============================================${NC}"
echo -e "${GREEN}✓ All services are starting!${NC}"
echo -e "${GREEN}============================================${NC}"
echo ""
echo "Services will be available at:"
echo "  • Frontend:         http://localhost:3000"
echo "  • API Gateway:      http://localhost:8080"
echo "  • User Service:     http://localhost:8081"
echo "  • Job Service:      http://localhost:8082"
echo "  • Matching Service: http://localhost:8083"
echo "  • Resume Service:   http://localhost:8084"
echo ""
echo "⏱️  Please wait 1-2 minutes for all services to fully start"
echo "📱 Then open: http://localhost:3000"
echo ""
echo "To stop all services, close the terminal windows or press Ctrl+C in each"
echo ""

