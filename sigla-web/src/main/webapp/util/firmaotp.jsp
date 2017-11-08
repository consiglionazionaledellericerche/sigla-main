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
	<table align="center" class="Window card col-5">
		<tr>
		  	<td class="FormTitle card-header h5">
		  		<%if (bp.getParentRoot().isBootstrap()) { %>
		  			<i class="fa fa-question-circle fa-fw fa-2x text-info" aria-hidden="true"></i>
		  		<%} else {%>
		  		<img src="img/question.gif">
		  		<%}%>
		  		<span>Inserire l'utente, la password e l'otp per la firma del documento.</span>
		  	</td>
		</tr>
		<tr>
			<td>
				<table class="Panel" width="100%">
					<tr>
						<td><% bp.getController().writeFormLabel(out,"userNameOTP"); %></td>
						<td class="col-12"><% bp.getController().writeFormInput(out,"userNameOTP"); %></td>						
					</tr>
					<tr>
						<td><% bp.getController().writeFormLabel(out,"passwordOTP"); %></td>
						<td class="col-12"><% bp.getController().writeFormInput(out,"passwordOTP"); %></td>						
					</tr>
					<tr>
						<td><% bp.getController().writeFormLabel(out,"otp"); %></td>
						<td class="col-12"><% bp.getController().writeFormInput(out,"otp"); %></td>						
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<hr>			
				<table class="Panel" width="100%">
					<tr>
						<td style="width:50%">
							<input type="button" class="btn btn-info" name="comando.doAnnulla" style="width:100%" value="Annulla" onclick="submitForm('doAnnulla')">
						</td>
						<td style="width:50%">
							<input type="button" class="btn btn-primary" name="comando.doConferma" style="width:100%" value="Conferma" onclick="submitForm('doConferma')">
						</td>
					</tr>
				</table>
			</td>
		</tr>      
   	</table>
<% bp.closeForm(pageContext); %>
</body>