var appletToLoad = false;

function setAppletToLoad(bApplet)
{
	appletToLoad=bApplet;
}

function isAppletToLoad()
{
	return appletToLoad;
}

function loadApplet()
{
    var _info = navigator.userAgent; var _ns = false;
    var _ie = (_info.indexOf("MSIE") > 0 && _info.indexOf("Win") > 0
        && _info.indexOf("Windows 3.1") < 0);
    var _ns = (navigator.appName.indexOf("Netscape") >= 0
            && ((_info.indexOf("Win") > 0 && _info.indexOf("Win16") < 0
            && java.lang.System.getProperty("os.version").indexOf("3.5") < 0)
            || _info.indexOf("Sun") > 0));
    
    if (_ie == true) document.writeln('<OBJECT '+
    	  'classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"'+
		  'codebase = "http://java.sun.com/update/1.6.0/jinstall-6-windows-i586.cab#Version=1,5,0,0"'+
    	  'WIDTH="320" HEIGHT="28" NAME="sign_applet">'+
    	  '<PARAM NAME="code" VALUE="it.actalis.applet.Sign.class" >'+
    	  '<PARAM NAME="codebase" VALUE="applets" >'+
    	  '<PARAM NAME="archive" VALUE="applet-sign.jar, applet-resources.jar" >' +
    	  '<PARAM NAME="name" VALUE="sign_applet" >'+
    	  '<param name="cache_option" value="no">'+
    	  '<PARAM NAME="GUI.CharFont" VALUE="Tahoma" >'+
    	  '<PARAM NAME="GUI.CharSize" VALUE="12" >'+
          '<param name="GUI.Language" value="IT">'+
    	  '<PARAM NAME="type" VALUE="application/x-java-applet;version=1.5.0">'+
    	  '<param name="startOnInit" value=false>'+
    	  '<param name="Sign.AutoStart" value=false>'+
    	  '<param name="scriptable" value=true>'+
          '<param name="GUI.ViewSaveDoc" value="false">'+
          '<param name="GUI.SaveEnv" value="false">'+
          '<param name="Trace.DebugLevel" value="128">'+
          '</OBJECT>');
    		
    else if (_ns == true) document.writeln('<APPLET '+
                'CODEBASE="applets"'+
                'code="it.actalis.applet.Sign.class"'+
                'name="sign_applet" width="320" height="28"'+
                'cache_option="no"'+
                'archive="applet-sign.jar, applet-resources.jar" MAYSCRIPT>'+
                'No JDK 1.6 support for APPLET!!'+
           '</APPLET>');
    }		
    
function signIterative (){
    if (window.document.sign_applet == null) {
    	alert("Riferimento sign_applet è null");
    	return;
    }else{
    	s=window.document.sign_applet;
    	s.setParamHT("Trace.Log", "actalis_sign_trace.log");
    	s.setParamHT("Sign.TokenType", "DoNotCare");
    	s.setParamHT("Sign.TokenArgs", "DoNotCare");
    	s.setParamHT("Sign.DataSource", "String");
    	s.setParamHT("Sign.Data", "sono stringa da firmare");
    	s.setParamHT("Sign.PostURL", "http://ServerA/servlets/parseEnv");
    	s.setParamHT("Sign.IncludeTime", "true");
    	s.setParamHT("Cert.DayToExpire", "10");
    	s.setParamHT("JS.LoginInfo", "Login Info");
    	s.setParamHT("JS.performLogin", "false");
    
    	sign();
    }
}

function signIterative (dataURL, postURL, bpPath, fileName) {
    if (window.document.sign_applet == null) {
    	//alert("Riferimento sign_applet è null");
    	alert("Inizializzazione del programma di firma digitale fallita. Verificare l'installazione del dispositivo Smart Card!");
    	return;
    }else{
    	s=window.document.sign_applet;
    	s.setParamHT("Trace.Log", "actalis_sign_trace.log");
    	s.setParamHT("Sign.TokenType", "DoNotCare");
    	s.setParamHT("Sign.TokenArgs", "DoNotCare");
    	s.setParamHT("Sign.DataSource", "URL");
    	s.setParamHT("Sign.DataURL", dataURL);
    	s.setParamHT("Sign.PostURL", postURL);
    	//s.setParamHT("Sign.Type", "Detached");
    	s.setParamHT("Sign.PostParams", "sign_nome_file="+fileName+"&sign_bp_path="+bpPath);
    	s.setParamHT("Trace.DebugLevel", "128");
    	s.setParamHT("Sign.IncludeTime", "true");
    	s.setParamHT("Cert.DayToExpire", "10");
    	s.setParamHT("JS.LoginInfo", "Login Info");
    	s.setParamHT("JS.performLogin", "false");
    	s.setParamHT("JS.useUniqueSigner", "true");
    	//s.setParamHT("JS.performShowDoc", "false");
    
    	sign();
    }
}

function sign()
{
	waitSignApplet(30000);
    errCode = actAppletSignStart();
    switch (errCode) {
    case 999100: 
       alert("Errore fatale in fase di inizializzazione, ricaricare l'applet");
       return; //break;
    case 999101: 
       alert("E' già in corso un'altra operazione: attendere e riprovare");
       return; //break;
    case 999103: 
       alert("Operazione annullata!\n\nIl PIN è vuoto");
       return; //break;
    default:
       alert("Errore sconosciuto o non gestito\n\nCodice di errore: "+errCode);
       return; //break;
    case 0: 
    
       break;
    }
    checkSign();
}
      
function checkSign()
{
	setAppletToLoad(true);
	restoreWorkspace();

	errCode = actAppletSignCheck();
    if (errCode == 999101) {
       window.setTimeout( "checkSign()", 20000);
       return;
    }
    //Sign is not running anymore!!	
    lastErrorCode=s.getLastErrorCode();
    lastErrorDesc=s.getLastErrorDesc();
    if (lastErrorCode != 0) {
       alert("Errore durante la fase di firma! \n\nCodice di errore:   " + lastErrorCode + "\n\n Descrizione errore:   " + lastErrorDesc);
    }
    /* else {
        //alert ("Firma effettuata con successo \n\n\nEnvelope: \n" + s.getPemEnvelope());
        alert ("Firma effettuata con successo.");
    }*/
}
function waitSignApplet(millis_)   
{  
	date = new Date();  
	var curDate = null;  
    do {
    	var curDate = new Date(); 
    	if (window.document.sign_applet != null) {
    		wait(5000);
    		break;
    	}
    }  
    while(curDate-date < millis_);  
}
function wait(millis_)   
{  
	date = new Date();  
	var curDate = null;  
    do {
    	var curDate = new Date(); 
    }  
    while(curDate-date < millis_);  
}