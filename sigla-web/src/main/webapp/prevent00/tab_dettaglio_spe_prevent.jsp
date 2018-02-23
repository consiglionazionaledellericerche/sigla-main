<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.prevent00.action.*,
		it.cnr.contab.prevent00.bp.*"
%>

<%
	CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
%>

<CENTER>
	<table border="0" cellspacing="0" cellpadding="2" align=center>
		<tr><% bp.getController().writeFormField(out,"im_ru_spese_costi_altrui");%></tr>
		<tr><% bp.getController().writeFormField(out,"im_rv_pagamenti");%></tr>
	</table>
</CENTER>

<%JSPUtils.tabbed(
		pageContext,
		"tabCostiSpese",
		new String[][] {
				{ "tabCostiConSpese", "Costi Con Spese","/prevent00/tab_det_costi_con_spese.jsp" },
				{ "tabCostiSenzaSpese", "Costi Senza Spese","/prevent00/tab_det_costi_senza_spese.jsp" },
				{ "tabSpeseSenzaCosti", "Spese Senza Costi","/prevent00/tab_det_spese_senza_costi.jsp" }
			       },
		bp.getTab("tabCostiSpese"),
		"center",
		"100%",
		null);
%>