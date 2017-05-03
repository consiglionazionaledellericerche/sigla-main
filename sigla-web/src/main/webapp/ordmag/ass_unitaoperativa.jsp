<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.incarichi00.bp.*,
		it.cnr.contab.incarichi00.bulk.*"
%>

<%
CRUDConfigRepertorioLimitiBP bp = (CRUDConfigRepertorioLimitiBP)BusinessProcess.getBusinessProcess(request);
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
		   <TD><% bp.getController().writeFormInput(out,"dsUnitaOperativa");%></TD>
		</TR>
	</table>
	<table class="Panel">
		<TR>
		 	<TD colspan="3">
			<fieldset>
				<legend class="GroupLabel">Unità operative associate</legend>
				<table width="100%">
				<tr>
					<td colspan="4">
					<% JSPUtils.tabbed(
									pageContext,
									"tab",
									new String[][] {
										{ "tabUop","Uop associate","/ordmag/tab_ass_uop.jsp" }							
									},
									bp.getTab("tab"),
									"center",
									"100%",
									null ); %>
				
				    </td>
				</tr>
				</table>
			</fieldset>
			</TD>
		</TR>
	</table>
	bp.closeFormWindow(pageContext); %>
</body>