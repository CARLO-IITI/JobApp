# 🎯 How to Use Your JobApp Platform

## 🌟 **Everything is NOW WORKING!**

### ✅ Services Running:
- User Service (8081) ✅
- Job Service (8082) ✅  
- Resume Service (8084) ✅
- Frontend (3000) ✅

---

## 👤 **For Job Seekers (Candidates)**

### 1. **Complete Your Profile**

**Go to**: http://localhost:3000/profile

**Click**: "Edit Profile" button

You'll see an amazing profile editor with:

#### 📤 **AI-Powered CV Upload**
1. Click **"Upload Resume"** button (big green button at top)
2. Select your CV file (PDF, DOC, DOCX)
3. ✨ **Magic happens!**
   - Skills auto-extracted
   - Experience calculated
   - Education parsed
   - All fields pre-filled
4. Review the extracted data
5. Edit anything that needs correction
6. Click **"Save Profile"**

#### ⌨️ **Manual Entry (or edit extracted data)**

**Add Skills:**
- Type: `Python` → Press **Enter**
- Type: `React` → Press **Enter**
- Type: `Java` → Press **Enter**
- Each skill appears as a chip
- Click **X** to remove

**Add Locations:**
- Same process: Type → Press Enter
- Add multiple preferred locations

**Add Languages:**
- Type → Press Enter
- English, Spanish, French, etc.

**Other Fields:**
- Years of experience
- Current job title
- Education & university
- Salary expectations
- Notice period
- Job preferences
- Remote work preference

### 2. **Browse Jobs**

**Go to**: http://localhost:3000/jobs

- Search by keywords
- View job details
- See match scores (coming soon)
- Apply with cover letter

### 3. **Track Applications**

**Go to**: http://localhost:3000/applications

- See all your applications
- Track status changes
- View recruiter feedback

---

## 👔 **For Recruiters**

### 1. **Post a Job**

**Go to**: http://localhost:3000/post-job

**Add Skills** (IMPORTANT!):
1. Find the "Required Skills" field
2. Type skill name: `Java`
3. Press **Enter** (not Tab!)
4. Skill appears as a blue chip
5. Repeat for more skills
6. Fill other fields
7. Click "Post Job"

### 2. **View Applicants**

**Go to**: http://localhost:3000/applications (or click "Applications" in nav)

**You'll see:**
- **Left side**: All your posted jobs
- **Right side**: Click any job → See applicants table

**For each applicant:**
- Candidate ID
- Application date
- Current status
- Match score (if available)
- Cover letter
- **Review button** → Update status, add notes

### 3. **Manage Applications**

**Click "Review" on any application:**
- Update status: Shortlisted, Interview Scheduled, Accepted, Rejected
- Add private recruiter notes
- Save changes

---

## 🤖 How CV Parsing Works

### Upload Flow:
```
1. User uploads CV (PDF/Word)
   ↓
2. Sent to Resume Service (Port 8084)
   ↓
3. Apache Tika extracts text
   ↓
4. Regex patterns extract:
   - Email: john@example.com
   - Phone: +1-555-0123
   - Education: "B.S. Computer Science"
   ↓
5. NLP extracts skills:
   - Searches for 50+ tech keywords
   - "Experienced in Java and Python" → ["Java", "Python"]
   ↓
6. Data returned to frontend
   ↓
7. Profile form auto-populated
   ↓
8. User reviews and saves
```

### Skill Extraction Example:

**CV Contains:**
```
TECHNICAL SKILLS
- Programming Languages: Java, Python, JavaScript
- Frameworks: Spring Boot, React, Django
- Databases: PostgreSQL, MongoDB
- Cloud: AWS, Docker, Kubernetes
```

**Extracted:**
```json
{
  "skills": [
    "Java", "Python", "JavaScript",
    "Spring", "React", "Django",
    "PostgreSQL", "MongoDB",
    "AWS", "Docker", "Kubernetes"
  ]
}
```

### Supported Skills (50+ keywords):
- **Languages**: Java, Python, JavaScript, TypeScript, C++, C#, Ruby, Go, Rust
- **Frontend**: React, Angular, Vue, HTML, CSS
- **Backend**: Spring, Django, Flask, Express, Node.js
- **Databases**: SQL, MySQL, PostgreSQL, MongoDB, Redis, Elasticsearch
- **Cloud**: AWS, Azure, GCP, Docker, Kubernetes, Jenkins
- **AI/ML**: Machine Learning, Deep Learning, Data Science, AI
- **Other**: Git, REST, GraphQL, Microservices, Agile, Scrum

---

## 📱 Quick Actions Guide

### Upload & Parse CV:
1. Profile → Edit Profile
2. Click "Upload Resume"
3. Select file
4. Wait 2-3 seconds
5. See auto-filled data!
6. Save

### Add Skills Manually:
1. Profile → Edit Profile
2. Find "Add Skills" field
3. Type skill name
4. **Press Enter** (important!)
5. Repeat for more skills
6. Save

### Post a Job:
1. Click "Post Job"
2. Fill title, company, description
3. **Add skills**: Type → Enter
4. Fill experience, salary
5. Post Job

### View Applicants:
1. Click "Applications"
2. Click any job on left
3. See applicants on right
4. Click "Review" to manage

---

## 🎨 UI Tips

- **Navy buttons** = Primary actions
- **Green chips** = Success/positive
- **Red chips** = Rejected/errors
- **Press Enter** to add skills/locations/languages (not Tab!)
- **Click X** on chips to remove them

---

## 🔧 Troubleshooting

### CV Not Parsing?
- Check file format (PDF, DOC, DOCX only)
- File size < 5MB
- Check Resume Service logs in terminal

### Skills Not Adding?
- Make sure to **press Enter** after typing
- Don't press Tab or click away
- You should see a chip appear immediately

### Can't See Applicants?
- Make sure you posted jobs as recruiter
- Jobs must have applications
- Click on the job in left panel

---

## 📊 Your Data

All data is stored in PostgreSQL:
- Users & authentication
- Job postings
- Applications
- Candidate profiles
- Skills (as arrays)

**Data persistence**: Everything is saved to database, survives restarts!

---

## 🚀 Ready to Use!

**Refresh your browser**: http://localhost:3000

1. **Hard refresh**: Cmd + Shift + R
2. Go to **Profile**
3. Click **"Edit Profile"**
4. Try uploading a CV!
5. Watch the AI extract your data!

---

Enjoy your world-class job matching platform! 🌟

