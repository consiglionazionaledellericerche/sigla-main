<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.utenze00.bp.*,it.cnr.contab.utenze00.bulk.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>

<title>Sistema non disponibile per l'utente</title>

<body class="Workspace">

<% 	LoginBP bp = (LoginBP)BusinessProcess.getBusinessProcess(request);
	bp.openForm(pageContext,"Login",null); %>

<table align="center" class="Window">
	<tr>
		<td>
			<table class="Panel">
			  <tr>
				<td rowspan="2"><img src="img/error.gif"></td>
			  	<td>Siamo spiacenti ma la tua utenza non ha accesso a questo sistema.</td>
				<td rowspan="2">
                    <%JSPUtils.button(out,
                        bp.getParentRoot().isBootstrap() ? "fa fa-2x fa-sign-out" : "img/home16.gif",
                        "Logout ",
                        "javascript:doLogout()",
                        "btn-outline-info btn-title",
                        bp.getParentRoot().isBootstrap());%>
				</td>
			  </tr>
			</table>
		</td>
	</tr>
</table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>