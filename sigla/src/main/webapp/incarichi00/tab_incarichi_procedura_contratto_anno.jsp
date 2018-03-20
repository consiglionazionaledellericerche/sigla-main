<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bulk.*,
		it.cnr.contab.incarichi00.bp.*,
		it.cnr.jada.util.jsp.*"
%>

<%
CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
SimpleDetailCRUDController controller = bp.getRipartizioneIncarichiPerAnno();
Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)bp.getIncarichiColl().getModel();
%>
<table class="Panel" width=100%>
	<TR><TD>
	<fieldset>
		<table>
		<TR><TD>
			<% controller.writeHTMLTable(
								pageContext,
								null,
								incarico!=null&&!bp.isSearching(),
								false,
								incarico!=null&&!bp.isSearching(),
								"100%",
								"150px"); %>
		</TD></TR>
		</table>	
<% if (bp.isTabCompensiIncaricoAnnoEnabled()) {
	  JSPUtils.tabbed(
				pageContext,
				"tabCompensiIncaricoAnno",
				new String[][] {
						{ "tabCompensiIncaricoAnnoImporti","Importo","/incarichi00/tab_incarichi_procedura_contratto_anno_importi.jsp" },
						{ "tabCompensiIncaricoAnnoCompensi","Compensi Associati","/incarichi00/tab_incarichi_procedura_contratto_anno_compensi.jsp" } },
				bp.getTab("tabCompensiIncaricoAnno"),
				"center",
				"100%",
				null);
  } else {
%>
	<jsp:include page="/incarichi00/tab_incarichi_procedura_contratto_anno_importi.jsp" />
<%}%>
	</fieldset>
	</TD></TR>
</table>	
