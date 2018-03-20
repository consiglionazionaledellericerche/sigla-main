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
<%	StampaCertificazioneBP bp = (StampaCertificazioneBP)BusinessProcess.getBusinessProcess(request); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title> <%= bp.getJSPTitle() %> </title>
</head>
<body class="Form"> 

<%	bp.openFormWindow(pageContext); %>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"esercizio"); %></td>
  </tr>
</table> 
<table> 
  <tr>
	<td>
		<% bp.getController().writeFormLabel(out,"ti_cert");%>
	</td>
	<td>
		<% bp.getController().writeFormInput(out,null,"ti_cert",!bp.isEditingTi_cert(),null,"onClick=\"submitForm('doChangeTi_cert')\"");%>
	</td>
  </tr>	
</table>
  <br>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"findAnagraficoForPrint"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"cd_anag"); %>
		<% bp.getController().writeFormInput(out,"denominazione"); %>
		<% bp.getController().writeFormInput(out,"findAnagraficoForPrint"); %>
	</td>
  </tr>
</table>
  <br>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"note"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"note"); %></td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>