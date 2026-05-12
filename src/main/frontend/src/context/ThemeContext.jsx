import { createContext, useContext, useEffect } from 'react'
import { useLocation } from 'react-router-dom'

const ThemeContext = createContext()

export function ThemeProvider({ children }) {
  const location = useLocation()
  const theme = location.pathname === '/login' ? 'dark' : 'light'

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme)
  }, [theme])

  return (
    <ThemeContext.Provider value={{ theme }}>
      {children}
    </ThemeContext.Provider>
  )
}

export function useTheme() {
  const ctx = useContext(ThemeContext)
  if (!ctx) throw new Error('useTheme must be inside ThemeProvider')
  return ctx
}
