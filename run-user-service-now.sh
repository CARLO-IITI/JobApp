#!/bin/bash
cd /Users/s0a0hu5/Personal/JobApp/backend/user-service
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"
echo "🟢 Starting User Service on port 8081..."
echo ""
echo "Watch for: 'Started UserServiceApplication'"
echo ""
mvn spring-boot:run -s ../common/.mvn/settings.xml

