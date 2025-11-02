#!/bin/bash

echo "🚀 Starting JobApp Locally (Fixed Version)"
echo "==========================================="

# Set Java 17
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"

# Set Node 18
export PATH="/opt/homebrew/opt/node@18/bin:$PATH"

echo "✓ Using Java: $(java -version 2>&1 | head -1)"
echo "✓ Using Node: $(node --version)"
echo ""

# Get the script directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo "📦 Building Common Library..."
cd "$SCRIPT_DIR/backend/common"
mvn clean install -DskipTests -s .mvn/settings.xml
if [ $? -eq 0 ]; then
    echo "✅ Common library built successfully"
else
    echo "❌ Failed to build common library"
    exit 1
fi

echo ""
echo "🚀 Starting services in new terminals..."
echo ""

# Start User Service
osascript <<EOF
tell application "Terminal"
    do script "cd '$SCRIPT_DIR/backend/user-service' && export JAVA_HOME=/opt/homebrew/opt/openjdk@17 && export PATH=\"\$JAVA_HOME/bin:\$PATH\" && echo '🟢 Starting User Service...' && mvn spring-boot:run -s ../common/.mvn/settings.xml"
end tell
EOF

sleep 3

# Start Job Service
osascript <<EOF
tell application "Terminal"
    do script "cd '$SCRIPT_DIR/backend/job-service' && export JAVA_HOME=/opt/homebrew/opt/openjdk@17 && export PATH=\"\$JAVA_HOME/bin:\$PATH\" && echo '🟢 Starting Job Service...' && mvn spring-boot:run -s ../common/.mvn/settings.xml"
end tell
EOF

sleep 3

# Start Matching Service
osascript <<EOF
tell application "Terminal"
    do script "cd '$SCRIPT_DIR/backend/matching-service' && export JAVA_HOME=/opt/homebrew/opt/openjdk@17 && export PATH=\"\$JAVA_HOME/bin:\$PATH\" && echo '🟢 Starting Matching Service...' && mvn spring-boot:run -s ../common/.mvn/settings.xml"
end tell
EOF

sleep 3

# Start Resume Service
osascript <<EOF
tell application "Terminal"
    do script "cd '$SCRIPT_DIR/backend/resume-service' && export JAVA_HOME=/opt/homebrew/opt/openjdk@17 && export PATH=\"\$JAVA_HOME/bin:\$PATH\" && echo '🟢 Starting Resume Service...' && mvn spring-boot:run -s ../common/.mvn/settings.xml"
end tell
EOF

echo ""
echo "⏳ Waiting 45 seconds for backend services to start..."
sleep 45

echo ""
echo "🎨 Starting Frontend..."

# Start Frontend
osascript <<EOF
tell application "Terminal"
    do script "cd '$SCRIPT_DIR/frontend' && export PATH=\"/opt/homebrew/opt/node@18/bin:\$PATH\" && echo '📦 Installing dependencies...' && npm install && echo '🎨 Starting React app...' && npm run dev"
end tell
EOF

echo ""
echo "============================================"
echo "✅ All services are starting!"
echo "============================================"
echo ""
echo "Services will be available at:"
echo "  🎨 Frontend:         http://localhost:3000"
echo "  🔧 User Service:     http://localhost:8081"
echo "  💼 Job Service:      http://localhost:8082"
echo "  🤖 Matching Service: http://localhost:8083"
echo "  📄 Resume Service:   http://localhost:8084"
echo ""
echo "⏱️  Please wait 2-3 minutes for everything to fully start"
echo "📱 Then open: http://localhost:3000"
echo ""
echo "👀 Watch the terminal windows for startup progress"
echo "✅ Look for 'Started [Service]Application' in each window"
echo ""
echo "To stop: Close the terminal windows or press Ctrl+C in each"
echo ""

