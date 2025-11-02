#!/bin/bash
cd /Users/s0a0hu5/Personal/JobApp/backend/matching-service
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"
echo "🤖 Building Matching Service..."
mvn clean install -DskipTests -s ../common/.mvn/settings.xml -q
echo "✅ Build complete"
echo ""
echo "🟢 Starting AI Matching Service on port 8083..."
echo "Watch for: 'Started MatchingServiceApplication'"
echo ""
mvn spring-boot:run -s ../common/.mvn/settings.xml

