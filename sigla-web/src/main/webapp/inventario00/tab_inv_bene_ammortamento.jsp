<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario00.docs.bulk.*,
		it.cnr.contab.inventario00.bp.*"
%>

<% CRUDInventarioBeniBP bp = (CRUDInventarioBeniBP)BusinessProcess.getBusinessProcess(request); 	
   Inventario_beniBulk bene = (Inventario_beniBulk)bp.getModel(); %>
   
   	<div class="Group">
	<table>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"ds_bene"); %>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,null,"ds_bene",true,null,null); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"fl_ammortamento"); %>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,null,"fl_ammortamento",bp.isNotAmmortizzabile(),null,"onClick=\"submitForm('doNonAmmortizzato')\""); %>
			</td>
		</tr>		
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"ti_ammortamento"); %>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,null,"ti_ammortamento",(bp.isInserting() || bp.isNonAmmortizzato()),null,"onChange=\"submitForm('doSelezionaTiAmmortamento')\""); %>				
			</td>			
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"perc_primo_anno"); %>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,"perc_primo_anno"); %>
			</td>
		</tr>
	    <tr>
			<td>
				<% bp.getController().writeFormLabel(out,"perc_successivi"); %>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,"perc_successivi"); %>
			</td>
		</tr>
	</table>
	 </div>