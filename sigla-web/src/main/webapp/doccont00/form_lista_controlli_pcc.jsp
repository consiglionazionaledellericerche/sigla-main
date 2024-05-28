<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
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
<%
	SelezionatoreListaBP bp = (SelezionatoreListaBP)BusinessProcess.getBusinessProcess(request);
%>
<title>Risultato ricerca</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript">
function doScaricaCSV() {
	doOpenWindow('<%=JSPUtils.getAppRoot(request)%>genericdownload/pcc.csv?methodName=scaricaCSV&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>',
			'CSV', 'toolbar=no, location=no, directories=no, status=no, menubar=no,resizable,scrollbars,width=800,height=600');
}
</script>

</head>

<body class="Form">

<% 
	bp.openFormWindow(pageContext);
%>
<%	if (bp.getParentRoot().isBootstrap()) { %>
	<% bp.writeHTMLTable(pageContext,"100%","100%"); %>
	<% bp.writeHTMLNavigator(out); %>
<% } else {%>	
	<table class="Panel" height="100%" width="100%">
		<tr height="100%"><td>
		<% bp.writeHTMLTable(pageContext,"100%","100%"); %>
		</td></tr>
		<tr><td>
		<% bp.writeHTMLNavigator(out); %>
		</td></tr>
	</table>
<% } %>
<%bp.closeFormWindow(pageContext); %>
</body>
</html>