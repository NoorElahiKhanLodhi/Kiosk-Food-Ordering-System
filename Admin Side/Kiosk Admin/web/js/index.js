
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
var database = firebase.database();



function login(){
  console.log('Logging in....');
  const user = document.getElementById('username').value;
  const pass = document.getElementById('password').value;
  const login = database.ref('/auth/');

login.child(user).get().then((snapshot) =>{
  if (snapshot.exists() && snapshot.val() == pass) {
    document.getElementById('cat').hidden=false;
    document.getElementById('menu').hidden=false;
    document.getElementById('order').hidden=false;
    alert("Login Success!");

    document.getElementById('togle').hidden=true;
    const welcome = document.createElement('h1');
    welcome.textContent = "Welcome "+user.toUpperCase();
    welcome.style.padding = "100px 100px 100px 100px";


    document.getElementById('signin').appendChild(welcome);


  } else {
alert("Incorrect Credentials\nCould not Login");
  }
}).catch((error) =>{
  alert("Error Logging in ....\n"+error);
});


  //   if(login){
// login.
//   }
}








