<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Registro unico delle fatture</title>
</head>

<body class="Form">
<% ElaboraNumUnicoFatturaPBP bp = (ElaboraNumUnicoFatturaPBP)BusinessProcess.getBusinessProcess(request); 
   bp.openFormWindow(pageContext); %>
		<table width="50%">
				<td>
					<% bp.getController().writeFormLabel(out,"dataRegistrazioneA");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"dataRegistrazioneA",false,null,"");%>
				</td>
		</table>
<%bp.closeFormWindow(pageContext); %>
</body>
</html> 