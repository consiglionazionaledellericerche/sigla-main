<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<% CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP)BusinessProcess.getBusinessProcess(request); 
	Fattura_attivaBulk fattura = (Fattura_attivaBulk)bp.getModel();
	String nomeFileFirmato = fattura != null?fattura.getNomeFileInvioSdi():"";
%>
<title> <%=(bp.getModel() instanceof Nota_di_credito_attivaBulk) ?"Nota di Credito":(bp.getModel() instanceof Nota_di_debito_attivaBulk)?"Nota di Debito":"Fattura Attiva"%></title>

<script language="JavaScript">
function doVisualizzaDocumentoFatturaElettronica() {
	doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/visualizzaDocumentoAttivo.html?methodName=visualizzaDocumentoAttivo&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>', 
			'Documento', 'toolbar=no, location=no, directories=no, status=no, menubar=no,resizable,scrollbars,width=800,height=600').focus() ;
}
function doScaricaFatturaAttivaHtml() {
	window.open('<%=JSPUtils.getAppRoot(request)%>genericdownload/fatturaAttiva.html?methodName=scaricaFatturaAttivaHtml&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>', 
			'Fattura Attiva', 'toolbar=no, location=no, directories=no, status=no, menubar=no,resizable,scrollbars,width=800,height=600').focus() ;
}
function doScaricaFatturaAttivaFirmata() {
	window.open('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=nomeFileFirmato%>?methodName=scaricaFatturaAttivaFirmata&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>', 
			'Fattura Attiva', 'toolbar=no, location=no, directories=no, status=no, menubar=no,resizable,scrollbars,width=800,height=600').focus() ;
}
function doRistampaFatturaElettronica() {
	window.open('<%=JSPUtils.getAppRoot(request)%>genericdownload/ristampaFatturaElettronica.html?methodName=ristampaFatturaElettronica&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>', 
			'Fattura Attiva', 'toolbar=no, location=no, directories=no, status=no, menubar=no,resizable,scrollbars,width=800,height=600').focus() ;
}

</script>
</head>
<body class="Form">
<%	bp.openFormWindow(pageContext);
	String[][] pages = null;
	if (bp.getModel() instanceof Nota_di_credito_attivaBulk) {
		Nota_di_credito_attivaBulk ndc = (Nota_di_credito_attivaBulk)bp.getModel();
		int length = 0;
		int offset = 4;
		java.util.Hashtable accertamenti = ndc.getFattura_attiva_accertamentiHash();
		java.util.Hashtable obbligazioni = ndc.getObbligazioniHash();
		boolean hasAccertamenti = !(accertamenti == null || accertamenti.isEmpty());
		boolean hasObbligazioni = !(obbligazioni == null || obbligazioni.isEmpty());

		String [][] defaultPages = new String[][] {
					{ "tabFatturaAttiva","Testata","/docamm00/tab_fattura_attiva.jsp" },
					{ "tabCliente","Cliente","/docamm00/tab_cliente.jsp" },					
					{ "tabFatturaAttivaDettaglio","Dettaglio","/docamm00/tab_fattura_attiva_dettaglio.jsp" }, 
					{ "tabFatturaAttivaConsuntivo","Consuntivo","/docamm00/tab_fattura_attiva_consuntivo.jsp" },
					{ "tabFatturaAttivaAccertamenti","Accertamenti","/docamm00/tab_fattura_attiva_accertamenti.jsp" },
					{ "tabFatturaAttivaObbligazioni","Impegni","/docamm00/tab_fattura_attiva_obbligazioni.jsp" },
                    { "tabFatturaPassivaEconomica", "Economico/Patrimoniale", "/coepcoan00/tab_docamm_economica.jsp" }
				};

		if (ndc == null) {
			pages = defaultPages;
		} else {
			if (!hasObbligazioni && !hasAccertamenti)
				pages = defaultPages;
			else {
				pages = new String[defaultPages.length-1][3];
				for (int i = 0; i < pages.length-1; i++)
					pages[i] = defaultPages[i];
				pages[pages.length-1] = (hasAccertamenti)?
					new String[] { "tabFatturaAttivaAccertamenti","Accertamenti","/docamm00/tab_fattura_attiva_accertamenti.jsp" }:
					new String[] { "tabFatturaAttivaObbligazioni","Impegni","/docamm00/tab_fattura_attiva_obbligazioni.jsp" };
					//pages[pages.length-1] = new String[] { "tabFatturaAttivaIntrastat","Intrastat","/docamm00/tab_fattura_attiva_intrastat.jsp" };
			}
		}
	} else {
		if (bp.getModel() instanceof Fattura_attiva_IBulk && ((Fattura_attiva_IBulk)bp.getModel()).getPg_fattura_attiva() != null) {
			pages = new String[][] {
					{ "tabFatturaAttiva","Testata","/docamm00/tab_fattura_attiva.jsp" },
					{ "tabCliente","Cliente","/docamm00/tab_cliente.jsp" },					
					{ "tabFatturaAttivaDettaglio","Dettaglio","/docamm00/tab_fattura_attiva_dettaglio.jsp" }, 
					{ "tabFatturaAttivaConsuntivo","Consuntivo","/docamm00/tab_fattura_attiva_consuntivo.jsp" },
					{ "tabFatturaAttivaAccertamenti","Accertamenti","/docamm00/tab_fattura_attiva_accertamenti.jsp" },
					{ "tabFatturaAttivaIntrastat","Intrastat","/docamm00/tab_fattura_attiva_intrastat.jsp" },
					{ "tabFatturaAttivaAllegati","Allegati Aggiunti","/docamm00/tab_fattura_attiva_allegati.jsp" },
					{ "tabFatturaPassivaEconomica", "Economico/Patrimoniale", "/coepcoan00/tab_docamm_economica.jsp" }

				};
		}else {
			pages = new String[][] {
					{ "tabFatturaAttiva","Testata","/docamm00/tab_fattura_attiva.jsp" },
					{ "tabCliente","Cliente","/docamm00/tab_cliente.jsp" },					
					{ "tabFatturaAttivaDettaglio","Dettaglio","/docamm00/tab_fattura_attiva_dettaglio.jsp" }, 
					{ "tabFatturaAttivaConsuntivo","Consuntivo","/docamm00/tab_fattura_attiva_consuntivo.jsp" },
					{ "tabFatturaAttivaAccertamenti","Accertamenti","/docamm00/tab_fattura_attiva_accertamenti.jsp" },
					{ "tabFatturaAttivaIntrastat","Intrastat","/docamm00/tab_fattura_attiva_intrastat.jsp" },
					{ "tabFatturaPassivaEconomica", "Economico/Patrimoniale", "/coepcoan00/tab_docamm_economica.jsp" }
				};
		}
	}
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