document.addEventListener('DOMContentLoaded', () => {
    const activeChatsDiv = document.getElementById('active-chats');
    const chatMessagesDiv = document.getElementById('chat-messages');
    const messageInput = document.getElementById('message-input');
    const sendBtn = document.getElementById('send-btn');

    let selectedOrderId = null;

    // Fetch active chats from Firebase
    firebase.database().ref('Chats').on('value', (snapshot) => {
        activeChatsDiv.innerHTML = '<h2>Active Chats</h2>';
        snapshot.forEach((childSnapshot) => {
            const orderId = childSnapshot.key;
            const chatItem = document.createElement('div');
            chatItem.className = 'chat-item';
            chatItem.textContent = orderId;
            chatItem.addEventListener('click', () => loadChatMessages(orderId));
            activeChatsDiv.appendChild(chatItem);
        });
    });

    // Load chat messages for the selected order
    function loadChatMessages(orderId) {
        selectedOrderId = orderId;
        chatMessagesDiv.innerHTML = ''; // Clear previous messages

        firebase.database().ref(`Chats/${orderId}`).on('value', (snapshot) => {
            chatMessagesDiv.innerHTML = ''; // Clear and reload messages
            snapshot.forEach((childSnapshot) => {
                const messageData = childSnapshot.val();
                const messageKey = childSnapshot.key;

                const messageContent = messageData.split('::')[1];
                const messageSender = messageData.split('::')[0];

                const messageDiv = document.createElement('div');
                messageDiv.className = `message ${messageSender === 'app' ? 'client' : 'admin'}`;
                messageDiv.textContent = `${messageSender === 'app' ? 'Client' : 'You'}: ${messageContent}`;

                chatMessagesDiv.appendChild(messageDiv);
            });

            // Scroll to the bottom of chat messages
            chatMessagesDiv.scrollTop = chatMessagesDiv.scrollHeight;
        });
    }

    // Send message to Firebase
    sendBtn.addEventListener('click', sendMessage);
    messageInput.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') sendMessage();
    });

    function sendMessage() {
        const message = messageInput.value.trim();
        if (message === '' || !selectedOrderId) return;

        // Prefix the message with "admin::" for admin
        const timestamp = new Date().getTime();
        firebase.database().ref(`Chats/${selectedOrderId}/${timestamp}`).set(`admin::${message}`);

        messageInput.value = ''; // Clear the input field
    }
});
