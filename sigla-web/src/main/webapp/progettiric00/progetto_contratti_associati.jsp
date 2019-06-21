<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
	    java.util.Optional,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.progettiric00.bp.*,
		it.cnr.contab.progettiric00.core.bulk.*"
%>

<%
	TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = bp.getContrattiAssociati();
%>

<%	controller.writeHTMLTable(pageContext,"contrattiAssociatiProgetto",false,false,false,"100%","auto"); %>
