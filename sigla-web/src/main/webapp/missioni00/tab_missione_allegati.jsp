<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.jada.*,it.cnr.contab.missioni00.bp.*,it.cnr.contab.util00.bp.*"
%>
<%	CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);%>
<script language="JavaScript">
function doScaricaAllegato() {	
	  doPrint('genericdownload/<%=bp.getNomeAllegato()%>?methodName=scaricaAllegatoGenerico&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
<%  bp.getCrudArchivioAllegati().writeHTMLTable(pageContext,bp.getAllegatiFormName(),false,false,false,"100%","150px"); %>  
<div class="Group">
  <table>
  	<% bp.getCrudArchivioAllegati().writeForm(out, bp.getAllegatiFormName());  %>
  </table>
</div> 