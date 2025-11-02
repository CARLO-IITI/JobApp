# 📚 API Documentation

Complete API documentation for the JobApp Platform.

## Base URL

```
http://localhost:8080/api
```

## Authentication

All protected endpoints require a JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

---

## 🔐 Authentication API

### Register User

**POST** `/auth/register`

Register a new user (candidate or recruiter).

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "fullName": "John Doe",
  "phone": "+1234567890",
  "role": "CANDIDATE"
}
```

**Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "userId": 1,
    "email": "user@example.com",
    "fullName": "John Doe",
    "role": "CANDIDATE"
  }
}
```

### Login

**POST** `/auth/login`

Authenticate and get JWT token.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "userId": 1,
    "email": "user@example.com",
    "fullName": "John Doe",
    "role": "CANDIDATE"
  }
}
```

### Validate Token

**GET** `/auth/validate`

Validate JWT token.

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "data": true
}
```

---

## 👤 User API

### Get User by ID

**GET** `/users/{userId}`

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "email": "user@example.com",
    "fullName": "John Doe",
    "phone": "+1234567890",
    "role": "CANDIDATE",
    "isActive": true,
    "emailVerified": false,
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

### Update User

**PUT** `/users/{userId}`

**Request Body:**
```json
{
  "fullName": "John Updated",
  "phone": "+9876543210",
  "profilePicture": "https://example.com/photo.jpg"
}
```

---

## 💼 Job API

### Get All Jobs

**GET** `/jobs?page=0&size=10&sortBy=createdAt&sortDir=DESC`

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 10)
- `sortBy` (optional): Sort field (default: createdAt)
- `sortDir` (optional): Sort direction ASC/DESC (default: DESC)

**Response:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "title": "Senior Java Developer",
        "description": "We are looking for...",
        "companyName": "Tech Corp",
        "companyId": 1,
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
        "status": "ACTIVE",
        "viewCount": 150,
        "applicationCount": 25,
        "createdAt": "2024-01-01T10:00:00"
      }
    ],
    "totalElements": 100,
    "totalPages": 10,
    "number": 0,
    "size": 10
  }
}
```

### Get Job by ID

**GET** `/jobs/{jobId}`

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "Senior Java Developer",
    "description": "Detailed description...",
    "responsibilities": "Key responsibilities...",
    "qualifications": "Required qualifications...",
    "benefits": "Benefits package...",
    "companyName": "Tech Corp",
    "location": "San Francisco, CA",
    "requiredSkills": ["Java", "Spring Boot"],
    "minSalary": 120000,
    "maxSalary": 180000
  }
}
```

### Search Jobs

**GET** `/jobs/search?keyword=java&page=0&size=10`

**Query Parameters:**
- `keyword` (required): Search keyword
- `page` (optional): Page number
- `size` (optional): Page size

### Get Latest Jobs

**GET** `/jobs/latest`

Returns the 10 most recent active jobs.

### Create Job (Recruiter Only)

**POST** `/jobs`

**Request Body:**
```json
{
  "title": "Senior Java Developer",
  "description": "We are looking for an experienced Java developer...",
  "companyId": 1,
  "companyName": "Tech Corp",
  "companyDescription": "Leading tech company...",
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
  "responsibilities": "Key responsibilities...",
  "qualifications": "Required qualifications...",
  "benefits": "Health insurance, 401k...",
  "postedBy": 1
}
```

### Update Job

**PUT** `/jobs/{jobId}`

**Request Body:**
```json
{
  "title": "Updated Title",
  "description": "Updated description...",
  "status": "ACTIVE"
}
```

### Delete Job

**DELETE** `/jobs/{jobId}`

Marks the job as CLOSED.

---

## 📝 Application API

### Apply for Job

**POST** `/applications`

**Request Body:**
```json
{
  "jobId": 1,
  "candidateId": 1,
  "coverLetter": "I am very interested in this position...",
  "resumeUrl": "https://example.com/resume.pdf"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Application submitted successfully",
  "data": {
    "id": 1,
    "jobId": 1,
    "candidateId": 1,
    "coverLetter": "I am very interested...",
    "status": "SUBMITTED",
    "appliedAt": "2024-01-01T10:00:00"
  }
}
```

### Get Application by ID

**GET** `/applications/{applicationId}`

### Get Applications by Candidate

**GET** `/applications/candidate/{candidateId}?page=0&size=10`

Returns all applications for a specific candidate.

### Get Applications by Job

**GET** `/applications/job/{jobId}?page=0&size=10`

Returns all applications for a specific job (recruiter access).

### Update Application Status

**PUT** `/applications/{applicationId}/status?status=SHORTLISTED&notes=Great candidate`

**Query Parameters:**
- `status` (required): New status (SUBMITTED, REVIEWED, SHORTLISTED, INTERVIEW_SCHEDULED, REJECTED, ACCEPTED, WITHDRAWN)
- `notes` (optional): Recruiter notes

### Withdraw Application

**DELETE** `/applications/{applicationId}/withdraw?candidateId={candidateId}`

---

## 🤖 Matching API

### Find Matching Jobs

**POST** `/matching/find-jobs`

Use AI to find the best matching jobs for a candidate.

**Request Body:**
```json
{
  "candidateId": 1,
  "skills": ["Java", "Spring Boot", "PostgreSQL"],
  "yearsOfExperience": 5,
  "currentLocation": "San Francisco, CA",
  "preferredLocations": ["San Francisco, CA", "New York, NY"],
  "jobPreference": "FULL_TIME",
  "expectedSalary": 150000,
  "openToRemote": true
}
```

**Response:**
```json
{
  "success": true,
  "message": "Matches found successfully",
  "data": [
    {
      "jobId": 1,
      "jobTitle": "Senior Java Developer",
      "companyName": "Tech Corp",
      "overallScore": 0.85,
      "skillsMatchScore": 0.90,
      "experienceMatchScore": 1.0,
      "locationMatchScore": 1.0,
      "culturalFitScore": 0.95,
      "details": {
        "matchingSkills": ["Java", "Spring Boot"],
        "missingSkills": ["Kubernetes"],
        "experienceGap": 0
      },
      "recommendation": "Excellent match! Strongly recommended to apply."
    }
  ]
}
```

---

## 📄 Resume API

### Parse Resume

**POST** `/resume/parse`

Parse a resume file and extract information.

**Request:**
- Content-Type: `multipart/form-data`
- File parameter: `file`

**Example (cURL):**
```bash
curl -X POST http://localhost:8080/api/resume/parse \
  -H "Authorization: Bearer <token>" \
  -F "file=@/path/to/resume.pdf"
```

**Response:**
```json
{
  "success": true,
  "message": "Resume parsed successfully",
  "data": {
    "fileName": "resume.pdf",
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+1234567890",
    "skills": ["Java", "Spring Boot", "PostgreSQL", "Docker"],
    "education": "Bachelor of Science in Computer Science",
    "experience": 5,
    "rawText": "Full resume text..."
  }
}
```

---

## 📊 Response Format

All API responses follow this format:

### Success Response
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { /* response data */ },
  "timestamp": "2024-01-01T10:00:00"
}
```

### Error Response
```json
{
  "success": false,
  "message": "Error message",
  "errorCode": "ERROR_CODE",
  "timestamp": "2024-01-01T10:00:00"
}
```

---

## 🔒 Error Codes

| Code | Description |
|------|-------------|
| `UNAUTHORIZED` | Invalid or missing authentication token |
| `FORBIDDEN` | Insufficient permissions |
| `RESOURCE_NOT_FOUND` | Requested resource not found |
| `BAD_REQUEST` | Invalid request parameters |
| `VALIDATION_ERROR` | Request validation failed |
| `INTERNAL_ERROR` | Internal server error |

---

## 🚀 Rate Limiting

Current rate limits:
- Authentication endpoints: 10 requests per minute
- General API: 100 requests per minute
- File upload: 5 requests per minute

---

## 📦 Pagination

All list endpoints support pagination:

**Request:**
```
GET /api/jobs?page=0&size=20
```

**Response:**
```json
{
  "content": [...],
  "totalElements": 100,
  "totalPages": 5,
  "number": 0,
  "size": 20,
  "first": true,
  "last": false
}
```

---

## 🧪 Testing with cURL

### Register and Login
```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "fullName": "Test User",
    "role": "CANDIDATE"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Browse Jobs
```bash
curl http://localhost:8080/api/jobs
```

### Apply for a Job
```bash
curl -X POST http://localhost:8080/api/applications \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "jobId": 1,
    "candidateId": 1,
    "coverLetter": "I am interested in this position"
  }'
```

---

For more information, visit the Swagger UI at: http://localhost:8080/swagger-ui.html

