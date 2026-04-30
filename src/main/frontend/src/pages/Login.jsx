import { useState, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import LiquidGlass from 'liquid-glass-react'
import { login, setAuthToken } from '../api'
import { useTheme } from '../context/ThemeContext'

export default function Login() {
  const navigate = useNavigate()
  const { theme, toggleTheme } = useTheme()
  const containerRef = useRef(null)
  const [username, setUsername] = useState('qi')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const res = await login(username, password)
      if (res.data && res.data.token) {
        setAuthToken(res.data.token)
        navigate('/dashboard', { replace: true })
      } else {
        setError('Unexpected response from server')
      }
    } catch (e) {
      if (e.response) {
        if (e.response.status === 401) {
          setError('Invalid username or password.')
        } else {
          setError('Server error: ' + e.response.status)
        }
      } else if (e.request) {
        setError('Cannot connect to server. Is it running?')
      } else {
        setError('Error: ' + e.message)
      }
      setPassword('')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="login-wrapper" ref={containerRef}>
      <div style={{ position: 'fixed', top: 22, right: 22, zIndex: 200 }}>
        <button
          className="theme-toggle"
          onClick={toggleTheme}
          aria-label={theme === 'light' ? 'Switch to dark mode' : 'Switch to light mode'}
          title={theme === 'light' ? 'Dark mode' : 'Light mode'}
        />
      </div>
      <LiquidGlass
        mouseContainer={containerRef}
        elasticity={0.10}
        blurAmount={0.045}
        saturation={128}
        displacementScale={44}
        cornerRadius={28}
        aberrationIntensity={1.8}
        overLight={theme === 'light'}
        style={{ width: 380 }}
      >
        <div className="login-inner">
          <h1>
            <span className="logo-dot" style={{ width: 12, height: 12, borderRadius: 6 }} />
            JYso
          </h1>
          <p className="subtitle">JNDI Exploitation Toolkit</p>
          <p className="tagline">Sign in to access the dashboard</p>
          {error && <div className="error-msg">{error}</div>}
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Username</label>
              <input
                type="text"
                value={username}
                onChange={e => setUsername(e.target.value)}
                placeholder="Enter username"
              />
            </div>
            <div className="form-group">
              <label>Password</label>
              <input
                type="password"
                value={password}
                onChange={e => setPassword(e.target.value)}
                placeholder="Enter password"
              />
            </div>
            <button className="btn btn-primary" type="submit" disabled={loading}>
              {loading ? 'Logging in...' : 'Sign In'}
            </button>
          </form>
        </div>
      </LiquidGlass>
    </div>
  )
}
