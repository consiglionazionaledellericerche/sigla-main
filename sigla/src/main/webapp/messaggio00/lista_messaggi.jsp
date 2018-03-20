<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<title>Messaggi</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<script>
	function onClose() {
		if (!window.opener.closed);
			window.opener.startCheckmessageOnlyIf(!document.mainForm.submitted);
	}
</script>
</head>

<body class="Form">

<% SelezionatoreListaBP bp = (SelezionatoreListaBP)BusinessProcess.getBusinessProcess(request);
	 bp.openForm(pageContext); %>

	<table class="Panel" width="100%" height="100%">
		<tr height="100%"><td colspan="2">
		<% bp.writeHTMLTable(pageContext,"100%","100%"); %>
		</tr>
		<tr><% bp.writeFormField(out,"view","soggetto"); %></tr>
		<tr><% bp.writeFormField(out,"view","priorita"); %></tr>
		<tr><% bp.writeFormField(out,"view","corpo"); %></tr>
		<tr><td colspan="2">
			<% bp.writeHTMLNavigator(out); %>
		</td></tr>		
	</table>

<%	bp.closeForm(pageContext); %>
</body>

</html>