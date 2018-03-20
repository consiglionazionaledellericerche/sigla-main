<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>
 
<table class="Panel">
<tr>
<td colspan="2">
<% it.cnr.contab.config00.bp.CRUDWorkpackageBP bp = (it.cnr.contab.config00.bp.CRUDWorkpackageBP)BusinessProcess.getBusinessProcess(request);
	bp.getRisultati().writeHTMLTable(pageContext,null,true,false,true,"100%","100px"); %>
</td>
</tr>
<% 	bp.getRisultati().writeForm(out); %>