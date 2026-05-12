import { useRef } from 'react'
import LiquidGlass from 'liquid-glass-react'

export default function UserInfo() {
  const containerRef = useRef(null)

  return (
    <div
      ref={containerRef}
      style={{
        width: '100vw',
        height: '100vh',
        position: 'relative',
        overflow: 'hidden',
        background:
          'linear-gradient(180deg, #f4d89d 0%, #f8df9e 15%, #f5c86e 35%, #e4a84a 50%, #b47d35 65%, #7a4e28 80%, #4a2e18 100%)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        fontFamily:
          "'Inter', -apple-system, BlinkMacSystemFont, 'SF Pro Text', 'Segoe UI', sans-serif",
      }}
    >
      {/* Soft cloud texture */}
      <div
        style={{
          position: 'absolute',
          inset: 0,
          background:
            'radial-gradient(ellipse 70% 40% at 25% 18%, rgba(255,255,255,0.35) 0%, transparent 60%),' +
            'radial-gradient(ellipse 50% 30% at 65% 12%, rgba(255,255,255,0.2) 0%, transparent 55%),' +
            'radial-gradient(ellipse 60% 35% at 85% 28%, rgba(255,245,210,0.15) 0%, transparent 50%)',
          pointerEvents: 'none',
        }}
      />

      {/* Sun glow */}
      <div
        style={{
          position: 'absolute',
          top: '38%',
          left: '55%',
          transform: 'translate(-50%, -50%)',
          width: '500px',
          height: '350px',
          background:
            'radial-gradient(ellipse, rgba(255,240,190,0.55) 0%, rgba(255,220,150,0.25) 40%, transparent 70%)',
          pointerEvents: 'none',
        }}
      />

      {/* Water line / horizon */}
      <div
        style={{
          position: 'absolute',
          bottom: '22%',
          left: 0,
          right: 0,
          height: '1px',
          background: 'rgba(80,50,25,0.35)',
          pointerEvents: 'none',
        }}
      />

      {/* Water shimmer */}
      <div
        style={{
          position: 'absolute',
          bottom: 0,
          left: 0,
          right: 0,
          height: '22%',
          background:
            'linear-gradient(180deg, rgba(180,125,53,0.2) 0%, rgba(74,46,24,0.5) 100%)',
          pointerEvents: 'none',
        }}
      />

      {/* Pier silhouette - left side */}
      <svg
        viewBox="0 0 420 700"
        style={{
          position: 'absolute',
          left: '-60px',
          bottom: '-10px',
          width: '380px',
          height: '560px',
          opacity: 0.9,
          pointerEvents: 'none',
        }}
        preserveAspectRatio="xMidYMax meet"
      >
        <g fill="#1e140e">
          {/* Thick main supports */}
          <rect x="25" y="60" width="18" height="640" rx="2" />
          <rect x="75" y="110" width="14" height="590" rx="2" />
          <rect x="120" y="155" width="12" height="545" rx="2" />
          <rect x="160" y="195" width="10" height="505" rx="2" />
          <rect x="195" y="235" width="10" height="465" rx="2" />
          <rect x="225" y="275" width="8" height="425" rx="2" />
          <rect x="250" y="315" width="8" height="385" rx="2" />

          {/* Horizontal deck beams */}
          <rect x="0" y="120" width="230" height="12" rx="2" />
          <rect x="5" y="210" width="225" height="10" rx="2" />
          <rect x="15" y="300" width="215" height="8" rx="2" />
          <rect x="25" y="390" width="205" height="8" rx="2" />
          <rect x="35" y="480" width="195" height="6" rx="2" />

          {/* Cross bracing (diagonal) */}
          <polygon points="34,120 75,210 82,210 38,120" />
          <polygon points="82,210 120,300 127,300 85,210" />
          <polygon points="127,300 160,390 166,390 130,300" />
          <polygon points="166,390 195,480 200,480 170,390" />

          <polygon points="38,210 82,300 75,300 34,210" />
          <polygon points="85,300 127,390 120,390 82,300" />
          <polygon points="130,390 170,480 163,480 127,390" />

          {/* Some broken/jagged top edges to look like ruins */}
          <polygon points="25,60 43,60 40,85 28,80" />
          <polygon points="75,110 89,110 86,130 78,128" />
        </g>
      </svg>

      {/* Birds - right side */}
      <svg
        viewBox="0 0 220 140"
        style={{
          position: 'absolute',
          right: '6%',
          top: '18%',
          width: '180px',
          height: '115px',
          opacity: 0.65,
          pointerEvents: 'none',
        }}
      >
        <g fill="#1e140e">
          {/* Bird 1 */}
          <path d="M175,35 Q181,26 187,35 Q181,31 175,35 Z" />
          {/* Bird 2 */}
          <path d="M130,55 Q137,46 143,55 Q137,51 130,55 Z" />
          {/* Bird 3 */}
          <path d="M160,72 Q166,64 172,72 Q166,68 160,72 Z" />
          {/* Bird 4 */}
          <path d="M195,58 Q200,50 206,58 Q200,54 195,58 Z" />
          {/* Bird 5 - smaller, farther */}
          <path d="M150,28 Q154,22 158,28 Q154,25 150,28 Z" />
        </g>
      </svg>

      <LiquidGlass
        mouseContainer={containerRef}
        displacementScale={70}
        blurAmount={0.08}
        saturation={145}
        aberrationIntensity={2.5}
        elasticity={0.25}
        cornerRadius={40}
        padding="36px 44px"
        style={{
          width: '480px',
          maxWidth: '92vw',
          position: 'relative',
          zIndex: 10,
          marginTop: '-8vh',
          marginLeft: '2vw',
        }}
      >
        <div style={{ color: '#fff' }}>
          <h2
            style={{
              fontSize: '24px',
              fontWeight: 600,
              marginBottom: '24px',
              letterSpacing: '-0.4px',
              textShadow: '0 2px 4px rgba(0,0,0,0.25)',
            }}
          >
            User Info
          </h2>

          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '18px',
              marginBottom: '32px',
            }}
          >
            <div
              style={{
                width: '64px',
                height: '64px',
                borderRadius: '50%',
                background: 'rgba(0,0,0,0.18)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                fontSize: '22px',
                fontWeight: 600,
                color: '#fff',
                border: '1px solid rgba(255,255,255,0.25)',
                textShadow: '0 1px 2px rgba(0,0,0,0.2)',
                flexShrink: 0,
              }}
            >
              JD
            </div>
            <div>
              <div
                style={{
                  fontSize: '22px',
                  fontWeight: 600,
                  letterSpacing: '-0.3px',
                  textShadow: '0 2px 4px rgba(0,0,0,0.25)',
                }}
              >
                John Doe
              </div>
              <div
                style={{
                  fontSize: '15px',
                  fontWeight: 400,
                  color: 'rgba(255,255,255,0.85)',
                  marginTop: '3px',
                  textShadow: '0 1px 2px rgba(0,0,0,0.2)',
                }}
              >
                Software Engineer
              </div>
            </div>
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            <InfoRow label="Email" value="john.doe@example.com" />
            <InfoRow label="Location" value="San Francisco, CA" />
            <InfoRow label="Joined" value="March 2023" />
          </div>
        </div>
      </LiquidGlass>
    </div>
  )
}

function InfoRow({ label, value }) {
  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        fontSize: '16px',
      }}
    >
      <span
        style={{
          color: 'rgba(255,255,255,0.85)',
          fontWeight: 400,
          textShadow: '0 1px 2px rgba(0,0,0,0.2)',
        }}
      >
        {label}:
      </span>
      <span
        style={{
          color: '#fff',
          fontWeight: 500,
          textShadow: '0 1px 2px rgba(0,0,0,0.25)',
        }}
      >
        {value}
      </span>
    </div>
  )
}
