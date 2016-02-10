<%@ page 
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
<%	FatturaPassivaElettronicaBP bp = (FatturaPassivaElettronicaBP)BusinessProcess.getBusinessProcess(request);%>
<script language="JavaScript">
function doScaricaFile() {	
  doPrint('genericdownload/<%=bp.getNomeFileAllegato()%>?methodName=scaricaAllegato&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
<%  bp.getCrudDocEleAllegatiColl().writeHTMLTable(pageContext,"default",false,false,false,"100%","150px"); %>  
<div class="Group">
  <table>
  	<% bp.getCrudDocEleAllegatiColl().writeForm(out, "default");  %>
  </table>
</div> 