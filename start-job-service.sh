#!/bin/bash
cd /Users/s0a0hu5/Personal/JobApp/backend/job-service
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"
echo "💼 Building Job Service..."
mvn clean install -DskipTests -s ../common/.mvn/settings.xml -q
echo "✅ Build complete"
echo ""
echo "🟢 Starting Job Service on port 8082..."
echo "Watch for: 'Started JobServiceApplication'"
echo ""
mvn spring-boot:run -s ../common/.mvn/settings.xml

