<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.doccont00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
    <script language="JavaScript" src="scripts/util.js"></script>
    <script language="javascript" src="scripts/css.js"></script>
    <% JSPUtils.printBaseUrl(pageContext);%>
	<title>Carica File Cassiere</title>
</head>
<body class="Form">
    <% CaricaFileCassiereBP bp = (CaricaFileCassiereBP)BusinessProcess.getBusinessProcess(request); %>
    <% bp.openFormWindow(pageContext); %>
    <span class="FormLabel h1 text-primary" style="color:blue">Files</span>
    <div class="card p-2 mb-1">
        <% bp.writeHTMLTable(pageContext,"100%","300px"); %>
        <% if (bp.getParentRoot().isBootstrap()) { %>
        <br>
        <% } %>
        <% bp.writeHTMLNavigator(out); %>
	</div>
    <span class="FormLabel h1 text-primary" style="color:blue">Logs</span>
    <div class="card p-2 mt-2">
        <% bp.getLogs().writeHTMLTable(pageContext,"default",false,false,false,"100%","200px"); %>
    </div>
    <%bp.closeFormWindow(pageContext); %>
</body>