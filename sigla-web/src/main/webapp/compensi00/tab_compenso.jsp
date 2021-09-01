<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.*,
		it.cnr.contab.util.enumeration.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.contab.compensi00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<% UserContext uc = HttpActionContext.getUserContext(session);
   CRUDCompensoBP bp = (CRUDCompensoBP)BusinessProcess.getBusinessProcess(request);
   CompensoBulk compenso = (CompensoBulk)bp.getModel();%>

<div class="Group card">
<table>

  <% if (bp.isSearching()) { %>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_registrazioneForSearch"); %></td>
	<td colspan=3><% bp.getController().writeFormInput(out,"dt_registrazioneForSearch"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_da_competenza_cogeForSearch"); %></td>
	<td colspan=3><% bp.getController().writeFormInput(out,"dt_da_competenza_cogeForSearch"); %>
		<% bp.getController().writeFormLabel(out,"dt_a_competenza_cogeForSearch"); %>
		<% bp.getController().writeFormInput(out,"dt_a_competenza_cogeForSearch"); %></td>
  </tr>
  <% } else { %>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_registrazione"); %></td>
	<td colspan=3><% bp.getController().writeFormInput(out,"dt_registrazione"); %></td>
  </tr>
  <tr> 
	<td><% bp.getController().writeFormLabel(out,"dt_da_competenza_coge"); %></td>
	<td colspan=3><% bp.getController().writeFormInput(out,"dt_da_competenza_coge"); %>
		<% bp.getController().writeFormLabel(out,"dt_a_competenza_coge"); %>
		<% bp.getController().writeFormInput(out,"dt_a_competenza_coge"); %></td>
  </tr>
  <% } %>

  <tr> 
	<td><% bp.getController().writeFormLabel(out,"ds_compenso"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"ds_compenso"); %></td>   
  </tr>

  <% if (bp.isSearching()) { 
	  boolean isInSpesaMode = (bp instanceof IDocumentoAmministrativoSpesaBP && ((IDocumentoAmministrativoSpesaBP)bp).isSpesaBP()) ? true : false; %>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_istituz_commercForSearch"); %></td>
	<td><% bp.getController().writeFormInput(out,"ti_istituz_commercForSearch"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"stato_cofiForSearch"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"stato_cofiForSearch", isInSpesaMode, null, ""); %></td>
   	<td>
   		<% bp.getController().writeFormLabel(out,"stato_liquidazione");%>
   		<% bp.getController().writeFormInput(out,null,"stato_liquidazione",isInSpesaMode,null,"onChange=\"submitForm('doOnStatoLiquidazioneChange')\"");%>
   	</td>
   	<td> 
   		<% bp.getController().writeFormLabel(out,"causale");%>
   		<% bp.getController().writeFormInput(out,null,"causale",false,null,"onChange=\"submitForm('doOnCausaleChange')\"");%>
   	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_associato_manrevForSearch"); %></td>
	<td><% bp.getController().writeFormInput(out,"ti_associato_manrevForSearch"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"stato_pagamento_fondo_ecoForSearch");%></td>
    <td><% bp.getController().writeFormInput(out,null,"stato_pagamento_fondo_ecoForSearch", isInSpesaMode, null,""); %></td>
  </tr>
  <% } else { %>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_istituz_commerc"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"ti_istituz_commerc",false,null,"onChange=\"submitForm('doOnTipoIstituzCommercChange')\"");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"stato_cofi"); %></td>
	<td><% bp.getController().writeFormInput(out,"stato_cofi"); %></td>
   	<td>
   		<% bp.getController().writeFormLabel(out,"stato_liquidazione");%>
   		<% bp.getController().writeFormInput(out,null,"stato_liquidazione",false,null,"onChange=\"submitForm('doOnStatoLiquidazioneChange')\"");%>
   	</td>
   	<td> 
   		<% bp.getController().writeFormLabel(out,"causale");%>
   		<% bp.getController().writeFormInput(out,null,"causale",false,null,"onChange=\"submitForm('doOnCausaleChange')\"");%>
   	</td>
    </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_associato_manrev"); %></td>
	<td><% bp.getController().writeFormInput(out,"ti_associato_manrev"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"stato_pagamento_fondo_eco");%></td>      	
	<td><% bp.getController().writeFormInput(out,null,"stato_pagamento_fondo_eco",false,null,"onChange=\"submitForm('doOnStatoPagamentoFondoEcoChange')\"");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_pagamento_fondo_eco"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_pagamento_fondo_eco"); %></td>
  </tr>
  <% } %>

</table>
</div>
<div class="Group card">
<table>
<tr>
	<td>
		<% bp.getController().writeFormInput(out,null,"fl_senza_calcoli",false,null,"onClick=\"submitForm('doOnFlSenzaCalcoliChange()')\"");%>
		<% bp.getController().writeFormLabel(out,"fl_senza_calcoli"); %>
	</td>
	<td>
		<% bp.getController().writeFormInput(out,"fl_recupero_rate");%>
		<% bp.getController().writeFormLabel(out,"fl_recupero_rate"); %>
	</td>	
	<td>
		<% bp.getController().writeFormInput(out,"fl_compenso_stipendi"); %>
		<% bp.getController().writeFormLabel(out,"fl_compenso_stipendi"); %>
	</td>
	<td>
		<% bp.getController().writeFormInput(out,"fl_diaria"); %>
		<% bp.getController().writeFormLabel(out,"fl_diaria"); %>
	</td>
	<td>
		<% bp.getController().writeFormInput(out,"fl_compenso_conguaglio"); %>
		<% bp.getController().writeFormLabel(out,"fl_compenso_conguaglio"); %>
	</td>
	<td>
		<% bp.getController().writeFormInput(out,"fl_compenso_minicarriera"); %>
		<% bp.getController().writeFormLabel(out,"fl_compenso_minicarriera"); %>
	</td>
  </tr>
</table>
</div>

<div class="Group card">
<table>
<% if(bp.isGestiteDeduzioniIrpef(uc)) {%>
  <tr>   
	<td>
		<% bp.getController().writeFormLabel(out,"fl_escludi_qvaria_deduzione"); %>	
		<% bp.getController().writeFormInput(out,"fl_escludi_qvaria_deduzione"); %>
	</td>
	<td>
		<% bp.getController().writeFormLabel(out,"fl_intera_qfissa_deduzione"); %>	
		<% bp.getController().writeFormInput(out,"fl_intera_qfissa_deduzione"); %>
	</td>		
  </tr>
<% } %>
<tr>
	<td colspan=2>
		<% bp.getController().writeFormLabel(out,"fl_accantona_add_terr"); %>	
		<% bp.getController().writeFormInput(out,"fl_accantona_add_terr"); %>
	</td>		
</tr>  
      <tr>
		<%	if (compenso != null && compenso.isCollegatoCapitoloPerTrovato()) { %>
			  	  <td>
				  	<% bp.getController().writeFormLabel(out,"pg_trovato");%>
				  </td>
				  <td colspan="3">	
				  <% bp.getController().writeFormInput(out,null,"pg_trovato",false,null,"");%>
				  	<% bp.getController().writeFormField(out,"titoloTrovato");%>
				  	<% bp.getController().writeFormField(out,"inventoreTrovato");%>
			  	</td>
		<%	}  %>
 	  </tr>
</table>
</div>

<fieldset class="fieldset card">
<legend ACCESSKEY=G TABINDEX=1 class="GroupLabel card-header text-primary">Dati Fattura</legend>
<div class="Panel">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio_fattura_fornitore"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"esercizio_fattura_fornitore", bp.isRODatiFatturaPerChiusura(compenso), null, null); %></td>
	<td><% bp.getController().writeFormLabel(out,"nr_fattura_fornitore"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"nr_fattura_fornitore", bp.isRODatiFatturaPerChiusura(compenso), null, null); %></td>	
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_fattura_fornitore"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"dt_fattura_fornitore", bp.isRODatiFatturaPerChiusura(compenso), null, null); %></td>		
	<td><% bp.getController().writeFormLabel(out,"fl_generata_fattura"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"fl_generata_fattura", bp.isRODatiFatturaPerChiusura(compenso),null,"onClick=\"submitForm('doOnFlGenerataFatturaChange()')\"");%></td>
	<td><% bp.getController().writeFormLabel(out,"fl_documento_ele"); %></td>
	<td><% bp.getController().writeFormInput(out,"fl_documento_ele");%></td>
	<td><% bp.getController().writeFormLabel(out,"fl_split_payment"); %></td>
	<td><% bp.getController().writeFormInput(out,"fl_split_payment");%></td>
	
	<% if(compenso!=null &&compenso.getTi_istituz_commerc()!=null && compenso.getTi_istituz_commerc().compareTo(TipoIVA.COMMERCIALE.value())==0 && compenso.getStato_pagamento_fondo_eco().compareTo(compenso.LIBERO_FONDO_ECO)==0 && compenso.getFl_generata_fattura().booleanValue()) {%>
		<td><% bp.getController().writeFormLabel(out,"fl_liquidazione_differita"); %></td>
		<td><% bp.getController().writeFormInput(out,null,"fl_liquidazione_differita", bp.isRODatiFatturaPerChiusura(compenso), null, "onClick=\"submitForm('doOnFlLiquidazioneDifferitaChange()')\""); %></td>
	<%} %>
	<tr>
		<td><% bp.getController().writeFormLabel(out,"dt_scadenza"); %> </td> 
		<td><% bp.getController().writeFormInput(out,null,"dt_scadenza", bp.isRODatiFatturaPerChiusura(compenso), null, null); %></td>
		<td><% bp.getController().writeFormLabel(out,"data_protocollo"); %></td>
		<td><% bp.getController().writeFormInput(out,null,"data_protocollo", bp.isRODatiFatturaPerChiusura(compenso), null, null); %></td>	
		<td><% bp.getController().writeFormLabel(out,"numero_protocollo"); %></td>
		<td><%bp.getController().writeFormInput(out,null,"numero_protocollo", bp.isRODatiFatturaPerChiusura(compenso), null, null); %></td>
    </tr>
   
</table>
</div>
</fieldset>