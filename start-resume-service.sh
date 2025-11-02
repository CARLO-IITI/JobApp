#!/bin/bash
cd /Users/s0a0hu5/Personal/JobApp/backend/resume-service
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"
echo "📄 Building Resume Service..."
mvn clean install -DskipTests -s ../common/.mvn/settings.xml -q
echo "✅ Build complete"
echo ""
echo "🟢 Starting Resume Service on port 8084..."
echo "Watch for: 'Started ResumeServiceApplication'"
echo ""
mvn spring-boot:run -s ../common/.mvn/settings.xml

