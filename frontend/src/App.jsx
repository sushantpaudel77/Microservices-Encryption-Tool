import React from 'react'

const App = () => {
  return (
    <div className='min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 p-4'>
      <div className='max-w-6xl mx-auto'>
       <header className='text-center mb-8'>
      <h1 className='text-4xl font-bold text-gray-800 mb-2'>
        Encryption & Descryption Tool
      </h1>
      <p className='text-gray-600'>Microservices Architecture Demo</p>
       </header>

       <div className='grid grid-cols-1 lg:grid-cols-3 gap-6'>
        {/* User management Panel */}
        <div className='bg-white rounded-lg shadow-lg p-6'>
          <div className='flex items-center justify-between mb-4'>
            <h2 className='text-xl font-semibold flex items-center'>
              <User className='mr-2' size={24} />
              Users
            </h2>
            <button 
            onClick={() => setShowCreateUser(!showCreateUser)}
            className='bg-blue-500 hover:bg-blue-600 text-white px-5 rounded-md flex items-center'>
              <Plus size={20} className='mr-1' />
              Add
            </button>
          </div>

          {showCreateUser && (
            <div className='mb-4 p-4 bg-gray-50 rounded-lg'>
              <input
              type='text'
              placeholder='Username'
              value={newUser.username}
            onChange={(e) => setNewUser({ ...newUser, username: e.target.value })}
            className='w-full p-2 border rounded md-2'
            />

            <input
            type='email'
            placeholder='Email'
            value={newUser.email}
            onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
            className='w-full p-2 border rounded mb-2'
            />
            <div className='flex gap-2'>
            <button
            onClick={() => setShowCreateUser(false)}
            className='bg-gray-500 hover:bg-gray-600 text-white px-3 py-1 rounded'>
              Cancel
            </button>
            </div>
            </div>
          )}
            
            <div className='space-y-2'>
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
                  <div className='bg-white rounded-lg shadow-lg p-6'>
                    <h2 className='text-xl font-semibold mb-4 flex items-center'>
                      <Lock className='mr-2' size={20} />
                      Encryption Tool
                    </h2>

{!selectedUser && (
  <div className='text-center text-gray-500 py-8'>
    Please select a user first
  </div>
)}

                  </div>
                </div>
              ))}
            </div>

        </div>
        </div>        
      </div>

    </div>
  )
}

export default App