export default function ServerStatusCard({ label, port, isRunning, toggling, onClick }) {
  return (
    <div
      className={'status-item status-clickable' + (toggling ? ' status-toggling' : '')}
      onClick={onClick}
      title={`Click to toggle ${label} server`}
    >
      <span className="status-label">{label} ({port})</span>
      <span className={'status-value ' + (isRunning ? 'status-online' : 'status-offline')}>
        {isRunning ? 'ONLINE' : 'OFFLINE'}
      </span>
    </div>
  )
}
