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
		<script language="JavaScript">
		function setFocus() {
			document.getElementsByName("main.find_cds.cd_proprio_unita")[0].focus();
		}
		</script>
	</head>
	<body class="Form" onload="setFocus()">

<%	SelezionaCdsBP bp = (SelezionaCdsBP)BusinessProcess.getBusinessProcess(request,"/GestioneUtenteBP/SelezionaCdsBP");;
	bp.openForm(pageContext,"SelezionaCds",null);
	bp.writeToolbar(pageContext); %>

		<table cellspacing=8>
			<tr>
				<td>
					<% bp.getUserInfo().writeFormLabel(out,"scelta_esercizio_uo","esercizio", bp.getParentRoot().isBootstrap()); %>
				</td>
				<td>
					<% bp.getUserInfo().writeFormInput(out,"scelta_esercizio_uo","esercizio",false,null,"onchange=\"submitForm('doSelezionaEsercizio')\"",null,FormController.EDIT,bp.getFieldValidationMap(), false); %>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"find_cds"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,"find_cds"); %>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"find_uo"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,"find_uo"); %>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"find_cdr"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,"find_cdr"); %>
				</td>
			</tr>
		</table>
	<%="</form>"%> 		
</body>
</html>
