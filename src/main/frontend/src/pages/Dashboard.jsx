import { useState, useEffect, useMemo, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import LiquidGlass from 'liquid-glass-react'
import { useTheme } from '../context/ThemeContext'
import {
  getStatus, startServers as apiStartServers, getGadgets,
  generatePayload as apiGenerate, updateConfig, setAuthToken
} from '../api'

export default function Dashboard() {
  const navigate = useNavigate()
  const { theme, toggleTheme } = useTheme()
  const containerRef = useRef(null)
  const [status, setStatus] = useState({})
  const [loading, setLoading] = useState(false)
  const [msg, setMsg] = useState({ success: '', error: '' })
  const [activeTab, setActiveTab] = useState('servers')
  const [gadgets, setGadgets] = useState([])
  const [gadgetSearch, setGadgetSearch] = useState('')
  const [selectedGadget, setSelectedGadget] = useState('')
  const [payloadCmd, setPayloadCmd] = useState('')
  const [payloadResult, setPayloadResult] = useState('')

  const [serverCfg, setServerCfg] = useState({
    ldap: false, ldaps: false, http: false, rmi: false
  })

  const [configForm, setConfigForm] = useState({
    ip: '', ldapPort: 1389, ldapsPort: 1669, httpPort: 3456, rmiPort: 1099, codeBase: ''
  })

  const filteredGadgets = useMemo(() => {
    if (!gadgetSearch) return gadgets
    return gadgets.filter(g => g.name.toLowerCase().includes(gadgetSearch.toLowerCase()))
  }, [gadgetSearch, gadgets])

  async function loadStatus() {
    try {
      const res = await getStatus()
      setStatus(res.data)
      setConfigForm(prev => ({
        ...prev,
        ip: res.data.ip || '',
        ldapPort: res.data.ldapPort || 1389,
        ldapsPort: res.data.ldapsPort || 1669,
        httpPort: res.data.httpPort || 3456,
        rmiPort: res.data.rmiPort || 1099,
        codeBase: res.data.codeBase || ''
      }))
    } catch (e) {
      if (e.response && e.response.status === 401) {
        setAuthToken(null)
        navigate('/login', { replace: true })
      }
    }
  }

  async function loadGadgets() {
    try {
      const res = await getGadgets()
      setGadgets(res.data)
    } catch (e) { /* ignore */ }
  }

  async function handleStartServers() {
    setLoading(true)
    setMsg({ success: '', error: '' })
    try {
      const res = await apiStartServers({
        ldap: serverCfg.ldap, ldaps: serverCfg.ldaps,
        http: serverCfg.http, rmi: serverCfg.rmi,
        ip: configForm.ip, ldapPort: configForm.ldapPort,
        ldapsPort: configForm.ldapsPort, httpPort: configForm.httpPort,
        rmiPort: configForm.rmiPort
      })
      if (res.data.success) {
        setMsg({ success: 'Servers starting: ' + (res.data.started.join(', ') || 'none'), error: '' })
      }
      setTimeout(loadStatus, 2000)
    } catch (e) {
      setMsg({ success: '', error: 'Failed to start servers' })
    } finally { setLoading(false) }
  }

  async function handleSaveConfig() {
    setLoading(true)
    setMsg({ success: '', error: '' })
    try {
      await updateConfig(configForm)
      setMsg({ success: 'Configuration saved', error: '' })
      loadStatus()
    } catch (e) {
      setMsg({ success: '', error: 'Failed to save configuration' })
    } finally { setLoading(false) }
  }

  async function handleGeneratePayload() {
    setLoading(true)
    setMsg({ success: '', error: '' })
    try {
      const res = await apiGenerate({ gadget: selectedGadget, command: payloadCmd })
      if (res.data.success) {
        setPayloadResult(res.data.message || 'Payload generated successfully')
        setMsg({ success: 'Payload generated', error: '' })
      } else {
        setPayloadResult('Error: ' + (res.data.error || 'unknown'))
        setMsg({ success: '', error: res.data.error || 'Generation failed' })
      }
    } catch (e) {
      setMsg({ success: '', error: 'Failed to generate payload' })
    } finally { setLoading(false) }
  }

  function logout() {
    setAuthToken(null)
    navigate('/login', { replace: true })
  }

  useEffect(() => { loadStatus(); loadGadgets() }, [])

  const liquidProps = {
    mouseContainer: containerRef,
    elasticity: 0.07,
    blurAmount: 0.042,
    saturation: 120,
    displacementScale: 36,
    cornerRadius: 24,
    overLight: theme === 'light',
  }

  return (
    <div ref={containerRef}>
      <div className="dashboard-bg" />

      <div className="page-shell">
        <LiquidGlass {...liquidProps} style={{ marginBottom: 20 }}>
          <div className="header">
            <h1>
              <span className="logo-dot" />
              JYso
            </h1>
            <div className="header-right">
              <button
                className="theme-toggle"
                onClick={toggleTheme}
                aria-label={theme === 'light' ? 'Switch to dark mode' : 'Switch to light mode'}
                title={theme === 'light' ? 'Dark mode' : 'Light mode'}
              >
                <span className="pill-sun" style={{ position: 'absolute', top: 4, left: 5, opacity: theme === 'dark' ? 0.35 : 1, transition: 'opacity 0.2s' }}>
                  &#x2600;&#xFE0F;
                </span>
                <span className="pill-moon" style={{ position: 'absolute', top: 4, right: 5, opacity: theme === 'dark' ? 1 : 0.35, transition: 'opacity 0.2s' }}>
                  &#x1F319;
                </span>
              </button>
              <button className="logout-btn" onClick={logout}>Logout</button>
            </div>
          </div>
        </LiquidGlass>

        {msg.success && <div className="success-msg">{msg.success}</div>}
        {msg.error && <div className="error-msg">{msg.error}</div>}

        <LiquidGlass {...liquidProps} style={{ marginBottom: 20 }}>
          <div className="glass-card">
            <h2>Server Status</h2>
            <div className="status-grid">
              <div className="status-item">
                <span className="status-label">LDAP ({status.ldapPort})</span>
                <span className={'status-value ' + (status.ldapRunning ? 'status-online' : 'status-offline')}>
                  {status.ldapRunning ? 'ONLINE' : 'OFFLINE'}
                </span>
              </div>
              <div className="status-item">
                <span className="status-label">LDAPS ({status.ldapsPort})</span>
                <span className={'status-value ' + (status.ldapsRunning ? 'status-online' : 'status-offline')}>
                  {status.ldapsRunning ? 'ONLINE' : 'OFFLINE'}
                </span>
              </div>
              <div className="status-item">
                <span className="status-label">HTTP ({status.httpPort})</span>
                <span className={'status-value ' + (status.httpRunning ? 'status-online' : 'status-offline')}>
                  {status.httpRunning ? 'ONLINE' : 'OFFLINE'}
                </span>
              </div>
              <div className="status-item">
                <span className="status-label">RMI ({status.rmiPort})</span>
                <span className={'status-value ' + (status.rmiRunning ? 'status-online' : 'status-offline')}>
                  {status.rmiRunning ? 'ONLINE' : 'OFFLINE'}
                </span>
              </div>
              <div className="status-item">
                <span className="status-label">IP Address</span>
                <span className="status-value" style={{ color: 'var(--accent)' }}>{status.ip || '0.0.0.0'}</span>
              </div>
              <div className="status-item">
                <span className="status-label">Version</span>
                <span className="status-value" style={{ color: 'var(--accent)' }}>{status.version || '-'}</span>
              </div>
            </div>
          </div>
        </LiquidGlass>

        <LiquidGlass {...liquidProps}>
          <div className="glass-card">
            <h2>Controls</h2>
            <div className="control-bar">
              <button className="btn btn-primary" style={{ width: 'auto' }} onClick={loadStatus}>
                Refresh Status
              </button>
              <button
                className={'btn btn-secondary' + (activeTab === 'servers' ? ' active-tab' : '')}
                onClick={() => setActiveTab('servers')}>Servers</button>
              <button
                className={'btn btn-secondary' + (activeTab === 'config' ? ' active-tab' : '')}
                onClick={() => setActiveTab('config')}>Configuration</button>
              <button
                className={'btn btn-secondary' + (activeTab === 'payload' ? ' active-tab' : '')}
                onClick={() => setActiveTab('payload')}>Payload Generator</button>
            </div>

            {activeTab === 'servers' && (
              <div>
                <div className="grid-2">
                  <label className="form-group">
                    <input type="checkbox" checked={serverCfg.ldap}
                      onChange={e => setServerCfg({ ...serverCfg, ldap: e.target.checked })} /> LDAP
                  </label>
                  <label className="form-group">
                    <input type="checkbox" checked={serverCfg.ldaps}
                      onChange={e => setServerCfg({ ...serverCfg, ldaps: e.target.checked })} /> LDAPS
                  </label>
                  <label className="form-group">
                    <input type="checkbox" checked={serverCfg.http}
                      onChange={e => setServerCfg({ ...serverCfg, http: e.target.checked })} /> HTTP
                  </label>
                  <label className="form-group">
                    <input type="checkbox" checked={serverCfg.rmi}
                      onChange={e => setServerCfg({ ...serverCfg, rmi: e.target.checked })} /> RMI
                  </label>
                </div>
                <button className="btn btn-primary" style={{ marginTop: 16 }} onClick={handleStartServers} disabled={loading}>
                  {loading ? 'Starting...' : 'Start Selected Servers'}
                </button>
              </div>
            )}

            {activeTab === 'config' && (
              <div>
                <div className="config-form">
                  <div className="form-group">
                    <label>IP Address</label>
                    <input type="text" value={configForm.ip}
                      onChange={e => setConfigForm({ ...configForm, ip: e.target.value })} />
                  </div>
                  <div className="form-group">
                    <label>LDAP Port</label>
                    <input type="number" value={configForm.ldapPort}
                      onChange={e => setConfigForm({ ...configForm, ldapPort: parseInt(e.target.value) || 1389 })} />
                  </div>
                  <div className="form-group">
                    <label>LDAPS Port</label>
                    <input type="number" value={configForm.ldapsPort}
                      onChange={e => setConfigForm({ ...configForm, ldapsPort: parseInt(e.target.value) || 1669 })} />
                  </div>
                  <div className="form-group">
                    <label>HTTP Port</label>
                    <input type="number" value={configForm.httpPort}
                      onChange={e => setConfigForm({ ...configForm, httpPort: parseInt(e.target.value) || 3456 })} />
                  </div>
                  <div className="form-group">
                    <label>RMI Port</label>
                    <input type="number" value={configForm.rmiPort}
                      onChange={e => setConfigForm({ ...configForm, rmiPort: parseInt(e.target.value) || 1099 })} />
                  </div>
                  <div className="form-group">
                    <label>CodeBase URL</label>
                    <input type="text" value={configForm.codeBase} placeholder="http://x.x.x.x:port/"
                      onChange={e => setConfigForm({ ...configForm, codeBase: e.target.value })} />
                  </div>
                  <button className="btn btn-primary" onClick={handleSaveConfig} disabled={loading}>
                    {loading ? 'Saving...' : 'Save Configuration'}
                  </button>
                </div>
              </div>
            )}

            {activeTab === 'payload' && (
              <div className="grid-2">
                <div>
                  <h3 style={{ fontSize: 13, marginBottom: 10, color: 'var(--text-secondary)', fontWeight: 600, textTransform: 'uppercase', letterSpacing: 0.5 }}>
                    Select Gadget
                  </h3>
                  <div className="form-group">
                    <input type="text" placeholder="Search..." value={gadgetSearch}
                      onChange={e => setGadgetSearch(e.target.value)} />
                  </div>
                  <div className="gadget-list">
                    {filteredGadgets.map(g => (
                      <div key={g.name}
                        className={'gadget-item' + (selectedGadget === g.name ? ' selected' : '')}
                        onClick={() => setSelectedGadget(g.name)}>
                        {g.name}
                      </div>
                    ))}
                  </div>
                </div>
                <div>
                  <div className="form-group">
                    <label>Command</label>
                    <textarea rows={4} value={payloadCmd} placeholder="e.g. whoami"
                      onChange={e => setPayloadCmd(e.target.value)} />
                  </div>
                  <button className="btn btn-primary" onClick={handleGeneratePayload}
                    disabled={loading || !selectedGadget}>
                    {loading ? 'Generating...' : 'Generate Payload'}
                  </button>
                  {payloadResult && (
                    <div className="payload-output">{payloadResult}</div>
                  )}
                </div>
              </div>
            )}
          </div>
        </LiquidGlass>
      </div>
    </div>
  )
}
