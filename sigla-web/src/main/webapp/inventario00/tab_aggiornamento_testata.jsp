<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario00.docs.bulk.*,
		it.cnr.contab.inventario00.bp.*"
%>
<% CRUDAggiornamentoInventarioBP bp = (CRUDAggiornamentoInventarioBP)BusinessProcess.getBusinessProcess(request); 
%>
	<table>
	  <tr>
	  	<td colspan = "4">
		  <% bp.getDettagliCRUDController().writeHTMLTable(
				pageContext,
				bp.getColumnSetName(),
				bp.isInserting(),
				false,
				bp.isInserting(),
				null,
				"385px",
				true); %>
		</td>
	  </tr>
	</table>	


