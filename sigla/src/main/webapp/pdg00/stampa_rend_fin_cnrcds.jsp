<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.cdip.bulk.*,
		it.cnr.contab.pdg00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Rendiconti Finanziari CNR per CDS</title>
</head>
<body class="Form">

<%
	StampaRendFinanziarioCNRBP bp = (StampaRendFinanziarioCNRBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>
<br><br>
<table>
  <tr>											  
	<td><% bp.getController().writeFormLabel(out,"findCds"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"cd_cds"); %>
		<% bp.getController().writeFormInput(out,"ds_cds"); %>
		<% bp.getController().writeFormInput(out,"findCds"); %>
	</td>
  </tr>
</table>
<br><br>
<table class="Panel">
    <tr>
		<td colspan=4> </td>
	</tr>

	<tr>
		<td colspan=4><b>Stampa Entrate (importi diversi da zero)</b>
		</td>
	</tr>
	<tr>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per capitolo", "if (disableDblClick()) javascript:submitForm('doStampaEntrateCDSPerCapitolo')", bp.getParentRoot().isBootstrap());%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per articolo", "if (disableDblClick()) javascript:submitForm('doStampaEntrateCDSPerArticolo')", bp.getParentRoot().isBootstrap());%></td>
		<td></td>
	</tr>


		<td colspan=4> </td>
	<tr>	
	<tr>
		<td colspan=4><b>Stampa Spese (importi diversi da zero)</b>
		</td>
	</tr>
	<tr>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per capitolo", "if (disableDblClick()) javascript:submitForm('doStampaSpeseCDSPerCapitolo')", bp.getParentRoot().isBootstrap());%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per articolo", "if (disableDblClick()) javascript:submitForm('doStampaSpeseCDSPerArticolo')", bp.getParentRoot().isBootstrap());%></td>
		<td></td>
	</tr>


		<td colspan=4> </td>
	<tr>	
		
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>