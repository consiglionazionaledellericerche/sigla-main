<%@ page pageEncoding="UTF-8"
	session="false"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//Dtd HTML 4.0 Transitional//EN">

<html>
	<head>
		<% JSPUtils.printBaseUrl(pageContext); %>
		<script language="JavaScript" src="scripts/util.js"></script>
		<script language="javascript" src="scripts/css.js"></script>
		<title>Login</title>
		<% if (request.getQueryString() != null && request.getQueryString().contains("error-code")) { %>
		    <script>showMessage(1,"img/errormsg.gif","Nome utente o password sbagliati.")</script>
		<%}%>
	</head>
	<body class="Workspace">
        <%
	    it.cnr.contab.utenze00.bp.LoginBP bp = (it.cnr.contab.utenze00.bp.LoginBP)BusinessProcess.getBusinessProcessRoot(request);
	    if (bp!=null)
			bp.openForm(pageContext);
	    else { %>
		<form name="mainForm" action="j_security_check" method="post" onsubmit="return disableDblClick()">
			<input type="hidden" name="comando">
			<input type="hidden" name="requestor" value="/SIGLA/utenze00/form_login.jsp">
		<%
			}
	    %>
	        <table align="center" class="Window">
				<tbody>
					<tr>
						<td><img src="img/question.gif"></td>
					  	<td>Inserire l'utente e la password per accedere all'applicazione</td>
					</tr>
					<tr>
						<td colspan="2">
							<table class="Panel" width="100%">
								<tbody><tr><td><span class="FormLabel">Utente</span></td><td><input type="text" name="j_username" class="FormInput" maxlength="200" size="15" onclick="cancelBubble(event)"></td></tr>
								<tr><td><span class="FormLabel">Password</span></td><td><input type="password" name="j_password" class="FormInput" maxlength="200" size="15" onfocus="focused(this)"></td></tr>
								<tr><td>&nbsp;</td></tr>
							</tbody></table>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<table class="Panel" width="100%">
								<tbody>
									<tr>
										<td style="width:50%"><input style="width:100%" type="submit" value="Entra" name="comando.doEntra"></td>
										<td style="width:50%"><input style="width:100%" type="submit" value="Cambia password" name="comando.doCambiaPassword"></td>
									</tr>
								</tbody>
							</table>
						</td>
					</tr>
		  		</tbody>
		  	</table>
		</form>
	</body>
</html>