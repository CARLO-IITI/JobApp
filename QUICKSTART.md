# 🚀 Quick Start Guide

## Choose Your Path

You have **two options** to run JobApp:

### Option 1: 🐳 Docker (Recommended - Easiest)
### Option 2: 💻 Local Development (More Control)

---

## Option 1: Docker Setup (Recommended)

**Prerequisites:** 
- Install Docker Desktop: https://www.docker.com/products/docker-desktop

**Steps:**

```bash
# 1. Start all services with one command
docker-compose up -d

# 2. Check if services are running
docker-compose ps

# 3. View logs
docker-compose logs -f

# 4. Access the application
# Frontend: http://localhost:3000
# API: http://localhost:8080
```

**That's it!** ✨ All services will start automatically.

---

## Option 2: Local Development Setup

This gives you more control for development.

### Step 1: Install Prerequisites

1. **Java 17**
   ```bash
   # macOS (using Homebrew)
   brew install openjdk@17
   
   # Or download from: https://adoptium.net/
   ```

2. **Node.js 18+**
   ```bash
   # macOS (using Homebrew)
   brew install node@18
   
   # Or download from: https://nodejs.org/
   ```

3. **Maven**
   ```bash
   # macOS (using Homebrew)
   brew install maven
   
   # Or download from: https://maven.apache.org/
   ```

4. **PostgreSQL**
   ```bash
   # macOS (using Homebrew)
   brew install postgresql@14
   brew services start postgresql@14
   
   # Create database
   createdb jobapp
   ```

5. **Redis (Optional - for caching)**
   ```bash
   brew install redis
   brew services start redis
   ```

### Step 2: Start Backend Services

Open **5 separate terminal windows** and run:

**Terminal 1 - Build Common Library:**
```bash
cd /Users/s0a0hu5/Personal/JobApp/backend/common
mvn clean install
```

**Terminal 2 - User Service:**
```bash
cd /Users/s0a0hu5/Personal/JobApp/backend/user-service
mvn clean install
mvn spring-boot:run
```

**Terminal 3 - Job Service:**
```bash
cd /Users/s0a0hu5/Personal/JobApp/backend/job-service
mvn clean install
mvn spring-boot:run
```

**Terminal 4 - Matching Service:**
```bash
cd /Users/s0a0hu5/Personal/JobApp/backend/matching-service
mvn clean install
mvn spring-boot:run
```

**Terminal 5 - Resume Service:**
```bash
cd /Users/s0a0hu5/Personal/JobApp/backend/resume-service
mvn clean install
mvn spring-boot:run
```

Wait for all services to start (you'll see "Started [Service]Application" in each terminal).

### Step 3: Start Frontend

Open a **new terminal**:

```bash
cd /Users/s0a0hu5/Personal/JobApp/frontend
npm install
npm run dev
```

### Step 4: Access the Application

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **User Service**: http://localhost:8081
- **Job Service**: http://localhost:8082
- **Matching Service**: http://localhost:8083
- **Resume Service**: http://localhost:8084

---

## 🎯 First Steps After Starting

### 1. Create Your First Account

Go to http://localhost:3000 and click **"Get Started"**

**As a Job Seeker:**
- Email: `candidate@example.com`
- Password: `password123`
- Role: **Job Seeker**

**As a Recruiter:**
- Email: `recruiter@example.com`
- Password: `password123`
- Role: **Recruiter**

### 2. Explore Features

**For Candidates:**
- ✅ Browse jobs
- ✅ Apply for positions
- ✅ Track applications
- ✅ Get AI-powered matches

**For Recruiters:**
- ✅ Post jobs
- ✅ Review applications
- ✅ Manage candidates
- ✅ View analytics

---

## 🧪 Test the API

### Using cURL:

```bash
# 1. Register a user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "fullName": "Test User",
    "role": "CANDIDATE"
  }'

# 2. Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'

# 3. Get all jobs
curl http://localhost:8080/api/jobs
```

---

## 🛠️ Troubleshooting

### Port Already in Use

```bash
# Find what's using the port
lsof -i :8080

# Kill the process
kill -9 <PID>
```

### Database Connection Failed

```bash
# Check if PostgreSQL is running
brew services list

# Start PostgreSQL
brew services start postgresql@14

# Verify database exists
psql -l | grep jobapp
```

### Frontend Won't Start

```bash
# Clear cache and reinstall
cd frontend
rm -rf node_modules package-lock.json
npm install
npm run dev
```

### Backend Build Failed

```bash
# Clean and rebuild
cd backend
mvn clean install -DskipTests
```

---

## 📊 Verify Everything is Working

### Check Service Health

```bash
# User Service
curl http://localhost:8081/actuator/health

# Job Service
curl http://localhost:8082/actuator/health

# Matching Service
curl http://localhost:8083/actuator/health

# Resume Service
curl http://localhost:8084/actuator/health
```

All should return: `{"status":"UP"}`

---

## 🎓 What to Do Next

1. **Explore the UI** - Create accounts, post jobs, apply
2. **Test the API** - Use the API documentation
3. **Customize** - Modify colors, add features
4. **Deploy** - Deploy to cloud (AWS, Heroku, etc.)

---

## 🆘 Need Help?

- **Documentation**: Check README.md and SETUP_GUIDE.md
- **API Docs**: http://localhost:8080/swagger-ui.html (when running)
- **Issues**: Check the logs in each terminal

---

## 🎉 You're All Set!

Your world-class job matching platform is ready to use!

**Happy coding!** 🚀

