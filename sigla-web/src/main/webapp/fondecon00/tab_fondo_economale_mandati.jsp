<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.fondecon00.bp.*,
		it.cnr.contab.fondecon00.core.bulk.*"
%>

<%
	FondoEconomaleBP bp = (FondoEconomaleBP)BusinessProcess.getBusinessProcess(request);
	Fondo_economaleBulk fondo = (Fondo_economaleBulk)bp.getModel();
%>

<div class="Group" style="width:100%">
	<table width="100%">
		<tr>
			<td>
				<%	bp.getAssociazioniMandati().writeHTMLTable(pageContext,"mandatiSet",true,false,true,"100%","300px"); %>
			</td>
		</tr>
	</table>
</div>