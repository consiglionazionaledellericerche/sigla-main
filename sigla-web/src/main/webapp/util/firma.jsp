<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.jada.firma.bulk.*,
		it.cnr.jada.firma.bp.*"
%>

<%
	CRUDFirmaBP bp = (CRUDFirmaBP)BusinessProcess.getBusinessProcess(request);
	Doc_firma_digitaleBulk doc = (Doc_firma_digitaleBulk)bp.getModel();
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript">
function doScaricaFile() {	
   larghFinestra=5;
   altezFinestra=5;
   sinistra=(screen.width)/2;
   alto=(screen.height)/2;
   window.open("<%=(doc==null?null:doc.getDownloadUrl())%>","DOWNLOAD","left="+sinistra+",top="+alto+",width="+larghFinestra+", height="+altezFinestra+",menubar=no,toolbar=no,location=no,resizable=yes")
}
</script>

<title><%=bp.getBulkInfo().getShortDescription()%></title>
</head>

<body class="Form">
<% bp.openFormWindow(pageContext);%>
	<table class="Panel">
		<tr>
	  	    <td><% bp.getController().writeFormLabel(out,"default","data"); %></td>
			<td><% bp.getController().writeFormInput(out,"default","data"); %></td>
		</tr>
		<tr>
	        <td><% bp.getController().writeFormLabel(out,"default","nome_file"); %></td>
	        <td><% bp.getController().writeFormInput(out,"default","nome_file"); %>
				<% if (!(bp.getStatus()==bp.INSERT)) {%>
					<% bp.getController().writeFormField(out,"default","attivaFile_blob"); %>
				<% } %>
			</td>
		</tr>
		<tr>
	        <td><% bp.getController().writeFormLabel(out,"default","ds_file"); %></td>
	        <td><% bp.getController().writeFormInput(out,"default","ds_file"); %></td>
		</tr>
		<% if (bp.getCaller()==null && !(bp.getStatus()==bp.INSERT || bp.getStatus()==bp.EDIT)) {%>
		<tr>
	        <td><% bp.getController().writeFormLabel(out,"default","blob"); %></td>
	        <td><% bp.getController().writeFormInput(out,"default","blob"); %></td>
	    </tr>
		<% } %>
   	</table>
<% bp.closeFormWindow(pageContext); %>
</body>