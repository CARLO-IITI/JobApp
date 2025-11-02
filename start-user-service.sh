#!/bin/bash

echo "🟢 Starting User Service on port 8081..."
echo "=========================================="

cd "$(dirname "$0")/backend/user-service"

export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"

echo "Building User Service..."
mvn clean install -DskipTests -s ../common/.mvn/settings.xml

if [ $? -eq 0 ]; then
    echo "✅ Build successful, starting User Service..."
    echo ""
    echo "Waiting for service to start..."
    echo "Look for: 'Started UserServiceApplication'"
    echo ""
    mvn spring-boot:run -s ../common/.mvn/settings.xml
else
    echo "❌ Build failed"
    exit 1
fi

