<%@ page
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.*,
		it.cnr.contab.doccont00.bp.*,
		it.cnr.contab.util00.bp.*"
%>
<%
	AllegatiDocContBP bp = (AllegatiDocContBP)BusinessProcess.getBusinessProcess(request);
%>
<script language="JavaScript">
function doScaricaFile() {
  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getNomeAllegato()!=null?bp.getNomeAllegato().replace("'", "_"):""%>?methodName=scaricaAllegatoGenerico&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
<%  bp.getCrudArchivioAllegati().writeHTMLTable(pageContext,"default",true,false,true,"100%","250px"); %>
<div class="Group card p-2 mt-1">
  <table>
  	<% bp.getCrudArchivioAllegati().writeForm(out, bp.getAllegatiFormName());  %>
  </table>
</div>