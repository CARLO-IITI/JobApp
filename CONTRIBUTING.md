# 🤝 Contributing to JobApp

Thank you for considering contributing to JobApp! We welcome contributions from the community.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Workflow](#development-workflow)
- [Coding Standards](#coding-standards)
- [Commit Guidelines](#commit-guidelines)
- [Pull Request Process](#pull-request-process)
- [Testing](#testing)

## Code of Conduct

- Be respectful and inclusive
- Accept constructive criticism
- Focus on what is best for the community
- Show empathy towards other community members

## Getting Started

1. **Fork the repository**
   ```bash
   git clone https://github.com/your-username/JobApp.git
   cd JobApp
   ```

2. **Set up your development environment**
   ```bash
   ./setup.sh
   ./start-dev.sh
   ```

3. **Create a branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

## Development Workflow

### Backend Development

1. Make changes to the relevant service
2. Test locally
3. Run tests: `mvn test`
4. Build: `mvn clean install`

### Frontend Development

1. Make changes to React components
2. Test in browser: `npm run dev`
3. Run linter: `npm run lint`
4. Build: `npm run build`

## Coding Standards

### Java (Backend)

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Keep methods small and focused
- Use Lombok to reduce boilerplate

**Example:**
```java
/**
 * Creates a new job posting
 * @param jobDTO Job data transfer object
 * @return Created job with ID
 */
@Transactional
public JobDTO createJob(JobDTO jobDTO) {
    Job job = mapToEntity(jobDTO);
    job = jobRepository.save(job);
    return mapToDTO(job);
}
```

### TypeScript/React (Frontend)

- Follow [Airbnb JavaScript Style Guide](https://github.com/airbnb/javascript)
- Use functional components with hooks
- Use TypeScript for type safety
- Keep components small and reusable
- Use meaningful component and variable names

**Example:**
```typescript
interface JobCardProps {
  job: Job
  onApply: (jobId: number) => void
}

const JobCard: React.FC<JobCardProps> = ({ job, onApply }) => {
  return (
    <Card>
      <CardContent>
        <Typography variant="h6">{job.title}</Typography>
        <Button onClick={() => onApply(job.id)}>Apply</Button>
      </CardContent>
    </Card>
  )
}
```

### General Guidelines

- **DRY**: Don't Repeat Yourself
- **SOLID**: Follow SOLID principles
- **KISS**: Keep It Simple, Stupid
- Write self-documenting code
- Add comments for complex logic
- Handle errors gracefully
- Log important events

## Commit Guidelines

We follow [Conventional Commits](https://www.conventionalcommits.org/):

### Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

### Examples

```bash
feat(jobs): add job filtering by salary range

- Added min/max salary filters
- Updated search UI
- Added backend query support

Closes #123
```

```bash
fix(auth): resolve JWT token expiration issue

The token was expiring too quickly due to incorrect
time calculation.

Fixes #456
```

## Pull Request Process

1. **Update your branch**
   ```bash
   git checkout main
   git pull origin main
   git checkout feature/your-feature
   git rebase main
   ```

2. **Run tests**
   ```bash
   # Backend
   mvn test
   
   # Frontend
   npm test
   ```

3. **Push your changes**
   ```bash
   git push origin feature/your-feature
   ```

4. **Create Pull Request**
   - Go to GitHub
   - Click "New Pull Request"
   - Fill in the PR template
   - Link related issues

5. **PR Template**
   ```markdown
   ## Description
   Brief description of changes
   
   ## Type of Change
   - [ ] Bug fix
   - [ ] New feature
   - [ ] Breaking change
   - [ ] Documentation update
   
   ## Testing
   - [ ] Unit tests pass
   - [ ] Integration tests pass
   - [ ] Manual testing completed
   
   ## Checklist
   - [ ] Code follows style guidelines
   - [ ] Self-review completed
   - [ ] Comments added for complex code
   - [ ] Documentation updated
   - [ ] No new warnings generated
   
   ## Screenshots (if applicable)
   
   ## Related Issues
   Closes #123
   ```

6. **Code Review**
   - Address reviewer comments
   - Make requested changes
   - Update PR

7. **Merge**
   - Squash commits if necessary
   - Merge when approved

## Testing

### Backend Testing

**Unit Tests:**
```java
@Test
public void testCreateJob() {
    JobDTO jobDTO = createTestJobDTO();
    JobDTO created = jobService.createJob(jobDTO);
    
    assertNotNull(created.getId());
    assertEquals(jobDTO.getTitle(), created.getTitle());
}
```

**Integration Tests:**
```java
@SpringBootTest
@AutoConfigureMockMvc
public class JobControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void testGetAllJobs() throws Exception {
        mockMvc.perform(get("/api/jobs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }
}
```

### Frontend Testing

**Component Tests:**
```typescript
import { render, screen } from '@testing-library/react'
import JobCard from './JobCard'

test('renders job title', () => {
  const job = { id: 1, title: 'Developer', ... }
  render(<JobCard job={job} onApply={() => {}} />)
  expect(screen.getByText('Developer')).toBeInTheDocument()
})
```

## Feature Requests

1. Check if the feature already exists
2. Search existing issues
3. Create a new issue with:
   - Clear description
   - Use cases
   - Expected behavior
   - Any relevant mockups

## Bug Reports

When reporting bugs, include:

1. **Description**: Clear description of the bug
2. **Steps to Reproduce**: Detailed steps
3. **Expected Behavior**: What should happen
4. **Actual Behavior**: What actually happens
5. **Screenshots**: If applicable
6. **Environment**:
   - OS
   - Browser (for frontend)
   - Java version (for backend)
   - Node version (for frontend)

**Template:**
```markdown
## Bug Description
Brief description

## Steps to Reproduce
1. Go to...
2. Click on...
3. See error

## Expected Behavior
Should show...

## Actual Behavior
Shows error...

## Screenshots
[Attach screenshots]

## Environment
- OS: macOS 13.0
- Browser: Chrome 120
- Java: 17
- Node: 18.16
```

## Documentation

- Update README.md for major changes
- Add JSDoc/JavaDoc for new code
- Update API documentation
- Add inline comments for complex logic

## Questions?

- Open an issue
- Join our Discord server
- Email: dev@jobapp.com

Thank you for contributing! 🎉

