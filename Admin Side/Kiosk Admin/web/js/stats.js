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

let totalRevenue = 0;
let thisMonthRevenue = 0;
let lastMonthRevenue = 0;
let monthlyRevenues = {};
let lostRevenues = {};

const now = new Date();
const thisMonth = now.getMonth() + 1;
const thisYear = now.getFullYear();

function fetchOrders() {
    db.ref('orders').on('value', (snapshot) => {
        const orders = snapshot.val();
        totalRevenue = 0;
        thisMonthRevenue = 0;
        lastMonthRevenue = 0;
        monthlyRevenues = {};
        lostRevenues = {};

        for (let orderId in orders) {
            const order = orders[orderId];
            const [day, month, year] = order.date.split(':').map(Number);
            const orderMonthYear = `${month}-${year}`;
            const amount = parseFloat(order.amt);

            if (order.status === 'completed') {
                totalRevenue += amount;
                if (month === thisMonth && year === thisYear) {
                    thisMonthRevenue += amount;
                }
                if (month === thisMonth - 1 && year === thisYear) {
                    lastMonthRevenue += amount;
                }
                if (!monthlyRevenues[orderMonthYear]) {
                    monthlyRevenues[orderMonthYear] = 0;
                }
                monthlyRevenues[orderMonthYear] += amount;
            } else if (order.status === 'rejected') {
                if (!lostRevenues[orderMonthYear]) {
                    lostRevenues[orderMonthYear] = 0;
                }
                lostRevenues[orderMonthYear] += amount;
            }
        }

        displayStats();
        renderCharts();
    });
}

function displayStats() {
    document.getElementById('totalRevenue').textContent = `Total Revenue: Rs. ${totalRevenue.toFixed(2)}`;
    document.getElementById('monthlyRevenue').textContent = `This Month's Revenue: Rs. ${thisMonthRevenue.toFixed(2)}`;
    document.getElementById('comparison').textContent = `Comparison with Last Month: ${thisMonthRevenue > lastMonthRevenue ? 'Increased' : 'Decreased'}`;
}

function renderCharts() {
    const months = Object.keys(monthlyRevenues);
    const revenues = Object.values(monthlyRevenues);
    const highestRevenue = Math.max(...revenues);
    const lowestRevenue = Math.min(...revenues);

    const colors = revenues.map(revenue => {
        if (revenue === highestRevenue) return 'green';
        if (revenue === lowestRevenue) return 'red';
        return '#00ffe5';
    });

    // Bar Chart
    new Chart(document.getElementById('monthlyRevenueBarChart'), {
        type: 'bar',
        data: {
            labels: months,
            datasets: [{
                label: 'Monthly Revenue',
                data: revenues,
                backgroundColor: colors,
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: value => `Rs. ${value}`
                    }
                }
            }
        }
    });

    // Pie Chart
    new Chart(document.getElementById('monthlyRevenuePieChart'), {
        type: 'pie',
        data: {
            labels: months,
            datasets: [{
                label: 'Monthly Revenue',
                data: revenues,
                backgroundColor: colors,
            }]
        },
        options: {
            plugins: {
                tooltip: {
                    callbacks: {
                        label: (context) => `Rs. ${context.raw}`
                    }
                }
            }
        }
    });

    // Line Chart
    new Chart(document.getElementById('monthlyRevenueLineChart'), {
        type: 'line',
        data: {
            labels: months,
            datasets: [{
                label: 'Monthly Revenue',
                data: revenues,
                borderColor: '#00ffe5',
                backgroundColor: 'rgba(0, 255, 229, 0.2)',
                fill: true,
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: value => `Rs. ${value}`
                    }
                }
            }
        }
    });

    // Lost Revenue Bar Chart
    const lostRevenueMonths = Object.keys(lostRevenues);
    const lostRevenueValues = Object.values(lostRevenues);
    new Chart(document.getElementById('lostRevenueBarChart'), {
        type: 'bar',
        data: {
            labels: lostRevenueMonths,
            datasets: [{
                label: 'Lost Revenue',
                data: lostRevenueValues,
                backgroundColor: 'red',
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: value => `Rs. ${value}`
                    }
                }
            }
        }
    });
}

// Fetch orders on page load
fetchOrders();
