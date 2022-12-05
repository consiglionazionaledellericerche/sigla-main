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
<% ElaboraFileIntraBP bp = (ElaboraFileIntraBP)BusinessProcess.getBusinessProcess(request); 
   bp.openFormWindow(pageContext); %>

	<div class="Group card p-2" style="width:100%">
	<fieldset>
		<table width="100%">
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"mese");%>
					<% bp.getController().writeFormInput(out,null,"mese",false,null,"onChange=\"submitForm('doOnMeseChange')\"");%>
				</td>
			</tr>
			  <%if ((bp.getModel() instanceof it.cnr.contab.docamm00.docs.bulk.VIntrastatBulk)&& (bp.getFile()!=null) && bp.getInvio()) {%>
				<tr>
					<td>
						<% bp.getController().writeFormLabel(out,"nrProtocolloAcq");%>
						<% bp.getController().writeFormInput(out,null,"nrProtocolloAcq",false,null,null);%>
					</td>
					<td>
						<% bp.getController().writeFormLabel(out,"nrProtocolloVen");%>
						<% bp.getController().writeFormInput(out,null,"nrProtocolloVen",false,null,null);%>
					</td>
				</tr>	
			 <%}%>
			
		</table>
	</fieldset>
	</div>
<%bp.closeFormWindow(pageContext); %>
</body>
</html> 