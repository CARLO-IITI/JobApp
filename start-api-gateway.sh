#!/bin/bash

echo "🚪 Starting API Gateway on port 8080..."
echo "========================================"

cd "$(dirname "$0")/backend/api-gateway"

export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"

echo "Building API Gateway..."
mvn clean install -DskipTests -s ../common/.mvn/settings.xml

if [ $? -eq 0 ]; then
    echo "✅ Build successful, starting API Gateway..."
    mvn spring-boot:run -s ../common/.mvn/settings.xml
else
    echo "❌ Build failed"
    exit 1
fi

