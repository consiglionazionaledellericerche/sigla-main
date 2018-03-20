function disableRightClick(e) {
 if (parseInt(navigator.appVersion)>3) {
  var clickType=1;
  if (navigator.appName=="Netscape") clickType=e.which;
  else clickType=event.button;
  if (clickType!=1) {
   alert('Non è possibile usare il menu contestuale');
   return false;
  }
 }
 return true;
}
if (parseInt(navigator.appVersion)>3) {
 window.onmousedown = disableRightClick;
 document.onmousedown = disableRightClick;
 if (navigator.appName=="Netscape") 
  window.captureEvents(Event.MOUSEDOWN);
}