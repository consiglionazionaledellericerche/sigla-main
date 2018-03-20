<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.jada.*,it.cnr.contab.missioni00.bp.*,it.cnr.contab.util00.bp.*"
%>
<%	CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);%>
<script language="JavaScript">
function doScaricaAllegato() {	
	  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getNomeAllegato()!=null?bp.getNomeAllegato().replace("'", "_"):""%>?methodName=scaricaAllegatoGenerico&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
<%  bp.getCrudArchivioAllegati().writeHTMLTable(pageContext,bp.getAllegatiFormName(),true,false,true,"100%","150px"); %>  
<div class="Group">
  <table>
  	<% bp.getCrudArchivioAllegati().writeForm(out, bp.getAllegatiFormName());  %>
  </table>
</div> 