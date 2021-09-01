<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.jada.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*"
%>
<%	CRUDFatturaPassivaElettronicaBP bp = (CRUDFatturaPassivaElettronicaBP)BusinessProcess.getBusinessProcess(request);%>
<%  bp.getCrudDocEleAcquistoColl().writeHTMLTable(pageContext,"default",false,false,false,"100%","150px"); %>
<%  bp.getCrudDocEleAcquistoColl().closeHTMLTable(pageContext);%>
<div class="Group">
  <table>
  	<% bp.getCrudDocEleAcquistoColl().writeForm(out, "default");  %>
  </table>
</div>