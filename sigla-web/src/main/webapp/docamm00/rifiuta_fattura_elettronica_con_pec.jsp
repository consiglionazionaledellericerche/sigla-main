<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.docamm00.fatturapa.bulk.*"
%>

<%
	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	RifiutaFatturaBulk bulk = (RifiutaFatturaBulk)bp.getModel();
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Rifiuta Fattura Elettronica</title>
</head>

<body class="Workspace">
<% bp.openForm(pageContext);%>
	<table align="center" class="Window card col-5">
		<tr>
		  	<td class="FormTitle card-header h5">
		  		<img src="img/question.gif"><span>Rifiuto della fattura al Fornitore</span>
		  	</td>
		</tr>
		<tr>
			<td>
				<table class="Panel" width="100%">
                    <tr><% bp.getController().writeFormField(out,"emailPEC"); %></tr>
                    <%if (bulk.isDecorrenzaTermini()) { %>
                        <tr><% bp.getController().writeFormField(out,"message_option_nullable"); %></tr>
                    <%}%>
                    <%if (!bulk.isMessageOptionSelected()) { %>
                        <tr><% bp.getController().writeFormField(out,bulk.isNota() ? "message_text_nota": "message_text"); %></tr>
                    <%}%>
                    <tr><% bp.getController().writeFormField(out,"note"); %></tr>
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