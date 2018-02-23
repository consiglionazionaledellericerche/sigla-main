<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario00.docs.bulk.*,
		it.cnr.contab.inventario00.bp.*"
%>
<% CRUDTrasferimentoInventarioBP bp = (CRUDTrasferimentoInventarioBP)BusinessProcess.getBusinessProcess(request); 
	Trasferimento_inventarioBulk trasf = (Trasferimento_inventarioBulk)bp.getController().getModel();
    Inventario_beniBulk bene = (Inventario_beniBulk)bp.getDettController().getModel(); %>
  
	<table>
	  <tr>
	  	<td colspan = "4">
		  <% bp.getDettController().writeHTMLTable(
				pageContext,
				"default",
				bp.isInserting(),
				false,
				bp.isInserting(),
				null,
				"200px",
				true); %>
		</td>
	  </tr>
	</table>
	<% if (bp.isTrasferimentoExtraInv() && bp.isAmministratore()){ %>
		<table>	  
		  <tr>
			<td>
			  <% bp.getController().writeFormLabel(out,"fl_scarica_tutti");%>
			 </td>
			 <td>
				<% bp.getController().writeFormInput(out,null,"fl_scarica_tutti",false,null,"onClick=\"submitForm('doOnFlTrasferisciTuttiChange')\"");%>
			  </td>
			</tr>
		</table>
	<% } %>
	<div class="Group">		
		
	<table>
	
	  <tr>
		<td><% bp.getDettController().writeFormLabel(out,"codiceCompleto"); %></td>
		<td colspan=3><% bp.getDettController().writeFormInput(out,"codiceCompleto"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getDettController().writeFormLabel(out,"ds_bene"); %></td>
		<td colspan=3><% bp.getDettController().writeFormInput(out,null,"ds_bene",true,null,null); %></td>
	  </tr>
	  <tr>
		<td><% bp.getDettController().writeFormLabel(out,"ti_istituzionale_commerciale"); %></td>
		<td><% bp.getDettController().writeFormInput(out,null,"ti_istituzionale_commerciale",true,null,null); %></td>
	  </tr>	 
	  <tr>
		<td><% bp.getDettController().writeFormLabel(out,"find_categoria_bene"); %></td>
		<td colspan=3><% bp.getDettController().writeFormInput(out,null,"find_categoria_bene",true,null,null); %></td>
	  </tr>	  
	  <% if (bp.isTrasferimentoIntraInv() && bene != null){ %>
			<% if ( bp.isAmministratore() && trasf.isFl_cambio_categoria()){ %>
			<tr>
				<td >
					<% bp.getDettController().writeFormLabel(out,"find_nuova_categoria"); %>
				</td>
				<td colspan=3>
					<%
						bp.getDettController().writeFormInput(out,null,"find_nuova_categoria",false ,null,null); 
					%>
				</td>
			</tr>
			<% } %> 
	 		 <% if (!trasf.isFl_cambio_categoria()){ %>
	         <% if (bene.isBeneAccessorio()){ %> 
				<tr>
					<td colspan=4>
						<% bp.getDettController().writeFormLabel(out,"fl_trasf_come_principale"); %>
						<% bp.getDettController().writeFormInput(out,null,"fl_trasf_come_principale",false,null,"onClick=\"submitForm('doOnFlTrasfComePrincChange')\""); %>
					</td>
				</tr>	
			<% } %>  
			<tr>
				<td >
					<% bp.getDettController().writeFormLabel(out,"find_nuovo_bene_padre"); %>
				</td>
				<td colspan=3>
					<%
						boolean roNuovoBenePadre = false;
						if (bene != null && bene.getFl_trasf_come_principale()!= null)
							roNuovoBenePadre = bene.getFl_trasf_come_principale().booleanValue();
				 		bp.getDettController().writeFormInput(out,null,"find_nuovo_bene_padre",roNuovoBenePadre ,null,null); 
					%>
				</td>
			</tr>
		</table>	
		</div>
	<% } %>	
	<% } %>	
	<div class="Group">		
	<table>	
	  <tr>
		<td><% bp.getDettController().writeFormLabel(out,"valore_iniziale"); %></td>
		<td><% bp.getDettController().writeFormInput(out,null,"valore_iniziale",true,null,null); %></td>
		<td><% bp.getDettController().writeFormLabel(out,"valore_bene"); %></td>
		<td><% bp.getDettController().writeFormInput(out,"valore_bene"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getDettController().writeFormLabel(out,"variazione_meno"); %></td>
		<td><% bp.getDettController().writeFormInput(out,null,"variazione_meno",true,null,null); %></td>
		<td><% bp.getDettController().writeFormLabel(out,"fl_totalmente_scaricato"); %></td>
		<td><% bp.getDettController().writeFormInput(out,null,"fl_totalmente_scaricato",true,null,"checked"); %></td>
	  </tr>
	</table>
	</div>	