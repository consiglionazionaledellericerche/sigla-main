<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.utenze00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
	<head>
		<% JSPUtils.printBaseUrl(pageContext); %>
		<title>Unita organizzative</title>
		<script language="JavaScript" src="scripts/util.js"></script>
		<script language="JavaScript" src="scripts/css.js"></script>
	</head>

	<body class="Form">

<%	SelezionatoreUnitaOrganizzativaBP bp = (SelezionatoreUnitaOrganizzativaBP)BusinessProcess.getBusinessProcess(request,"/GestioneUtenteBP/SelezionatoreUnitaOrganizzativa");;
	bp.openForm(pageContext,"SelezionatoreUnitaOrganizzativa",null); %>

		<table class="Panel" width="100%" height="100%">
			<tr>
				<td>
					<% bp.getUserInfo().writeFormLabel(out,"scelta_esercizio_uo","esercizio", bp.getParentRoot().isBootstrap()); %>
					<% bp.getUserInfo().writeFormInput(out,"scelta_esercizio_uo","esercizio",false,null,"onchange=\"submitForm('doSelezionaEsercizio')\"",null,FormController.EDIT,bp.getFieldValidationMap(), bp.getParentRoot().isBootstrap()); %>
				</td>
			</tr>
			<tr>
				<td><span class="FormLabel">Unità organizzativa</span></td>
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