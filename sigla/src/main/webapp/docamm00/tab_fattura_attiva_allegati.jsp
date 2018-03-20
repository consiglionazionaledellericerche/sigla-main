<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.*,
		it.cnr.contab.util00.bp.*"
%>
<%	AllegatiCRUDBP bp = (AllegatiCRUDBP)BusinessProcess.getBusinessProcess(request);%>
<script language="JavaScript">
function doScaricaFile() {	
  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getNomeAllegato()!=null?bp.getNomeAllegato().replace("'", "_"):""%>?methodName=scaricaAllegatoGenerico&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
<%  bp.getCrudArchivioAllegati().writeHTMLTable(pageContext,bp.getAllegatiFormName(),true,false,true,"100%","150px"); %>  
<div class="Group card">
  <table>
  	<% bp.getCrudArchivioAllegati().writeForm(out, bp.getAllegatiFormName());  %>
  </table>
</div> 