const SearchIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/>
  </svg>
)

export default function GadgetSelector({ value, onChange, items, searchValue, onSearchChange, open, onOpenChange, selectedGadget, label }) {
  return (
    <div style={{ position: 'relative' }}>
      <div className="form-group input-icon-wrap">
        <label>{label || 'Gadget'}</label>
        <input
          type="text"
          value={value}
          placeholder="Type or search..."
          onFocus={() => onOpenChange(true)}
          onBlur={() => setTimeout(() => onOpenChange(false), 150)}
          onChange={e => { onChange(e.target.value); if (onSearchChange) onSearchChange(e.target.value); }}
          style={{ cursor: 'text' }}
        />
        <span className="input-icon"><SearchIcon /></span>
      </div>
      {open && (
        <div className="gadget-dropdown" style={{ top: 'calc(100% - 8px)' }}>
          <div className="gadget-list" style={{ maxHeight: 180, border: 'none', borderRadius: 12 }}>
            {items.map(g => (
              <div
                key={g.name || g}
                className={'gadget-item' + (selectedGadget === (g.name || g) ? ' selected' : '')}
                onMouseDown={e => {
                  e.preventDefault()
                  const name = g.name || g
                  onChange(name)
                  if (onSearchChange) onSearchChange(name)
                  onOpenChange(false)
                }}
              >
                {g.name || g}
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}
