<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.utenze00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
	<head>
		<% JSPUtils.printBaseUrl(pageContext); %>
		<title>Cdr</title>
		<script language="JavaScript" src="scripts/util.js"></script>
		<script language="JavaScript" src="scripts/css.js"></script>
	</head>

	<body class="Form">

<%	SelezionatoreCdrBP bp = (SelezionatoreCdrBP)BusinessProcess.getBusinessProcess(request,"/GestioneUtenteBP/SelezionatoreCdr");;
	bp.openForm(pageContext,"SelezionatoreCdr",null); %>

		<table class="Panel" width="100%" height="100%">
			<tr>
				<td><span class="FormLabel">Cdr</span></td>
			</tr>
			<tr height="100%">
				<td>
					<% bp.writeHTMLTable(pageContext,"100%","100%"); %>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.writeHTMLNavigator(out); %>
				</td>
			</tr>
		</table>

<%	bp.closeForm(pageContext); %>
	</body>

</html>