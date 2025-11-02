# 🚀 JobApp Setup Guide

Complete guide to set up and run the Intelligent Job Matching Platform.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17+** - [Download](https://adoptium.net/)
- **Node.js 18+** - [Download](https://nodejs.org/)
- **Maven 3.8+** - [Download](https://maven.apache.org/)
- **Docker & Docker Compose** - [Download](https://www.docker.com/)
- **Git** - [Download](https://git-scm.com/)

## Quick Start (Docker)

The fastest way to get started is using Docker Compose:

```bash
# 1. Clone the repository
git clone <repository-url>
cd JobApp

# 2. Run setup script
chmod +x setup.sh
./setup.sh

# 3. Start all services
docker-compose up -d

# 4. Check service status
docker-compose ps

# 5. View logs
docker-compose logs -f
```

**Access the application:**
- Frontend: http://localhost:3000
- API Gateway: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

## Manual Setup (Development)

### 1. Start Infrastructure Services

```bash
# Start PostgreSQL, Redis, Elasticsearch, RabbitMQ
chmod +x start-dev.sh
./start-dev.sh
```

### 2. Build and Run Backend Services

#### Common Library (Build First)
```bash
cd backend/common
mvn clean install
```

#### User Service (Port 8081)
```bash
cd backend/user-service
mvn clean install
mvn spring-boot:run
```

#### Job Service (Port 8082)
```bash
cd backend/job-service
mvn clean install
mvn spring-boot:run
```

#### Matching Service (Port 8083)
```bash
cd backend/matching-service
mvn clean install
mvn spring-boot:run
```

#### Resume Service (Port 8084)
```bash
cd backend/resume-service
mvn clean install
mvn spring-boot:run
```

#### API Gateway (Port 8080)
```bash
cd backend/api-gateway
mvn clean install
mvn spring-boot:run
```

### 3. Run Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend will be available at http://localhost:3000

## Environment Configuration

### Backend Configuration

Each service uses `application.yml` for configuration. You can override settings using environment variables:

```bash
# Database
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=jobapp
export DB_USER=postgres
export DB_PASSWORD=postgres123

# JWT
export JWT_SECRET=your-secret-key
export JWT_EXPIRATION=86400000

# Elasticsearch
export ELASTICSEARCH_URL=http://localhost:9200

# Redis
export REDIS_HOST=localhost
export REDIS_PORT=6379
```

### Frontend Configuration

Create `.env` file in the `frontend` directory:

```env
VITE_API_URL=http://localhost:8080/api
```

## Database Setup

The application uses PostgreSQL with automatic schema creation via Hibernate.

### Manual Database Creation (Optional)

```bash
# Connect to PostgreSQL
psql -U postgres -h localhost

# Create database
CREATE DATABASE jobapp;

# Exit
\q
```

The application will automatically create tables on first run.

## Testing the Application

### 1. Create Test Data

**Register as a Candidate:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "candidate@example.com",
    "password": "password123",
    "fullName": "John Doe",
    "role": "CANDIDATE"
  }'
```

**Register as a Recruiter:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "recruiter@example.com",
    "password": "password123",
    "fullName": "Jane Smith",
    "role": "RECRUITER"
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "candidate@example.com",
    "password": "password123"
  }'
```

Save the returned JWT token for subsequent requests.

### 3. Post a Job (as Recruiter)

```bash
curl -X POST http://localhost:8080/api/jobs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "title": "Senior Java Developer",
    "description": "We are looking for an experienced Java developer...",
    "companyId": 1,
    "companyName": "Tech Corp",
    "location": "San Francisco, CA",
    "jobType": "FULL_TIME",
    "experienceLevel": "SENIOR",
    "requiredSkills": ["Java", "Spring Boot", "PostgreSQL"],
    "minExperience": 5,
    "maxExperience": 10,
    "minSalary": 120000,
    "maxSalary": 180000,
    "currency": "USD",
    "remoteAllowed": true,
    "openings": 2,
    "postedBy": 1
  }'
```

### 4. Browse Jobs

```bash
curl http://localhost:8080/api/jobs
```

### 5. Apply for a Job

```bash
curl -X POST http://localhost:8080/api/applications \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "jobId": 1,
    "candidateId": 1,
    "coverLetter": "I am very interested in this position..."
  }'
```

## Monitoring & Management

### View Service Health

```bash
# User Service
curl http://localhost:8081/actuator/health

# Job Service
curl http://localhost:8082/actuator/health

# Matching Service
curl http://localhost:8083/actuator/health
```

### View Logs

**Docker:**
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f user-service
docker-compose logs -f frontend
```

**Local Development:**
Check console output in the terminal where each service is running.

### Database Management

**Using pgAdmin or DBeaver:**
- Host: localhost
- Port: 5432
- Database: jobapp
- Username: postgres
- Password: postgres123

### Elasticsearch

View Elasticsearch status:
```bash
curl http://localhost:9200/_cluster/health?pretty
```

### RabbitMQ Management

Access RabbitMQ management UI:
- URL: http://localhost:15672
- Username: admin
- Password: admin123

## Troubleshooting

### Port Already in Use

```bash
# Find process using port
lsof -i :8080

# Kill process
kill -9 <PID>
```

### Docker Issues

```bash
# Clean up Docker
docker-compose down -v
docker system prune -a

# Rebuild containers
docker-compose build --no-cache
docker-compose up -d
```

### Database Connection Issues

```bash
# Check if PostgreSQL is running
docker-compose ps postgres

# Restart PostgreSQL
docker-compose restart postgres

# View PostgreSQL logs
docker-compose logs postgres
```

### Frontend Build Issues

```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
npm run dev
```

## Production Deployment

### Build for Production

**Backend:**
```bash
cd backend
mvn clean package -DskipTests
```

**Frontend:**
```bash
cd frontend
npm run build
```

### Deploy with Docker

```bash
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

### Environment Variables for Production

Update the `.env` file with production values:

```env
# Use strong passwords
DB_PASSWORD=<strong-password>

# Use a strong JWT secret (at least 512 bits)
JWT_SECRET=<generate-strong-secret>

# Configure email service
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password

# Configure external APIs
SENDGRID_API_KEY=your-sendgrid-key
GOOGLE_CALENDAR_API_KEY=your-google-api-key
```

## Performance Optimization

### Database

1. **Create indexes:**
```sql
CREATE INDEX idx_jobs_status ON jobs(status);
CREATE INDEX idx_jobs_location ON jobs(location);
CREATE INDEX idx_applications_candidate ON applications(candidate_id);
CREATE INDEX idx_applications_job ON applications(job_id);
```

2. **Enable connection pooling** (already configured in application.yml)

### Elasticsearch

1. **Increase heap size:**
```yaml
ES_JAVA_OPTS=-Xms1g -Xmx1g
```

2. **Monitor cluster health:**
```bash
curl http://localhost:9200/_cluster/health
```

### Caching

Redis is configured for caching. You can adjust TTL and cache size in application.yml.

## Support

For issues and questions:
- Check the logs: `docker-compose logs -f`
- Review the README.md
- Check API documentation: http://localhost:8080/swagger-ui.html

## Next Steps

1. **Customize the application** - Modify branding, colors, and features
2. **Add more ML models** - Enhance the matching algorithm
3. **Integrate third-party services** - Add LinkedIn, Indeed integrations
4. **Set up monitoring** - Add Prometheus and Grafana
5. **Configure CI/CD** - Set up automated deployments

Happy coding! 🚀

