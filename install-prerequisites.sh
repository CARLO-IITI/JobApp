#!/bin/bash

echo "🔧 Installing JobApp Prerequisites (No Docker Required)"
echo "========================================================"

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Check if Homebrew is installed
if ! command -v brew &> /dev/null; then
    echo -e "${YELLOW}Homebrew not found. Installing Homebrew...${NC}"
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
else
    echo -e "${GREEN}✓ Homebrew already installed${NC}"
fi

# Install Java 17
echo -e "${BLUE}Installing Java 17...${NC}"
if java -version 2>&1 | grep -q "version \"17"; then
    echo -e "${GREEN}✓ Java 17 already installed${NC}"
else
    brew install openjdk@17
    echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
    export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
    echo -e "${GREEN}✓ Java 17 installed${NC}"
fi

# Install Node.js
echo -e "${BLUE}Installing Node.js...${NC}"
if command -v node &> /dev/null; then
    echo -e "${GREEN}✓ Node.js already installed ($(node --version))${NC}"
else
    brew install node@18
    echo -e "${GREEN}✓ Node.js installed${NC}"
fi

# Install PostgreSQL
echo -e "${BLUE}Installing PostgreSQL...${NC}"
if command -v psql &> /dev/null; then
    echo -e "${GREEN}✓ PostgreSQL already installed${NC}"
else
    brew install postgresql@14
    echo -e "${GREEN}✓ PostgreSQL installed${NC}"
fi

# Start PostgreSQL
echo -e "${BLUE}Starting PostgreSQL...${NC}"
brew services start postgresql@14
sleep 3

# Create database
echo -e "${BLUE}Creating database...${NC}"
createdb jobapp 2>/dev/null
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Database 'jobapp' created${NC}"
else
    echo -e "${YELLOW}Database 'jobapp' already exists or couldn't be created${NC}"
fi

echo ""
echo -e "${GREEN}============================================${NC}"
echo -e "${GREEN}✓ All prerequisites installed successfully!${NC}"
echo -e "${GREEN}============================================${NC}"
echo ""
echo "Next steps:"
echo "1. Run: ./run-local.sh"
echo "2. Wait for all services to start"
echo "3. Open http://localhost:3000 in your browser"
echo ""

