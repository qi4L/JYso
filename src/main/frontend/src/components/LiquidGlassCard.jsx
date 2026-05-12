import LiquidGlass from 'liquid-glass-react'

export default function LiquidGlassCard({
  children,
  className = '',
  style = {},
  ...props
}) {
  return (
    <div className={`liquid-glass-wrapper ${className}`} style={style}>
      <LiquidGlass
        cornerRadius={28}
        displacementScale={48}
        blurAmount={1.2}
        saturation={160}
        aberrationIntensity={1.5}
        elasticity={0.12}
        mode="standard"
        padding="0px"
        {...props}
      >
        <div className="liquid-glass-inner">
          {children}
        </div>
      </LiquidGlass>
    </div>
  )
}
