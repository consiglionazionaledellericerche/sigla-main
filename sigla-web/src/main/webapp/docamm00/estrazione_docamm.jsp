<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.docamm00.consultazioni.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Estrazione documenti amministrativi</title>
</head>
<body class="Form">
<%
	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>
    <div class="card p-2">
	    <table>
	        <tr><% bp.getController().writeFormField(out,"findAnagrafico"); %></tr>
        </table>
    </div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html> 
