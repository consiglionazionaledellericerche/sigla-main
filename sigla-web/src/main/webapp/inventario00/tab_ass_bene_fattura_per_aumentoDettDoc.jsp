<%@ page pageEncoding="UTF-8"
	import="
		it.cnr.jada.action.*,
		it.cnr.contab.inventario00.bp.*"
%>

<% AssBeneFatturaBP bp = (AssBeneFatturaBP)BusinessProcess.getBusinessProcess(request); %>
  
  <% bp.getDettagliDocumento().writeHTMLTable(pageContext,"inventarioSet",false,false,false,"100%","200px"); %> 
  <div class="Group">
	<table>

	  <tr>
	  	<td colspan = "4">
		  <% bp.getRigheInventarioDaDocumento().writeHTMLTable(
				pageContext,
				null,
				true,
				false,
				true,
				null,
				"140px",
				true); %>
		</td>
	  </tr>
	</table>
	<table>	
	  <tr>
		<td>
			<% bp.getRigheInventarioDaDocumento().writeFormLabel(out,"codiceCompleto"); %>
		</td>
		<td>
			<% bp.getRigheInventarioDaDocumento().writeFormInput(out,null,"codiceCompleto",true,null,null); %>
		</td>
	  </tr>	
	  <tr>
		<td>
			<% bp.getRigheInventarioDaDocumento().writeFormLabel(out,"ds_bene"); %>
		</td>
		<td>
			<% bp.getRigheInventarioDaDocumento().writeFormInput(out,null,"ds_bene",true,null,null); %>
		</td>
	  </tr>
   </table>
   <table>
	  <tr>
		<td>
			<% bp.getRigheInventarioDaDocumento().writeFormLabel(out,"find_categoria_bene"); %>
		</td>
		<td colspan = "4">
			<% bp.getRigheInventarioDaDocumento().writeFormInput(out,null,"find_categoria_bene",true,null,null); %>
		</td>
	  </tr>	
	  <tr>
		<td>
			<% bp.getRigheInventarioDaDocumento().writeFormLabel(out,"ds_assegnatario"); %>
		</td>
		<td colspan = "4">
			<% bp.getRigheInventarioDaDocumento().writeFormInput(out,null,"cd_assegnatario",true,null,null); %>
			<% bp.getRigheInventarioDaDocumento().writeFormInput(out,null,"ds_assegnatario",true,null,null); %>
		</td>
	  </tr>
	  <tr>
		<td>
			<% bp.getRigheInventarioDaDocumento().writeFormLabel(out,"find_ubicazione"); %>
		</td>
		<td>
			<% bp.getRigheInventarioDaDocumento().writeFormInput(out,null,"find_ubicazione",true,null,null); %>
		</td>
	  </tr>
   </table>
   
	<div class="Group card">   
	<table>	
	  <tr>
		<td><% bp.getRigheInventarioDaDocumento().writeFormLabel(out,"valore_iniziale"); %></td>
		<td><% bp.getRigheInventarioDaDocumento().writeFormInput(out,null,"valore_iniziale",true,null,null); %></td>
		<td><% bp.getRigheInventarioDaDocumento().writeFormLabel(out,"valoreBene"); %></td>
		<td><% bp.getRigheInventarioDaDocumento().writeFormInput(out,null,"valoreBene",true,null,null); %></td>
	  </tr>
	  <tr>
		<td><% bp.getRigheInventarioDaDocumento().writeFormLabel(out,"variazione_piuPerAssocia"); %></td>
		<td><% bp.getRigheInventarioDaDocumento().writeFormInput(out,"variazione_piuPerAssocia"); %></td>
	  </tr>
	</table>
	 </div>
	
 </div>