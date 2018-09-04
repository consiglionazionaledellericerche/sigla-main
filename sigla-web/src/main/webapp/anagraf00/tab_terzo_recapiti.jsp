<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.anagraf00.core.bulk.*"
	
%>

<%
	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	JSPUtils.tabbed(
			pageContext,
			"tabRecapiti",
			new String[][] {
					{ "tabTelefoni","Telefoni","/anagraf00/tab_terzo_telefoni.jsp" },
					{ "tabFax","Fax","/anagraf00/tab_terzo_fax.jsp" },
					{ "tabEmail","Email","/anagraf00/tab_terzo_email.jsp" },
					{ "tabPec","Pec","/anagraf00/tab_terzo_pec.jsp" } },
			bp.getTab("tabRecapiti"),
			"center",
			"100%",
			null);
%>