import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import EncryptionApp from './EncryptionApp'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <EncryptionApp />
  </StrictMode>,
)
