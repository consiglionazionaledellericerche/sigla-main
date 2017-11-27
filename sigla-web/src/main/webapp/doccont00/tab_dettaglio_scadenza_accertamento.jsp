<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page 
	import=	"it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*, 
			it.cnr.jada.util.action.*,
			it.cnr.contab.doccont00.bp.*"
%>
<%  
	CRUDAccertamentoBP bp = (CRUDAccertamentoBP)BusinessProcess.getBusinessProcess(request);
	it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento = (it.cnr.contab.doccont00.core.bulk.AccertamentoBulk)bp.getModel();
	if ( accertamento.getFl_calcolo_automatico()!= null && accertamento.getFl_calcolo_automatico().booleanValue() ) {
     	bp.getScadenzarioDettaglio().writeHTMLTable(pageContext,null,false,false,false,"100%","100px", true);
	} else {
		bp.getScadenzarioDettaglio().writeHTMLTable(pageContext,null,false,false,false,"100%","100px", false);
	} 
%>