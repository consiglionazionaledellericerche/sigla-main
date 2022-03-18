<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.doccont00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
	<head>
		<script language="JavaScript" src="scripts/util.js"></script>
		<% JSPUtils.printBaseUrl(pageContext);%>
	</head>
	<script language="javascript" src="scripts/css.js"></script>
	<title>Prospetto spese per Cdr e GAE</title>
	<body class="Form">
	<% ProspettoSpeseCdrBP bp = (ProspettoSpeseCdrBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

<table>
	<tr>
		<td >
			<% bp.getController().writeFormInput(out,"cd_cdr");	 %>
		</td>
		<td >
			<% bp.getController().writeFormInput(out,"ds_cdr");	 %>
		</td>
		
	</tr>
</table>
<table class="w-100">
	<tr>
		<td>
			<% bp.getSpeseCdr().setEnabled(false);
			   bp.getSpeseCdr().writeHTMLTable(pageContext,null,false,false,false,"100%","300px");	 %>
		</td>
	</tr>
</table>
<table>	
	<tr>
		<td >
			<% bp.getController().writeFormLabel(out,"totSpeseDaPdg1");	 %>
		</td>
		<td >
			<% bp.getController().writeFormInput(out,"totSpeseDaPdg1");	 %>
		</td>
		<td >
			<% bp.getController().writeFormLabel(out,"totObbligazioni1");	 %>
		</td>
		<td >
			<% bp.getController().writeFormInput(out,"totObbligazioni1");	 %>
		</td>
	</tr>
	<tr>
		<td >
			<% bp.getController().writeFormLabel(out,"totSpeseDaPdg2");	 %>
		</td>
		<td >
			<% bp.getController().writeFormInput(out,"totSpeseDaPdg2");	 %>
		</td>
		<td >
			<% bp.getController().writeFormLabel(out,"totObbligazioni2");	 %>
		</td>
		<td >
			<% bp.getController().writeFormInput(out,"totObbligazioni2");	 %>
		</td>
	</tr>
	<tr>
		<td >
			<% bp.getController().writeFormLabel(out,"totSpeseDaPdg3");	 %>
		</td>
		<td >
			<% bp.getController().writeFormInput(out,"totSpeseDaPdg3");	 %>
		</td>
		<td >
			<% bp.getController().writeFormLabel(out,"totObbligazioni3");	 %>
		</td>
		<td >
			<% bp.getController().writeFormInput(out,"totObbligazioni3");	 %>
		</td>
	</tr>
	<% if (((it.cnr.contab.doccont00.core.bulk.ProspettoSpeseCdrBulk) bp.getModel()).getMessage1() != null )
	{ %>
	<tr>
		<td colspan=4>
			<% bp.getController().writeFormInput(out,null, "message1",false,null,"style=\"color:red\"");	 %>
		</td>
	</tr>
	<%}%>
	<% if (((it.cnr.contab.doccont00.core.bulk.ProspettoSpeseCdrBulk) bp.getModel()).getMessage2() != null )
	{ %>
	<tr>
		<td colspan=4>
			<% bp.getController().writeFormInput(out,null, "message2",false,null,"style=\"color:red\"");	 %>
		</td>
	</tr>
	<%}%>
	<% if (((it.cnr.contab.doccont00.core.bulk.ProspettoSpeseCdrBulk) bp.getModel()).getMessage3() != null )
	{ %>
	
	<tr>
		<td colspan=4>
			<% bp.getController().writeFormInput(out,null, "message3",false,null,"style=\"color:red\"");	 %>
		</td>
	</tr>
	<%}%>	
</table>
	
	<%	bp.closeFormWindow(pageContext); %>
</body>