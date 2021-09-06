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
   Inventario_beniBulk bene = (Inventario_beniBulk)bp.getModel();  %>   
<table>
 <tr>
	
	<table>		
	  <tr>
		<td>
			<% bp.getController().writeFormLabel(out,"etichetta"); %>
		</td>
		<td>
			<% bp.getController().writeFormInput(out,"etichetta"); %>
		</td>

		<td>
			<% bp.getController().writeFormLabel(out,"id_bene_origine"); %>
		</td>
		<td>
			<% bp.getController().writeFormInput(out,"id_bene_origine"); %>
		</td>
	  </tr>
	  <tr>
		<td>
			<% bp.getController().writeFormLabel(out,"nr_inventario"); %>
		</td>
		<td>
			<% bp.getController().writeFormInput(out,"nr_inventario"); %>
		</td>
	  </tr>	
	  <tr>
		<td>
			<% bp.getController().writeFormLabel(out,"progressivo"); %>
		</td>
		<td>
			<% bp.getController().writeFormInput(out,"progressivo"); %>
		</td>
		<td>
			<% bp.getController().writeFormLabel(out,"num_buono"); %>
		</td>
		<td>
			<% bp.getController().writeFormInput(out,"num_buono"); %>
		</td>
	  </tr>
	  <tr>
		<td>
			<% bp.getController().writeFormLabel(out,"ds_bene"); %>
		</td>
		<td colspan="5">	
			<% bp.getController().writeFormInput(out,null,"ds_bene",bp.isInserting(),null,null); %>
		</td>
	  </tr>	
	  <tr>
		<td>
			<% bp.getController().writeFormLabel(out,"find_categoria_bene"); %>
		</td>
		<td colspan="3">	
			<% bp.getController().writeFormInput(out,null,"find_categoria_bene",bp.isInserting(),null,null); %>
		</td>
		<td>
				<% bp.getController().writeFormLabel(out,"targa"); %>
				<% bp.getController().writeFormInput(out,null,"targa"); %>
		</td>	
		<td>
				<% bp.getController().writeFormLabel(out,"seriale"); %>
				<% bp.getController().writeFormInput(out,null,"seriale",((bene!=null && bene.isROseriale())),null,null); %>
		</td>		
	  <tr>
		<td>
			<% bp.getController().writeFormLabel(out,"condizioni"); %>
		</td>
		<td>
			<% bp.getController().writeFormInput(out,null,"condizioni",bp.isInserting(),null,null); %>
		</td>
		<td>
			<% bp.getController().writeFormLabel(out,"cd_barre"); %>
		</td>
		<td>
			<% bp.getController().writeFormInput(out,"cd_barre"); %>
		</td>
	  </tr>
					
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"ti_istituzionale_commerciale"); %>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,null,"ti_istituzionale_commerciale",true,null,null); %>
			</td>	
		</tr>
	</table>
	

   	<% if (bene != null && bene.isBeneAccessorio()){ %>
	   	<div class="Group">	
		<table>			
			<tr>				
				<td>
					<% bp.getController().writeFormLabel(out,"find_bene_principale"); %>
				</td>					
				<td colspan="5">	
					<% bp.getController().writeFormInput(out,null,"find_bene_principale",true,null,null); %>
				</td>
			</tr>
		</table>
		</div>
   	<% } %>
  			
	<div class="Group">	
	 <table>	
		<tr>
			<td >
				<% bp.getController().writeFormLabel(out,"find_ubicazione"); %>
			</td >	
			<td colspan="5">	
				<% bp.getController().writeFormInput(out,null,"find_ubicazione",(bp.isInserting()||bene.isBeneAccessorio()),null,null); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"collocazione"); %>
			</td >	
			<td colspan="5">		
				<% bp.getController().writeFormInput(out,null,"collocazione",!bene.isPubblicazione(),null,null); %>
			</td>			
		</tr>	
		<tr>
			<td >
				<% bp.getController().writeFormLabel(out,"find_assegnatario"); %>
			</td>	
			<td colspan="5"	>
				<% bp.getController().writeFormInput(out,null,"find_assegnatario",(bp.isInserting()||bene.isBeneAccessorio()),null,null); %>
			</td>
		</tr>		
		
	</table>
 </div>	

	<div class="Group">	
	<table>	 
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"valore_iniziale"); %></td>
		<td><% bp.getController().writeFormInput(out,null,"valore_unitario",bp.isROvaloreIniziale(),null,null); %></td>
	  </tr> 
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"valore_bene"); %></td>
		<td><% bp.getController().writeFormInput(out,null,"valore_bene",bp.isInserting(),null,null); %></td>
	  </tr>
	</table>
   </div>

   <div class="Group">
	<table> 		
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"valore_ammortizzato"); %></td>
		<td><% bp.getController().writeFormInput(out,"valore_ammortizzato"); %></td>
	  </tr>			
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"imponibile_ammortamento"); %></td>
		<td><% bp.getController().writeFormInput(out,"imponibile_ammortamento"); %></td>
	  </tr>	  
	</table>
	 </div>
   	<% if (bene != null && bene.isDaOrdini()){ %>
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
				<td>
					<% bp.getController().writeFormLabel(out,"estremiOrdine"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"estremiOrdine"); %>
				</td>
			</tr>
		</table>
		</div>
   	<% } %>
 </tr>
	</table>
	 	 