<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.docamm00.fatturapa.bulk.*,
		it.cnr.contab.docamm00.bp.*,
		it.cnr.contab.utenze00.bulk.CNRUserInfo"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<% 
	CRUDFatturaPassivaElettronicaBP bp = (CRUDFatturaPassivaElettronicaBP)BusinessProcess.getBusinessProcess(request); 
	CNRUserInfo userInfo = (CNRUserInfo)HttpActionContext.getUserInfo(request);
	DocumentoEleTestataBulk model = (DocumentoEleTestataBulk)bp.getModel();
	String nomeFile = model != null?model.getNomeFile("EC"):"";
	String nomeFileFirmato = model != null?model.getNomeFileFirmato():"";

%>
<title>FATTURA ELETTRONICA</title>
<script language="JavaScript">
function doScaricaFatturaHtml() {
	doOpenWindow('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=nomeFileFirmato%>.html?methodName=scaricaFatturaHtml&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>',
			'Fattura', 'toolbar=no, location=no, directories=no, status=no, menubar=no,resizable,scrollbars,width=800,height=600');
}
function doScaricaFatturaFirmata() {
	doOpenWindow('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=nomeFileFirmato%>?methodName=scaricaFatturaFirmata&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>',
			'Fattura', 'toolbar=no, location=no, directories=no, status=no, menubar=no,resizable,scrollbars,width=800,height=600');
}
function doScaricaEsito() {
	doOpenWindow('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=nomeFile%>?methodName=scaricaEsito&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>',
			'Notifica', 'toolbar=no, location=no, directories=no, status=no, menubar=no,resizable,scrollbars,width=800,height=600');
}
</script>
</head>
<body class="Form">
<%	bp.openFormWindow(pageContext); %>
	<div class="Group">	
	<table class="Panel w-100">
		<tr>
			<td class="w-25"><% bp.getController().writeFormLabel(out,"idCodice");%></td>
			<td><% bp.getController().writeFormInput(out,"idCodice");%></td>
		<% 	if (!bp.isSearching() && model != null && model.isIrregistrabile()) {%>
			<td colspan="2">
			  	<span style="font-weight:bold; font-family:sans-serif; font-size:16px; color:blue">NON REGISTRABILE</span>
			</td>
		<% } %>
		</tr>
		
		<tr>
			<td><% bp.getController().writeFormLabel(out,"identificativoSdi");%></td>
			<td><% bp.getController().writeFormInput(out,"identificativoSdi");%></td>
			<td colspan="2">
				<% bp.getController().writeFormLabel(out,"progressivo");%>
				<% bp.getController().writeFormInput(out,"progressivo");%>
			</td>
		</tr>
			
		<tr><% bp.getController().writeFormField(out,"dataDocumento");%></tr>

		<tr><% bp.getController().writeFormField(out,"codiceDestinatario");%></tr>

		<tr>
		<% if (userInfo.getUnita_organizzativa().getCd_tipo_unita().equalsIgnoreCase("ENTE")) {%>
			<td><% bp.getController().writeFormLabel(out,"unitaOrganizzativa");%></td>
			<td colspan="2"><% bp.getController().writeFormInput(out,"unitaOrganizzativa");%></td>
		<% } else {%>
			<td><% bp.getController().writeFormLabel(out,"unitaDestinazione");%></td>
			<td colspan="2"><% bp.getController().writeFormInput(out,"unitaDestinazione");%></td>
		<% } %>	 
		</tr>			
		
		<tr>
			<td><% bp.getController().writeFormLabel(out,"unitaCompetenza");%></td>
			<td colspan="2"><% bp.getController().writeFormInput(out,"unitaCompetenza");%></td>
			<% 	if (!bp.isSearching() && model != null && 
					model.getUnitaCompetenza() != null &&
					userInfo.getUnita_organizzativa().equalsByPrimaryKey(model.getUnitaCompetenza())) { %>
				<td>
					<% bp.getController().writeFormLabel(out,"flCompletato"); %>
					<% bp.getController().writeFormInput(out,"flCompletato"); %>
				</td>
			<% 	} %>
		</tr>

		<% 	if (bp.isSearching()) {%>				
		<tr>
			<% bp.getController().writeFormField(out,"flIrregistrabile");%>			
		</tr>
		<% } %>	    

		<tr>
			<% 	if (bp.isSearching()) {%>				
				<% bp.getController().writeFormField(out,"statoDocumento");%>
			<% } else {%>
				<td>
					<%bp.getController().writeFormInput(out,null,"statoDocumentoVisual",true,"GroupLabel text-primary h5","style=\"background: #F5F5DC;background-color:transparent;border-style : none; cursor:default;font-size : 16px;\"");%>
				</td>
				<% 	if (model != null && model.getStatoNotificaEsito()!= null) {%>
					<td colspan="2">
						<%bp.getController().writeFormInput(out,null,"statoNotificaEsitoVisual",true,"GroupLabel text-primary h5","style=\"background: #F5F5DC;background-color:transparent;border-style : none; width:300; cursor:default; font-size : 16px;\"");%>
					</td>
				<% } %> 
			<% } %>	    
		</tr>

		<% 	if (!bp.isSearching() && model != null && model.isRicevutaDecorrenzaTermini() && model.isRifiutabile()) {%>
		<tr>
			<td colspan="4">
				<%bp.getController().writeFormInput(out,null,"ricevutaDecorrenza",true,"GroupLabel text-danger h5","style=\"background: #F5F5DC;background-color:transparent;border-style : none; width:300; cursor:default; font-size : 16px;\"");%>
			</td>
		</tr>				
		<% } %> 

		<% 	if (!bp.isSearching() && model != null && model.getStatoDocumento() != null &&
				model.getStatoDocumento().equals(StatoDocumentoEleEnum.RIFIUTATO.name())) {%>
		<tr>
			<% bp.getController().writeFormField(out,"motivoRifiuto");%>
		</tr>
		<% } %>
	</table>
	</div>	
<%
	String[][] pages = new String[][] {
		{ "tabEleTrasmissione", bp.getParentRoot().isBootstrap() ? "Trasmissione" : "Dati relativi alla trasmissione","/docamm00/tab_fatt_ele_trasmissione.jsp" },
		{ "tabEleTestata",bp.getParentRoot().isBootstrap() ? "Documento" : "Dati generali del documento","/docamm00/tab_fatt_ele_testata.jsp" },					
		{ "tabEleDettaglio","Dettaglio","/docamm00/tab_fatt_ele_dettaglio.jsp" }, 
		{ "tabEleIVA","Riepilogo IVA","/docamm00/tab_fatt_ele_iva.jsp" },
		{ "tabEleOrdine",bp.getParentRoot().isBootstrap() ? "Acquisto" : "Riferimento acquisto","/docamm00/tab_fatt_ele_ordine.jsp" },
		{ "tabEleTrasporto","Trasporto","/docamm00/tab_fatt_ele_trasporto.jsp" },
		{ "tabEleTributi","Tributi","/docamm00/tab_fatt_ele_tributi.jsp" },
		{ "tabEleSconto","Sconto","/docamm00/tab_fatt_ele_sconto.jsp" },
		{ "tabEleAllegati",bp.getParentRoot().isBootstrap() ? "<i class=\"fa fa-fw fa-file\" aria-hidden=\"true\"></i> Ricevuti" : "Allegati Ricevuti","/docamm00/tab_fatt_ele_allegati.jsp" },
		{ "tabAllegati",bp.getParentRoot().isBootstrap() ? "<i class=\"fa fa-fw fa-file\" aria-hidden=\"true\"></i> Aggiunti" : "Allegati Aggiunti","/util00/tab_allegati.jsp" }
	};
	JSPUtils.tabbed(
					pageContext,
					"tab",
					pages,
					bp.getTab("tab"),
					"center",
					"100%",
					null );
	bp.closeFormWindow(pageContext); %>
</body>