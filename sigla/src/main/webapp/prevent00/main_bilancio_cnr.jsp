<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.prevent00.bp.*,
		it.cnr.contab.prevent00.bulk.*"		
%> 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Bilancio preventivo CNR</title>
</head>
<body class="Form">

<%  
	CRUDBilancioPrevCnrBP bp = (CRUDBilancioPrevCnrBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);

	Bilancio_preventivoBulk bilancioPrev = (Bilancio_preventivoBulk) bp.getModel();
%>

<table class="Panel">

	<tr>
	<td ALIGN="CENTER">
		<%JSPUtils.button(out, "img/edit16.gif", "img/edit16.gif", "Predispone Bilancio CNR", "if (disableDblClick()) submitForm('doProduceBilancioCnr')", null, bilancioPrev.isBottoneProduciCnrEnabled(bp), bp.getParentRoot().isBootstrap());%>
		<%JSPUtils.button(out, "img/edit16.gif", "img/edit16.gif", "Approva", "if (disableDblClick()) submitForm('doApprovaBilancioCnr')", null, bilancioPrev.isBottoneApprovaCnrEnabled(bp), bp.getParentRoot().isBootstrap());%>
<!-- 	<%JSPUtils.button(out, "img/edit16.gif", "Variazioni di bilancio", "if (disableDblClick()) submitForm('doVariazioniBilancioCnr')", bp.getParentRoot().isBootstrap());%> -->
		<%JSPUtils.button(out, "img/edit16.gif", "Stampe", "if (disableDblClick()) submitForm('doApriStampe')", bp.getParentRoot().isBootstrap());%>
	</td>
	</tr>

</table>	
<table class="Panel">
	
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>	
		
	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	<td>	<% bp.getController().writeFormInput( out, "esercizio"); %></td>
	</tr>
	
	<tr>
	<td>	<span class="FormLabel">Ente</span></td>
	<td>	<% bp.getController().writeFormInput( out, "cd_cds"); %></td>
	</tr>	
	
	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "stato"); %></td>	
	<td>	<% bp.getController().writeFormInput( out, "stato"); %>
			<% bp.getController().writeFormLabel( out, "dt_approvazione"); %>
			<% bp.getController().writeFormInput( out, "dt_approvazione"); %></td>
	</tr>

	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>

	<tr>
	<td colspan=2 ALIGN="CENTER">
		<%JSPUtils.button(out, "img/transfer.gif", "img/transfer.gif", "Dettagli bilancio - Spese", "if (disableDblClick()) submitForm('doGestioneDettagliSpese')",null, bilancioPrev.areBottoniDettagliCnrEnabled(bp), bp.getParentRoot().isBootstrap());%>
		<%JSPUtils.button(out, "img/compressed.gif", "img/compressed.gif", "Dettagli bilancio - Entrate", "if (disableDblClick()) submitForm('doGestioneDettagliEntrate')",null, bilancioPrev.areBottoniDettagliCnrEnabled(bp), bp.getParentRoot().isBootstrap());%>
	</td>
	</tr>
		
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>