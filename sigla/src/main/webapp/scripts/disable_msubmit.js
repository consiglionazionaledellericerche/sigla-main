var submitted=false; 
function noDoppioClick() {
  if(submitted==false) {
    submitted=true; 
    return true;
  }
  alert("Attendere la risposta del server!");
  return false;
}
document.mainForm.onsubmit=noDoppioClick