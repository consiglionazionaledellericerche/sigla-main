<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario00.docs.bulk.*,
		it.cnr.contab.inventario00.bp.*"
%>
<% CRUDTransitoBeniOrdiniBP bp = (CRUDTransitoBeniOrdiniBP)BusinessProcess.getBusinessProcess(request);
   Transito_beni_ordiniBulk bene = (Transito_beni_ordiniBulk)bp.getModel();  %>
<table>
 <tr>
	
	<table>
	  <tr>
		<td>
			<% bp.getController().writeFormLabel(out,"ds_bene"); %>
		</td>
		<td colspan="5">	
			<% bp.getController().writeFormInput(out,null,"ds_bene",false,null,null); %>
		</td>
	  </tr>
	</table>
	<table>
	  <tr>
		<td>
			<% bp.getController().writeFormLabel(out,"cd_barre"); %>
		</td>
		<td>
			<% bp.getController().writeFormInput(out,"cd_barre"); %>
		</td>
		<td>
				<% bp.getController().writeFormLabel(out,"targa"); %>
				<% bp.getController().writeFormInput(out,null,"targa"); %>
		</td>
		<td>
				<% bp.getController().writeFormLabel(out,"seriale"); %>
				<% bp.getController().writeFormInput(out,null,"seriale"); %>
		</td>
		<tr>
                <td>
                    <% bp.getController().writeFormLabel(out,"condizioni"); %>
                </td>
                <td>
                    <% bp.getController().writeFormInput(out,null,"condizioni"); %>
                </td>
			<td>
				<% bp.getController().writeFormLabel(out,"ti_istituzionale_commerciale"); %>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,null,"ti_istituzionale_commerciale"); %>
			</td>	
		</tr>
	</table>
	

	<div class="Group">
	 <table>	
		<tr>
			<td >
				<% bp.getController().writeFormLabel(out,"find_ubicazione"); %>
			</td >	
			<td colspan="5">	
				<% bp.getController().writeFormInput(out,null,"find_ubicazione"); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"collocazione"); %>
			</td >	
			<td colspan="5">		
				<% bp.getController().writeFormInput(out,null,"collocazione"); %>
			</td>			
		</tr>	
		<tr>
			<td >
				<% bp.getController().writeFormLabel(out,"find_assegnatario"); %>
			</td>	
			<td colspan="5"	>
				<% bp.getController().writeFormInput(out,null,"find_assegnatario"); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"fl_ammortamento"); %>
			</td>
    			<td>
				<% bp.getController().writeFormInput(out,null,"fl_ammortamento",false,null,null); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"tipo_ammortamento"); %>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,null,"tipo_ammortamento",false,null,null); %>
			</td>
		</tr>
	</table>
 </div>	

	<div class="Group">	
	<table>	 
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"valore_iniziale"); %></td>
		<td><% bp.getController().writeFormInput(out,null,"valore_iniziale"); %></td>
	  </tr> 
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"cd_categoria_bene"); %></td>
		<td><% bp.getController().writeFormInput(out,null,"cd_categoria_bene"); %></td>
		<td><% bp.getController().writeFormInput(out,null,"ds_categoria_bene"); %></td>
	  </tr>
	</table>
   </div>
	   	<div class="Group">
		<table>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"dataBolla"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"dataBolla"); %>
				</td>
				<td>
					<% bp.getController().writeFormLabel(out,"numeroBolla"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"numeroBolla"); %>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"dtRiferimento"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"dtRiferimento"); %>
				</td>
				<td>
					<% bp.getController().writeFormLabel(out,"ordine"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"ordine"); %>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"cd_bene_servizio"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"cd_bene_servizio"); %>
				</td>
				<td>
					<% bp.getController().writeFormLabel(out,"notaRiga"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"notaRiga"); %>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"cdTerzo"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"cdTerzo"); %>
				</td>
				<td>
					<% bp.getController().writeFormLabel(out,"dsTerzo"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"dsTerzo"); %>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"cdUnitaOperativa"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"cdUnitaOperativa"); %>
				</td>
				<td>
					<% bp.getController().writeFormLabel(out,"dsUnitaOperativa"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"dsUnitaOperativa"); %>
				</td>
			</tr>
		</table>
		</div>

 </tr>
	</table>
