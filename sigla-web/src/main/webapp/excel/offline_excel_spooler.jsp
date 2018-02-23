<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.jada.excel.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<title>Coda</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>

<body class="Form">
<% OfflineExcelSpoolerBP bp = (OfflineExcelSpoolerBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>
	L'estrazione richiesta verrà accodata.
	<table>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"ds_estrazione"); %>
				    <% bp.getController().writeFormInput(out,"ds_estrazione"); %></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"fl_email"); %>
				    <% bp.getController().writeFormInput(out,"fl_email"); %></td>
			</tr>
	</table>	
	<% if (bp.isEMailEnabled()){ %>
		<div class="GroupLabel">E-Mail</div>          
		<table class="Group" style="width:80%">
			<tr><% bp.getController().writeFormField(out,"email_a");%></tr>
			<tr><% bp.getController().writeFormField(out,"email_cc");%></tr>
			<tr><% bp.getController().writeFormField(out,"email_ccn");%></tr>
			<tr><% bp.getController().writeFormField(out,"email_subject");%></tr>
			<tr><% bp.getController().writeFormField(out,"email_body");%></tr>
		</table>
		<table class="Group" style="width:80%">
			<tr>
				<td colspan="5">
					Formato data/ora: DD/MM/YYYY HH:MM
				</td>
			</tr>
			<tr>
				<% bp.getController().writeFormField(out,"dt_partenza");%>
				<% bp.getController().writeFormField(out,"intervallo");%>
				<% bp.getController().writeFormField(out,"ti_intervallo");%>
			</tr>
		</table>
		
		<script type="text/javascript">		
		  var oFCKeditor = new FCKeditor('main.email_body') ;
		  var sSkinPath = "<%=request.getContextPath()%>/scripts/editor/skins/office2003/" ;
		  oFCKeditor.Config['SkinPath'] = sSkinPath ;
		  oFCKeditor.Config['PreloadImages'] =
			sSkinPath + 'images/toolbar.start.gif' + ';' +
			sSkinPath + 'images/toolbar.end.gif' + ';' +
			sSkinPath + 'images/toolbar.bg.gif' + ';' +
			sSkinPath + 'images/toolbar.buttonarrow.gif' ;
		  oFCKeditor.ReplaceTextarea();
		</script>		
	<%}%>
	
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>