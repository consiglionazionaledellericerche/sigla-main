<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->
<%@page import="it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoBulk"%>
<%@page import="it.cnr.contab.ordmag.magazzino.bp.ScaricoManualeMagazzinoBP"%>
<%@page import="it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoRigaBulk"%>
<%@page 
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*"
%>
<%  
	ScaricoManualeMagazzinoBP bp= (ScaricoManualeMagazzinoBP)BusinessProcess.getBusinessProcess(request); 
	ScaricoMagazzinoBulk model = (ScaricoMagazzinoBulk)bp.getModel();
	ScaricoMagazzinoRigaBulk modelRiga = (ScaricoMagazzinoRigaBulk)bp.getBeniServiziColl().getModel();
	bp.getLottiMagazzinoColl().writeHTMLTable(pageContext,
		modelRiga!=null && modelRiga.isImputazioneScaricoSuLottiEnable()?"scaricoMagazzinoEditable":"datiLottoMovimentiMagazzino",
		false,false,false,"100%","100px", false);
%>