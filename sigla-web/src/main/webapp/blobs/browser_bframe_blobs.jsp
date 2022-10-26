<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.jada.blobs.bp.*"
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
<% 	CRUDBframeBlobBP bp = (CRUDBframeBlobBP)BusinessProcess.getBusinessProcess(request); %>
<title>File esterni</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript">
function doScarica() {
	doOpenWindow("<%= bp.getDownloadUrl(pageContext) %>","DOWNLOAD",null);
}
</script>
<script language="javascript" src="scripts/css.js"></script>
</head>

<body class="Form">

<%	bp.openFormWindow(pageContext); %>

	<table class="Panel" width="100%" height="100%">
		<tr>
			<td>
				<% bp.getSelezione_blob_tipoController().writeFormLabel(out,"blob_tipo"); %>
			</td>
			<td width="100%">
				<% bp.getSelezione_blob_tipoController().writeFormInput(out,"blob_tipo"); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getSelezione_blob_tipoController().writeFormLabel(out,"ti_visibilita"); %>
			</td>
			<td width="100%">
				<% bp.getSelezione_blob_tipoController().writeFormInput(out,null,"ti_visibilita",false,null,"onclick=\"javascript:submitForm('doCambiaVisibilita')\""); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.writeHistoryLabel(pageContext); %>
			</td>
			<td width="100%">
				<% bp.writeHistoryField(pageContext,"filename"); %>
			</td>
		</tr>
		<tr height="100%">
			<td colspan="2">
				<% bp.writeHTMLTable(pageContext,"100%","100%"); %>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<% bp.writeHTMLNavigator(out); %>
			</td>
		</tr>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>

</html>