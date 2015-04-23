<%@ page 
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
%>
<title>FATTURA ELETTRONICA</title>
<script language="JavaScript">
function doScaricaFatturaHtml() {
	window.open('genericdownload/fattura.html?methodName=scaricaFatturaHtml&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>', 
			'Fattura', 'toolbar=no, location=no, directories=no, status=no, menubar=no,resizable,scrollbars,width=800,height=600').focus() ;
}
function doScaricaEsito() {
	window.open('genericdownload/<%=nomeFile%>?methodName=scaricaEsito&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>',
			'Notifica', 'toolbar=no, location=no, directories=no, status=no, menubar=no,resizable,scrollbars,width=800,height=600').focus();
}
</script>
</head>
<body class="Form">
<%	bp.openFormWindow(pageContext); %>
	<div class="Group">	
	<table class="Panel">
		<tr><% bp.getController().writeFormField(out,"idCodice");%></tr>
		<tr><% bp.getController().writeFormField(out,"identificativoSdi");%></tr>
		<tr><% bp.getController().writeFormField(out,"dataDocumento");%></tr>
		<tr><% bp.getController().writeFormField(out,"codiceDestinatario");%></tr>
		<% if (userInfo.getUnita_organizzativa().getCd_tipo_unita().equalsIgnoreCase("ENTE")) {%>
			<tr><% bp.getController().writeFormField(out,"unitaOrganizzativa");%></tr>
		<% } else {%>
			<tr><% bp.getController().writeFormField(out,"unitaDestinazione");%></tr>			
		<% } %>	 
		<tr>
			<% bp.getController().writeFormField(out,"unitaCompetenza");%>
			<% 	if (!bp.isSearching() && model != null && 
					model.getDocumentoEleTrasmissione().getUnitaCompetenza() != null &&
					userInfo.getUnita_organizzativa().equalsByPrimaryKey(model.getDocumentoEleTrasmissione().getUnitaCompetenza())) {
					bp.getController().writeFormField(out,"flCompletato");
				}
			%>
		</tr>
		<tr>
			<% 	if (bp.isSearching()) {%>
				<% bp.getController().writeFormField(out,"statoDocumento");%>
			<% } else {%>
				<td>
						<%bp.getController().writeFormInput(out,null,"statoDocumentoVisual",true,"GroupLabel","style=\"background: #F5F5DC;background-color:transparent;border-style : none; cursor:default;font-size : 16px;\"");%>
				</td>
				<% 	if (model != null && model.getStatoNotificaEsito()!= null) {%>
					<td>
						<%bp.getController().writeFormInput(out,null,"statoNotificaEsitoVisual",true,"GroupLabel","style=\"background: #F5F5DC;background-color:transparent;border-style : none; width:300; cursor:default; font-size : 16px;\"");%>
					</td>
				<% } %> 
			<% } %>	    
		</tr>
			<% 	if (!bp.isSearching() && model != null && model.isRicevutaDecorrenzaTermini()) {%>
				<td>
					<%bp.getController().writeFormInput(out,null,"ricevutaDecorrenza",true,"GroupLabel","style=\"background: #F5F5DC;background-color:transparent;border-style : none; width:300; cursor:default; font-size : 16px;\"");%>
				</td>
			<% } %> 
		<% 	if (!bp.isSearching() && model != null && 
				model.getStatoDocumento().equals(StatoDocumentoEleEnum.RIFIUTATO.name())) {%>
		<tr>
			<% bp.getController().writeFormField(out,"motivoRifiuto");%>
		</tr>
		<% } %>
	</table>
	</div>	
<%
	String[][] pages = new String[][] {
		{ "tabEleTrasmissione","Dati relativi alla trasmissione","/docamm00/tab_fatt_ele_trasmissione.jsp" },
		{ "tabEleTestata","Dati generali del documento","/docamm00/tab_fatt_ele_testata.jsp" },					
		{ "tabEleDettaglio","Dettaglio","/docamm00/tab_fatt_ele_dettaglio.jsp" }, 
		{ "tabEleIVA","Riepilogo IVA","/docamm00/tab_fatt_ele_iva.jsp" },
		{ "tabEleOrdine","Riferimento acquisto","/docamm00/tab_fatt_ele_ordine.jsp" },
		{ "tabEleTrasporto","Trasporto","/docamm00/tab_fatt_ele_trasporto.jsp" },
		{ "tabEleTributi","Tributi","/docamm00/tab_fatt_ele_tributi.jsp" },
		{ "tabEleSconto","Sconto","/docamm00/tab_fatt_ele_sconto.jsp" },
		{ "tabEleAllegati","Allegati","/docamm00/tab_fatt_ele_allegati.jsp" }
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