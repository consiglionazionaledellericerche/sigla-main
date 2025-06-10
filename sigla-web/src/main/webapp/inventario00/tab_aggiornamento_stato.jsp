 <%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario00.docs.bulk.*,
		it.cnr.contab.inventario00.bp.*"
%>
<% CRUDAggiornamentoStatoInventarioBP bp = (CRUDAggiornamentoStatoInventarioBP)BusinessProcess.getBusinessProcess(request);
   Aggiornamento_inventarioBulk aggiornamenti = (Aggiornamento_inventarioBulk)bp.getModel();
    %>
    <table>	
	<div class="Group">	
			
		
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"stato"); %>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,"stato"); %>
			</td>
		</tr>
		<% if (aggiornamenti!=null && aggiornamenti.isBeneSmarrito()) { %>
		<tr>
   	        <td><% bp.getController().writeFormLabel(out,"default","blob"); %></td>
   	        <td colspan=4><% bp.getController().writeFormInput(out,"default","blob"); %></td>
   	    </tr>
   	    <% } %>
	</table>
   </div>	 
