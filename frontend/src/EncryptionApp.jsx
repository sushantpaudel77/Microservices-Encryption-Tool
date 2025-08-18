import React, { useState, useEffect } from 'react';
import { Lock, Unlock, User, History, Plus } from 'lucide-react';

const EncryptionApp = () => {
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [textToEncrypt, setTextToEncrypt] = useState('');
  const [textToDecrypt, setTextToDecrypt] = useState('');
  const [algorithm, setAlgorithm] = useState('AES');
  const [encryptionResult, setEncryptionResult] = useState(null);
  const [decryptionResult, setDecryptionResult] = useState(null);
  const [userHistory, setUserHistory] = useState([]);
  const [showCreateUser, setShowCreateUser] = useState(false);
  const [newUser, setNewUser] = useState({ username: '', email: '' });
  const [loading, setLoading] = useState(false);

  const API_BASE = 'http://localhost:8080/api';

  useEffect(() => {
    fetchUsers();
  }, []);

const fetchUsers = async () => {
  try {
    const response = await fetch(`${API_BASE}/users`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    setUsers(data);
  } catch (error) {
    console.error('Error fetching users:', error);
  }
};


  const createUser = async () => {
    try {
      setLoading(true);
      const response = await fetch(`${API_BASE}/users`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newUser)
      });
      
      if (response.ok) {
        const userData = await response.json();
        setUsers([...users, userData]);
        setNewUser({ username: '', email: '' });
        setShowCreateUser(false);
      }
    } catch (error) {
      console.error('Error creating user:', error);
    } finally {
      setLoading(false);
    }
  };

  const encryptText = async () => {
    if (!selectedUser || !textToEncrypt) return;
    
    try {
      setLoading(true);
      const response = await fetch(`${API_BASE}/encryption/encrypt`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          userId: selectedUser.id,
          text: textToEncrypt,
          algorithm: algorithm
        })
      });
      
      const data = await response.json();
      setEncryptionResult(data);
      setTextToDecrypt(data.encryptedText);
      fetchUserHistory();
    } catch (error) {
      console.error('Error encrypting text:', error);
    } finally {
      setLoading(false);
    }
  };

  const decryptText = async () => {
    if (!textToDecrypt || !algorithm) return;
    
    try {
      setLoading(true);
      const response = await fetch(`${API_BASE}/encryption/decrypt`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          encryptedText: textToDecrypt,
          algorithm: algorithm
        })
      });
      
      const data = await response.json();
      setDecryptionResult(data);
    } catch (error) {
      console.error('Error decrypting text:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchUserHistory = async () => {
    if (!selectedUser) return;
    
    try {
      const response = await fetch(`${API_BASE}/encryption/user/${selectedUser.id}/history`);
      const data = await response.json();
      setUserHistory(data);
    } catch (error) {
      console.error('Error fetching user history:', error);
    }
  };

  useEffect(() => {
    if (selectedUser) {
      fetchUserHistory();
    }
  }, [selectedUser]);

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 p-4">
      <div className="max-w-6xl mx-auto">
        <header className="text-center mb-8">
          <h1 className="text-4xl font-bold text-gray-800 mb-2">
            🔐 Encryption & Decryption Tool
          </h1>
          <p className="text-gray-600">Microservices Architecture Demo</p>
        </header>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* User Management Panel */}
          <div className="bg-white rounded-lg shadow-lg p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-xl font-semibold flex items-center">
                <User className="mr-2" size={20} />
                Users
              </h2>
              <button
                onClick={() => setShowCreateUser(!showCreateUser)}
                className="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded-md flex items-center"
              >
                <Plus size={16} className="mr-1" />
                Add
              </button>
            </div>

            {showCreateUser && (
              <div className="mb-4 p-4 bg-gray-50 rounded-lg">
                <input
                  type="text"
                  placeholder="Username"
                  value={newUser.username}
                  onChange={(e) => setNewUser({...newUser, username: e.target.value})}
                  className="w-full p-2 border rounded mb-2"
                />
                <input
                  type="email"
                  placeholder="Email"
                  value={newUser.email}
                  onChange={(e) => setNewUser({...newUser, email: e.target.value})}
                  className="w-full p-2 border rounded mb-2"
                />
                <div className="flex gap-2">
                  <button
                    onClick={createUser}
                    disabled={loading}
                    className="bg-green-500 hover:bg-green-600 text-white px-3 py-1 rounded"
                  >
                    Create
                  </button>
                  <button
                    onClick={() => setShowCreateUser(false)}
                    className="bg-gray-500 hover:bg-gray-600 text-white px-3 py-1 rounded"
                  >
                    Cancel
                  </button>
                </div>
              </div>
            )}

            <div className="space-y-2">
              {users.map(user => (
                <div
                  key={user.id}
                  onClick={() => setSelectedUser(user)}
                  className={`p-3 rounded-lg cursor-pointer transition-colors ${
                    selectedUser?.id === user.id
                      ? 'bg-blue-100 border-blue-300 border'
                      : 'bg-gray-50 hover:bg-gray-100'
                  }`}
                >
                  <div className="font-medium">{user.username}</div>
                  <div className="text-sm text-gray-600">{user.email}</div>
                </div>
              ))}
            </div>
          </div>

          {/* Encryption/Decryption Panel */}
          <div className="bg-white rounded-lg shadow-lg p-6">
            <h2 className="text-xl font-semibold mb-4 flex items-center">
              <Lock className="mr-2" size={20} />
              Encryption Tool
            </h2>

            {!selectedUser && (
              <div className="text-center text-gray-500 py-8">
                Please select a user first
              </div>
            )}

            {selectedUser && (
              <div className="space-y-4">
                <div className="p-3 bg-blue-50 rounded">
                  <strong>Selected User:</strong> {selectedUser.username}
                </div>

                <div>
                  <label className="block text-sm font-medium mb-2">Algorithm</label>
                  <select
                    value={algorithm}
                    onChange={(e) => setAlgorithm(e.target.value)}
                    className="w-full p-2 border rounded"
                  >
                    <option value="AES">AES Encryption</option>
                    <option value="BASE64">Base64 Encoding</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium mb-2">Text to Encrypt</label>
                  <textarea
                    value={textToEncrypt}
                    onChange={(e) => setTextToEncrypt(e.target.value)}
                    placeholder="Enter text to encrypt..."
                    className="w-full p-3 border rounded h-20 resize-none"
                  />
                  <button
                    onClick={encryptText}
                    disabled={loading || !textToEncrypt}
                    className="mt-2 w-full bg-green-500 hover:bg-green-600 disabled:bg-gray-400 text-white py-2 rounded flex items-center justify-center"
                  >
                    <Lock size={16} className="mr-2" />
                    {loading ? 'Encrypting...' : 'Encrypt'}
                  </button>
                </div>

                {encryptionResult && (
                  <div className="p-4 bg-green-50 rounded border border-green-200">
                    <h4 className="font-medium text-green-800 mb-2">Encryption Result:</h4>
                    <p className="text-sm text-green-700 break-all">{encryptionResult.encryptedText}</p>
                    <p className="text-xs text-green-600 mt-2">Algorithm: {encryptionResult.algorithm}</p>
                  </div>
                )}

                <div>
                  <label className="block text-sm font-medium mb-2">Text to Decrypt</label>
                  <textarea
                    value={textToDecrypt}
                    onChange={(e) => setTextToDecrypt(e.target.value)}
                    placeholder="Enter encrypted text to decrypt..."
                    className="w-full p-3 border rounded h-20 resize-none"
                  />
                  <button
                    onClick={decryptText}
                    disabled={loading || !textToDecrypt}
                    className="mt-2 w-full bg-orange-500 hover:bg-orange-600 disabled:bg-gray-400 text-white py-2 rounded flex items-center justify-center"
                  >
                    <Unlock size={16} className="mr-2" />
                    {loading ? 'Decrypting...' : 'Decrypt'}
                  </button>
                </div>

                {decryptionResult && (
                  <div className="p-4 bg-orange-50 rounded border border-orange-200">
                    <h4 className="font-medium text-orange-800 mb-2">Decryption Result:</h4>
                    <p className="text-sm text-orange-700">{decryptionResult.decryptedText}</p>
                    <p className="text-xs text-orange-600 mt-2">Algorithm: {decryptionResult.algorithm}</p>
                  </div>
                )}
              </div>
            )}
          </div>

          {/* History Panel */}
          <div className="bg-white rounded-lg shadow-lg p-6">
            <h2 className="text-xl font-semibold mb-4 flex items-center">
              <History className="mr-2" size={20} />
              Encryption History
            </h2>

            {!selectedUser && (
              <div className="text-center text-gray-500 py-8">
                Select a user to view history
              </div>
            )}

            {selectedUser && userHistory.length === 0 && (
              <div className="text-center text-gray-500 py-8">
                No encryption history yet
              </div>
            )}

            {selectedUser && userHistory.length > 0 && (
              <div className="space-y-3 max-h-96 overflow-y-auto">
                {userHistory.map(record => (
                  <div key={record.id} className="p-3 bg-gray-50 rounded border">
                    <div className="text-xs text-gray-500 mb-1">
                      {new Date(record.createdAt).toLocaleString()}
                    </div>
                    <div className="text-sm">
                      <strong>Original:</strong> 
                      <span className="ml-1 text-gray-700">{record.originalText}</span>
                    </div>
                    <div className="text-sm mt-1">
                      <strong>Encrypted:</strong> 
                      <span className="ml-1 text-gray-600 font-mono text-xs break-all">
                        {record.encryptedText.substring(0, 50)}...
                      </span>
                    </div>
                    <div className="text-xs text-blue-600 mt-1">
                      Algorithm: {record.algorithm}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>

        {/* Microservices Status */}
        <div className="mt-8 bg-white rounded-lg shadow-lg p-6">
          <h2 className="text-xl font-semibold mb-4">🏗️ Microservices Architecture</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="p-4 bg-blue-50 rounded-lg">
              <h3 className="font-medium text-blue-800">API Gateway</h3>
              <p className="text-sm text-blue-600">Port 8080 - Routes requests</p>
            </div>
            <div className="p-4 bg-green-50 rounded-lg">
              <h3 className="font-medium text-green-800">User Service</h3>
              <p className="text-sm text-green-600">Port 8081 - PostgreSQL</p>
            </div>
            <div className="p-4 bg-purple-50 rounded-lg">
              <h3 className="font-medium text-purple-800">Encryption Service</h3>
              <p className="text-sm text-purple-600">Port 8082 - MongoDB</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EncryptionApp;