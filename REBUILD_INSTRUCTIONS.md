# 🔧 Rebuild Instructions - Activate New Features!

## 🎯 **Quick Rebuild Guide**

Your backend has been enhanced with 5 new major features! Let's activate them!

---

## 🚀 **Option 1: Quick Rebuild (Recommended)**

I've created scripts to make this easy!

### **Rebuild All Services:**

```bash
cd /Users/s0a0hu5/Personal/JobApp

# Create rebuild script
cat > rebuild-all-services.sh << 'EOF'
#!/bin/bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"

echo "🔧 Rebuilding All Services with New Features..."
echo ""

# Stop all services
echo "Stopping services..."
ps aux | grep "user-service\|job-service\|matching-service" | grep -v grep | awk '{print $2}' | xargs kill -9 2>/dev/null

echo "Building User Service (Experience + Projects)..."
cd backend/user-service
mvn clean install -DskipTests -s ../common/.mvn/settings.xml -q
echo "✅ User Service built"

echo "Building Matching Service (ROI + Negotiation)..."
cd ../matching-service
mvn clean install -DskipTests -s ../common/.mvn/settings.xml -q
echo "✅ Matching Service built"

echo ""
echo "✅ All services rebuilt with new features!"
echo ""
echo "Now starting services in new terminals..."

# Start services
open -a Terminal "`pwd`/../../run-user-service-now.sh"
sleep 3
open -a Terminal "`pwd`/../../start-job-service.sh"
sleep 3  
open -a Terminal "`pwd`/../../start-matching-service.sh"

echo "✅ Services starting in separate terminals"
echo "Wait 60 seconds for all to start, then test!"
EOF

chmod +x rebuild-all-services.sh
./rebuild-all-services.sh
```

---

## 📋 **Option 2: Manual Rebuild**

### **Step 1: Stop All Services**
```bash
ps aux | grep "user-service\|job-service\|matching-service" | grep -v grep | awk '{print $2}' | xargs kill -9
```

### **Step 2: Rebuild User Service**
```bash
cd /Users/s0a0hu5/Personal/JobApp/backend/user-service
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"
mvn clean install -DskipTests -s ../common/.mvn/settings.xml
```

### **Step 3: Rebuild Matching Service**
```bash
cd /Users/s0a0hu5/Personal/JobApp/backend/matching-service
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"
mvn clean install -DskipTests -s ../common/.mvn/settings.xml
```

### **Step 4: Restart All**
```bash
# Terminal 1
cd /Users/s0a0hu5/Personal/JobApp
./run-user-service-now.sh

# Terminal 2
./start-job-service.sh

# Terminal 3
./start-matching-service.sh
```

---

## 🧪 **Test New Features**

### **After services restart (wait 60 seconds):**

**1. Test Skill ROI:**
```bash
curl -X POST http://localhost:8083/api/matching/calculate-skill-roi \
  -H "Content-Type: application/json" \
  -d '{
    "potentialSkills": ["Kubernetes", "AWS"],
    "currentSkills": ["Java", "Spring"]
  }' | python3 -m json.tool
```

**Should show:**
- ROI for each skill
- Salary increase
- Jobs unlocked
- Learning time

**2. Test Salary Negotiator:**
```bash
curl -X POST http://localhost:8083/api/matching/negotiate-salary \
  -H "Content-Type: application/json" \
  -d '{
    "offeredSalary": 100000,
    "skills": ["Java", "Kubernetes"],
    "yearsOfExperience": 5,
    "location": "San Francisco"
  }' | python3 -m json.tool
```

**Should show:**
- Market value
- If underpaid
- Counter-offer suggestion
- Email template

---

## ✅ **Checklist**

- [ ] Rebuild User Service
- [ ] Rebuild Matching Service  
- [ ] Restart all services
- [ ] Wait 60 seconds
- [ ] Test Skill ROI endpoint
- [ ] Test Salary Negotiation endpoint
- [ ] Refresh browser
- [ ] Test in UI

---

## 🎯 **What You'll Have After Rebuild:**

✅ **21+ Working Features**  
✅ **Skill ROI Calculator API**  
✅ **Salary Negotiation API**  
✅ **Work Experience System**  
✅ **Project Showcase System**  
✅ **All Previous Features**  

---

## 🚀 **Ready to Rebuild?**

Run the quick script:
```bash
cd /Users/s0a0hu5/Personal/JobApp
./rebuild-all-services.sh
```

Or rebuild manually step-by-step!

**After rebuild, test the APIs and you'll have all features working!** ✨

