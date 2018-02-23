<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.utenze00.bp.*,it.cnr.contab.utenze00.bulk.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript">
function apriMenu(cd_nodo) {
	if (!disableDblClick()) return;
	document.mainForm.target = "menu_tree";
	submitForm('doApriMenu('+cd_nodo+')');
}
function collapseAllNode() {
	if (!disableDblClick()) return;
	document.mainForm.target = "menu_tree";
	submitForm('doCollapseAll()');
}

function chiudiMenu(cd_nodo) {
	if (!disableDblClick()) return;
	document.mainForm.target = "menu_tree";
	submitForm('doChiudiMenu('+cd_nodo+')');
}
function selezionaMenu(cd_nodo) {
//	if (!disableDblClick()) return;
	var frame = window.parent.frames['workspace'];
	if (window.top.AttendereText && window.top.AttendereImg){
	  try { 
  		  window.top.AttendereText.innerHTML = "Caricamento in corso...";	
	  	  window.top.AttendereImg.src = "img/progression.gif";
	  	  }catch(er) {
	  	  }
  	}	
	if (frame && frame.document.mainForm && frame.document.mainForm.comando && !frame.document.mainForm.submitted) {
		_submitForm(frame.document.mainForm,'doSelezionaMenu('+cd_nodo+')',true,"GestioneMenu.do");
	} else {
		document.mainForm.target = "workspace";
		_submitForm(document.mainForm,'doSelezionaMenu('+cd_nodo+')');
	}
}
</script>
<script language="javascript" src="scripts/css.js"></script>
<title>Menu</title>
</head>
<% GestioneUtenteBP bp = (GestioneUtenteBP)BusinessProcess.getBusinessProcess(request,"/GestioneUtenteBP"); %>

<body class="TreeFrame">
<form name="mainForm" action="GestioneMenu.do" method=post onSubmit="return disableDblClick()">
<input type=hidden name="comando">
<% JSPUtils.scrollSupport(pageContext); %>
<% BusinessProcess.encode(bp,pageContext); %>
<table>
	<tr>
		<td nowrap><% bp.writeMenu(out); %></td>
	</tr>
</table>
</form>
</body>
</html>