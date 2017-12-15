<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bulk.*,
		it.cnr.contab.incarichi00.bp.*,
		it.cnr.jada.util.jsp.*"
	pageEncoding="ISO-8859-1"
%>

<%
CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
SimpleDetailCRUDController controller = bp.getIncarichiColl();
%>
<table class="Panel w-100">
	<TR><TD>
<% if (!bp.isSearching()&&
  	   bp.getModel()!=null &&
	   ((Incarichi_proceduraBulk)bp.getModel()).isProceduraMultiIncarico()) { %>
		<table class="w-100">
		<TR><TD>
			<% controller.writeHTMLTable(
								pageContext,
								null,
								!bp.isSearching(),
								false,
								!bp.isSearching(),
								"100%",
								"150px"); %>
		</TD></TR>
		</table>	
		<table width=100%>
		<TR><TD>
<%
        JSPUtils.tabbed(
				pageContext,
				"tabProceduraContratto",
				bp.getTabsMultiIncarico(),
				bp.getTab("tabProceduraContratto"),
				"center",
				"100%",
				null);
  } else {
%>
	<jsp:include page="/incarichi00/tab_incarichi_procedura_contratto_testata.jsp" />
<%}%>
		</TD></TR>
		</table>	
	</TD></TR>
</table>	
