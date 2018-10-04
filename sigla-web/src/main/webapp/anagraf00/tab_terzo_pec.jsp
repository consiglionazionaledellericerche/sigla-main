<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.anagraf00.core.bulk.*"
	
%>

<%	CRUDTerzoBP bp = (CRUDTerzoBP)BusinessProcess.getBusinessProcess(request); 
	TerzoBulk terzo = (TerzoBulk)bp.getModel(); %>

<% bp.getCrudPec().writeHTMLTable(pageContext,"pec",!terzo.isDipendente(),false,!terzo.isDipendente(),"100%","200px",false); %>