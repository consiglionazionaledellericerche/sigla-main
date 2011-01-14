<!-- 
 ?ResourceName "liquidazione_definitiva_iva.jsp"
 ?ResourceTimestamp "09/08/01 16.54.00"
 ?ResourceEdition "1.0"
-->

<%@ page 
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
				<% if (liquidazione.isLiquidazione_commerciale()) { %>
				   <td><% bp.getController().writeFormLabel(out,"tipoImpegnoFlag");%></td>
				   <td><% bp.getController().writeFormInput(out,null,"tipoImpegnoFlag",false,null,"");%></td>
				<% } %>
			</tr>     	
			<% if (!richiamaChiusuraMode) { %>
				<tr>
					<td>
						<% bp.getController().writeFormLabel(out,"mese");%>
					</td>
					<td>
						<% bp.getController().writeFormInput(out,null,"mese",false,null,"onChange=\"submitForm('doOnMeseChange')\"");%>
					</td>
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
					new String[][] {
					{ "tabEsigDetr","Esigibilità/Detraibilità","/gestiva00/tab_esigdetr.jsp" },
					{ "tabImporti","Importi aggiuntivi","/gestiva00/tab_importi.jsp" },
					{ "tabAltro","Altro","/gestiva00/tab_altro.jsp" }},
					bp.getTab("tab"),
					"center",
					"100%",
					null );%>
			</table>
		<%	} %>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>