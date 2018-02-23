<%@ page pageEncoding="UTF-8"
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
<title>Repertorio Limiti</title>
</head>
<body class="Form">
<%
	bp.openFormWindow(pageContext);
%>
<div class="card p-2">
	<table class="Panel w-100">
		<TR>
		   <TD><% bp.getController().writeFormLabel(out,"cd_tipo_limite");%></TD>
		   <TD><% bp.getController().writeFormInput(out,"cd_tipo_limite");%></TD>
		</TR>
		<TR>
		   <TD><% bp.getController().writeFormLabel(out,"ds_tipo_limite");%></TD>
		   <TD><% bp.getController().writeFormInput(out,"ds_tipo_limite");%></TD>
		</TR>
		<TR>
		   <TD><% bp.getController().writeFormLabel(out,"fl_raggruppa");%></TD>
		   <TD><% bp.getController().writeFormInput(out,"fl_raggruppa");%></TD>
		</TR>
	</table>
</div>
<div class="card p-2 mt-2">
	<table class="Panel w-100">
		<TR>
		 	<TD colspan="3">
			<fieldset>
				<legend class="GroupLabel h5 font-weight-bold text-primary">Repertorio Limiti per anno</legend>
				<table width="100%">
				<tr>
					<td colspan="4">
					<% JSPUtils.tabbed(
									pageContext,
									"tab",
									bp.getTabs(),
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

	<table class="Panel mt-2">
		<tr>
			<td><% bp.getRepertorioLimiti().writeFormLabel(out,"esercizio"); %></td>
			<td><% bp.getRepertorioLimiti().writeFormInput(out,"esercizio"); %></td>
		</tr>
		<tr>
	  	    <td><% bp.getRepertorioLimiti().writeFormLabel(out,"importo_limite"); %></td>
			<td><% bp.getRepertorioLimiti().writeFormInput(out,"importo_limite"); %></td>
		</tr>
	</table>
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>