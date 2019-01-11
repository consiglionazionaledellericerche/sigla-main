<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.contab.compensi00.docs.bulk.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*"
%>

<% CRUDConguaglioBP bp = (CRUDConguaglioBP)BusinessProcess.getBusinessProcess(request);
	ConguaglioBulk conguaglio = (ConguaglioBulk)bp.getModel(); %>

<div class="Group" style="width:100%">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_terzo"); %></td>
	<td colspan=3>
		<% bp.getController().writeFormInput(out,"cd_terzo"); %>
		<% bp.getController().writeFormInput(out,"find_terzo"); %>
	</td>
  </tr>

  <tr>
	<td><% bp.getController().writeFormLabel( out, "cd_precedente"); %></td>
	<td><% bp.getController().writeFormInput( out, "cd_precedente"); %></td>
  </tr>  
  
  <tr>
	<td><% bp.getController().writeFormLabel(out,"nome");%></td>
	<td><% bp.getController().writeFormInput(out,"nome");%></td>
	<td><% bp.getController().writeFormLabel(out,"cognome");%></td>
	<td><% bp.getController().writeFormInput(out,"cognome");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ragione_sociale"); %></td>
	<td colspan=3><% bp.getController().writeFormInput(out,"ragione_sociale"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"indirizzoTerzo");%></td>
	<td colspan=3><% bp.getController().writeFormInput(out,"indirizzoTerzo");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_comune");%></td>
	<td><% bp.getController().writeFormInput(out,"ds_comune");%></td>
	<td><% bp.getController().writeFormLabel(out,"ds_provincia");%></td>
	<td><% bp.getController().writeFormInput(out,"ds_provincia");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_regione");%></td>
	<td colspan=3><% bp.getController().writeFormInput(out,"ds_regione");%></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"codice_fiscale"); %></td>
	<td><% bp.getController().writeFormInput(out,"codice_fiscale"); %></td>
	<td><% bp.getController().writeFormLabel(out,"partita_iva"); %></td>
	<td><% bp.getController().writeFormInput(out,"partita_iva"); %></td>	
  </tr>
  <tr>
 	<td><% bp.getController().writeFormLabel(out,"termini_pagamento");%></td>
 	<td colspan=3><% bp.getController().writeFormInput(out,"termini_pagamento");%></td> 	
  </tr>
  <tr>
 	<td><% bp.getController().writeFormLabel(out,"modalita_pagamento");%></td>
    <td colspan=3>
    	<% bp.getController().writeFormInput(out,null,"modalita_pagamento",false,null,"onChange=\"submitForm('doOnModalitaPagamentoChange')\"");%>
		<% if (conguaglio.getBanca() != null) bp.getController().writeFormInput(out, null, "listaBanche", false, null, ""); %>
	</td>
  </tr>
  <tr>
    <td colspan=3>
	  <% if (conguaglio.getBanca() != null) {
			if (Rif_modalita_pagamentoBulk.BANCARIO.equalsIgnoreCase(conguaglio.getBanca().getTi_pagamento())) {
	 	     	bp.getController().writeFormInput(out,"contoB");
			} else if (Rif_modalita_pagamentoBulk.POSTALE.equalsIgnoreCase(conguaglio.getBanca().getTi_pagamento())) {
			   	bp.getController().writeFormInput(out,"contoP");
			} else if (Rif_modalita_pagamentoBulk.QUIETANZA.equalsIgnoreCase(conguaglio.getBanca().getTi_pagamento())) {
			   	bp.getController().writeFormInput(out,"contoQ");
			} else if (Rif_modalita_pagamentoBulk.ALTRO.equalsIgnoreCase(conguaglio.getBanca().getTi_pagamento())) { 
			   	bp.getController().writeFormInput(out,"contoA");
			} else if (Rif_modalita_pagamentoBulk.IBAN.equalsIgnoreCase(conguaglio.getBanca().getTi_pagamento())) { 
			   	bp.getController().writeFormInput(out,"contoN");
			} else if (Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(conguaglio.getBanca().getTi_pagamento()) && conguaglio.getBanca().isTABB()) {
                bp.getController().writeFormInput(out,"contoB");
            }
  		} else if (conguaglio.getModalitaPagamento() != null && (conguaglio.getV_terzo() != null && conguaglio.getV_terzo().getCrudStatus() != conguaglio.getV_terzo().UNDEFINED)) { %>
			<span class="FormLabel" style="color:red">
				Nessun riferimento trovato per la modalità di pagamento selezionata!
			</span>
	  <% } %>
	</td>
  </tr>

</table>
</div>

<div class="Group" style="width:100%">
<table>
  <tr>
 	<td><% bp.getController().writeFormLabel(out,"tipoRapporto");%></td>
	<td><% bp.getController().writeFormInput(out,null,"tipoRapporto",false,null,"onChange=\"submitForm('doOnTipoRapportoChange')\""); %></td>
  </tr>
  <tr>
 	<td><% bp.getController().writeFormLabel(out,"tipoTrattamento");%></td>
<!-- <td><% bp.getController().writeFormInput(out,"tipoTrattamento"); %></td> -->
	<td><% bp.getController().writeFormInput(out,null,"tipoTrattamento",false,null,"onChange=\"submitForm('doOnTipoTrattamentoChange')\""); %></td>
  </tr>
</table>
</div>

<div class="Panel" style="width:100%">
<table>
  <tr>
	<td colspan=2><% JSPUtils.button(out,null,null,"Abilita conguaglio","javascript:submitForm('doAbilitaConguaglio')",null,bp.isBottoneAbilitaConguaglioEnabled(), bp.getParentRoot().isBootstrap());%></td>
  </tr>
</table>
</div>