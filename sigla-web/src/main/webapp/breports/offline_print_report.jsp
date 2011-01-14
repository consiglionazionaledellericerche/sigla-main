<%@ page 
	import="it.cnr.contab.reports.bp.*,it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.reports.servlet.*"
%>

<html>
<head>
<script language="javascript" src="scripts/fckeditor.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Stampa offline</title>
<body class="Form">
<% OfflineReportPrintBP bp = (OfflineReportPrintBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>
	La stampa richiesta verrà accodata.
	<table class="Panel">
		<tr>
			<td>
				<% bp.writeFormLabel(out,"ds_utente"); %>
			</td>
			<td colspan="4">
				<% bp.writeFormInput(out,"ds_utente"); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.writeFormLabel(out,"ti_visibilita"); %>
			</td>
			<td colspan="4">
				<% bp.writeFormInput(out,"ti_visibilita"); %>
			</td>
		</tr>
	</table>
	<% if (bp.isEMailVisible()){ %>		
		<table>
			<tr>
				<td><% bp.writeFormLabel(out,"fl_email"); %></td>
				<td><% bp.writeFormInput(out,"fl_email"); %></td>
			</tr>
		</table>	
	<%} %>
	<% if (bp.isEMailEnabled()){ %>
		<div class="GroupLabel">E-Mail</div>          
		<table class="Group" style="width:80%">
			<tr><% bp.writeFormField(out,"email_a");%></tr>
			<tr><% bp.writeFormField(out,"email_cc");%></tr>
			<tr><% bp.writeFormField(out,"email_ccn");%></tr>
			<tr><% bp.writeFormField(out,"email_subject");%></tr>
			<tr><% bp.writeFormField(out,"email_body");%></tr>
		</table>
		<table class="Group" style="width:80%">
			<tr>
				<td colspan="5">
					Formato data/ora: DD/MM/YYYY HH:MM
				</td>
			</tr>
			<tr>
				<% bp.writeFormField(out,"dt_partenza");%>
				<% bp.writeFormField(out,"intervallo");%>
				<% bp.writeFormField(out,"ti_intervallo");%>
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