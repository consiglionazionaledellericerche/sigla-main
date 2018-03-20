<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.contab.incarichi00.bp.*,
		it.cnr.jada.util.jsp.*"
%>

<%
CRUDIncarichiRepertorioBP bp = (CRUDIncarichiRepertorioBP)BusinessProcess.getBusinessProcess(request);
%>
<table class="Panel">
	<TR><TD>
	<fieldset>
		<table>
		<TR><TD>
<% bp.getRipartizionePerAnno().writeHTMLTable(
								pageContext,
								null,
								!bp.isSearching(),
								!bp.isSearching(),
								!bp.isSearching(),
								"100%",
								"150px"); %>
		</TD></TR>
		</table>	
<% if (bp.isTabIncarichiRepAnnoCompensiEnabled()) {
  JSPUtils.tabbed(
				pageContext,
				"tabIncarichiRepAnno",
				new String[][] {
						{ "tabIncarichiRepAnnoImporti","Importo","/incarichi00/tab_incarichi_repertorio_anno_importi.jsp" },
						{ "tabIncarichiRepAnnoCompensi","Compensi Associati","/incarichi00/tab_incarichi_repertorio_anno_compensi.jsp" } },
				bp.getTab("tabIncarichiRepAnno"),
				"center",
				"100%",
				null);
  } else {
%>
	<jsp:include page="/incarichi00/tab_incarichi_repertorio_anno_importi.jsp" />
<%}%>
	</fieldset>
	</TD></TR>
</table>	
