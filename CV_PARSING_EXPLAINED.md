# 📄 CV Parsing & Data Extraction System

## 🤖 How It Works

### Architecture Overview
```
User Uploads CV → Frontend → Resume Service (Port 8084) → Apache Tika Parser
                                                          ↓
                          Extracted Data ← Regex & NLP Processing
                                                          ↓
                          Auto-fill Profile ← Key-Value Mapping
```

---

## 🔧 Technology Stack

### 1. **Apache Tika** (Document Parser)
- **Purpose**: Extract raw text from various file formats
- **Supports**: PDF, DOC, DOCX, TXT, RTF, HTML
- **How it works**: 
  - Reads binary file
  - Identifies format
  - Extracts pure text content
  - Preserves structure (headings, sections)

### 2. **Regex Pattern Matching** (Information Extraction)
- **Purpose**: Find specific data patterns in text
- **Extracts**:
  - **Email**: `[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}`
  - **Phone**: `\+?\d{1,3}[-.\\s]?\(?\d{3}\)?[-.\\s]?\d{3}[-.\\s]?\d{4}`
  - **Education**: `(Bachelor|Master|PhD|B\.?S\.?|M\.?S\.?|MBA)[^\n]*`
  - **Experience**: `(\d+)\+?\s*years?\s*(?:of\s*)?experience`

### 3. **Skill Extraction** (NLP-based)
- **Method**: Dictionary matching with 50+ tech keywords
- **Skills Library**:
  ```java
  - Programming: Java, Python, JavaScript, C++, Go, Rust
  - Frameworks: React, Angular, Spring, Django, Flask
  - Databases: SQL, PostgreSQL, MongoDB, Redis
  - Cloud: AWS, Azure, GCP, Docker, Kubernetes
  - ML/AI: Machine Learning, Deep Learning, Data Science
  ```

### 4. **Key-Value Mapping**
Extracted data is mapped to profile fields:

```json
{
  "name": "John Doe",              // From document header
  "email": "john@example.com",     // Regex extraction
  "phone": "+1234567890",          // Regex extraction
  "skills": ["Java", "Python"],    // NLP keyword matching
  "education": "B.S. Computer Science", // Pattern matching
  "experience": 5                  // "5 years experience" → 5
}
```

---

## 📊 Data Flow

### Step 1: Upload
```typescript
// Frontend sends file
const formData = new FormData()
formData.append('file', file)
resumeService.parseResume(file)
```

### Step 2: Backend Processing
```java
// Resume Service receives file
@PostMapping("/resume/parse")
public ResponseEntity parseResume(@RequestParam MultipartFile file) {
    // 1. Extract text using Apache Tika
    String text = tika.parseToString(file.getInputStream())
    
    // 2. Extract structured data
    Map<String, Object> data = new HashMap<>();
    data.put("skills", extractSkills(text))
    data.put("email", extractEmail(text))
    data.put("phone", extractPhone(text))
    data.put("education", extractEducation(text))
    data.put("experience", extractExperience(text))
    
    return data;
}
```

### Step 3: Skill Extraction Example
```java
private List<String> extractSkills(String text) {
    List<String> skills = new ArrayList<>();
    String lowerText = text.toLowerCase();
    
    for (String skill : TECH_SKILLS) {
        Pattern pattern = Pattern.compile(
            "\\b" + skill.toLowerCase() + "\\b", 
            Pattern.CASE_INSENSITIVE
        );
        
        if (pattern.matcher(lowerText).find()) {
            skills.add(skill);
        }
    }
    
    return skills;
}
```

### Step 4: Auto-fill Profile
```typescript
// Frontend receives parsed data
const response = await resumeService.parseResume(file)

// Auto-populate form
setProfile({
  ...profile,
  skills: response.data.skills,        // Extracted skills
  yearsOfExperience: response.data.experience,
  education: response.data.education,
  // User can review and edit before saving
})
```

---

## 🎯 Advanced Features (Future Enhancements)

### 1. **Word Embeddings for Skill Matching**
```python
# Using Word2Vec for semantic similarity
from gensim.models import Word2Vec

# Train on job descriptions and resumes
model = Word2Vec(sentences, vector_size=100, window=5, min_count=1)

# Find similar skills
similar_skills = model.wv.most_similar('python', topn=5)
# Output: ['java', 'javascript', 'ruby', 'c++', 'golang']
```

### 2. **Entity Recognition (NER)**
```python
# Using spaCy for Named Entity Recognition
import spacy

nlp = spacy.load("en_core_web_sm")
doc = nlp(resume_text)

# Extract entities
for ent in doc.ents:
    if ent.label_ == "ORG":  # Company names
        companies.append(ent.text)
    if ent.label_ == "DATE":  # Work dates
        dates.append(ent.text)
```

### 3. **Skill Embeddings for Better Matching**
```python
# Create skill vectors
skill_vectors = {
    'python': [0.2, 0.8, 0.1, ...],  # 100-dimensional vector
    'java': [0.3, 0.7, 0.2, ...],
}

# Calculate cosine similarity
from sklearn.metrics.pairwise import cosine_similarity
similarity = cosine_similarity([candidate_vector], [job_vector])
```

### 4. **Inverted Index for Fast Search**
```
Skill → Candidate IDs
{
    'python': [1, 5, 12, 45, 67],
    'java': [1, 12, 34, 56],
    'react': [5, 23, 45, 78]
}

Query: Find candidates with Python AND React
Result: intersection([1,5,12,45,67], [5,23,45,78]) = [5, 45]
```

---

## 🔍 Current Implementation

### Supported File Formats:
- ✅ PDF (.pdf)
- ✅ Microsoft Word (.doc, .docx)
- ✅ Text files (.txt)

### Extracted Fields:
- ✅ Full Name
- ✅ Email Address
- ✅ Phone Number
- ✅ Skills (50+ tech keywords)
- ✅ Years of Experience
- ✅ Education & Degree
- ✅ University

### Validation:
- Max file size: 5MB
- File type validation
- Text extraction fallback

---

## 💡 Usage Tips

### For Best Results:
1. **Use standard CV format** (chronological or functional)
2. **Include clear section headers**: Skills, Experience, Education
3. **List skills explicitly**: "Skills: Java, Python, React"
4. **Mention years of experience**: "5 years of experience in..."
5. **Standard contact info format**

### After Upload:
1. ✨ Skills auto-populate
2. 📊 Experience calculated
3. 🎓 Education extracted
4. ✏️ **Review and edit** all fields
5. 💾 Save to profile

---

## 🚀 Next Level Features (To Implement)

### 1. **Semantic Search**
- Use embeddings to find similar skills
- "JavaScript" → also matches "JS", "TypeScript", "Node.js"

### 2. **Experience Timeline**
- Extract work history
- Map: Company → Role → Duration

### 3. **Skill Categorization**
```
{
  "languages": ["Java", "Python"],
  "frameworks": ["React", "Spring"],
  "databases": ["PostgreSQL", "MongoDB"],
  "cloud": ["AWS", "Docker"]
}
```

### 4. **Confidence Scores**
- Each extracted field gets confidence score
- Low confidence → ask user to verify

### 5. **Multi-language Support**
- Parse resumes in different languages
- Translate to English for matching

---

## 📈 Data Storage

### Database Schema:
```sql
CREATE TABLE candidate_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    skills TEXT[],  -- Array of skills
    years_of_experience INTEGER,
    education TEXT,
    -- Embeddings for advanced matching
    skill_embedding VECTOR(100),  -- Future: pgvector
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Index for fast skill search
CREATE INDEX idx_skills ON candidate_profiles USING GIN(skills);
```

---

## 🎯 Matching Algorithm

### Current: Cosine Similarity
```
Similarity = (A · B) / (||A|| × ||B||)

Where:
A = Candidate skill vector
B = Job requirement vector
```

### Weights:
- Skills Match: 40%
- Experience Match: 30%
- Location Match: 20%
- Salary Match: 10%

---

Your CV parsing system is now ready! 🚀
Upload a CV and watch the magic happen!

