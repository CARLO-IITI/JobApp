#!/bin/bash

echo "🎨 Running Frontend Only (Demo Mode)"
echo "====================================="

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

cd "$(dirname "$0")/frontend"

echo -e "${BLUE}Installing dependencies...${NC}"
npm install

echo ""
echo -e "${GREEN}Starting frontend in demo mode...${NC}"
echo ""
echo "Note: This will run the frontend only."
echo "Some features requiring backend will show errors, but you can:"
echo "  • See the beautiful UI"
echo "  • Navigate between pages"
echo "  • View the design and layout"
echo ""
echo "Press Ctrl+C to stop"
echo ""

npm run dev

