<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario01.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk,
		it.cnr.contab.inventario01.bp.*"
%>
<% CRUDScaricoInventarioBP bp = (CRUDScaricoInventarioBP)BusinessProcess.getBusinessProcess(request); 
   Buono_carico_scaricoBulk buonoCarico = (Buono_carico_scaricoBulk)bp.getModel(); %>
  
  <% bp.getDettagliFattura().writeHTMLTable(pageContext,"inventarioSet",false,false,false,"100%","200px"); %> 
  <div class="Group">
	<table>

	  <tr>
	  	<td colspan = "4"> 
		 <% bp.getRigheInventarioDaFattura().writeHTMLTable(
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
		<td><% bp.getRigheInventarioDaFattura().writeFormLabel(out,"codiceCompleto"); %></td>
		<td colspan="2"><% bp.getRigheInventarioDaFattura().writeFormInput(out,"codiceCompleto"); %></td>
	  </tr>		
	  <tr>
		<td><% bp.getRigheInventarioDaFattura().writeFormLabel(out,"ds_bene"); %></td>
		<td colspan=2><% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"ds_bene",true,null,null); %></td>
	  </tr>
	  <tr>
		<td><% bp.getRigheInventarioDaFattura().writeFormLabel(out,"ti_istituzionale_commerciale"); %></td>
		<td colspan=2><% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"ti_istituzionale_commerciale",true,null,null); %></td>
	  </tr>
	  <tr>
		<td><% bp.getRigheInventarioDaFattura().writeFormLabel(out,"find_categoria_bene"); %></td>
		<td><% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"find_categoria_bene",true,null,null); %></td>
	  </tr>
	</table>
	<table>	
	  <tr>
		<td><% bp.getRigheInventarioDaFattura().writeFormLabel(out,"valore_iniziale"); %></td>
		<td><% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"valore_iniziale",true,null,null); %></td>
		<td><% bp.getRigheInventarioDaFattura().writeFormLabel(out,"valore_bene"); %></td>
		<td><% bp.getRigheInventarioDaFattura().writeFormInput(out,"valore_bene"); %></td>
	  </tr>
	  <% if (bp.getDettagliFattura().getModel() instanceof Fattura_attiva_rigaIBulk){ %>
	  <tr>
		<td><% bp.getRigheInventarioDaFattura().writeFormLabel(out,"variazione_meno"); %></td>
		<td><% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"variazione_meno",true,null,null); %></td>
		<td><% bp.getRigheInventarioDaFattura().writeFormLabel(out,"fl_totalmente_scaricato"); %></td>
		<td><% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"fl_totalmente_scaricato",true,null,"checked"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getRigheInventarioDaFattura().writeFormLabel(out,"valore_alienazione_apg"); %></td>
		<td colspan="3"><% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"valore_alienazione_apg",bp.isEditing() || !bp.isBottoneAggiungiBeneEnabled(),null,null); %></td>
	  </tr>
	  <%} else{ %>
  	  <tr>
		 <tr>
		<td><% bp.getRigheInventarioDaFattura().writeFormLabel(out,"variazione_menoPerAssocia"); %></td>
		<td><% bp.getRigheInventarioDaFattura().writeFormInput(out,"variazione_menoPerAssocia"); %></td>
	  </tr>
	  <%} %>
	</table>
  </div>