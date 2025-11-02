import { createTheme } from '@mui/material/styles';

const theme = createTheme({
  palette: {
    primary: {
      main: '#0F2B46', // Navy
      light: '#1a4066',
      dark: '#081a2e',
      contrastText: '#fff',
    },
    secondary: {
      main: '#10B981', // Emerald Green
      light: '#34d399',
      dark: '#059669',
      contrastText: '#fff',
    },
    success: {
      main: '#10b981', // Emerald Green
    },
    error: {
      main: '#EF4444', // Error Red
    },
    warning: {
      main: '#f59e0b',
    },
    info: {
      main: '#0F2B46', // Navy
    },
    background: {
      default: '#F8F9FA', // Off-White
      paper: '#ffffff',
    },
    text: {
      primary: '#212529', // Text Black
      secondary: '#6C757D', // Cool Gray
    },
  },
  typography: {
    fontFamily: '"Inter", "Roboto", "Helvetica", "Arial", sans-serif',
    h1: {
      fontWeight: 700,
      fontSize: '2.5rem',
    },
    h2: {
      fontWeight: 700,
      fontSize: '2rem',
    },
    h3: {
      fontWeight: 600,
      fontSize: '1.75rem',
    },
    h4: {
      fontWeight: 600,
      fontSize: '1.5rem',
    },
    h5: {
      fontWeight: 600,
      fontSize: '1.25rem',
    },
    h6: {
      fontWeight: 600,
      fontSize: '1rem',
    },
    button: {
      textTransform: 'none',
      fontWeight: 600,
    },
  },
  shape: {
    borderRadius: 12,
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: '10px',
          padding: '12px 28px',
          fontSize: '0.95rem',
          fontWeight: 600,
          textTransform: 'none',
          boxShadow: 'none',
          transition: 'all 0.3s ease',
          '&:hover': {
            boxShadow: '0 6px 20px rgba(15, 43, 70, 0.15)',
            transform: 'translateY(-2px)',
          },
        },
        contained: {
          background: 'linear-gradient(135deg, #0F2B46 0%, #10B981 100%)',
          '&:hover': {
            background: 'linear-gradient(135deg, #081a2e 0%, #059669 100%)',
            boxShadow: '0 8px 25px rgba(15, 43, 70, 0.25)',
          },
        },
        outlined: {
          borderWidth: '2px',
          '&:hover': {
            borderWidth: '2px',
            background: 'rgba(15, 43, 70, 0.04)',
          },
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: '20px',
          boxShadow: '0 4px 20px rgba(0, 0, 0, 0.08)',
          border: '1px solid rgba(0, 0, 0, 0.06)',
          '&:hover': {
            boxShadow: '0 8px 30px rgba(0, 0, 0, 0.12)',
            transform: 'translateY(-4px)',
          },
          transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
        },
      },
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: '12px',
            backgroundColor: '#FAFBFC',
            transition: 'all 0.3s ease',
            '&:hover': {
              backgroundColor: '#F5F7FA',
            },
            '&.Mui-focused': {
              backgroundColor: '#ffffff',
              boxShadow: '0 0 0 3px rgba(15, 43, 70, 0.1)',
            },
          },
        },
      },
    },
    MuiChip: {
      styleOverrides: {
        root: {
          borderRadius: '8px',
          fontWeight: 600,
          padding: '4px 8px',
          transition: 'all 0.2s ease',
          '&:hover': {
            transform: 'scale(1.05)',
          },
        },
        colorPrimary: {
          background: 'linear-gradient(135deg, #0F2B46 0%, #10B981 100%)',
        },
      },
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          backgroundImage: 'none',
        },
        elevation1: {
          boxShadow: '0 2px 12px rgba(0, 0, 0, 0.08)',
        },
      },
    },
  },
});

export default theme;

