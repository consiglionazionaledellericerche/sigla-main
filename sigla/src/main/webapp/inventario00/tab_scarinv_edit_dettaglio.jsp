<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk,
		it.cnr.contab.inventario01.bulk.*,
		it.cnr.contab.inventario01.bp.*"
%>
<% CRUDScaricoInventarioBP bp = (CRUDScaricoInventarioBP)BusinessProcess.getBusinessProcess(request); 
	Buono_carico_scarico_dettBulk riga = (Buono_carico_scarico_dettBulk)bp.getEditDettController().getModel();    
	Inventario_beniBulk bene = null; 
	if (riga != null) bene = riga.getBene(); %>   
  <div class="Group">
	<table>
	  <tr>
	  	<td colspan = "4">
		  <% bp.getEditDettController().writeHTMLTable(
				pageContext,
				"righeDaScarico",
				bp.isInserting(),
				false,
				bp.isInserting(),
				null,
				"200px",
				true); %>
		</td>
	  </tr>
	</table>
	<table>
	  <tr>
		<td><% bp.getEditDettController().writeFormLabel(out,"codiceCompleto"); %></td>
		<td colspan="2"><% bp.getEditDettController().writeFormInput(out,"codiceCompleto"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getEditDettController().writeFormLabel(out,"ds_bene"); %></td>
		<td colspan=2><% bp.getEditDettController().writeFormInput(out,null,"ds_bene",true,null,null); %></td>
	  </tr>
	  <tr>
		<td>
			<% bp.getEditDettController().writeFormLabel(out,"ti_istituzionale_commerciale"); %>
		</td>
		<td>
			<% bp.getEditDettController().writeFormInput(out,null,"ti_istituzionale_commerciale",true,null,null); %>
		</td>			
		</tr>
	  <tr>
		<td><% bp.getEditDettController().writeFormLabel(out,"find_categoria_bene"); %></td>
		<td><% bp.getEditDettController().writeFormInput(out,null,"find_categoria_bene",true,null,null); %></td>
	  </tr>	  
	</table>

	<table>	
	  <tr>
		<td><% bp.getEditDettController().writeFormLabel(out,"valoreInizialeBene"); %></td>
		<td><% bp.getEditDettController().writeFormInput(out,null,"valoreInizialeBene",true,null,null); %></td>
		<td><% bp.getEditDettController().writeFormLabel(out,"valore_bene"); %></td>
		<td><% bp.getEditDettController().writeFormInput(out,"valore_bene"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getEditDettController().writeFormLabel(out,"valore_da_scaricare"); %></td>
		<td><% bp.getEditDettController().writeFormInput(out,null,"valore_da_scaricare",((!bp.isModValore_unitario()||bp.isNonUltimo(HttpActionContext.getUserContext(session),riga) || riga.isTotalmenteScaricato()||
				riga.getBuono_cs().getTipoMovimento().getFl_buono_per_trasferimento().booleanValue()||riga.isROValore_unitario()||bp.isAssociata(HttpActionContext.getUserContext(session),riga)) &&!bp.isInserting()),null,null); %></td>
		<td><% bp.getEditDettController().writeFormLabel(out,"fl_totalmente_scaricato"); %></td>
		<td><% bp.getEditDettController().writeFormInput(out,null,"fl_totalmente_scaricato",bp.isFlagScaricoTotaleRO(),null,"onClick=\"submitForm('doClickFlagScaricoTotale')\""); %></td>
	  </tr>
	</table>
  </div>

