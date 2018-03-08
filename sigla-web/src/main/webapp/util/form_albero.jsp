<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<!-- 
 ?ResourceName "gestione_conti.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript">
function selezionaRiga(riga) {
	document.mainForm.comando.value = "doSelezionaRiga";
	document.mainForm.riga.value = riga;
	document.mainForm.submit();
}
</script>
<script language="javascript" src="scripts/css.js"></script>
<title>Risultato ricerca</title>
</head>

<body class="Form">
<%
  SelezionatoreAlberoBP bp = (SelezionatoreAlberoBP)BusinessProcess.getBusinessProcess(request);
%>
<FORM name=mainForm action="Seleziona.do" action-ng="Seleziona.do" method=post>
<input type=hidden name="comando">
<input type=hidden name="riga">
<%
	BusinessProcess.encode(bp,pageContext); 
%>
<table border="3" cellspacing="0" height=100% cellpadding="2" bgcolor="silver" align="center" bordercolorlight="lightgrey">
	<tr height=100%><td colspan=2>
	<div style="position:relative; width=100%; height=100%; overflow:auto; background-color: white">
	<table bgcolor=white cellspacing="0" cellpadding="0" border=0 align=center >
<%	request.setAttribute("treerows",bp.getRighe());
		request.setAttribute("levels",new Integer(bp.getNumeroLivelli()));
		request.setAttribute("joinL","img/treejoinL16.gif");
		request.setAttribute("joinT","img/treejoinT16.gif");
		request.setAttribute("joinI","img/treejoinI16.gif");
		request.setAttribute("folder","img/FolderSmall.gif");
		request.setAttribute("onselect","selezionaRiga");%>
	</table>
	</div>
	</td></tr>
</table>
</FORM>
</body>
</html>