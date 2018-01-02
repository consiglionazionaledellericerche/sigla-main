<%@page import="it.cnr.contab.ordmag.anag00.bp.CRUDAssUnitaOperativaBP"%>
<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.ordmag.anag00.bp.*"
%>

<%
CRUDAssUnitaOperativaBP bp = (CRUDAssUnitaOperativaBP)BusinessProcess.getBusinessProcess(request);
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Associazione Unità Operativa</title>
</head>
<body class="Form">
<%
	bp.openFormWindow(pageContext);
%>
	<table class="Panel">
		<TR>
		   <TD><% bp.getController().writeFormLabel(out,"cdUnitaOperativa");%></TD>
		   <TD><% bp.getController().writeFormInput(out,"cdUnitaOperativa");%></TD>
		</TR>
		<TR>
		   <TD><% bp.getController().writeFormLabel(out,"dsUnitaOperativa");%></TD>
		   <TD><% bp.getController().writeFormInput(out,null,"dsUnitaOperativa",true,null,null);%></TD>
		</TR>
	</table>
	<table class="Panel">
		<TR>
		 	<TD colspan="3">
			<fieldset>
				<legend class="GroupLabel">Unità operative associate</legend>
				<table width="100%">
				<tr>
					<td ><%bp.getAssUnitaOperativaController().writeHTMLTable(pageContext,null,true,false,true,"100%","200px"); %></td>
				</tr>
				</table>
			</fieldset>
			</TD>
		</TR>
	</table>
	<table width="100%">
		<tr></tr>
		<tr></tr>
		<tr>
		<td><% bp.getAssUnitaOperativaController().writeFormLabel( out, "cdUnitaOperativaRif");%></td>
			<% bp.getAssUnitaOperativaController().writeFormInput(out,"default","findUnitaOperativaRif",!bp.isEditingAssociazione(),"FormInput", null);%></td>	
		</tr>
	
		<tr></tr>
		<tr></tr>
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>