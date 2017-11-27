<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*,it.cnr.contab.doccont00.bp.*"
%>
<%  
	CRUDObbligazioneBP bp = (CRUDObbligazioneBP)BusinessProcess.getBusinessProcess(request);
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = (it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk)bp.getModel();
	if ( obbligazione.getFl_calcolo_automatico()!= null && obbligazione.getFl_calcolo_automatico().booleanValue() ) {
		bp.getScadenzarioDettaglio().writeHTMLTable(pageContext,bp.isFlNuovoPdg()?"nuovoPdg":null,false,false,false,"100%","100px", true);
	} else {
		bp.getScadenzarioDettaglio().writeHTMLTable(pageContext,bp.isFlNuovoPdg()?"nuovoPdg":null,false,false,false,"100%","100px", false);
	} 
%>