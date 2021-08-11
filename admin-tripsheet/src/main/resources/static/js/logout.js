/**
 * 
 */

var logoutBtn = document.getElementById("logoutbtn");

logoutBtn.addEventListener("click", performLogout);

function performLogout() {
	try{	
	window.location.href = "/logout"
		}
		catch(e){
		alert(e.message);
		}
}