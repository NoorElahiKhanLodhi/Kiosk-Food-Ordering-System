// Initialize Firebase
const firebaseConfig = {
    apiKey: "AIzaSyCmjF9NtHMjqpbGHPj97oBnDSs8XYIZCts",
    authDomain: "kiosk-app-162.firebaseapp.com",
    databaseURL: "https://kiosk-app-162-default-rtdb.firebaseio.com",
    projectId: "kiosk-app-162",
    storageBucket: "kiosk-app-162.appspot.com",
    messagingSenderId: "115063990425",
    appId: "1:115063990425:web:5e08f8b54794d08b827008",
    measurementId: "G-R7NGKJWRSW"
};

firebase.initializeApp(firebaseConfig);
const db = firebase.database();

function fetchOrders() {
    const ordersContainer = document.getElementById('orders');
    db.ref('orders').on('value', (snapshot) => {
        ordersContainer.innerHTML = ''; // Clear previous content
        const orders = snapshot.val();
        for (let orderId in orders) {
            const order = orders[orderId];
            if (order.status !== 'completed' && order.status !== 'rejected') {
                displayOrder(orderId, order);
            }
        }
    });
}

function displayOrder(orderId, order) {
    const ordersContainer = document.getElementById('orders');
    const orderDiv = document.createElement('div');
    orderDiv.classList.add('order');

    orderDiv.innerHTML = `
        <h2>Order ID: ${orderId}</h2>
        <div class="order-details">
            <p><strong>Amount:</strong> ${order.amt}</p>
            <p><strong>Date:</strong> ${order.date}</p>
            <p><strong>Status:</strong> ${order.status}</p>
            <div id="items-${orderId}"></div>
        </div>
        <button class="complete-btn" onclick="updateOrderStatus('${orderId}', 'completed')">Complete Order</button>
        <button class="reject-btn" onclick="updateOrderStatus('${orderId}', 'rejected')">Reject Order</button>
    `;

    ordersContainer.appendChild(orderDiv);

    fetchItems(orderId);
}

function fetchItems(orderId) {
    db.ref(`carts/${orderId}/Items`).once('value', (snapshot) => {
        const items = snapshot.val();
        const itemsDiv = document.getElementById(`items-${orderId}`);
        itemsDiv.innerHTML = '<h3>Items Ordered:</h3>';
        for (let itemId in items) {
            db.ref(`menu/categories`).once('value', (menuSnapshot) => {
                const categories = menuSnapshot.val();
                let itemDetails;
                for (let category in categories) {
                    if (categories[category].items[itemId]) {
                        itemDetails = categories[category].items[itemId];
                        break;
                    }
                }
                if (itemDetails) {
                    itemsDiv.innerHTML += `
                        <p><strong>${itemDetails.name}</strong> (Qty: ${items[itemId].qty})</p>
                    `;
                }
            });
        }
    });
}

function updateOrderStatus(orderId, status) {
    db.ref(`orders/${orderId}`).update({ status: status })
        .then(() => {
            alert(`Order ${orderId} marked as ${status}`);
        })
        .catch((error) => {
            console.error("Error updating order status: ", error);
        });
}

// Fetch orders on page load
fetchOrders();
