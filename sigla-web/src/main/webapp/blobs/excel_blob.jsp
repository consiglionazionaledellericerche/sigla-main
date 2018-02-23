<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.blobs.bp.*,
		it.cnr.jada.util.jsp.*"
%>
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<% CRUDConfigExcelBlobBP bp = (CRUDConfigExcelBlobBP)BusinessProcess.getBusinessProcess(request);%>
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
<title>File Excel</title>
<body class="Form">
<%	bp.openFormWindow(pageContext);%>
<div class="Group" style="width:100%">
	<table class="Panel">
		<tr>
            <td><% bp.writeFormLabel(out,"default","blob"); %></td>
            <td colspan=4><% bp.writeFormInput(out,"default","blob"); %></td>
        </tr>   
		<tr>
            <td><% bp.writeFormLabel(out,"default","ds_file"); %></td>
            <td colspan=4><% bp.writeFormInput(out,"default","ds_file"); %></td>
		</tr>
		<tr>
			<% bp.writeFormField(out,"default","nome_file"); %>
			<% bp.writeFormField(out,"default","attivaExcel_blob"); %>
		</tr>
		<tr>
            <td><% bp.writeFormLabel(out,"default","stato"); %></td>
            <td colspan=4><% bp.writeFormInput(out,"default","stato"); %></td>			
		</tr>
		<tr>
            <td><% bp.writeFormLabel(out,"default","tipo"); %></td>
            <td colspan=4><% bp.writeFormInput(out,"default","tipo"); %></td>						
		</tr>
		<tr>
			<% bp.writeFormField(out,"default","dacr"); %>
		</tr>
		<tr>
			<% bp.writeFormField(out,"default","duva"); %>
		</tr>			
	</table>
</div>	
<%	bp.closeFormWindow(pageContext); %>
</body>	