<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario01.bulk.*,
		it.cnr.contab.inventario01.bp.*"
%>

<% CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)BusinessProcess.getBusinessProcess(request); 
   Buono_carico_scaricoBulk buonoCarico = (Buono_carico_scaricoBulk)bp.getModel(); 
   Buono_carico_scarico_dettBulk riga = (Buono_carico_scarico_dettBulk)bp.getDettaglio().getModel(); %>
   
   	<% bp.getDettaglio().writeHTMLTable(pageContext,"righeSetConCodice",false,false,false,"100%","200px"); %>

	<div class="Group">	
	<table>
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"ds_bene"); %>
			</td>
			<td>
				<% bp.getDettaglio().writeFormInput(out,null,"ds_bene",true,null,null); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"fl_ammortamento"); %>
			</td>
			<td>
				<% bp.getDettaglio().writeFormInput(out,null,"fl_ammortamento",(bp.isEditing()||bp.isNotAmmortizzabile()),null,"onClick=\"submitForm('doNonAmmortizzato')\""); %>
			</td>
		</tr>
		<% 
			if (bp.isInserting()){ 
		%>
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"ti_ammortamento"); %>
			</td>
			<td>
				<% bp.getDettaglio().writeFormInput(out,null,"ti_ammortamento",(bp.isEditing()||bp.isNonAmmortizzato()),null,"onChange=\"submitForm('doSelezionaTiAmmortamento')\""); %>				
			</td>			
		</tr>
		<%
			}
			else {
		%>
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"ti_ammortamento_notIns"); %>
			</td>
			<td>
				<% bp.getDettaglio().writeFormInput(out,"ti_ammortamento_notIns"); %>				
			</td>			
		</tr>
		<% }  %>
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"perc_primo_anno"); %>
			</td>
			<td>
				<% bp.getDettaglio().writeFormInput(out,"perc_primo_anno"); %>
			</td>
		</tr>
	    <tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"perc_successivi"); %>
			</td>
			<td>
				<% bp.getDettaglio().writeFormInput(out,"perc_successivi"); %>
			</td>
		</tr>
	</table>
	 </div>