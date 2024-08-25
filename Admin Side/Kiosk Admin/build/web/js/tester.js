

// For Firebase JS SDK v7.20.0 and later, measurementId is optional
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
  
  var fileItem;
  var FileName;
  
  function getFile(e) {
    fileItem = e.target.files[0];
    FileName = fileItem.name;
  }
  
  function uploadImage() {
    let storageRef = firebase.storage().ref("images/" + FileName);
    let uploadTask = storageRef.put(fileItem);
  
    uploadTask.on(
      "state_changed",
      (snapshot) => {
        console.log(snapshot);
      },
      (error) => {
        console.log(error);
      },
      () => {
        uploadTask.snapshot.ref.getDownloadURL().then((url) => {
          console.log("URL", url);
        });
      }
    );
  }
  