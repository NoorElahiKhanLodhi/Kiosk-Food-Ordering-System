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

angular.module('myApp', ['ngAnimate'])
.controller('myCtrl', function($scope) {
  var database = firebase.database();
  const categoriesRef = database.ref('/menu/categories/');

  // Fetch category names from Firebase
  categoriesRef.once('value')
    .then(snapshot => {
        const categories = snapshot.val();

        // Initialize categories with hidden state, button symbol, and image URL
        // $scope.categories = Object.keys(categories).map(name => ({
        //   name: name,
        //   imageUrl: categories[name].image,
        //   hidden: true,
        //   buttonSymbol: '⤵️',
        //   items: Object.keys(categories[name].items).map(itemId => {
        //     let item = categories[name].items[itemId];
        //     item.id = itemId;
        //     return item;      
        //   })
        // }));


        $scope.categories = Object.keys(categories).map(name => ({
          name: name,
          imageUrl: categories[name].image,
          hidden: true,
          buttonSymbol: '⤵️',
          items: categories[name].items ? Object.keys(categories[name].items).map(itemId => {
            let item = categories[name].items[itemId];
            item.id = itemId;
            return item;      
          }) : []
        }));


        $scope.$apply(); // Apply scope changes
    })
    .catch(error => {
        console.error('Error fetching category names from Firebase:', error);
    });

  $scope.toggle = function(category) {
    $scope.categories.forEach(cat => {
      if (cat.name !== category.name) {
        cat.hidden = true;
        cat.buttonSymbol = '⤵️';
      }
    });
    category.hidden = !category.hidden;
    category.buttonSymbol = category.hidden ? '⤵️' : '⤴️';
  };

  $scope.deleteCategory = function(categoryName) {
    if (confirm("Are you sure you want to delete this category?")) {
      const categoryRef = database.ref(`/menu/categories/${categoryName}`);
      categoryRef.remove()
        .then(() => {
          alert("Category deleted successfully.");
          // Remove the category from the local scope
          $scope.categories = $scope.categories.filter(category => category.name !== categoryName);
          $scope.$apply(); // Apply scope changes
        })
        .catch(error => {
          console.error('Error deleting category:', error);
          alert("Failed to delete category.");
        });
    }
  };

  $scope.deleteItem = function(categoryName, itemId) {
    if (confirm("Are you sure you want to delete this item?")) {
      const itemRef = database.ref(`/menu/categories/${categoryName}/items/${itemId}`);
      itemRef.remove()
        .then(() => {
          alert("Item deleted successfully.");
          // Remove the item from the local scope
          $scope.categories = $scope.categories.map(category => {
            if (category.name === categoryName) {
              category.items = category.items.filter(item => item.id !== itemId);
            }
            return category;
          });
          $scope.$apply(); // Apply scope changes
        })
        .catch(error => {
          console.error('Error deleting item:', error);
          alert("Failed to delete item.");
        });
    }
  };

  $scope.editItem = function(categoryName, item) {
    const newItemImage = prompt("Edit Item Image URL", item.image);
    const newItemName = prompt("Edit Item Name", item.name);
    const newItemDescription = prompt("Edit Item Description", item.description);
    const newItemPrice = prompt("Edit Item Price", item.price);
const newItemDiscount = prompt("Edit Item Discount", item.discount);
const newItemUnit = prompt("Edit Item Unit", item.unit);
if (newItemImage && newItemName && newItemDescription && newItemPrice && newItemDiscount && newItemUnit) {
  const updatedItem = {
    image: newItemImage,
    name: newItemName,
    description: newItemDescription,
    price: newItemPrice,
    discount: newItemDiscount,
    unit: newItemUnit
  };

  const itemRef = database.ref(`/menu/categories/${categoryName}/items/${item.id}`);
  itemRef.update(updatedItem)
    .then(() => {
      alert("Item updated successfully.");
      // Update the item in the local scope
      $scope.categories = $scope.categories.map(category => {
        if (category.name === categoryName) {
          category.items = category.items.map(i => i.id === item.id ? {...i, ...updatedItem} : i);
        }
        return category;
      });
      $scope.$apply(); // Apply scope changes
    })
    .catch(error => {
      console.error('Error updating item:', error);
      alert("Failed to update item.");
    });
}
};

});

































// ADD DB DOT JS


// Initialize Firebase  aLREADY INIT
// const firebaseConfig = {
//   apiKey: "AIzaSyCmjF9NtHMjqpbGHPj97oBnDSs8XYIZCts",
//   authDomain: "kiosk-app-162.firebaseapp.com",
//   databaseURL: "https://kiosk-app-162-default-rtdb.firebaseio.com",
//   projectId: "kiosk-app-162",
//   storageBucket: "kiosk-app-162.appspot.com",
//   messagingSenderId: "115063990425",
//   appId: "1:115063990425:web:5e08f8b54794d08b827008",
//   measurementId: "G-R7NGKJWRSW"
// };
firebase.initializeApp(firebaseConfig);
var database = firebase.database();
var storage = firebase.storage();


// ADD PRODUCTS TO DATABASE -----------------------------------------------------------------------------
function add_item(img_url) {
  
var database = firebase.database();
  var op = document.getElementById("myComboBox");
  var cat = op.options[op.selectedIndex].value;
  var id = document.getElementById("id").value;
  var name = document.getElementById("name").value;
  var unit = document.getElementById("unit").value;
  var price = document.getElementById("price").value;
  var active = document.getElementById("active");
  var discount = document.getElementById("dis_price");
  var description = document.getElementById("description").value;
  var status = "";
  if (active.checked == true){  
       status = "y";
  }   else{
    status = "n";
  }
var modifier = {};
var i=0;
var mod='';
var pri='';

for(i = 0; i<document.getElementById('mod_no').innerText ; i++){
    


    modifier[document.getElementById('modifier'+i).value]= document.getElementById('mod_price'+i).value;
//      modifier['mod_type'+i]= document.getElementById('modType'+i).value;
    

  }


  console.log(modifier);
  var ref = database.ref('menu/categories/'+cat+'/items/'+id); //Save inside

  if(id!="" & name!="" & unit!="" & price!=""){
if (document.getElementById('dis_price').value != ""){
    var inputData = {
  id: id,
  image: img_url,
  name: name,
  status: status,
  unit: unit,
  price: price,
  category: cat,
  discount: document.getElementById('dis_price').value,
  description: description,
  modifier    
};
}else{
var inputData = {
  id: id,
  image: img_url,
  name: name,
  status: status,
  unit: unit,
  price: price,
  category: cat,
  discount: "",
  description: description,
  modifier    
};
}

//firebase start
ref.set(inputData)
 .then(function() {
     alert('Product '+name+' registered successfully!');
location.reload();
 })
 .catch(function(error) {
     console.error('Product '+name+" could not be registered due to error: " + error);
     location.reload();
 });
//firebase end

var allInputs = document.querySelectorAll('input');
allInputs.forEach(singleInput => singleInput.value = '');



}else{
alert("Fill All Information!");
  document.getElementById('myPopup').classList.remove("show");
}
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
function add_cat(img_url) {
  var cat = document.getElementById("cat").value;
  
  var ref = database.ref('menu/categories/'+cat); //Save inside

            
  
  if(cat!="" && img_url!=""){

var inputData = {
  cat: cat,
  image: img_url
};

//firebase start
ref.set(inputData)
 .then(function() {
     alert('Category '+cat+' registered successfully!');
     location.reload();
 })
 .catch(function(error) {
     console.error('Category '+cat+" could not be registered due to error: " + error);
     location.reload();
 });
//firebase end

var allInputs = document.querySelectorAll('input');
allInputs.forEach(singleInput => singleInput.value = '');
  }else{alert("Fill All Information!");
  document.getElementById('myPopup').classList.remove("show");
}
}
///ADD CATEGORY TO DATABASE end


///UPLOADING CAT & IMAGE TO THE STORAGE AND GET URL
function uploadcat() {


document.getElementById('myPopup').classList.add("show");

  let storageRef = firebase.storage().ref("images/" + FileName);
  let uploadTask = storageRef.put(fileItem);
  var img_url = "";
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
        add_cat(String(url));
      });
    }
  );
  
}
///UPLOADING CAT & IMAGE TO THE STORAGE AND GET URL end



///UPLOADING ITEM & IMAGE TO THE STORAGE AND GET URL
function uploaditem() {

document.getElementById('myPopup').classList.add("show");

let storageRef = firebase.storage().ref("images/" + FileName);
let uploadTask = storageRef.put(fileItem);
var img_url = "";
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
      add_item(String(url));
    });
  }
);

}
///UPLOADING ITEM & IMAGE TO THE STORAGE AND GET URL end









// /////////////////populating category combo box in add item start

// const categoriesRef = database.ref('/menu/categories/');

//   // Function to populate the combo box with category names
//   function populateCategoryComboBox(categoryNames) {
//     const categoryComboBox = document.getElementById('myComboBox');

//     // Clear existing options
//     categoryComboBox.innerHTML = '';

//     // Populate the combo box with category names
//     categoryNames.forEach(categoryName => {
//         const option = document.createElement('option');
//         option.value = categoryName;
//         option.text = categoryName;
//         categoryComboBox.appendChild(option);
//     });
// }

// // Fetch category names from Firebase
// categoriesRef.once('value')
//     .then(snapshot => {
//         // Convert Firebase snapshot to an array of category names
//         const categoryNames = Object.keys(snapshot.val());

//         // Populate the combo box
//         populateCategoryComboBox(categoryNames);
//     })
//     .catch(error => {
//         console.error('Error fetching category names from Firebase:', error);
//     });

// //////////////////////populating category combo box in add item end










///////////////////modifier table
// Function to toggle visibility of modifier table based on slider value
function toggleModifierTable() {
  const modifierTableContainer = document.getElementById('modifierTableContainer');
  const modifierSlider = document.getElementById('modifierSlider');

  if (modifierSlider.value > 0) {
      modifierTableContainer.style.display = 'block';
  } else {
      modifierTableContainer.style.display = 'none';
  }
}

// Add an event listener for the modifier slider
document.getElementById('modifierSlider').addEventListener('input', toggleModifierTable);

// Function to generate modifier table based on slider value
function generateModifierTable() {
  const modifierTableContainer = document.getElementById('modifierTableContainer');
  const modifierSlider = document.getElementById('modifierSlider');

  const numModifiers = modifierSlider.value;
  document.getElementById("mod_no").innerText = numModifiers;
  modifierTableContainer.innerHTML = ''; // Clear previous content

  if (numModifiers > 0) {
      const table = document.createElement('table');
      table.border = '1';
      table.style.margin = 'auto';

      // Add table headers
      const tr = document.createElement('tr');
      const th1 = document.createElement('th');
      th1.textContent = 'Modifier Type';
      const th2 = document.createElement('th');
      th2.textContent = 'Modifiers';
      const th3 = document.createElement('th');
      th3.textContent = 'Modifier Price';
      tr.appendChild(th1);
      tr.appendChild(th2);
      tr.appendChild(th3);
      table.appendChild(tr);

      // Add table rows based on slider value
      for (let i = 0; i < numModifiers; i++) {
          const tr = document.createElement('tr');
          const td1 = document.createElement('td');
          const td2 = document.createElement('td');
          const td3 = document.createElement('td');

          // Add input fields for modifier and modifier price
          const modifierType = document.createElement('select');
          modifierType.id = 'modType'+String(i);
          var add = document.createElement('option');
          add.innerHTML = "ADD";
          add.value = 1;
          var sub = document.createElement('option');
          sub.innerHTML = "REMOVE";
          sub.value = 2;
          modifierType.appendChild(add);
          modifierType.appendChild(sub);

          const modifierInput = document.createElement('input');
          modifierInput.type = 'text';
          modifierInput.id = 'modifier'+String(i);
          const modPriceInput = document.createElement('input');
          modPriceInput.type = 'number';
          modPriceInput.id = 'mod_price'+String(i);

          // if(modifierType.value == 1){
          //   modPriceInput.hidden(true);
          // }

          td1.appendChild(modifierType);
          td2.appendChild(modifierInput);
          td3.appendChild(modPriceInput);
          tr.appendChild(td1);
          tr.appendChild(td2);
          tr.appendChild(td3);
          table.appendChild(tr);


      }


      
      modifierTableContainer.appendChild(table);





  }
  var x=0;
  var xs= "";
if(numModifiers>0){

  while(document.getElementById('modType'+String(x))){
    const xs=String(x);
    document.getElementById('modType'+xs).addEventListener('click', (e) => {
      
      if(e.target.value != 1){
        document.getElementById('mod_price'+xs).hidden = true;
      }else{
        document.getElementById('mod_price'+xs).hidden = false;
      }
    }, true); 
    x++;
  
  }
}


}




// Add an event listener for modifier slider change
document.getElementById('modifierSlider').addEventListener('change', generateModifierTable);




/// Enable discount box
function adddiscount(){
  if ((document.getElementById('discount')).checked == true){  
    document.getElementById('dis_price').style.visibility = 'visible';
}   else{
  document.getElementById('dis_price').style.visibility = 'hidden';
}
}











// function showItemDiv() {
//   // style="display: none;"
//   console.log(document.getElementById("cat").value);
//   // location.href="/web/add_item.html?cat="+document.getElementById("cat").textContent;
// };









function doit(obj){
  location.href = "add_item.html?category="+obj.id;
}