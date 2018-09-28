<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.progettiric00.bp.*,
		it.cnr.contab.progettiric00.core.bulk.*"
%>

<%
	TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = ( (TestataProgettiRicercaBP)bp ).getCrudPianoEconomicoTotale();
	boolean isKeyEditable = controller.getModel()!=null && controller.getModel().isNotNew();
%>

<%	controller.writeHTMLTable(pageContext,"piano_economico1",true,false,true,"100%","100px"); %>