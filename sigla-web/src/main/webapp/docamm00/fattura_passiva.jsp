<%@ page 
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
<% CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP)BusinessProcess.getBusinessProcess(request); %>
<title> <%=(bp.getModel() instanceof Nota_di_creditoBulk) ?"Nota di Credito":(bp.getModel() instanceof Nota_di_debitoBulk)?"Nota di Debito":"Fattura Passiva"%></title>

</head>
<body class="Form">
<%	bp.openFormWindow(pageContext);
	String[][] pages = null;
	String [][] defaultPages= null;
	if (bp.getModel() instanceof Nota_di_creditoBulk) {
		Nota_di_creditoBulk ndc = (Nota_di_creditoBulk)bp.getModel();
		java.util.Hashtable obbligazioni = ndc.getFattura_passiva_obbligazioniHash();
		java.util.Hashtable accertamenti = ndc.getAccertamentiHash();
		boolean hasObbligazioni = !(obbligazioni == null || obbligazioni.isEmpty());
		boolean hasAccertamenti = !(accertamenti == null || accertamenti.isEmpty());
		if (ndc.isCommerciale() && ndc.getTi_bene_servizio() != null && 
				Bene_servizioBulk.BENE.equalsIgnoreCase(ndc.getTi_bene_servizio()) 
				&& ndc.getFl_intra_ue() && ndc.getFl_merce_extra_ue()!=null && ndc.getFl_merce_extra_ue()){
			defaultPages = new String[][] {
					{ "tabFatturaPassiva","Testata","/docamm00/tab_fattura_passiva.jsp" },
					{ "tabFornitore","Fornitore","/docamm00/tab_fornitore.jsp" },					
					{ "tabFatturaPassivaDettaglio","Dettaglio","/docamm00/tab_fattura_passiva_dettaglio.jsp" }, 
					{ "tabFatturaPassivaConsuntivo","Consuntivo","/docamm00/tab_fattura_passiva_consuntivo.jsp" },
					{ "tabFatturaPassivaObbligazioni","Storni","/docamm00/tab_fattura_passiva_obbligazioni.jsp" },
					{ "tabFatturaPassivaAccertamenti","Accertamenti","/docamm00/tab_fattura_passiva_accertamenti.jsp" }
				};
		}else{
	 		defaultPages = new String[][] {
				{ "tabFatturaPassiva","Testata","/docamm00/tab_fattura_passiva.jsp" },
				{ "tabFornitore","Fornitore","/docamm00/tab_fornitore.jsp" },					
				{ "tabFatturaPassivaDettaglio","Dettaglio","/docamm00/tab_fattura_passiva_dettaglio.jsp" }, 
				{ "tabFatturaPassivaConsuntivo","Consuntivo","/docamm00/tab_fattura_passiva_consuntivo.jsp" },
				{ "tabFatturaPassivaObbligazioni","Storni","/docamm00/tab_fattura_passiva_obbligazioni.jsp" },
				{ "tabFatturaPassivaAccertamenti","Accertamenti","/docamm00/tab_fattura_passiva_accertamenti.jsp" }
			 	//,{ "tabFatturaPassivaIntrastat","Intrastat","/docamm00/tab_fattura_passiva_intrastat.jsp" }
			};		
		}
		if (ndc == null) {
			pages = defaultPages;
		} else {
			if (!hasObbligazioni && !hasAccertamenti)
				pages = defaultPages;
			else {
				pages = new String[defaultPages.length-1][3];
				for (int i = 0; i < pages.length-1; i++)
					pages[i] = defaultPages[i];
				pages[pages.length-1] = (hasObbligazioni)?
					new String[] { "tabFatturaPassivaObbligazioni","Storni","/docamm00/tab_fattura_passiva_obbligazioni.jsp" }:
					new String[] { "tabFatturaPassivaAccertamenti","Accertamenti","/docamm00/tab_fattura_passiva_accertamenti.jsp" };
				//pages[pages.length-1] = new String[] { "tabFatturaPassivaIntrastat","Intrastat","/docamm00/tab_fattura_passiva_intrastat.jsp" };
			}
		}
	} else if (bp.getModel() instanceof Nota_di_debitoBulk) {
		Nota_di_debitoBulk ndd =(Nota_di_debitoBulk)bp.getModel();
		if (ndd.isCommerciale() && ndd.getTi_bene_servizio() != null && 
    					Bene_servizioBulk.BENE.equalsIgnoreCase(ndd.getTi_bene_servizio()) 
    					&& ndd.getFl_intra_ue() && ndd.getFl_merce_extra_ue()!=null && ndd.getFl_merce_extra_ue()){
			pages = new String[][] {
						{ "tabFatturaPassiva","Testata","/docamm00/tab_fattura_passiva.jsp" },
						{ "tabFornitore","Fornitore","/docamm00/tab_fornitore.jsp" },					
						{ "tabFatturaPassivaDettaglio","Dettaglio","/docamm00/tab_fattura_passiva_dettaglio.jsp" }, 
						{ "tabFatturaPassivaConsuntivo","Consuntivo","/docamm00/tab_fattura_passiva_consuntivo.jsp" },
						{ "tabFatturaPassivaObbligazioni","Impegni","/docamm00/tab_fattura_passiva_obbligazioni.jsp" },
					};
		}else
		{
			pages = new String[][] {
					{ "tabFatturaPassiva","Testata","/docamm00/tab_fattura_passiva.jsp" },
					{ "tabFornitore","Fornitore","/docamm00/tab_fornitore.jsp" },					
					{ "tabFatturaPassivaDettaglio","Dettaglio","/docamm00/tab_fattura_passiva_dettaglio.jsp" }, 
					{ "tabFatturaPassivaConsuntivo","Consuntivo","/docamm00/tab_fattura_passiva_consuntivo.jsp" },
					{ "tabFatturaPassivaObbligazioni","Impegni","/docamm00/tab_fattura_passiva_obbligazioni.jsp" }
					//,{ "tabFatturaPassivaIntrastat","Intrastat","/docamm00/tab_fattura_passiva_intrastat.jsp" }
				};		
		}
	}else if(bp.getModel() instanceof Fattura_passiva_IBulk) { 
		Fattura_passiva_IBulk fatturaPassiva=(Fattura_passiva_IBulk)bp.getModel();
	    		if (fatturaPassiva.isCommerciale() && fatturaPassiva.getTi_bene_servizio() != null && 
	        					Bene_servizioBulk.BENE.equalsIgnoreCase(fatturaPassiva.getTi_bene_servizio()) 
	        					&& fatturaPassiva.getFl_intra_ue() && fatturaPassiva.getFl_merce_extra_ue()!=null && fatturaPassiva.getFl_merce_extra_ue()){
	    			pages = new String[][] {
							{ "tabFatturaPassiva","Testata","/docamm00/tab_fattura_passiva.jsp" },
							{ "tabFornitore","Fornitore","/docamm00/tab_fornitore.jsp" },					
							{ "tabFatturaPassivaDettaglio","Dettaglio","/docamm00/tab_fattura_passiva_dettaglio.jsp" }, 
							{ "tabFatturaPassivaConsuntivo","Consuntivo","/docamm00/tab_fattura_passiva_consuntivo.jsp" },
							{ "tabFatturaPassivaObbligazioni","Impegni","/docamm00/tab_fattura_passiva_obbligazioni.jsp" },
							{ "tabLetteraPagamentoEstero","Documento 1210","/docamm00/tab_lettera_pagam_estero.jsp" },
						};
	    		}
	    		else
	    		{
	    			pages = new String[][] {
							{ "tabFatturaPassiva","Testata","/docamm00/tab_fattura_passiva.jsp" },
							{ "tabFornitore","Fornitore","/docamm00/tab_fornitore.jsp" },					
							{ "tabFatturaPassivaDettaglio","Dettaglio","/docamm00/tab_fattura_passiva_dettaglio.jsp" }, 
							{ "tabFatturaPassivaConsuntivo","Consuntivo","/docamm00/tab_fattura_passiva_consuntivo.jsp" },
							{ "tabFatturaPassivaObbligazioni","Impegni","/docamm00/tab_fattura_passiva_obbligazioni.jsp" },
							{ "tabLetteraPagamentoEstero","Documento 1210","/docamm00/tab_lettera_pagam_estero.jsp" },
							{ "tabFatturaPassivaIntrastat","Intrastat","/docamm00/tab_fattura_passiva_intrastat.jsp" }
						};
	    		}
	}else {
		pages = new String[][] {
						{ "tabFatturaPassiva","Testata","/docamm00/tab_fattura_passiva.jsp" },
						{ "tabFornitore","Fornitore","/docamm00/tab_fornitore.jsp" },					
						{ "tabFatturaPassivaDettaglio","Dettaglio","/docamm00/tab_fattura_passiva_dettaglio.jsp" }, 
						{ "tabFatturaPassivaConsuntivo","Consuntivo","/docamm00/tab_fattura_passiva_consuntivo.jsp" },
						{ "tabFatturaPassivaObbligazioni","Impegni","/docamm00/tab_fattura_passiva_obbligazioni.jsp" },
						{ "tabLetteraPagamentoEstero","Documento 1210","/docamm00/tab_lettera_pagam_estero.jsp" },
						{ "tabFatturaPassivaIntrastat","Intrastat","/docamm00/tab_fattura_passiva_intrastat.jsp" }
					};
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