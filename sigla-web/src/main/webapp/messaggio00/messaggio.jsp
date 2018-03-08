<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Messaggi</title>
</head>
<body class="Form">

<%	CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>
	<div class="Group" style="width:100%">
		<table width="100%">
			<tr>
	   			<% bp.writeForm(out); %>
			</tr>
		</table>
	</div>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>