<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*, it.cnr.jada.bulk.*, it.cnr.contab.docamm00.bp.*"%>

<%  
	CRUDAnticipoBP bp = (CRUDAnticipoBP)BusinessProcess.getBusinessProcess(request);
	AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

	if(anticipo == null)
		anticipo = new AnticipoBulk();
%>


<table width="100%">
	<tr></tr>
	<tr></tr>	
	<tr></tr>	
		
	<tr>
	<td>
		<% bp.getController().writeFormLabel( out, "im_anticipo_divisa"); %>
		<% bp.getController().writeFormInput( out, null, "im_anticipo_divisa", !anticipo.isEditable(), null, ""); %>
	</td>
	</tr>
	
	<!--
		Se devi inserire la divisa e il cambio guarda la versione 1.0
	-->

	<tr></tr>	
	<tr></tr>		
	<tr></tr>
</table>

<div class="Group" style="width:100%">
<table>
<tr></tr>
<tr></tr>

 <% if (bp.isSearching()) 
 	{ 
	  boolean isInSpesaMode = (bp instanceof IDocumentoAmministrativoSpesaBP && ((IDocumentoAmministrativoSpesaBP)bp).isSpesaBP()) ? true : false; %>
	  	<tr>
		<td><% bp.getController().writeFormLabel(out,"stato_cofiForSearch"); %></td>
		<td><% bp.getController().writeFormInput(out,null,"stato_cofiForSearch", isInSpesaMode, null, ""); %></td>
		</tr>
		
	  	<tr>
		<td><% bp.getController().writeFormLabel(out,"ti_associato_manrevForSearch"); %></td>
		<td><% bp.getController().writeFormInput(out,"ti_associato_manrevForSearch"); %></td>
		</tr>
		
	  	<tr>
		<td><% bp.getController().writeFormLabel(out,"stato_pagamento_fondo_ecoForSearch");%></td>
	    <td><% bp.getController().writeFormInput(out,null,"stato_pagamento_fondo_ecoForSearch", isInSpesaMode, null,""); %>
	    </td>
	  	</tr>
	  	
  <% } else { %>
	  
	  	<tr>
		<td><% bp.getController().writeFormLabel(out,"stato_cofi"); %></td>
		<td><% bp.getController().writeFormInput(out,"stato_cofi"); %></td>
	  	</tr>
	  	
	  	<tr>
		<td><% bp.getController().writeFormLabel(out,"ti_associato_manrev"); %></td>
		<td><% bp.getController().writeFormInput(out,"ti_associato_manrev"); %></td>
	  	</tr>
	  	
	  	<tr>
		<td><% bp.getController().writeFormLabel(out,"stato_pagamento_fondo_eco");%></td>      	
		<td><% bp.getController().writeFormInput(out,null,"stato_pagamento_fondo_eco",false,null,"onChange=\"submitForm('doDefault')\"");%></td>
	  	</tr>
	  	
	  	<tr>
		<td><% bp.getController().writeFormLabel(out,"dt_pagamento_fondo_eco"); %></td>
		<td><% bp.getController().writeFormInput(out,"dt_pagamento_fondo_eco"); %></td>
	  	</tr>
  <% } %>
 
<tr></tr>
<tr></tr>   
</table>
</div>

<fieldset>
<legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px; color:blue">Scadenza Impegno</legend>
<div class="Panel" style="width:100%">
<table>
	<tr>	
	<td><% bp.getController().writeFormLabel( out, "esercizio_ori_obbligazione"); %></td>
	<td><% bp.getController().writeFormInput(out, "esercizio_ori_obbligazione");%></td>
	<td><% bp.getController().writeFormLabel( out, "pg_obbligazione"); %></td>
	<td><% bp.getController().writeFormInput(out, "pg_obbligazione");%></td>
	</tr>
</table>
<table width="100%">
	<tr>		
	<td><% bp.getController().writeFormLabel(out, "pg_obbligazione_scadenzario");%></td>
	<td><% bp.getController().writeFormInput(out, "pg_obbligazione_scadenzario");%></td>
	<td><% bp.getController().writeFormLabel(out, "ds_scadenza_obbligazione");%></td>
	<td><% bp.getController().writeFormInput(out, "ds_scadenza_obbligazione");%></td>
	<td><% bp.getController().writeFormLabel(out, "im_scadenza");%></td>
	<td><% bp.getController().writeFormInput(out, "im_scadenza");%></td>	
	</tr>

	<tr>		
	<td><% bp.getController().writeFormLabel(out, "dt_scadenza");%></td>
	<td><% bp.getController().writeFormInput(out, "dt_scadenza");%></td>
	<td><% bp.getController().writeFormLabel(out, "esercizio_obbligazione");%></td>	
	<td><% bp.getController().writeFormInput(out, "esercizio_obbligazione");%></td>
	<td><% bp.getController().writeFormLabel(out, "cd_cds_obbligazione");%></td>
	<td><% bp.getController().writeFormInput(out, "cd_cds_obbligazione");%></td>	
	</tr>	
	<tr></tr>
	<tr></tr>
	<tr></tr>		
</table>
<table width="100%">		
	<tr>
	<td ALIGN="CENTER">
		<% JSPUtils.button(out,bp.encodePath("img/new24.gif"),bp.encodePath("img/new24.gif"),"Crea/Ricerca<br>impegno","javascript:submitForm('doRicercaScadenzaObbligazione')",bp.areBottoniObbligazioneAbilitati(),bp.getParentRoot().isBootstrap()); %>
		<% JSPUtils.button(out,bp.encodePath("img/remove24.gif"),bp.encodePath("img/remove24.gif"),"Elimina<br>impegno","javascript:submitForm('doEliminaScadenzaObbligazione')",bp.areBottoniObbligazioneAbilitati(),bp.getParentRoot().isBootstrap()); %>
		<% JSPUtils.button(out,bp.encodePath("img/redo24.gif"),bp.encodePath("img/redo24.gif"), "Aggiorna in<br>manuale", "javascript:submitForm('doOpenObbligazioniWindow')", bp.isBottoneObbligazioneAggiornaManualeAbilitato(),bp.getParentRoot().isBootstrap()); %>
		<% JSPUtils.button(out,bp.encodePath("img/refresh24.gif"),bp.encodePath("img/refresh24.gif"), "Aggiorna in<br>automatico", "javascript:submitForm('doModificaScadenzaInAutomatico')", bp.areBottoniObbligazioneAbilitati(), bp.getParentRoot().isBootstrap()); %>
	</td>
	</tr>
</table>
</div>
</fieldset>

<% if (anticipo.isAnticipoConMissione()) { %>
<fieldset>
<legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px; color:blue">Missione</legend>	
<div class="Panel" style="width:100%">
<table>
		<tr>		
		<td><% bp.getController().writeFormLabel(out, "pg_missione");%></td>
		<td><% bp.getController().writeFormInput(out, "pg_missione");%></td>
		<td><% bp.getController().writeFormLabel(out, "esercizio_missione");%></td>
		<td><% bp.getController().writeFormInput(out, "esercizio_missione");%></td>
		</tr>

		<tr>		
		<td><% bp.getController().writeFormLabel(out, "cd_uo_missione");%></td>
		<td><% bp.getController().writeFormInput(out, "cd_uo_missione");%></td>
		<td><% bp.getController().writeFormLabel(out, "cd_cds_missione");%></td>	
		<td><% bp.getController().writeFormInput(out, "cd_cds_missione");%></td>
		</tr>
	
		<tr></tr>
		<tr></tr>
		<tr></tr>
</table>
<table width="100%">
	<tr>
	<td ALIGN="CENTER">
		<% JSPUtils.button(out, null, null, "Visualizza missione", "javascript:submitForm('doVisualizzaMissione')", true,
				bp.getParentRoot().isBootstrap()); %>
	</td>
	</tr>
</table>
</div>
</fieldset>
<%}%>