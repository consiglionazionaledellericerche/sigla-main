<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bp.*,
		it.cnr.contab.incarichi00.tabrif.bulk.*"
%>

<%
CRUDConfigRepertorioLimitiBP bp = (CRUDConfigRepertorioLimitiBP)BusinessProcess.getBusinessProcess(request);
SimpleDetailCRUDController controller;
if (((Tipo_limiteBulk)bp.getModel()).getFl_raggruppa())
   controller = bp.getIncarichiScaduti();
else
   controller = bp.getIncarichiScadutiAll();
%>
<table class="w-100">
	<tr><td>
		<% controller.writeHTMLTable(
								pageContext,
								"incarichiScaduti",
								false,
								false,
								false,
								"100%",
								"150px"); 
		%>
	</td></tr>
</table>