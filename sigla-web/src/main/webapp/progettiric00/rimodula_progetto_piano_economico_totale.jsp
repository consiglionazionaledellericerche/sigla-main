<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.progettiric00.bp.*,
		it.cnr.contab.progettiric00.core.bulk.*"
%>

<%
	RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = bp.getCrudPianoEconomicoTotale();
%>

<%	controller.writeHTMLTable(pageContext,"piano_economico1_rimodulato",true,false,true,"100%","100px"); %>