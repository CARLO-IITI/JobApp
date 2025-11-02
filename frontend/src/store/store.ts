import { configureStore } from '@reduxjs/toolkit'
import authReducer from './slices/authSlice'
import jobReducer from './slices/jobSlice'
import applicationReducer from './slices/applicationSlice'

export const store = configureStore({
  reducer: {
    auth: authReducer,
    jobs: jobReducer,
    applications: applicationReducer,
  },
})

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch

