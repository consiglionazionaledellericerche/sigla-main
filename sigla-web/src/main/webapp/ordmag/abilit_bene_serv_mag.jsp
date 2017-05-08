<%@page import="it.cnr.contab.ordmag.anag00.bp.CRUDAssUnitaOperativaBP"%>
<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.ordmag.anag00.bp.*"
%>

<%
CRUDAbilitBeneServMagBP bp = (CRUDAbilitBeneServMagBP)BusinessProcess.getBusinessProcess(request);
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Abilitazione Magazzino/Categorie</title>
</head>
<body class="Form">
<%
	bp.openFormWindow(pageContext);
%>
	<table class="Panel">
		<TR>
		   <TD><% bp.getController().writeFormLabel(out,"cdMagazzino");%></TD>
		   <TD><% bp.getController().writeFormInput(out,"cdMagazzino");%></TD>
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
				<legend class="GroupLabel">Categorie/gruppo Abilitate</legend>
				<table width="100%">
				<tr>
					<td ><%bp.getAbilitBeneServMagController().writeHTMLTable(pageContext,null,true,false,true,"100%","200px"); %></td>
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
		<td><% bp.getAbilitBeneServMagController().writeFormLabel( out, "cdCategoriaGruppo");%></td>
			<% bp.getAbilitBeneServMagController().writeFormInput(out,"default","findMagazzino",!bp.isEditingAbilitazione(),"FormInput", null);%></td>	
		</tr>
	
		<tr></tr>
		<tr></tr>
		<tr>
		  <td><% bp.getAbilitBeneServMagController().writeFormLabel(out,"dtIniValidita");%></td>
		  <td colspan="2"><% bp.getAbilitBeneServMagController().writeFormInput(out,"dtIniValidita");%></td>
		</tr>
		<tr>
		  <td><% bp.getAbilitBeneServMagController().writeFormLabel(out,"dtFinValidita");%></td>
		  <td colspan="2"><% bp.getAbilitBeneServMagController().writeFormInput(out,"dtFinValidita");%></td>
		</tr>
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>