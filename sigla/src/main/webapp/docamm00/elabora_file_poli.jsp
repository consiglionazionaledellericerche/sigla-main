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
<title>Elaborazione File</title>
</head>

<body class="Form">
<% ElaboraFilePoliBP bp = (ElaboraFilePoliBP)BusinessProcess.getBusinessProcess(request); 
   bp.openFormWindow(pageContext); %>

	<div class="Group" style="width:100%">
	<fieldset> 
		<table width="100%">
			<tr>
			<td>
					<% bp.getController().writeFormLabel(out,"flBlacklist");%>
					<% bp.getController().writeFormInput(out,null,"flBlacklist",false,null,"onChange=\"submitForm('doOnFlBlacklistChange')\"");%>
					<% bp.getController().writeFormLabel(out,"comunicazionePoliv");%>
					<% bp.getController().writeFormInput(out,null,"comunicazionePoliv",false,null,"onChange=\"submitForm('doOnComunicazionePolivChange')\"");%>
				
				</td>
				</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"mese");%>
					<% bp.getController().writeFormInput(out,null,"mese",false,null,"onChange=\"submitForm('doOnMeseChange')\"");%>
				</td>
		</table>
	</fieldset>
	</div>
<%bp.closeFormWindow(pageContext); %>
</body>
</html> 