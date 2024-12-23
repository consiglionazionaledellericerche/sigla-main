<!-- 
 ?ResourceName "liquidazione_definitiva_iva.jsp"
 ?ResourceTimestamp "09/08/01 16.54.00"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.gestiva00.bp.*,
		it.cnr.contab.gestiva00.core.bulk.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>

<%	LiquidazioneDefinitivaIvaBP bp = (LiquidazioneDefinitivaIvaBP)BusinessProcess.getBusinessProcess(request);
    Liquidazione_definitiva_ivaVBulk liquidazione = (Liquidazione_definitiva_ivaVBulk)bp.getModel();
	boolean richiamaChiusuraMode = bp instanceof VisualizzaLiquidazioneDefinitivaIvaBP; %>
<title><%=((richiamaChiusuraMode)?"Richiama chiusura mensile dell'IVA": "Liquidazione definitiva IVA") %></title>
</head>
<body class="Form">

	<% bp.openFormWindow(pageContext); %>

	<div class="Group" style="width:100%">
		<table width="100%">
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"tipoSezionaleFlag");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"tipoSezionaleFlag",false,null,"onChange=\"submitForm('doOnTipoChange')\"");%>
				</td>
				<% if (bp.isAggiornaIvaDaVersareEnable()) { %>
						<td><% bp.getController().writeFormLabel(out,"debitoLastLiquidazioneProvvisoria"); %> </td>
						<td><% bp.getController().writeFormInput(out,"debitoLastLiquidazioneProvvisoria"); %> 
							<button class="Button" title="Aggiorna Iva da versare" onclick="submitForm('doAggiornaIvaDaVersare')"><img align="middle" class="Button" src="img/refresh16.gif"/></button>
						</td>
				 <% } %>
			</tr>     	
			<% if (!richiamaChiusuraMode) { %>
			    <tr>
					<td>
						<% bp.getController().writeFormLabel(out,"esercizio");%>
					</td>
			        <td><% bp.getController().writeFormInput(out,"esercizio");%></td>
			    </tr>
				<tr>
					<td>
						<% bp.getController().writeFormLabel(out,"mese");%>
					</td>
					<td>
						<% bp.getController().writeFormInput(out,null,"mese",false,null,"onChange=\"submitForm('doOnMeseChange')\"");%>
					</td>
					<% if (bp.isAggiornaIvaDaVersareEnable() && liquidazione!=null && liquidazione.getDataAggiornamentoLastLiquidazioneProvvisoria()!=null) { %>
						<td colspan="2">
							<% bp.getController().writeFormLabel(out,"dataAggiornamentoLastLiquidazioneProvvisoria"); %> 
							<% bp.getController().writeFormInput(out,"dataAggiornamentoLastLiquidazioneProvvisoria"); %> 
						</td>
					 <% } %>
				</tr>
				<tr>
					<td>
						<% bp.getController().writeFormLabel(out,"data_da");%>
					</td>
					<td>
						<% bp.getController().writeFormInput(out,null,"data_da",false,null,"");%>
					</td>
					<td>
						<% bp.getController().writeFormLabel(out,"data_a");%>
					</td>
					<td>
						<% bp.getController().writeFormInput(out,null,"data_a",false,null,"");%>
					</td>
				</tr>
			<%	} %>
			<tr>
				<% bp.getController().writeFormField(out,"pageNumber");%>
			</tr>
		</table>
		<table>
			<tr>
				<td>
					<span class="FormLabel">Prospetti stampati</span>
				</td>
				<td>
				   	<%bp.getDettaglio_prospetti().writeHTMLTable(pageContext,"prospetti_stampati",false,false,false,"100%","100px",true);%>
			   	</td>
			</tr>
		</table>
		<% if (!richiamaChiusuraMode) { %>
			<table>
			<%JSPUtils.tabbed(
					pageContext,
					"tab",
					bp.getTabs(),
					bp.getTab("tab"),
					"center",
					"100%",
					null );%>
			</table>
		<%	} %>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>