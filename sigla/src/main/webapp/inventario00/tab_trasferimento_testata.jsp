<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario00.docs.bulk.*,
		it.cnr.contab.inventario00.bp.*"
%>
<% CRUDTrasferimentoInventarioBP bp = (CRUDTrasferimentoInventarioBP)BusinessProcess.getBusinessProcess(request); %>
	<table>
	<tr><td>  
	 <div class="Group">	
	<table>				
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"esercizio"); %>
			</td>
			<td  colspan=2>
				<% bp.getController().writeFormInput(out,"esercizio"); %>
			</td>
		</tr>  
		<tr>
			<td><% bp.getController().writeFormLabel(out,"pg_inventario"); %></td>
			<td  colspan=3>
				<% bp.getController().writeFormInput(out,"pg_inventario"); %>
				<% bp.getController().writeFormInput(out,"ds_inventario"); %>
			</td>
		</tr>
	</table>
	
   </td></tr>
	<tr><td>    
	 <div class="Group">	
	<table>			
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"cd_uo_consegnataria"); %>
			</td>
			<td  colspan=3>
				<% bp.getController().writeFormInput(out,"cd_uo_consegnataria"); %>
				<% bp.getController().writeFormInput(out,"ds_uo_consegnataria"); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"cd_consegnatario"); %>
			</td>
			<td colspan=3>
				<% bp.getController().writeFormInput(out,"cd_consegnatario"); %>
				<% bp.getController().writeFormInput(out,"cognome_consegnatario"); %>
			</td>
		</tr>
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"cd_delegato"); %>
			</td>
			<td colspan=3>
				<% bp.getController().writeFormInput(out,"cd_delegato"); %>
				<% bp.getController().writeFormInput(out,"cognome_delegato"); %>
			</td>
		</tr>
	</table>
	</div>
   </td></tr>
	<% if (bp.isTrasferimentoExtraInv()){ %>
		<tr valign=top><td>  
	  <table>
		  <tr>
			<td><% bp.getController().writeFormLabel(out,"findUoDestinazione"); %></td>
			<td colspan=3>
				<% bp.getController().writeFormInput(out,"findUoDestinazione"); %>
			</td>
		  </tr>
		  <tr>
			<td><% bp.getController().writeFormLabel(out,"pg_inventario_dest");%></td>
			<td colspan=3>
				<% bp.getController().writeFormInput(out,"pg_inventario_dest");%>
				<% bp.getController().writeFormInput(out,"ds_inventario_dest");%>
			</td>
		  </tr>
	  </table>  
	   </td></tr>
  <% } %>
	<tr valign=top><td>  
	<table> 
		<tr>
			<td><% bp.getController().writeFormLabel(out,"data_registrazione"); %></td>
			<td><% bp.getController().writeFormInput(out,null,"data_registrazione",bp.isEditing(),null,null); %></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"tipo_movimento_scarico"); %></td>
			<td><% bp.getController().writeFormInput(out,"tipo_movimento_scarico"); %></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"tipo_movimento_carico"); %></td>
			<td><% bp.getController().writeFormInput(out,"tipo_movimento_carico"); %></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"ds_buono_carico_scarico"); %></td>
			<td colspan=3><% bp.getController().writeFormInput(out,null,"ds_buono_carico_scarico",bp.isEditing(),null,null); %></td>
		</tr>
       <% if (bp.isTrasferimentoIntraInv() &&  bp.isAmministratore()){ %>
			<tr>
			<td>
			  <% bp.getController().writeFormLabel(out,"fl_cambio_categoria");%>
			 </td>
			 <td>
				<% bp.getController().writeFormInput(out,null,"fl_cambio_categoria",false,null,null);%>
			  </td>
			</tr>
		<% } %>		
		
	</table>
   </td></tr>
   </table>