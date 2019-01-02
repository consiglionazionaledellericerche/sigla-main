<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript" src="scripts/disableRightClick.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Mandato/Reversale</title>
<body class="Form">
<% 	it.cnr.contab.doccont00.bp.RicercaMandatoReversaleBP bp = (it.cnr.contab.doccont00.bp.RicercaMandatoReversaleBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>
   <table class="Panel card p-2">
	<tr>
	<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	<td><% bp.getController().writeFormInput( out, "esercizio"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_unita_organizzativa"); %></td>
	<td><% bp.getController().writeFormInput( out, "complete_uo"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_tipo_documento_cont"); %></td>
	<td><% bp.getController().writeFormInput( out, "cd_tipo_documento_cont"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "ti_documento_cont"); %></td>
	<td><% bp.getController().writeFormInput( out, "ti_documento_cont"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "pg_documento_cont"); %></td>
	<td><% bp.getController().writeFormInput( out, "pg_documento_cont"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "im_documento_cont"); %></td>
	<td><% bp.getController().writeFormInput( out, "im_documento_cont"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "dt_emissione"); %></td>
	<td><% bp.getController().writeFormInput( out, "dt_emissione"); %></td>
	</tr>
<!--	
	<tr>
	<td><% bp.getController().writeFormLabel( out, "dt_annullamento"); %></td>
	<td><% bp.getController().writeFormInput( out, "dt_annullamento"); %></td>
	</tr>
-->	
	<tr>
	<td><% bp.getController().writeFormLabel( out, "stato"); %></td>
	<td><% bp.getController().writeFormInput( out, "stato"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_terzo"); %></td>
	<td><% bp.getController().writeFormInput( out, "complete_terzo"); %></td>
	</tr>
   </table>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>