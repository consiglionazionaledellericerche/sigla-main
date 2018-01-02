<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.progettiric00.bp.*,
		it.cnr.contab.progettiric00.core.bulk.*"
%>

<%
	TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);

	JSPUtils.tabbed(
				pageContext,
				"tabProgettoPianoEconomico",
				bp.getTabsPianoEconomico(),
				bp.getTab("tabProgettoPianoEconomico"),
				"center",
				"100%",
				null);	
%>