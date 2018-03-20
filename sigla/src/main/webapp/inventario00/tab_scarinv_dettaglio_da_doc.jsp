<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.contab.inventario01.bp.*"
%>
<% CRUDScaricoInventarioBP bp = (CRUDScaricoInventarioBP)BusinessProcess.getBusinessProcess(request);  %>
  
  <% bp.getDettagliDocumento().writeHTMLTable(pageContext,"inventarioSet",false,false,false,"100%","200px"); %> 
  <div class="Group">
	<table>

	  <tr>
	  	<td colspan = "4"> 
		 <% bp.getRigheInventarioDaDocumento().writeHTMLTable(
				pageContext,
				"default",
				bp.isBottoneAggiungiBeneEnabled(),
				false,
				bp.isBottoneAggiungiBeneEnabled(),
				null,
				"100px",
				true); %>
		</td>
	  </tr>
	</table>	
	<table>	
	  <tr>
		<td><% bp.getRigheInventarioDaDocumento().writeFormLabel(out,"codiceCompleto"); %></td>
		<td colspan="2"><% bp.getRigheInventarioDaDocumento().writeFormInput(out,"codiceCompleto"); %></td>
	  </tr>		
	  <tr>
		<td><% bp.getRigheInventarioDaDocumento().writeFormLabel(out,"ds_bene"); %></td>
		<td colspan=2><% bp.getRigheInventarioDaDocumento().writeFormInput(out,null,"ds_bene",true,null,null); %></td>
	  </tr>
	  <tr>
		<td><% bp.getRigheInventarioDaDocumento().writeFormLabel(out,"ti_istituzionale_commerciale"); %></td>
		<td colspan=2><% bp.getRigheInventarioDaDocumento().writeFormInput(out,null,"ti_istituzionale_commerciale",true,null,null); %></td>
	  </tr>
	  <tr>
		<td><% bp.getRigheInventarioDaDocumento().writeFormLabel(out,"find_categoria_bene"); %></td>
		<td><% bp.getRigheInventarioDaDocumento().writeFormInput(out,null,"find_categoria_bene",true,null,null); %></td>
	  </tr>
	</table>
	<table>	
	 <tr>
		<td><% bp.getRigheInventarioDaDocumento().writeFormLabel(out,"variazione_meno"); %></td>
		<td><% bp.getRigheInventarioDaDocumento().writeFormInput(out,null,"variazione_meno",true,null,null); %></td>
		<td><% bp.getRigheInventarioDaDocumento().writeFormLabel(out,"fl_totalmente_scaricato"); %></td>
		<td><% bp.getRigheInventarioDaDocumento().writeFormInput(out,null,"fl_totalmente_scaricato",true,null,"checked"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getRigheInventarioDaDocumento().writeFormLabel(out,"valore_alienazione_apg"); %></td>
		<td colspan="3"><% bp.getRigheInventarioDaDocumento().writeFormInput(out,null,"valore_alienazione_apg",bp.isEditing() || !bp.isBottoneAggiungiBeneEnabled(),null,null); %></td>
	  </tr>

	
	</table>
  </div>