<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*"
%>

<%
	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Firma tramite OTP</title>
</head>

<body class="Workspace">
<% bp.openForm(pageContext);%>
	<table align="center" class="Window">
		<tr>
			<td><img src="img/question.gif"></td>
		  	<td>Inserire l'utente, la password e l'otp per la firma del documento.</td>
		</tr>
		<tr>
			<td colspan="2">
				<table class="Panel" width="100%">
					<tr><% bp.getController().writeFormField(out,"userNameOTP"); %></tr>
					<tr><% bp.getController().writeFormField(out,"passwordOTP"); %></tr>
					<tr><% bp.getController().writeFormField(out,"otp"); %></tr>
					<tr><td>&nbsp;</td></tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<hr>			
				<table class="Panel" width="100%">
					<tr>
						<td style="width:50%"><input style="width:100%" type=submit value="Annulla" name="comando.doAnnulla"></td>
						<td style="width:50%"><input style="width:100%" type=submit value="Conferma" name="comando.doConferma"></td>
					</tr>
				</table>
			</td>
		</tr>      
   	</table>
<% bp.closeForm(pageContext); %>
</body>