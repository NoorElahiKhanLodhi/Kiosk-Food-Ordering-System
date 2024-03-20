
// Initialize Firebase (ADD YOUR OWN DATA)
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
var storage = firebase.storage();

var img_url = "";

// ADD PRODUCTS TO DATABASE AND GENERATE QR CODE
function add_item() {
    
var database = firebase.database();
    var id = document.getElementById("id").value;
    var name = document.getElementById("name").value;
    var quantity = document.getElementById("quantity").value;
    var price = document.getElementById("price").value;

    var ref = database.ref('menu/item/'+id); //Save inside

    if(id!="" & name!="" & quantity!="" & price!=""){
        uploadImage("items");
var inputData = {
    id: id,
    image: img_url,
    name: name,
    quantity: quantity,
    price: price
};

//firebase start
ref.set(inputData)
   .then(function() {
       alert('Product '+name+' registered successfully!');
   })
   .catch(function(error) {
       console.error('Product '+name+" could not be registered due to error: " + error);
   });
//firebase end

var allInputs = document.querySelectorAll('input');
allInputs.forEach(singleInput => singleInput.value = '');



}else{alert("Fill All Information!");}
}

///GET IMAGE FILE
var fileItem;
var FileName;
function previewImage(e) {
    fileItem = e.target.files[0];
    FileName = fileItem.name;

    // Display the selected image preview
    var imagePreview = document.getElementById('imagePreview');
    imagePreview.src = URL.createObjectURL(fileItem);
    imagePreview.style.display = 'block';
}
///GET IMAGE FILE

//ADD CATEGORY TO DATABASE
function add_cat() {
    var cat = document.getElementById("cat").value;
    
    var ref = database.ref('menu/categories/'+cat); //Save inside

    if(cat!=""){
        uploadImage("categories");
var inputData = {
    cat: cat,
    image: img_url
};

//firebase start
ref.set(inputData)
   .then(function() {
       alert('Category '+cat+' registered successfully!');
   })
   .catch(function(error) {
       console.error('Category '+cat+" could not be registered due to error: " + error);
   });
//firebase end

var allInputs = document.querySelectorAll('input');
allInputs.forEach(singleInput => singleInput.value = '');
    }else{alert("Fill All Information!");}
}
///ADD CATEGORY TO DATABASE end


///UPLOADING IMAGE TO THE STORAGE AND GET URL
function uploadImage(folder) {
    let storageRef = firebase.storage().ref(folder+"/" + FileName);
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
          img_url = String(url);
        });
      }
    );

}
  ///UPLOADING IMAGE TO THE STORAGE AND GET URL end