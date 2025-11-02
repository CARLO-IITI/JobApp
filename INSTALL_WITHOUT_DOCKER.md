# 🚀 Install & Run Without Docker

Complete guide to run JobApp locally without Docker.

## 📋 Prerequisites Check

Run this to see what you have:

```bash
# Check Java
java -version

# Check Node.js
node --version

# Check Maven
mvn --version

# Check PostgreSQL
psql --version
```

---

## 🔧 Step 1: Install Prerequisites (One Command!)

I've created an automated installer for you:

```bash
cd /Users/s0a0hu5/Personal/JobApp
chmod +x install-prerequisites.sh
./install-prerequisites.sh
```

This script will:
- ✅ Install Java 17 (required for Spring Boot 3)
- ✅ Install Node.js 18 (for frontend)
- ✅ Install PostgreSQL 14 (database)
- ✅ Start PostgreSQL
- ✅ Create the `jobapp` database

**Time:** ~5-10 minutes (downloads and installs everything)

---

## 🚀 Step 2: Run the Application (One Command!)

After prerequisites are installed:

```bash
chmod +x run-local.sh
./run-local.sh
```

This will:
- Build the common library
- Start all 4 backend services in separate terminals
- Start the frontend

**Time:** ~2-3 minutes for everything to start

---

## 🎯 Step 3: Access the Application

Open your browser to: **http://localhost:3000**

You'll see the beautiful landing page! 🎉

---

## 🎨 Quick Demo (Frontend Only)

If you just want to see the UI without waiting for backend:

```bash
chmod +x run-frontend-only.sh
./run-frontend-only.sh
```

This runs just the frontend so you can see the design and navigate pages.

---

## 🛠️ Manual Installation (If Script Fails)

### Install Java 17

```bash
# Using Homebrew (macOS)
brew install openjdk@17

# Add to PATH
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

# Verify
java -version
# Should show: openjdk version "17.x.x"
```

### Install Node.js

```bash
# Using Homebrew (macOS)
brew install node@18

# Verify
node --version
# Should show: v18.x.x
```

### Install PostgreSQL

```bash
# Using Homebrew (macOS)
brew install postgresql@14

# Start PostgreSQL
brew services start postgresql@14

# Create database
createdb jobapp

# Verify
psql -l | grep jobapp
```

### Install Maven (if needed)

```bash
brew install maven

# Verify
mvn --version
```

---

## 🏃 Manual Running (Step by Step)

If the automated script doesn't work, run manually:

### Terminal 1: Build Common Library
```bash
cd /Users/s0a0hu5/Personal/JobApp/backend/common
mvn clean install -DskipTests
```

### Terminal 2: User Service
```bash
cd /Users/s0a0hu5/Personal/JobApp/backend/user-service
mvn clean install -DskipTests
mvn spring-boot:run
```

Wait for: `Started UserServiceApplication`

### Terminal 3: Job Service
```bash
cd /Users/s0a0hu5/Personal/JobApp/backend/job-service
mvn clean install -DskipTests
mvn spring-boot:run
```

Wait for: `Started JobServiceApplication`

### Terminal 4: Matching Service
```bash
cd /Users/s0a0hu5/Personal/JobApp/backend/matching-service
mvn clean install -DskipTests
mvn spring-boot:run
```

Wait for: `Started MatchingServiceApplication`

### Terminal 5: Resume Service
```bash
cd /Users/s0a0hu5/Personal/JobApp/backend/resume-service
mvn clean install -DskipTests
mvn spring-boot:run
```

Wait for: `Started ResumeServiceApplication`

### Terminal 6: Frontend
```bash
cd /Users/s0a0hu5/Personal/JobApp/frontend
npm install
npm run dev
```

---

## ✅ Verify Everything Works

### 1. Check Backend Services

```bash
# User Service
curl http://localhost:8081/actuator/health
# Should return: {"status":"UP"}

# Job Service
curl http://localhost:8082/actuator/health
# Should return: {"status":"UP"}

# Matching Service
curl http://localhost:8083/actuator/health
# Should return: {"status":"UP"}

# Resume Service
curl http://localhost:8084/actuator/health
# Should return: {"status":"UP"}
```

### 2. Test API

```bash
# Register a user
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "fullName": "Test User",
    "role": "CANDIDATE"
  }'
```

### 3. Access Frontend

Open: http://localhost:3000

---

## 🐛 Troubleshooting

### "Port already in use"

```bash
# Find what's using the port
lsof -i :8081

# Kill it
kill -9 <PID>
```

### "Database connection failed"

```bash
# Check if PostgreSQL is running
brew services list | grep postgresql

# Start it
brew services start postgresql@14

# Check if database exists
psql -l | grep jobapp

# If not, create it
createdb jobapp
```

### "Maven build failed"

```bash
# Clean everything
cd /Users/s0a0hu5/Personal/JobApp/backend
mvn clean

# Build with more info
mvn clean install -X
```

### "Java version mismatch"

```bash
# Check version
java -version

# If it shows Java 8, update your PATH
export JAVA_HOME=/Library/Java/JavaVirtualMachines/openjdk-17.jdk/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH

# Make it permanent
echo 'export JAVA_HOME=/Library/Java/JavaVirtualMachines/openjdk-17.jdk/Contents/Home' >> ~/.zshrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.zshrc
source ~/.zshrc
```

### "Frontend won't start"

```bash
cd /Users/s0a0hu5/Personal/JobApp/frontend

# Clear cache
rm -rf node_modules package-lock.json

# Reinstall
npm install

# Try again
npm run dev
```

---

## 🎯 Quick Commands Reference

```bash
# Install everything
./install-prerequisites.sh

# Run everything
./run-local.sh

# Frontend only (demo)
./run-frontend-only.sh

# Stop services
# Just close the terminal windows or Ctrl+C in each
```

---

## ⚡ Performance Tips

1. **Skip Tests During Build** (faster):
   ```bash
   mvn clean install -DskipTests
   ```

2. **Use Maven Offline** (if dependencies already downloaded):
   ```bash
   mvn spring-boot:run -o
   ```

3. **Increase Maven Memory**:
   ```bash
   export MAVEN_OPTS="-Xmx1024m"
   ```

---

## 🎉 You're Ready!

Now you can run the entire application locally without Docker!

**Total time from zero to running:** ~15-20 minutes

Need help? Check the main README.md or SETUP_GUIDE.md!

