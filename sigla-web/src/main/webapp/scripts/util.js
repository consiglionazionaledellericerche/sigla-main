if (window.opener == null || window.opener.top.modalPopup != window)
	closePopup();
document.write("<script type='text/JavaScript' src='scripts/prototype.js'></script>");
document.write("<script type='text/JavaScript' src='scripts/window.js'></script>");
document.write("<script type='text/JavaScript' src='scripts/effects.js'></script>");
document.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/themes/default.css\">");
document.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/themes/alphacube.css\">");
//if (window.opener == null || window.opener.top.modalPopup != window)
//	alert(window.opener.top.modalPopup)
function apriListaMessaggi(url) {
	var winWidth= (screen.width*50)/100;
	var winHeight= (screen.height*50)/100;
    var win = new Window({className:"alphacube", width:winWidth, height:winHeight, title: "Messaggi",url: url, destroyOnClose:true, showEffect:Effect.BlindDown, hideEffect: Effect.SwitchOff, draggable:true, wiredDrag: true, onClose: function(event) { 
    	event.getContent().contentDocument.mainForm.comando.value = 'doCloseForm';
  		event.getContent().contentDocument.mainForm.submit();
	}});
    win.showCenter(true);    
	setMessage("img/spacer.gif",null);
}
function closePopup() {
	if (window.top.modalPopup) {
		window.top.modalPopup.close()
		window.top.modalPopup = null
	}
}

function showPopup(url,name,args) {
	closePopup()
	window.top.modalPopup = window.open(url,name,args)
	window.top.modalPopup.focus()
	return window.top.modalPopup
}
	
// Funzioni di carattere generale
function getElementById(document,id) {
	if (document.all)
		return document.all[id];
	else
		return document.getElementById(id);
}
function showElement(target,name) {
	if (target)
		frame = window.parent.frames[target];
	else
		frame = window;
	if (!frame) return;
	element = getElementById(frame.document,name);
	if (element)
		element.style.visibility = 'visible';
}

// Esegue il submit di una FORM HTML impostando il valore del parametro 
// definito da un SUBMIT con un valore diverso dalla label:
// <input type="submit" value"Premi qui per proseguire" name="submit" onclick="return submitForm(this,'doProsegui')">

function disableDblClick() {
	if(document.mainForm.submitted != true)
		return true;
//	alert("Attendere, richiesta gi� inoltrata...");
	return false;
}

function focused(element) {
	if (document.mainForm.focusedElement)
		document.mainForm.focusedElement.value = element.name;
}

function _submitForm(aForm,command,lockForm,action) {
	if (aForm.modal) return;
	if (action)
		aForm.action = action;
	aForm.comando.value = command;
	if (aForm.scrollx) {
		if (window.pageXOffset)
			aForm.scrollx.value = window.pageXOffset;
		else
			aForm.scrollx.value = document.body.scrollLeft;
	}
	if (aForm.scrolly) {
		if (window.pageYOffset)
			aForm.scrolly.value = window.pageYOffset;
		else
			aForm.scrolly.value = document.body.scrollTop;
	}
	if (lockForm)
		aForm.submitted=true; 
	setMessage('img/loading.gif','Attendere prego...');
	resetModalDisabled(aForm)
	aForm.submit();
	if (lockForm && !aForm.target){
		for (i = 0;i < aForm.elements.length;i++){
		  if (aForm.elements[i].type != "submit")
			aForm.elements[i].disabled = true;
		}
	}			
}

function submitForm(command) {
	_submitForm(document.mainForm,command,true);
}

function setFocusOnInput(inputName) {
	var input = document.mainForm.elements[inputName];
	if (input && !input.disabled && input.focus)
		input.focus();
}

// Funzione invocata per cambiare pagina in un tab
function doTab(tabName,pageName) {
	if (!disableDblClick()) return;
	submitForm("doTab("+tabName+","+pageName+")");
}

// funzione invocata per selezionare una riga in una tabella
function select(name,index) {
	if (disableDblClick()) {
		document.mainForm.elements[name+".focus"].value=index;
//		document.mainForm.elements[name+".selection"].value=index;
		submitForm("doSelection("+name+")");
	}
}

// funzione invocata per selezionare una riga in una tabella
function doNavigate(name,index) {
	if (disableDblClick())
		submitForm("doNavigate("+name+","+index+")");
}

function sort(name,property) {
	if (disableDblClick()) {
		submitForm("doSort("+name+","+property+")");
	}
}

function hiddenColumn(name,property) {
	if (disableDblClick()) {
		submitForm("doHiddenColumn("+name+","+property+")");
	}
}

function selectAll(tableName) {
	elementName = tableName+".selection";
	for (j = 0;j < document.mainForm.elements.length;j++)
		if (document.mainForm.elements[j].name == elementName && document.mainForm.elements[j].checked != null)
					document.mainForm.elements[j].checked = !document.mainForm.elements[j].checked;
}

// Funzioni invocate dai bottoni di una finestra di CRUD
function doCerca() {
	submitForm('doCerca');
}

function doRicercaLibera() {
	submitForm('doRicercaLibera');
}

function doSalva() {
	submitForm('doSalva');
}

function doNuovo() {
	submitForm('doNuovo');
}

function doElimina() {
	if (confirm('Vuoi confermare la cancellazione?'))
		submitForm('doElimina');
}

function doNuovaRicerca() {
	submitForm('doNuovaRicerca');
}

function doRiporta() {
	submitForm('doRiporta');
}

function doAnnullaRiporta() {
	submitForm('doAnnullaRiporta');
}

function doChiudiForm() {
	submitForm('doCloseForm');
}

function doStampa() {
	submitForm('doPrint');
}

function doExcel() {
	submitForm('doExcel');
}

function doLogout() {
	if (confirm('Vuoi continuare?'))
		submitForm('doLogout');
}

function doHelp(url) {
	window.open(url, "Aiuto", 'toolbar=no,resizable,scrollbars,width=767,height=600').focus()
}
function doScaricaExcel(url) {
	window.open(url, "Excel", 'toolbar=no,resizable,scrollbars,width=800,height=600').focus() 
}
function doPrint(url) {
	window.open(url, "Stampa", 'toolbar=no,resizable,scrollbars,width=800,height=600').focus() 
}

// Funzioni per implementare effetto di rollover
function setClassNames(element) {
	if (!element.originalClassName) {
		element.originalClassName = element.className;
		element.upClassName = element.className+"Up";
		element.downClassName = element.className+"Down";
		element.overClassName = element.className+"Over";
	}
}
function mouseOver(element) {
	if (document.mainForm.submitted) return;
	setClassNames(element);
	element.className = element.overClassName;
}
function mouseOut(element) {
	if (document.mainForm.submitted) return;
	setClassNames(element);
	element.className = element.originalClassName;
}
function mouseDown(element) {
	if (document.mainForm.submitted) return;
	setClassNames(element);
	element.className=element.downClassName
}
function mouseUp(element) {
	if (document.mainForm.submitted) return;
	setClassNames(element);
	element.className=element.upClassName
//	window.focus();
}
// Funzione per evitare propagazioni di eventi in IE
function cancelBubble(event) {
	if (event)
		event.cancelBubble = true;
}

var HomeFrameSet = getElementById(window.top.document,"home");
if (window.top.frames['desktop'] != null){
	var DesktopFrameSet = window.top.frames['desktop'].document.getElementById("desktopFrameSet");
}	

//if (HomeFrameSet && HomeFrameSet.document && HomeFrameSet.document.frames['desktop'].document)
//	var DesktopFrameSet = getElementById(HomeFrameSet.document.frames['desktop'].document,"desktopFrameSet")

// Funzione invocata per implementare effetto di massimizzazione dell'workspace
function maximizeWorkspace() {
	frame = window.top.document.body;
	if (frame.oldrows) {
		frame.rows = frame.oldrows;
		frame.oldrows = null;
	} else {
		frame.oldrows = frame.rows;
		frame.rows = "0,*";
	}
	frame = window.top.frames['login'].frames['workspace_menu'];
	if (frame.oldcols) {
		frame.cols = frame.oldcols;
		frame.oldcols = null;
	} else {
		frame.oldcols = frame.cols;
		frame.cols = "0,*";
	}
}

function maximizeRestoreWindow(name) {
	win = getElementById(document,name);
	if (!win || !HomeFrameSet) return;
	if (!HomeFrameSet.maximized) {
		document.body.className = document.body.oldClassName;
		document.body.oldClassName = null;
		win.className = win.oldClassName;
		win.oldClassName = null;
	}
	else {
		document.body.oldClassName = document.body.className;
		document.body.className = "Maximized"
		win.oldClassName = win.className;
		win.className = "WindowMaximized"
	}
}

function maximizeWorkspace() {
	if (!HomeFrameSet.maximized) {
		HomeFrameSet.maximized=true;
		HomeFrameSet.oldRows = HomeFrameSet.rows;
		HomeFrameSet.rows = "0,*,20";
		if (DesktopFrameSet) {
			DesktopFrameSet.oldCols = DesktopFrameSet.cols;
			DesktopFrameSet.cols = "0,*";
		}
		maximizeRestoreWindow('mainWindow');
	}
}

function restoreWorkspace() {
	if (HomeFrameSet && HomeFrameSet.maximized) {
		HomeFrameSet.maximized=false;
		HomeFrameSet.rows = HomeFrameSet.oldRows;
		if (DesktopFrameSet)
			DesktopFrameSet.cols = DesktopFrameSet.oldCols;
		maximizeRestoreWindow('mainWindow');
	}
}
function maximizeRestore() {
    if (HomeFrameSet == null)
      alert("Operazione non consentita.");
	if (HomeFrameSet.maximized)
		restoreWorkspace();
	else
		maximizeWorkspace();
}

function initializeWindow(name) {
	if (HomeFrameSet && HomeFrameSet.maximized)
		maximizeRestoreWindow(name);
}

function handleOnLoad() {
	window.onloadHandlers.evaluate()
}

function MultiEventHandler(handler,next,priority) {
	this.handler = handler;
	this.next = next;
	this.priority = priority;
	function evaluate() {
		if (this.next) this.next.evaluate()
		if (this.handler) this.handler();
	}
	this.evaluate = evaluate
}

function addOnloadHandler(handler,priority) {
	var curr = window.onloadHandlers;
	var prev = null;
	while((curr != null) && (priority >= curr.priority)) {
		prev = curr;
		curr = curr.next;
	}
	var meh = new MultiEventHandler(handler,curr,priority);
	if (prev == null)
		window.onloadHandlers = meh;
	else {
//		prev.handler = function() { alert('ciao') }
		prev.next = meh;
	}
	window.onload = handleOnLoad;
}

window.onloadHandlers = null

function showMessage(level,img,text) {
	setMessage(img,text);
//	if (level == 1 && text)
	if (text)
		addOnloadHandler(function() { alert(text) },999)
}

function setMessage(img,text) {
	if (!window.top.MessageText) return;
	if (!text) text = "&nbsp;";
	window.top.MessageText.innerHTML = text;
	if (img) window.top.MessageImg.src = img;
}

function setTempMessage(img,text) {
	window.top.messageSrc = window.top.MessageImg.src
	window.top.messageText = window.top.MessageText.innerHTML
	setMessage(img,text)
}

function restoreMessage() {
	setMessage(window.top.messageSrc,window.top.messageText)
}

setMessage("img/spacer.gif",null);
//document.oncontextmenu = function(){return false}

// Nuove funzioni per tags
function checkFormAlreadySubmitted() {
	if(submitted==false)
		return true;
	alert("Attendere, richiesta gi� inoltrata...");
	return false;
}

function Form_submitForm(command) {
	checkFormAlreadySubmitted();
	for (i = 1;i < arguments.length;i++) {
		if (i == 1)
			command += '('
		else
			command += ','
		command += arguments[i]
	}
	if (arguments.length > 1)
		command += ')'
	if (this.comando)
		this.comando.value = command;
	if (this.scrollx) {
		if (window.pageXOffset)
			this.scrollx.value = window.pageXOffset;
		else
			this.scrollx.value = document.body.scrollLeft;
	}
	if (this.scrolly) {
		if (window.pageYOffset)
			this.scrollx.value = window.pageYOffset;
		else
			this.scrolly.value = document.body.scrollTop;
	}
	submitted=true; 
	setMessage('img/loading.gif','Attendere prego...');
	this.submit();
}

function Form_selectTableRow(name,index) {
	if (disableDblClick()) {
		this.elements[name+".focus"].value=index;
//		document.mainForm.elements[name+".selection"].value=index;
		this.submitForm("doSelection("+name+")");
	}
}

function Form_selectAllTableRows(tableName) {
	elementName = tableName+".selection";
	for (j = 0;j < document.mainForm.elements.length;j++)
		if (document.mainForm.elements[j].name == elementName && document.mainForm.elements[j].checked != null)
					document.mainForm.elements[j].checked = !document.mainForm.elements[j].checked;
}

function subclassForm(formName) {
	var form = document.forms[formName]
	form.submitForm = Form_submitForm
	form.selectTableRow = Form_selectTableRow
	form.selectAllTableRows = Form_selectAllTableRows
}

function scrollIntoView(elementName) {
	try {
		getElementById(document,elementName).scrollIntoView(false)
	} catch(e) {
	}
}

// Funzioni per gli input modali
function modalInputFocused(input) {
	var form = input.form
	var confirmButton = form.elements[input.name+".confirm"]
	var cancelButton = form.elements[input.name+".cancel"]
	if (input.changed || input.modal) return;
	form.modal = true
	input.modal = true
	input.old_value = input.value
	for (i = 0;i < form.elements.length;i++) {
		var element = form.elements[i]
	  if (element != input && 
			  element != confirmButton && 
			  element != cancelButton && 
			 !element.disabled &&
			  element.className.indexOf("fieldset") == -1 &&
			  element.type.indexOf("fieldset") == -1
			 //r.p. 28/11/2007 firefox non disabilitava i bottoni e le tab
			 //&& element.type != "submit"
			 ) {
				element.modal_disabled = element.disabled 
				element.disabled = true 
		} else {
				element.modal_disabled = null 
		}
	}
}
function modalInputChanged(input) {
	input.changed = true
}
function resetModalDisabled(form,input) {
	form.modal = false
	if (input != null)
		input.modal = false;
	for (i = 0;i < form.elements.length;i++) {
		var element = form.elements[i]
	  if (element.modal_disabled != null)
				element.disabled = element.modal_disabled 
	}
}
function modalInputButtonFocused(button,name) {
	var form = button.form
	var input = form.elements[name]
	if (input.changed) return
	resetModalDisabled(form,input)
}
function cancelModalInputChange(button,name) {
	var form = button.form
	var input = form.elements[name]
	if (!input.changed) return;
	input.value = input.old_value
	input.changed = false
	resetModalDisabled(form,input)
}
function confirmModalInputChange(button,name,command) {
	var form = button.form
	var input = form.elements[name]
	if (!input.changed) return;
	form.modal = false
	submitForm(command)
}

/*-- Add by Stentella --*/

// Check browser version
var isNav4 = false, isNav5 = false, isIE4 = false
var strSeperator = "/"; 
// If you are using any Java validation on the back side you will want to use the / because 
// Java date validations do not recognize the dash as a valid date separator.
var vDateType = 3; // Global value for type of date format
//                1 = mm/dd/yyyy
//                2 = yyyy/dd/mm  (Unable to do date check at this time)
//                3 = dd/mm/yyyy
var vYearType = 4; //Set to 2 or 4 for number of digits in the year for Netscape
var vYearLength = 2; // Set to 4 if you want to force the user to enter 4 digits for the year before validating.
var err = 0; // Set the error code to a default of zero
if(navigator.appName == "Netscape") {
	if (navigator.appVersion < "5") {
		isNav4 = true;
		isNav5 = false;
	}else{
		if (navigator.appVersion > "4") {
			isNav4 = false;
			isNav5 = true;
		}
   }
}
else {
	isIE4 = true;
}

function DateFormat(vDateName, vDateValue, e, dateCheck, dateType) {
	vDateType = dateType;
	// vDateName = object name
	// vDateValue = value in the field being checked
	// e = event
	// dateCheck 
	// True  = Verify that the vDateValue is a valid date
	// False = Format values being entered into vDateValue only
	// vDateType
	// 1 = mm/dd/yyyy
	// 2 = yyyy/mm/dd
	// 3 = dd/mm/yyyy
	if (!vDateName.readOnly){
		var whichCode = (window.Event) ? e.which : e.keyCode;
		if (whichCode == null)
		  whichCode = 0;
		// Check to see if a seperator is already present.
		// bypass the date if a seperator is present and the length greater than 8
		if (vDateValue.length > 8 && isNav4) {
			if ((vDateValue.indexOf("-") >= 1) || (vDateValue.indexOf("/") >= 1))
				return true;
		}
		//Eliminate all the ASCII codes that are not valid
		var alphaCheck = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ/-";
		if (alphaCheck.indexOf(vDateValue) >= 1) {
		if (isNav4) {
			vDateName.value = "";
			vDateName.focus();
			vDateName.select();
			return false;
		}
		else {
			vDateName.value = vDateName.value.substr(0, (vDateValue.length-1));
			return false;
	   }
	}
	if (whichCode == 8) //Ignore the Netscape value for backspace. IE has no value
		return false;
	else {
		//Create numeric string values for 0123456789/
		//The codes provided include both keyboard and keypad values
		var strCheck = '47,48,49,50,51,52,53,54,55,56,57,58,59,95,96,97,98,99,100,101,102,103,104,105';
		if (strCheck.indexOf(whichCode) != -1) {
		if (isNav4) {
			if (vDateValue.length == 6 && dateCheck) {
				var mDay = vDateName.value.substr(2,2);
				var mMonth = vDateName.value.substr(0,2);
				var mYear = vDateName.value.substr(4,4)
				//Turn a two digit year into a 4 digit year
				if (mYear.length == 2 && vYearType == 4) {
					var mToday = new Date();
					//If the year is greater than 30 years from now use 19, otherwise use 20
					var checkYear = mToday.getFullYear() + 30; 
					var mCheckYear = '20' + mYear;
					if (mCheckYear >= checkYear)
						mYear = '19' + mYear;
					else
						mYear = '20' + mYear;
					}
					var vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
					return true;
				}else {
					// Reformat the date for validation and set date type to a 1
					if (vDateValue.length >= 8  && dateCheck) {
						if (vDateType == 1){  // mmddyyyy
							var mDay = vDateName.value.substr(2,2);
							var mMonth = vDateName.value.substr(0,2);
							var mYear = vDateName.value.substr(4,4)
							vDateName.value = mMonth+strSeperator+mDay+strSeperator+mYear;
						}
						if (vDateType == 2){  // yyyymmdd
							var mYear = vDateName.value.substr(0,4)
							var mMonth = vDateName.value.substr(4,2);
							var mDay = vDateName.value.substr(6,2);
							vDateName.value = mYear+strSeperator+mMonth+strSeperator+mDay;
						}
						if (vDateType == 3){ // ddmmyyyy
							var mMonth = vDateName.value.substr(2,2);
							var mDay = vDateName.value.substr(0,2);
							var mYear = vDateName.value.substr(4,4)
							vDateName.value = mDay+strSeperator+mMonth+strSeperator+mYear;
						}
						//Create a temporary variable for storing the DateType and change
						//the DateType to a 1 for validation.
						var vDateTypeTemp = vDateType;
						vDateType = 1;
						var vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
						vDateType = vDateTypeTemp;
						return true;
					}
				}
			}else {
				// Non isNav Check
				if (vDateValue.length > 10) {
        			vDateName.value = vDateName.value.substr(0, 10);
	        		return false;
				}
				// Reformat date to format that can be validated. mm/dd/yyyy
				if (vDateValue.length >= 8 && dateCheck) {
					// Additional date formats can be entered here and parsed out to
					// a valid date format that the validation routine will recognize.
					if (vDateType == 1){ // mm/dd/yyyy
						var mMonth = vDateName.value.substr(0,2);
						var mDay = vDateName.value.substr(3,2);
						var mYear = vDateName.value.substr(6,4)
					}
					if (vDateType == 2){ // yyyy/mm/dd
						var mYear = vDateName.value.substr(0,4)
						var mMonth = vDateName.value.substr(5,2);
						var mDay = vDateName.value.substr(8,2);
					}
					if (vDateType == 3){ // dd/mm/yyyy
						var mDay = vDateName.value.substr(0,2);
						var mMonth = vDateName.value.substr(3,2);
						var mYear = vDateName.value.substr(6,4)
					}
					// Create temp. variable for storing the current vDateType
					var vDateTypeTemp = vDateType;
					// Change vDateType to a 1 for standard date format for validation
					// Type will be changed back when validation is completed.
					vDateType = 1;
					// Store reformatted date to new variable for validation.
					var vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
					if (mYear.length == 2 && vYearType == 4 && dateCheck) {
						//Turn a two digit year into a 4 digit year
						var mToday = new Date();
						//If the year is greater than 30 years from now use 19, otherwise use 20
						var checkYear = mToday.getFullYear() + 30; 
						var mCheckYear = '20' + mYear;
						if (mCheckYear >= checkYear)
							mYear = '19' + mYear;
						else
							mYear = '20' + mYear;
						vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
						// Store the new value back to the field.  This function will
						// not work with date type of 2 since the year is entered first.
						if (vDateTypeTemp == 1) // mm/dd/yyyy
							vDateName.value = mMonth+strSeperator+mDay+strSeperator+mYear;
						if (vDateTypeTemp == 3) // dd/mm/yyyy
							vDateName.value = mDay+strSeperator+mMonth+strSeperator+mYear;
					} 
					vDateType = vDateTypeTemp;
					return true;
				}else {
					if (vDateType == 1) {
						if (vDateValue.length == 2) {
							vDateName.value = vDateValue+strSeperator;
						}
						if (vDateValue.length == 5) {
							vDateName.value = vDateValue+strSeperator;
	   					}
					}
					if (vDateType == 2) {
						if (vDateValue.length == 4) {
							vDateName.value = vDateValue+strSeperator;
						}
						if (vDateValue.length == 7) {
							vDateName.value = vDateValue+strSeperator;
	 					}
					} 
					if (vDateType == 3) {
						if (vDateValue.length == 2) {
							vDateName.value = vDateValue+strSeperator;
						}
						if (vDateValue.length == 5) {
							vDateName.value = vDateValue+strSeperator;
						}
					}
					return true;
				}
			}
			return false;
		}else {
			var strCheck2 = '67,86,12,16,17,18,19,20,32,33,34,35,36,37,38,39,40,45,46,113,114,115,116,117,118,119,120,121';
			if (strCheck2.indexOf(whichCode) != -1) {
				if(whichCode == 37 ||whichCode == 39||whichCode == 16)
					return true;        
        		vDateName.value = vDateName.value + "";
        		return false;
			}else {
				// If the value is not in the string return the string minus the last
				// key entered.
				if (isNav4) {
					vDateName.value = "";
					vDateName.focus();
					vDateName.select();
					return false;
				}else{
					vDateName.value = vDateName.value.substr(0, (vDateValue.length-1));
					return false;
        		}
         	}
		}
	}
}
}
function DateTimeFormat(vDateName, vDateValue, e, dateCheck, dateType) {
vDateType = dateType;
//Only 4 digit year admitted
var vYearLength = 4;
var vYearType = 4;
// vDateName = object name
// vDateValue = value in the field being checked
// e = event
// dateCheck 
// True  = Verify that the vDateValue is a valid date
// False = Format values being entered into vDateValue only
// vDateType
// 1 = mm/dd/yyyy
// 2 = yyyy/mm/dd
// 3 = dd/mm/yyyy
if (!vDateName.readOnly){
var whichCode = (window.Event) ? e.which : e.keyCode;
if (whichCode == null)
	whichCode = 0;

// Check to see if a seperator is already present.
// bypass the date if a seperator is present and the length greater than 8
if (vDateValue.length > 8 && isNav4) {
if ((vDateValue.indexOf("-") >= 1) || (vDateValue.indexOf("/") >= 1))
return true;
}
//Eliminate all the ASCII codes that are not valid
var alphaCheck = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ/-";
if (alphaCheck.indexOf(vDateValue) >= 1) {
if (isNav4) {
vDateName.value = "";
vDateName.focus();
vDateName.select();
return false;
}
else {
        if (vDateValue.indexOf(" ")!=10){
          vDateName.value = vDateName.value.substr(0, (vDateValue.length-1));
          return false;
        }
    }
}
if (whichCode == 8) //Ignore the Netscape value for backspace. IE has no value
return false;
else {
//Create numeric string values for 0123456789/
//The codes provided include both keyboard and keypad values
var strCheck = '47,48,49,50,51,52,53,54,55,56,57,58,59,95,96,97,98,99,100,101,102,103,104,105';
if (strCheck.indexOf(whichCode) != -1) {
if (isNav4) {
/*if (((vDateValue.length < 6 && dateCheck) || (vDateValue.length == 7 && dateCheck)) && (vDateValue.length >=1)) {
alert("Invalid Date\nPlease Re-Enter");
vDateName.value = "";
vDateName.focus();
vDateName.select();
return false;
}*/
if (vDateValue.length == 6 && dateCheck) {
var mDay = vDateName.value.substr(2,2);
var mMonth = vDateName.value.substr(0,2);
var mYear = vDateName.value.substr(4,4);
var mHour = vDateName.value.substr(8,2);
var mMinute = vDateName.value.substr(10,2)
//Turn a two digit year into a 4 digit year
if (mYear.length == 2 && vYearType == 4) {
var mToday = new Date();
//If the year is greater than 30 years from now use 19, otherwise use 20
var checkYear = mToday.getFullYear() + 30; 
var mCheckYear = '20' + mYear;
if (mCheckYear >= checkYear)
mYear = '19' + mYear;
else
mYear = '20' + mYear;
}
var vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
/*if (!dateValid(vDateValueCheck)) {
alert("Invalid Date\nPlease Re-Enter");
vDateName.value = "";
vDateName.focus();
vDateName.select();
return false;
}*/
return true;
}
else {
// Reformat the date for validation and set date type to a 1
if (vDateValue.length >= 8  && dateCheck) {
if (vDateType == 1) // mmddyyyy
{
var mDay = vDateName.value.substr(2,2);
var mMonth = vDateName.value.substr(0,2);
var mYear = vDateName.value.substr(4,4);
var mHour = vDateName.value.substr(8,2);
var mMinute = vDateName.value.substr(10,2)
vDateName.value = mMonth+strSeperator+mDay+strSeperator+mYear+" "+mHour+":"+mMinute;
}
if (vDateType == 2) // yyyymmdd
{
var mYear = vDateName.value.substr(0,4);
var mMonth = vDateName.value.substr(4,2);
var mDay = vDateName.value.substr(6,2);
var mHour = vDateName.value.substr(8,2);
var mMinute = vDateName.value.substr(10,2)
vDateName.value = mYear+strSeperator+mMonth+strSeperator+mDay+" "+mHour+":"+mMinute;
}
if (vDateType == 3) // ddmmyyyy
{
var mMonth = vDateName.value.substr(2,2);
var mDay = vDateName.value.substr(0,2);
var mYear = vDateName.value.substr(4,4);
var mHour = vDateName.value.substr(8,2);
var mMinute = vDateName.value.substr(10,2)
vDateName.value = mDay+strSeperator+mMonth+strSeperator+mYear+" "+mHour+":"+mMinute;
}
//Create a temporary variable for storing the DateType and change
//the DateType to a 1 for validation.
var vDateTypeTemp = vDateType;
vDateType = 1;
var vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear+" "+mHour+":"+mMinute;
/*if (!dateValid(vDateValueCheck)) {
alert("Invalid Date\nPlease Re-Enter");
vDateType = vDateTypeTemp;
vDateName.value = "";
vDateName.focus();
vDateName.select();
return false;
}*/
vDateType = vDateTypeTemp;
return true;
}
/*else {
if (((vDateValue.length < 8 && dateCheck) || (vDateValue.length == 9 && dateCheck)) && (vDateValue.length >=1)) {
alert("Invalid Date\nPlease Re-Enter");
vDateName.value = "";
vDateName.focus();
vDateName.select();
return false;
         }
      }*/
   }
}
else {
// Non isNav Check
if (vDateValue.length > 16) {
        vDateName.value = vDateName.value.substr(0, 16);
        return false;
}

/*if (((vDateValue.length < 8 && dateCheck) || (vDateValue.length == 9 && dateCheck)) && (vDateValue.length >=1)) {
alert("Invalid Date\nPlease Re-Enter");
vDateName.value = "";
vDateName.focus();
return true;
}*/
// Reformat date to format that can be validated. mm/dd/yyyy
if (vDateValue.length >= 16 && dateCheck) {
// Additional date formats can be entered here and parsed out to
// a valid date format that the validation routine will recognize.
if (vDateType == 1) // mm/dd/yyyy hh:mm
{
var mMonth = vDateName.value.substr(0,2);
var mDay = vDateName.value.substr(3,2);
var mYear = vDateName.value.substr(6,4)
var mHour = vDateName.value.substr(11,2);
var mMinute = vDateName.value.substr(14,2)
}
if (vDateType == 2) // yyyy/mm/dd hh:mm
{
var mYear = vDateName.value.substr(0,4)
var mMonth = vDateName.value.substr(5,2);
var mDay = vDateName.value.substr(8,2);
var mHour = vDateName.value.substr(11,2);
var mMinute = vDateName.value.substr(14,2)
}
if (vDateType == 3) // dd/mm/yyyy hh:mm
{
var mDay = vDateName.value.substr(0,2);
var mMonth = vDateName.value.substr(3,2);
var mYear = vDateName.value.substr(6,4);
var mHour = vDateName.value.substr(11,2);
var mMinute = vDateName.value.substr(14,2)

}
/*if (vYearLength == 4) {
if (mYear.length < 4) {
alert("Invalid Date\nPlease Re-Enter");
vDateName.value = "";
vDateName.focus();
return true;
   }
}*/
// Create temp. variable for storing the current vDateType
var vDateTypeTemp = vDateType;
// Change vDateType to a 1 for standard date format for validation
// Type will be changed back when validation is completed.
vDateType = 1;
// Store reformatted date to new variable for validation.
var vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
if (mYear.length == 2 && vYearType == 4 && dateCheck) {
//Turn a two digit year into a 4 digit year
var mToday = new Date();
//If the year is greater than 30 years from now use 19, otherwise use 20
var checkYear = mToday.getFullYear() + 30; 
var mCheckYear = '20' + mYear;
if (mCheckYear >= checkYear)
mYear = '19' + mYear;
else
mYear = '20' + mYear;
vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
// Store the new value back to the field.  This function will
// not work with date type of 2 since the year is entered first.
if (vDateTypeTemp == 1) // mm/dd/yyyy hh:pp
vDateName.value = mMonth+strSeperator+mDay+strSeperator+mYear+" "+mHour+":"+mMinute;
if (vDateTypeTemp == 3) // dd/mm/yyyy hh:pp
vDateName.value = mDay+strSeperator+mMonth+strSeperator+mYear+" "+mHour+":"+mMinute;
} 
/*if (!dateValid(vDateValueCheck)) {
alert("Invalid Date\nPlease Re-Enter");
vDateType = vDateTypeTemp;
vDateName.value = "";
vDateName.focus();
return true;
}*/
vDateType = vDateTypeTemp;
return true;
}
else {
if (vDateType == 1) {
if (vDateValue.length == 2) {
vDateName.value = vDateValue+strSeperator;
}
if (vDateValue.length == 5) {
vDateName.value = vDateValue+strSeperator;
   }
if (vDateValue.length == 10) {
vDateName.value = vDateValue+" ";
   }
if (vDateValue.length == 13) {
vDateName.value = vDateValue+":";
   }}
if (vDateType == 2) {
if (vDateValue.length == 4) {
vDateName.value = vDateValue+strSeperator;
}
if (vDateValue.length == 7) {
vDateName.value = vDateValue+strSeperator;
   }
if (vDateValue.length == 10) {
vDateName.value = vDateValue+" ";
   }
if (vDateValue.length == 13) {
vDateName.value = vDateValue+":";
   }} 
if (vDateType == 3) {
if (vDateValue.length == 2) {
vDateName.value = vDateValue+strSeperator;
}
if (vDateValue.length == 5) {
vDateName.value = vDateValue+strSeperator;
   }
if (vDateValue.length == 10) {
vDateName.value = vDateValue+" ";
   }
if (vDateValue.length == 13) {
vDateName.value = vDateValue+":";
   }
}
return true;
   }
}
if (vDateValue.length == 10&& dateCheck) {
/*if (!dateValid(vDateName)) {
// Un-comment the next line of code for debugging the dateValid() function error messages
//alert(err);  
alert("Invalid Date\nPlease Re-Enter");
vDateName.focus();
vDateName.select();
   }*/
}
return false;
}
else {
var strCheck2 = '65,67,86,12,16,17,18,19,20,32,33,34,35,36,37,38,39,40,45,46,113,114,115,116,117,118,119,120,121';
if (strCheck2.indexOf(whichCode) != -1) {
    if(whichCode == 37 ||whichCode == 39||whichCode == 16)
      return true;        
        vDateName.value = vDateName.value + "";
        return false;
        }
else {
// If the value is not in the string return the string minus the last
// key entered.
if (isNav4) {
vDateName.value = "";
vDateName.focus();
vDateName.select();
return false;
}
else
{
vDateName.value = vDateName.value.substr(0, (vDateValue.length-1));
return false;
            }
         }
      }
   }
}
}
/*--                     --*/