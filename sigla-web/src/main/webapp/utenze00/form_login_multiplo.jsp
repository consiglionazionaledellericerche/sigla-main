<%@ page pageEncoding="UTF-8"
	session="false"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,it.cnr.contab.utenze00.bp.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.jada.UserContext" %>
<!DOCTYPE HTML PUBLIC "-//W3C//Dtd HTML 4.0 Transitional//EN">

<html>
	<head>
		<% JSPUtils.printBaseUrl(pageContext); %>
		<script language="JavaScript" src="scripts/util.js"></script>
		<script language="javascript" src="scripts/css.js"></script>
		<title>Login</title>
	</head>

	<body class="Workspace">

<% LoginBP bp = (LoginBP)BusinessProcess.getBusinessProcess(request);
	UserContext uc = HttpActionContext.getUserContext(request.getSession());
	bp.openForm(pageContext); %>

	<table align="center" class="Window">
		<tr>
			<td><img src="img/question.gif"></td>
		  	<td>Selezionare l'utente applicativo con cui accedere</td>
		</tr>
		<tr>
			<td colspan="2">
				<table class="Panel" width="100%">
					<tr><% bp.getController().writeFormField(out,"utente_multiplo"); %></tr>
					<tr><td>&nbsp;</td></tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="20">
				<table class="Panel" width="100%">
					<tr>
						<td style="width:50%"><input style="width:100%" type=submit value="Entra" name="comando.doEntraUtenteMultiplo"></td>
						<td style="width:50%"><input style="width:100%" type=submit value="Annulla" name="comando.doLoginIniziale"></td>
					</tr>
				</table>
			</td>
		</tr>
  	</table>

	<%	bp.closeForm(pageContext); %>

	</body>

</html>