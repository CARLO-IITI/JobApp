import { Routes, Route, Navigate } from 'react-router-dom'
import { Box } from '@mui/material'
import { useSelector } from 'react-redux'
import { RootState } from './store/store'

// Pages
import LandingPage from './pages/LandingPage'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import DashboardPage from './pages/DashboardPage'
import JobsPage from './pages/JobsPage'
import JobDetailPage from './pages/JobDetailPage'
import ProfilePage from './pages/ProfilePage'
import EditProfilePage from './pages/EditProfilePage'
import ApplicationsPage from './pages/ApplicationsPage'
import PostJobPage from './pages/PostJobPage'
import RecruiterApplicationsPage from './pages/RecruiterApplicationsPage'
import RejectionFeedbackPage from './pages/RejectionFeedbackPage'
import SkillROICalculatorPage from './pages/SkillROICalculatorPage'

// Layout
import Layout from './components/Layout/Layout'

// Protected Route Component
const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
  const { isAuthenticated } = useSelector((state: RootState) => state.auth)
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" />
}

function App() {
  return (
    <Box sx={{ minHeight: '100vh' }}>
      <Routes>
        {/* Public Routes */}
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        {/* Protected Routes */}
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <Layout>
                <DashboardPage />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/jobs"
          element={
            <ProtectedRoute>
              <Layout>
                <JobsPage />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/jobs/:jobId"
          element={
            <ProtectedRoute>
              <Layout>
                <JobDetailPage />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/profile"
          element={
            <ProtectedRoute>
              <Layout>
                <ProfilePage />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/edit-profile"
          element={
            <ProtectedRoute>
              <Layout>
                <EditProfilePage />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/applications"
          element={
            <ProtectedRoute>
              <Layout>
                <ApplicationsPage />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/manage-applications"
          element={
            <ProtectedRoute>
              <Layout>
                <RecruiterApplicationsPage />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/post-job"
          element={
            <ProtectedRoute>
              <Layout>
                <PostJobPage />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/rejection-feedback/:applicationId"
          element={
            <ProtectedRoute>
              <Layout>
                <RejectionFeedbackPage />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/skill-roi"
          element={
            <ProtectedRoute>
              <Layout>
                <SkillROICalculatorPage />
              </Layout>
            </ProtectedRoute>
          }
        />
      </Routes>
    </Box>
  )
}

export default App

