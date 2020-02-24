<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->
<%@page import="it.cnr.contab.ordmag.magazzino.bulk.CaricoMagazzinoBulk"%>
<%@page import="it.cnr.contab.ordmag.magazzino.bp.CaricoManualeMagazzinoBP"%>
<%@page import="it.cnr.contab.ordmag.magazzino.bulk.CaricoMagazzinoRigaBulk"%>
<%@page 
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*"
%>
<%  
	CaricoManualeMagazzinoBP bp= (CaricoManualeMagazzinoBP)BusinessProcess.getBusinessProcess(request); 
	CaricoMagazzinoBulk model = (CaricoMagazzinoBulk)bp.getModel();
	CaricoMagazzinoRigaBulk modelRiga = (CaricoMagazzinoRigaBulk)bp.getBeniServiziColl().getModel();
	bp.getLottiMagazzinoColl().writeHTMLTable(pageContext,"datiLottoMovimentiMagazzino",false,false,false,"100%","100px", false);
%>