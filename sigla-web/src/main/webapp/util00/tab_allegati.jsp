<%@ page 
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
  doPrint('genericdownload/<%=bp.getNomeAllegato()%>?methodName=scaricaAllegatoGenerico&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
<%  bp.getCrudArchivioAllegati().writeHTMLTable(pageContext,"default",true,false,true,"100%","150px"); %>  
<div class="Group">
  <table>
  	<% bp.getCrudArchivioAllegati().writeForm(out, bp.getAllegatiFormName());  %>
  </table>
</div> 