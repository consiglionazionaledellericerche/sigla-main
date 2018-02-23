<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*,
	        it.cnr.jada.util.action.*,
	        it.cnr.jada.blobs.bp.*"
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
<% ExcelBlobBP bp = (ExcelBlobBP)BusinessProcess.getBusinessProcess(request); %>
<title>Risultato ricerca</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript">
function doScarica() {
   larghFinestra=5;
   altezFinestra=5;
   sinistra=(screen.width)/2;
   alto=(screen.height)/2;
   window.open("<%= bp.getDownloadUrl() %>","DOWNLOAD","left="+sinistra+",top="+alto+",width="+larghFinestra+", height="+altezFinestra+",menubar=no,toolbar=no,location=no")
}
</script>
<script language="javascript" src="scripts/css.js"></script>
</head>

<body class="Form">
<%	 bp.openFormWindow(pageContext); %>

	<table class="Panel" height="100%" width="100%">
		<tr><td  height="100%">
		<% bp.writeHTMLTable(pageContext,"100%","100%"); %>
		</td></tr>
		<tr><td>
		<% bp.writeHTMLNavigator(out); %>
		</td></tr>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>

</html>