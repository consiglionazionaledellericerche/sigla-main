//---------------------------------------------------------- COMMON ->
//Applet Sign not loaded     : 999100 
//Applet Sign is working     : 999101 
//Applet Sign not initialized: vedi applet e/o CAPI
//Applet Sign empty PIN      : 999103 
//---------------------------------------------------------- COMMON <-

//---------------------------------------------------------- SIGN   ->

var actAppletSignEnvelope = "";

//Al termine il recupero cod.errore avverra' tramite la Check corrispondente
//che dovra' essere attivata all'interno di una funzione soggetta a setTimeout
//In caso di esito positivo, il recupero successivo dei dati
//parametri:
// PIN            - il PIN di accesso al token PKCS11
function actAppletSignStart(){
   //alert ("dbg:actAppletSignStart()");

   if (window.document.sign_applet == null) {
	return 999100;
   }

   result = window.document.sign_applet.sign(); // PIN);
   if (result == 1)
	return 999101;

   return 0;
}

//vedi commento della corrispondente Start
function actAppletSignCheck(){
   //alert ("dbg:actAppletSignCheck()");

   // vari check gia' effettuati da Start
   // (window.document.sign_applet != null)
   result = window.document.sign_applet.signIsRunning(); //ritorna il valore di this.running
   if (result == 1) {
   	//window.setTimeout a carico del chiamante !!
	return 999101;
   }

   return 0;
}

//---------------------------------------------------------- SIGN   <-
