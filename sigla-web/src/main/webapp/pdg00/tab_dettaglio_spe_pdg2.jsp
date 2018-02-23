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
		it.cnr.contab.pdg00.action.*,
		it.cnr.contab.pdg00.bp.*"
%>

<%
	CRUDSpeDetPdGBP bp = (CRUDSpeDetPdGBP)BusinessProcess.getBusinessProcess(request);
%>

<table border="0" cellspacing="0" cellpadding="2">
	<tr>
		<% bp.getController().writeFormField(out,"im_rag_a2_spese_costi_altrui");%>
	</tr>
</table>

<%JSPUtils.tabbed(
		pageContext,
		"tabCostiSpese2",
		new String[][] {
				{ "tabCosti2", "Costi","/pdg00/tab_det_costi2.jsp" },
				{ "tabSpese2", "Spese","/pdg00/tab_det_spese2.jsp" }
			       },
		bp.getTab("tabCostiSpese2"),
		"center",
		"100%",
		"100%");
%>