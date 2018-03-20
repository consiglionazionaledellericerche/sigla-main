<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.compensi00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title> Elaborazione INPS Mensile </title>
</head>
<body class="Form"> 

<%	EstrazioneINPSMensileBP bp = (EstrazioneINPSMensileBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>

<table>
  <tr>
	<td height="50"><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td height="50"><% bp.getController().writeFormInput(out,"esercizio"); %></td>
  </tr>
  <tr>
    <td><% bp.getController().writeFormLabel(out,"mese");%></td>
    <td><% bp.getController().writeFormInput(out,"mese");%></td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>