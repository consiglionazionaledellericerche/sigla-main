<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.bollo00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<!-- 
 ?ResourceName "risultato_ricerca.jsp"
 ?ResourceTimestamp "13/12/00 19.48.42"
 ?ResourceEdition "1.0"
-->

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<title>Risultato ricerca</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>

<body class="Form">

<% ConsAttoBolloBP bp = (ConsAttoBolloBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>
	<table class="Panel" width="100%" height="100%">
		<% if (ConsAttoBolloBP.LIVELLO_TIP.equals(bp.getPathConsultazione()) || ConsAttoBolloBP.LIVELLO_UO.equals(bp.getPathConsultazione())) {%>
		<tr>
			<td>
				<% bp.writeFormLabel(out,"tipoConsultazione"); %>
				<div class="d-inline-block">
				    <% bp.writeFormInput(out,null,"tipoConsultazione",false,null,"onclick=\"javascript:submitForm('doCambiaVisibilita')\""); %>
				</div>
			</td>		
			<% bp.writeFormField(out,"numGeneraleFogli"); %>				
			<% bp.writeFormField(out,"numGeneraleEsemplari"); %>				
			<% bp.writeFormField(out,"imGeneraleBollo"); %>				
		</tr>
		<% } %>
		<tr height="100%">
			<td colspan="10">
				<% bp.writeHTMLTable(pageContext,"100%","100%"); %>
			</td> 
		</tr>
		<tr>
			<td colspan="10">
				<% bp.writeHTMLNavigator(out); %>
			</td>
		</tr>
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>

</html>
