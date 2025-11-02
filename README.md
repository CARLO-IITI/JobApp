# 🚀 Intelligent Job Matching Platform

A world-class AI-powered job matching platform that connects talented candidates with their dream opportunities using advanced machine learning algorithms and semantic analysis.

## 🌟 Features

### MVP (Phase 1)
- ✅ User Registration & Authentication (JWT-based)
- ✅ Dual Role System (Candidates & Recruiters)
- ✅ Advanced Profile Creation with Skills & Projects
- ✅ Job Posting & Management
- ✅ Smart Search & Filtering
- ✅ Application Tracking System

### Phase 2
- 🤖 AI-Powered Smart Matching (Cosine Similarity)
- 📧 Email Notifications (SendGrid)
- 📊 Company Dashboard with Analytics
- 📈 Real-time Application Insights

### Phase 3
- 📄 AI Resume Parser (Apache Tika + SpaCy)
- 🎯 Skill Gap Analysis
- 📅 Interview Scheduling (Google Calendar API)
- ✍️ Assessment Tests & Coding Challenges

### Scale Phase
- 🧠 ML-based Job Recommendations
- 💬 Real-time Chat (WebSocket)
- 🎥 Video Interviews (WebRTC)
- 🔌 API for B2B Integration
- 💎 Premium Features

## 🏗️ Architecture

### Microservices Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                      API Gateway (Port 8080)                 │
└─────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
┌───────▼────────┐   ┌────────▼────────┐   ┌───────▼────────┐
│  User Service  │   │  Job Service    │   │ Matching Service│
│   (Port 8081)  │   │  (Port 8082)    │   │  (Port 8083)   │
└────────────────┘   └─────────────────┘   └────────────────┘
        │                     │                     │
        └─────────────────────┼─────────────────────┘
                              │
                   ┌──────────▼──────────┐
                   │   PostgreSQL DB     │
                   │   Elasticsearch     │
                   │   Neo4j (Optional)  │
                   └─────────────────────┘
```

## 💻 Tech Stack

### Backend
- **Framework**: Spring Boot 3.2
- **Language**: Java 17
- **Security**: Spring Security + JWT
- **Database**: PostgreSQL
- **Search**: Elasticsearch
- **Graph DB**: Neo4j Community (Optional)
- **Message Queue**: RabbitMQ
- **Caching**: Redis

### Frontend
- **Framework**: React 18 with TypeScript
- **State Management**: Redux Toolkit
- **UI Library**: Material-UI (MUI) + Tailwind CSS
- **Forms**: React Hook Form + Yup
- **Charts**: Recharts
- **WebRTC**: Simple-peer

### AI/ML Stack
- **NLP**: Apache Tika, SpaCy
- **ML Libraries**: Apache Mahout, DL4J
- **Embeddings**: Word2Vec (pre-trained)
- **Similarity**: Cosine Similarity Algorithms

### DevOps
- **Containerization**: Docker & Docker Compose
- **CI/CD**: GitHub Actions
- **Cloud**: AWS Free Tier / Heroku
- **Monitoring**: Prometheus + Grafana

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Node.js 18+
- Docker & Docker Compose
- PostgreSQL 14+
- Maven 3.8+

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd JobApp
```

2. **Start with Docker Compose (Recommended)**
```bash
docker-compose up -d
```

3. **Or run manually:**

**Backend:**
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

**Frontend:**
```bash
cd frontend
npm install
npm start
```

### Environment Variables

Create `.env` files in respective directories:

**Backend (.env)**
```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=jobapp
DB_USER=postgres
DB_PASSWORD=your_password

JWT_SECRET=your-super-secret-jwt-key-change-in-production
JWT_EXPIRATION=86400000

ELASTICSEARCH_URL=http://localhost:9200

SENDGRID_API_KEY=your_sendgrid_key
GOOGLE_CALENDAR_API_KEY=your_google_api_key
```

**Frontend (.env)**
```env
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_WS_URL=ws://localhost:8080/ws
```

## 📁 Project Structure

```
JobApp/
├── backend/
│   ├── api-gateway/          # API Gateway Service
│   ├── user-service/         # User Management Service
│   ├── job-service/          # Job Management Service
│   ├── matching-service/     # AI Matching Engine
│   ├── resume-service/       # Resume Parsing Service
│   ├── notification-service/ # Email/SMS Notifications
│   └── common/               # Shared Libraries
├── frontend/
│   ├── public/
│   └── src/
│       ├── components/       # Reusable Components
│       ├── pages/            # Page Components
│       ├── services/         # API Services
│       ├── store/            # Redux Store
│       ├── utils/            # Utility Functions
│       └── styles/           # Global Styles
├── ml-models/                # Trained ML Models
├── docker-compose.yml
└── README.md
```

## 🔑 API Documentation

API documentation is available at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Spec: `http://localhost:8080/v3/api-docs`

## 🧪 Testing

**Backend Tests:**
```bash
cd backend
mvn test
```

**Frontend Tests:**
```bash
cd frontend
npm test
```

**Integration Tests:**
```bash
docker-compose -f docker-compose.test.yml up
```

## 📊 Monitoring & Analytics

- **Application Metrics**: http://localhost:8080/actuator
- **Grafana Dashboard**: http://localhost:3000
- **Prometheus**: http://localhost:9090

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🎯 Roadmap

- [x] MVP with core features
- [x] AI-powered matching
- [ ] Video interview integration
- [ ] Mobile apps (React Native)
- [ ] Blockchain-based credentials
- [ ] Advanced analytics dashboard
- [ ] API marketplace

## 💡 Key Algorithms

### 1. Resume Parsing Pipeline
```
Resume Upload → Apache Tika (Text Extraction) → 
SpaCy NER (Entity Recognition) → 
Word2Vec (Skill Embedding) → 
Structured Data Storage
```

### 2. Job Matching Algorithm
```
Cosine Similarity = (Candidate Skills · Job Requirements) / 
                    (||Candidate Skills|| × ||Job Requirements||)

Final Score = 0.4 × Skills Match + 
              0.3 × Experience Match + 
              0.2 × Location Match + 
              0.1 × Cultural Fit
```

### 3. Collaborative Filtering
```
Graph-based recommendations using Neo4j:
(Candidate)-[:HAS_SKILL]->(Skill)<-[:REQUIRES]-(Job)
(Candidate)-[:SIMILAR_TO]->(Candidate2)-[:APPLIED_TO]->(Job2)
```

## 🌟 Highlights

- **Zero API Costs**: Uses free tiers and open-source tools
- **Production Ready**: Includes monitoring, logging, and error handling
- **Scalable**: Microservices architecture for horizontal scaling
- **Modern UI**: Beautiful, responsive design with Material-UI
- **AI-Powered**: Intelligent matching using ML algorithms
- **Real-time**: WebSocket-based notifications and chat

## 📧 Support

For support, email support@jobmatchplatform.com or join our Slack channel.

---

**Built with ❤️ for connecting talent with opportunity**

