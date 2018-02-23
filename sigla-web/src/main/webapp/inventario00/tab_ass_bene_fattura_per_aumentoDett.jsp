<%@ page pageEncoding="UTF-8"
	import="
		it.cnr.jada.action.*,
		it.cnr.contab.inventario00.bp.*"
%>

<% AssBeneFatturaBP bp = (AssBeneFatturaBP)BusinessProcess.getBusinessProcess(request); %>
  
  <% bp.getDettagliFattura().writeHTMLTable(pageContext,"inventarioSet",false,false,false,"100%","200px"); %> 
  <div class="Group">
	<table>

	  <tr>
	  	<td colspan = "4">
		  <% bp.getRigheInventarioDaFattura().writeHTMLTable(
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
			<% bp.getRigheInventarioDaFattura().writeFormLabel(out,"codiceCompleto"); %>
		</td>
		<td>
			<% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"codiceCompleto",true,null,null); %>
		</td>
	  </tr>	
	  <tr>
		<td>
			<% bp.getRigheInventarioDaFattura().writeFormLabel(out,"ds_bene"); %>
		</td>
		<td>
			<% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"ds_bene",true,null,null); %>
		</td>
	  </tr>
   </table>
   <table>
	  <tr>
		<td>
			<% bp.getRigheInventarioDaFattura().writeFormLabel(out,"find_categoria_bene"); %>
		</td>
		<td colspan = "4">
			<% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"find_categoria_bene",true,null,null); %>
		</td>
	  </tr>	
	  <tr>
		<td>
			<% bp.getRigheInventarioDaFattura().writeFormLabel(out,"ds_assegnatario"); %>
		</td>
		<td colspan = "4">
			<% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"cd_assegnatario",true,null,null); %>
			<% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"ds_assegnatario",true,null,null); %>
		</td>
	  </tr>
	  <tr>
		<td>
			<% bp.getRigheInventarioDaFattura().writeFormLabel(out,"find_ubicazione"); %>
		</td>
		<td>
			<% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"find_ubicazione",true,null,null); %>
		</td>
	  </tr>
   </table>
   
	<div class="Group card">   
	<table>	
	  <tr>
		<td><% bp.getRigheInventarioDaFattura().writeFormLabel(out,"valore_iniziale"); %></td>
		<td><% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"valore_iniziale",true,null,null); %></td>
		<td><% bp.getRigheInventarioDaFattura().writeFormLabel(out,"valoreBene"); %></td>
		<td><% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"valoreBene",true,null,null); %></td>
	  </tr>
	  <tr>
		<td><% bp.getRigheInventarioDaFattura().writeFormLabel(out,"variazione_piuPerAssocia"); %></td>
		<td><% bp.getRigheInventarioDaFattura().writeFormInput(out,"variazione_piuPerAssocia"); %></td>
	  </tr>
	</table>
	 </div>
	
 </div>