<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->
<%@page import="it.cnr.contab.ordmag.ordini.bp.CRUDEvasioneOrdineBP"%>
<%@ page 
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
 <% CRUDEvasioneOrdineBP bp= (CRUDEvasioneOrdineBP)BusinessProcess.getBusinessProcess(request); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
 <title>Evasione Ordini</title>
<body class="Form">
<% 	bp.openFormWindow(pageContext); %>
	<table class="Panel">
		<tr>
			<table>
				<tr>
					<%
						bp.getController().writeFormField(out, "findUnitaOperativaOrd");
					%>
					<%
						bp.getController().writeFormField(out, "findNumerazioneMag");
					%>
				</tr>
			</table>
			<table>
			<% bp.getController().writeFormField(out, "dataBolla");%>
			<% bp.getController().writeFormField(out, "numeroBolla");%>
			<% bp.getController().writeFormField(out, "dataConsegna");%>
			</table>
		<tr>
	<div class="Group">		
	<table border="0" cellspacing="0" cellpadding="2">
		<tr>
			<td><% bp.getController().writeFormLabel( out, "find_cd_terzo"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_cd_terzo"); %></td>
			<td><% bp.getController().writeFormLabel( out, "find_cd_precedente"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_cd_precedente"); %></td>			
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "find_ragione_sociale"); %></td>
			<td colspan=3><% bp.getController().writeFormInput( out, "find_ragione_sociale"); %></td>			
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "find_esercizio_ordine"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_esercizio_ordine"); %></td>
			<td><% bp.getController().writeFormLabel( out, "find_data_ordine"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_data_ordine"); %></td>
			<td><% bp.getController().writeFormLabel( out, "find_cd_numeratore_ordine"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_cd_numeratore_ordine"); %></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "find_numero_ordine"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_numero_ordine"); %></td>
			<td><% bp.getController().writeFormLabel( out, "find_riga_ordine"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_riga_ordine"); %></td>
			<td><% bp.getController().writeFormLabel( out, "find_consegna_ordine"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_consegna_ordine"); %></td>
		</tr>
	</table>
	</div>	
			<td colspan=2 align="center">
				<% JSPUtils.button(out,bp.encodePath("img/find24.gif"),bp.encodePath("Ricerca"), "javascript:submitForm('doCercaConsegneDaEvadere')",null, bp.getParentRoot().isBootstrap()); %>
			</td>
		</tr>
	
		<tr>
			<td colspan=2>
			      <b ALIGN="CENTER"><font size=2>Consegne</font></b>
			      <% bp.getConsegne().writeHTMLTable(pageContext,"consegneSet",false,false,false,"100%","300px", true); %>
			</td>
		</tr>
		<table>
			<tr>
				<%
					bp.getConsegne().writeFormField(out, "quantita");
					bp.getConsegne().writeFormField(out, "tipoConsegna");
					bp.getConsegne().writeFormField(out, "dtPrevConsegna");
				%>
			</tr>
		</table>
		<table>
			<tr>
				<%
					bp.getConsegne().writeFormField(out, "findMagazzino");
					bp.getConsegne().writeFormField(out, "findLuogoConsegnaMag");
				%>
			</tr>
		</table>
		<table>
			<tr>
				<%
						bp.getConsegne().writeFormField(out, "findUnitaOperativaOrdDest");
				%>
			</tr>
		</table>
		<table>
			<tr>
				<%
					bp.getConsegne().writeFormField(out, "imImponibile");
					bp.getConsegne().writeFormField(out, "imIva");
					bp.getConsegne().writeFormField(out, "imIvaD");
					bp.getConsegne().writeFormField(out, "imTotaleConsegna");
				%>
			</tr>
		</table>
		<table>
			<tr>
				<%
					bp.getConsegne().writeFormField(out, "findObbligazioneScadenzario");
				%>
			</tr>
		</table>
	</table>
	<%
		bp.closeFormWindow(pageContext);
	%>
</body>
</html>