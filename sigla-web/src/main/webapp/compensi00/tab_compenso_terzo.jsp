<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.contab.compensi00.docs.bulk.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*"
%>

<% CRUDCompensoBP bp = (CRUDCompensoBP)BusinessProcess.getBusinessProcess(request);
	CompensoBulk compenso = (CompensoBulk)bp.getModel(); %>

<div class="Group card">
<table class="w-75">
  <tr>
	<td colspan="2"><% bp.getController().writeFormInput(out,null,"ti_anagrafico",false,null,"onClick=\"submitForm('doOnTipoAnagraficoChange')\""); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"find_terzo"); %></td>
	<td colspan="3">
		<% bp.getController().writeFormInput(out,"find_terzo"); %>
	</td>
  </tr>

  <tr>
	<td><% bp.getController().writeFormLabel( out, "cd_precedente"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput( out, "cd_precedente"); %></td>
  </tr>  
  
  <tr>
	<td><% bp.getController().writeFormLabel(out,"nome");%></td>
	<td><% bp.getController().writeFormInput(out,"nome");%></td>
	<td><% bp.getController().writeFormLabel(out,"cognome");%></td>
	<td><% bp.getController().writeFormInput(out,"cognome");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ragione_sociale"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"ragione_sociale"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"indirizzoTerzo");%></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"indirizzoTerzo");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_comune");%></td>
	<td><% bp.getController().writeFormInput(out,"ds_comune");%></td>
	<td><% bp.getController().writeFormLabel(out,"ds_provincia");%></td>
	<td><% bp.getController().writeFormInput(out,"ds_provincia");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"codice_fiscale"); %></td>
	<td><% bp.getController().writeFormInput(out,"codice_fiscale"); %></td>
	<td><% bp.getController().writeFormLabel(out,"partita_iva"); %></td>
	<td><% bp.getController().writeFormInput(out,"partita_iva"); %></td>	
  </tr>
  <tr>
 	<td><% bp.getController().writeFormLabel(out,"termini_pagamento");%></td>
 	<td colspan="3"><% bp.getController().writeFormInput(out,"termini_pagamento");%></td> 	
  </tr>
  <tr>
 	<td><% bp.getController().writeFormLabel(out,"modalita_pagamento");%></td>
    <td colspan="3">
    	<% bp.getController().writeFormInput(out,null,"modalita_pagamento",false,null,"onChange=\"submitForm('doOnModalitaPagamentoChange')\"");%>
		<% if (compenso.getBanca() != null) bp.getController().writeFormInput(out, null, "listaBanche", false, null, ""); %>
	</td>
  </tr>
  <tr>
 	<td colspan="4">
	<%	if (compenso.getBanca() != null) {
			if (Rif_modalita_pagamentoBulk.BANCARIO.equalsIgnoreCase(compenso.getBanca().getTi_pagamento())) {
	 	     	bp.getController().writeFormInput(out,"contoB");
			} else if (Rif_modalita_pagamentoBulk.POSTALE.equalsIgnoreCase(compenso.getBanca().getTi_pagamento())) {
			   	bp.getController().writeFormInput(out,"contoP");
			} else if (Rif_modalita_pagamentoBulk.QUIETANZA.equalsIgnoreCase(compenso.getBanca().getTi_pagamento())) {
			   	bp.getController().writeFormInput(out,"contoQ");
			} else if (Rif_modalita_pagamentoBulk.ALTRO.equalsIgnoreCase(compenso.getBanca().getTi_pagamento())) { 
			   	bp.getController().writeFormInput(out,"contoA");
			} else if (Rif_modalita_pagamentoBulk.IBAN.equalsIgnoreCase(compenso.getBanca().getTi_pagamento())) { 
			   	bp.getController().writeFormInput(out,"contoN");
			} else if (Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(compenso.getBanca().getTi_pagamento())&& compenso.getBanca().isTABB()) {
                bp.getController().writeFormInput(out,"contoB");
            }
  		} else if (compenso.getModalitaPagamento() != null && (compenso.getV_terzo() != null && compenso.getV_terzo().getCrudStatus() != compenso.getV_terzo().UNDEFINED)) { %>
			<span class="FormLabel" style="color:red">
				Nessun riferimento trovato per la modalità di pagamento selezionata!
			</span>
	<%	} %>
  	</td>
  </tr>
</table>
</div>

<div class="Group card">
<table>
  <tr>
 	<td><% bp.getController().writeFormLabel(out,"tipoRapporto");%></td>
	<td><% bp.getController().writeFormInput(out,null,"tipoRapporto",false,null,"onChange=\"submitForm('doOnTipoRapportoChange')\""); %></td>
  </tr>
  <tr>
 	<td><% bp.getController().writeFormLabel(out,"tipoTrattamento");%></td>
	<td><% bp.getController().writeFormInput(out,null,"tipoTrattamento",false,null,"onChange=\"submitForm('doOnTipoTrattamentoChange')\""); %></td>
  </tr>
  <% if (bp.isSearching() || (compenso!=null && compenso.isPrestazioneCompensoEnabled())) { %>
  <tr>
 	<td><% bp.getController().writeFormLabel(out,"tipoPrestazioneCompenso");%></td>
	<td><% bp.getController().writeFormInput(out,null,"tipoPrestazioneCompenso",false,null,"onChange=\"submitForm('doOnTipoPrestazioneCompensoChange')\""); %></td>
  </tr>
 <% } %>
</table>
</div>

<% if (bp.isGestioneIncarichiEnabled() && (compenso!=null && compenso.isPrestazioneCompensoEnabled()) && (bp.isSearching() || (compenso!=null && compenso.isIncaricoEnabled()))) { %>
<div class="Group">
<fieldset class="fieldset card">

<% if (compenso!=null && compenso.getCd_tipo_rapporto()!=null && compenso.getCd_tipo_rapporto().equals(new String ("BORS"))) { %>
	<legend class="Group cardLabel card-header text-primary">Borsa di studio</legend>
<% } else { %>
	<% if (compenso!=null && compenso.getCd_tipo_rapporto()!=null && compenso.getCd_tipo_rapporto().equals(new String ("ASS"))) { %>
	<legend class="Group cardLabel card-header text-primary">Assegno di ricerca</legend>
	<% } else {%>
	<legend class="Group cardLabel card-header text-primary">Incarico</legend>
	<% } %>
<% } %>
<div class="Panel">
<table>
  <tr>
  	<td><% bp.getController().writeFormLabel(out,"incarichi_repertorio_anno"); %></td>
 	<td colspan="3"><% bp.getController().writeFormInput(out,"incarichi_repertorio_anno"); %></td>
  </tr>
  <tr>	
	<td><% bp.getController().writeFormLabel(out,"importo_complessivo"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"importo_complessivo"); %></td>
	<td><% bp.getController().writeFormLabel(out,"importo_utilizzato"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"importo_utilizzato"); %></td>
  <tr>
  <tr>         
    <td><% bp.getController().writeFormLabel(out,"incarichi_oggetto");%></td>
    <td colspan="3"><% bp.getController().writeFormInput(out,"incarichi_oggetto");%></td>
  </tr>
</table>
</div>
</fieldset>
</div>
<% } %>

<% if ((compenso!=null && compenso.isPrestazioneCompensoEnabled()) && (bp.isSearching() || (compenso!=null && compenso.isContrattoEnabled()))) { %>
<div class="Group">
<fieldset class="fieldset card">
<legend class="Group cardLabel card-header text-primary">Repertorio Contratto</legend>
<div class="Panel">
<table>
  <tr>
  	<td><% bp.getController().writeFormLabel(out,"contratto"); %></td>
 	<td colspan="3"><% bp.getController().writeFormInput(out,"contratto"); %></td>
  </tr>
  <tr>         
    <td><% bp.getController().writeFormLabel(out,"oggetto_contratto");%></td>
    <td colspan="3"><% bp.getController().writeFormInput(out,"oggetto_contratto");%></td>
  </tr>
</table>
</div>
</fieldset>
</div>
<% } %>


<% if (compenso.isVisualizzaPignorato()) { %>
<div class="Group">
	<fieldset class="fieldset card">
	<legend class="Group cardLabel card-header text-primary">Pignorato</legend>
	<div class="Panel">
	<table>
	  <tr>
	   <td><% bp.getController().writeFormLabel( out, "cd_terzo_pignorato"); %></td>
	   <td colspan=2>
		<% bp.getController().writeFormInput( out, "cd_terzo_pignorato"); %>
		<% bp.getController().writeFormInput( out, "ds_pignorato"); %>
		<% bp.getController().writeFormInput( out, "find_pignorato"); %>
	   </td>
	  </tr>
	  <tr>
	   <td>	<% bp.getController().writeFormLabel( out, "codice_fiscale_pignorato"); %></td>
	   <td>	<% bp.getController().writeFormInput( out, "codice_fiscale_pignorato"); %></td>
	   <td>
            <% bp.getController().writeFormLabel( out, "partita_iva_pignorato"); %>
	        <% bp.getController().writeFormInput( out, "partita_iva_pignorato"); %>	        
	   </td>
	  </tr>
	</table>
	</div>
	</fieldset>	
</div>
<% } %>
<% if (compenso.isVisualizzaCodici_rapporti_inps()) { %>
<div class="Group card" style="width:100%">
<table>
  <tr>
  	<td><% bp.getController().writeFormLabel(out,"codici_rapporti_inps"); %></td>
 	<td colspan="3"><% bp.getController().writeFormInput(out,"codici_rapporti_inps"); %></td>
  </tr>
  <% if (compenso.isVisualizzaCodici_attivita_inps()) { %>
  <tr>
 	<td><% bp.getController().writeFormLabel(out,"codici_attivita_inps");%></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"codici_attivita_inps"); %></td>
  </tr>
  <% } %>
  <% if (compenso.isVisualizzaCodici_altra_forma_ass_inps()) { %>
  <tr>
 	<td><% bp.getController().writeFormLabel(out,"codici_altra_forma_ass_inps");%></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"codici_altra_forma_ass_inps"); %></td>
  </tr>  
  <% } %>
  <tr>
   	<td><% bp.getController().writeFormLabel(out,"ds_comune_inps");%></td>
  	<td><% bp.getController().writeFormInput(out,"ds_comune_inps");%>
	    <% bp.getController().writeFormInput(out,"find_comune_inps");%></td>
	<td><% bp.getController().writeFormField(out,"ds_provincia_inps");%></td>
  </tr>  
</table>
</div>
<% } %>