<%@page import="it.cnr.contab.ordmag.anag00.MagazzinoBulk"%>
<%@page import="it.cnr.contab.ordmag.anag00.bp.CRUDAssUnitaOperativaBP"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.ordmag.anag00.bp.*"
%>

<%
CRUDNumerazioneMagBP bp = (CRUDNumerazioneMagBP)BusinessProcess.getBusinessProcess(request);
MagazzinoBulk mag = (MagazzinoBulk)bp.getModel();
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Numerazione per Magazzino</title>
</head>
<body class="Form">
<%
	bp.openFormWindow(pageContext);
%>
	<table class="Panel">
		<TR>
		   <TD><% bp.getController().writeFormLabel(out,"cdMagazzino");%></TD>
		   <TD><% bp.getController().writeFormInput(out,null,"cdMagazzino", !mag.isInQuery(),null,null);%></TD>
		</TR>
		<TR>
		   <TD><% bp.getController().writeFormLabel(out,"dsMagazzino");%></TD>
		   <TD><% bp.getController().writeFormInput(out,null,"dsMagazzino",true,null,null);%></TD>
		</TR>
	</table>
	<table class="Panel">
		<TR>
		 	<TD colspan="3">
			<fieldset>
				<legend class="GroupLabel">Numeratori per magazzino</legend>
				<table width="100%">
				<tr>
					<td ><%bp.getNumerazioneMagController().writeHTMLTable(pageContext,null,true,false,true,"100%","200px"); %></td>
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
		   <TD><% bp.getNumerazioneMagController().writeFormField(out,"cdNumeratoreMag");%></TD>
		   <TD><% bp.getNumerazioneMagController().writeFormLabel(out,"corrente");%></TD>
		   <TD><% bp.getNumerazioneMagController().writeFormInput(out,"corrente");%></TD>
		</tr>
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>