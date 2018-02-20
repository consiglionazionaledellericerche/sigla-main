<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.contab.incarichi00.bp.*,
		it.cnr.contab.incarichi00.bulk.*,
		it.cnr.jada.util.jsp.*"
	pageEncoding="UTF-8"
%>

<%
CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)bp.getModel();
boolean multi_incarico = false;
if (procedura!=null && procedura.getFaseProcesso()!=null && 
	procedura.getNr_contratti()!=null && procedura.getIncarichi_repertorioValidiColl()!=null &&
	procedura.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_CONTRATTO)!=-1 &&
	procedura.getNr_contratti().compareTo(new Integer(1))==1)
	multi_incarico=true;
%>
<fieldset>
	<table CLASS="w-100">
		<TR><TD>
<% bp.getRipartizionePerAnno().writeHTMLTable(
								pageContext,
								multi_incarico?"multi_incarico":null,
								!bp.isSearching()&&!multi_incarico,
								false,
								!bp.isSearching()&&!multi_incarico,
								"100%",
								"150px"); %>
		</TD></TR>
	</table>	
	<% if (bp.isTabCompensiProceduraAnnoEnabled()) {
	  JSPUtils.tabbed(
					pageContext,
					"tabIncarichiProceduraAnno",
					new String[][] {
							{ "tabIncarichiProceduraAnnoImporti","Importo","/incarichi00/tab_incarichi_procedura_anno_importi.jsp" },
							{ "tabIncarichiProceduraAnnoCompensi","Compensi Associati","/incarichi00/tab_incarichi_procedura_anno_compensi.jsp" } },
					bp.getTab("tabIncarichiProceduraAnno"),
					"center",
					"100%",
					null);
	  } else {
	%>
	<jsp:include page="/incarichi00/tab_incarichi_procedura_anno_importi.jsp" />
	<%}%>
</fieldset>