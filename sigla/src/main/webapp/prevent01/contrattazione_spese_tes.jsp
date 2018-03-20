<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.prevent01.bp.*,
		it.cnr.contab.prevent01.bulk.*"
%>

<%
	CRUDDettagliContrSpeseBP bp = (CRUDDettagliContrSpeseBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = ((CRUDDettagliContrSpeseBP)bp).getCrudDettagliDipArea();
%>

<%	controller.writeHTMLTable(pageContext,bp.isFlNuovoPdg()?"nuovoPdg":null,true,false,true,"100%","300px"); %>

<div class="Group">
	<table class="Panel" cellspacing="2">
		<tr>
	  		<td><% controller.writeFormLabel(out,"dipartimento");%></td>
			<td><% controller.writeFormInput(out,"dipartimento");%></td>
		</tr>
		<% if (!bp.isFlNuovoPdg()) { %>
		<tr>
	  		<td><% controller.writeFormLabel(out,"area");%></td>
			<td><% controller.writeFormInput(out,"area");%></td>
		</tr>
		<% } %>
		<tr>
	  		<td><% controller.writeFormLabel(out,"importo_approvato");%></td>
			<td><% controller.writeFormInput(out,"importo_approvato");%></td>
		</tr>
	</table>
</div>

