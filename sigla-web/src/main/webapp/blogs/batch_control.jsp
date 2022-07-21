<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.logs.bulk.*,
		it.cnr.contab.logs.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Batch</title>
</head>
<body class="Form">

<% CRUDBatchControlBP bp = (CRUDBatchControlBP)BusinessProcess.getBusinessProcess(request);
Batch_controlBulk  model = (Batch_controlBulk)bp.getModel();
	 bp.openFormWindow(pageContext); %>

	<formFieldProperty
		name="ds_batch" />
	<formFieldProperty
		name="procedura" />

	<table class="Group" style="width:100%">
		<tr>
			<% bp.getController().writeFormField(out,null,"procedura",1,5);%>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,null,"ds_batch");%>
			</td>
			<td colspan="5">
				<% bp.getController().writeFormInput(out,null,"ds_batch",false,null,"style=\"width:100%\"");%>
			</td>
		</tr>
		<% if (model==null || model.getProcedura()==null || !model.getProcedura().isProceduraJava()) { %>
		<tr>
			<td colspan="5">
				Lasciando vuoto "Intervallo" il batch verrà eseguito immediatamente e solo una volta
			</td>
		</tr>
		<tr>
			<td colspan="5">
				Formato data/ora: DD/MM/YYYY HH:MM
			</td>
		</tr>
		<tr>
			<% bp.getController().writeFormField(out,"dt_partenza");%>
			<% bp.getController().writeFormField(out,"intervallo_calcolato");%>
			<% bp.getController().writeFormField(out,"tipo_intervallo");%>
		</tr>
		<% } %>
	</table>
	<table class="Group" style="width:100%">
	  <tr>
		<td>
		  <span class="FormLabel" style="color:black">Parametri</span>
		</td>
	  <tr>
	  <tr>
		<td>
		  <% bp.getParametri().writeHTMLTable(
				pageContext,
				null,
				false,
				false,
				false,
				"100%",
				"300px",
				false); %>
		</td>
	  </tr>
	</table>
	
<% bp.closeFormWindow(pageContext); %>
</body>
</html>