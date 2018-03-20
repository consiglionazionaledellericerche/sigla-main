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
		<% bp.getController().writeFormField(out,"im_rap_a3_spese_costi_altrui");%>
	</table>

<%	JSPUtils.tabbed(
			pageContext,
			"tabCostiSpese3",
			new String[][] {
					{ "tabCosti3", "Costi","/prevent00/tab_det_costi3.jsp" },
					{ "tabSpese3", "Spese","/prevent00/tab_det_spese3.jsp" }
				       },
			bp.getTab("tabCostiSpese3"),
			"center",
			"100%",
			null);
%>
</CENTER>