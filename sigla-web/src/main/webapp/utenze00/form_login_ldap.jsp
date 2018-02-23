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
			<td>
				<table class="Panel" width="500">
					<tr><td style="font-size : 14px"><img src="img/question.gif">Indicare il nuovo account ufficiale CNR (nome.cognome) e la relativa password.
					Premere il tasto "Entra senza nuovo account" se non si dispone del nuovo codice.</td>
				  	</tr>
					<tr><td height="30" valign="bottom">
						<a href="javascript:doHelp('utenze00/primo_accesso.html')">Istruzioni per il nuovo account ufficiale CNR</a>	
					</td></tr>
					<tr><td height="30" valign="top">
						<a href="javascript:doHelp('utenze00/recupero_password.html')">Recupero della password</a>
					</td></tr>
  					<% if (bp.getGiorniRimanenti(uc)>0) { %>
				  	<tr><td align="middle" style="font-size : 14px; font-weight:bold; text-decoration:underline; font-style:italic; color:#990000">si hanno ancora
				  		<% out.println(bp.getGiorniRimanenti(uc)); %>
				  		giorni di tempo per inserire il nuovo login</td>
				  	</tr>
					<% } else { %>
				  	<tr><td align="middle" style="font-size : 14px; font-weight:bold; text-decoration:underline; font-style:italic; color:#990000">E' obbligatorio inserire il nuovo account ufficiale CNR (nome.cognome)</td>
				  	</tr>
					<% } %>
        		</table>
        	</td>
		</tr>
		<tr>
			<td colspan="2">
				<table class="Panel" width="100%">
					<tr><% bp.getController().writeFormField(out,"ldap_userid"); %></tr>
					<tr><% bp.getController().writeFormField(out,"ldap_password"); %></tr>
					<tr><td>&nbsp;</td></tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table class="Panel" width="100%">
					<tr>
						<td style="width:50%"><input style="width:100%" type=submit value="Entra" name="comando.doEntraLdap"></td>
  						<% if (bp.getGiorniRimanenti(uc)>0) { %>
						<td style="width:50%"><input style="width:100%" type=submit value="Entra senza nuovo account" name="comando.doAnnullaLdap"></td>
						<% } else { %>
						<td style="width:50%"><input style="width:100%" type=submit value="Entra senza nuovo account" name="comando.doAnnullaLdap" disabled="true"></td>
						<% } %>
					</tr>
				</table>
			</td>
		</tr>
  	</table>

	<%	bp.closeForm(pageContext); %>

	</body>

</html>