//user profile
var data = JSON.parse(sessionStorage.getItem("Admin"));

 

var profileButton = document.getElementById("userprofile-icon");
var initials = data.employeeName.charAt(0); //+ splitName.pop().charAt(0);
profileButton.innerHTML = initials;

 

profileButton.addEventListener("click", userprofileData);

 

function userprofileData() {
    
    try{
        
        var employeeName = document.getElementById("employeeName");
        var emailId = document.getElementById("mailId");
    

 

    employeeName.innerHTML = data.employeeName;
   emailId.innerHTML =data.employeeMail;
  
//    projectName.innerHTML = "Project: " + data.projectName;
//    projectLead.innerHTML = "Project lead: " + data.projectLead;

 

    //var splitName = data.employeeName.split(' ');
    }
    catch(e){
        alert(e.message);
        jsExceptionHandling(e);
    }
    
}