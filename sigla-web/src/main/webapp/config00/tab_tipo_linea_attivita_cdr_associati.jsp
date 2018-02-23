<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.config00.bp.*,it.cnr.contab.config00.latt.bulk.*"
%>
<% CRUDTipo_linea_attivitaBP bp = (CRUDTipo_linea_attivitaBP)BusinessProcess.getBusinessProcess(request); %>
<table class="Panel">
<tr><td>
	<% bp.getCdrAssociati().writeHTMLTable(pageContext,null,true,true,true,"100%","300px"); %>
</td></tr>
</table>