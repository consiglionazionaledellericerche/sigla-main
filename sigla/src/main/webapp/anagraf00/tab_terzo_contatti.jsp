<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.anagraf00.core.bulk.*"
%>

<%	CRUDTerzoBP bp = (CRUDTerzoBP)BusinessProcess.getBusinessProcess(request);
	TerzoBulk terzo = (TerzoBulk)bp.getModel();
	
	bp.getCrudContatti().writeHTMLTable(pageContext,null,!terzo.isDipendente(),false,!terzo.isDipendente(),"100%","200px");
%>

<table class="Form">
<% bp.getCrudContatti().writeForm(out,"default"); %>
</table>