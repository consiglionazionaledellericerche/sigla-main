<%@page import="it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk"%>
<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.anag.*,
		it.cnr.contab.ordmag.richieste.*"
%>


<%	CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	RichiestaUopBulk richiesta = (RichiestaUopBulk)bp.getModel();
%>

<div class="Group">
	<table class="Panel">
	  	<tr>
		 	<% bp.getController().writeFormField(out,"cdUnitaOperativa"); %>
		 	<% bp.getController().writeFormField(out,"numero"); %>
		 	<% bp.getController().writeFormField(out,"esercizio"); %>
		</tr>
	    <% if (!bp.isSearching()) { %>	 
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"tipo_doc");%>
				</td>
				<td colspan="3">
					<% bp.getController().writeFormInput(out, null,"tipo_doc",false,null,"onChange=\"submitForm('doOnTiporichiestaChange')\"");%>
				</td>
			</tr>
			<tr>
			 	<% bp.getController().writeFormField(out,"stato_cofi");%>
				<% bp.getController().writeFormField(out,"ti_associato_manrev");%>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"flagEnte");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"flagEnte",false,null,"onClick=\"submitForm('doOnFlagEnteChange')\"");%>
				</td>
			</tr>
			<tr>
		     	<td>
		      		<% bp.getController().writeFormLabel(out,"stato_pagamento_fondo_eco");%>
		      	</td>      	
		     	<td>
		      		<% bp.getController().writeFormInput(out,null,"stato_pagamento_fondo_eco",false,null,"onChange=\"submitForm('doDefault')\"");%>
		      	</td>  
		      	<% if (richiesta.getTipo_richiesta()!=null && richiesta.getTipo_richiesta().getCd_tipo_richiesta_amm()!=null && richiesta.getTipo_richiesta().getCd_tipo_richiesta_amm().compareTo(richiesta.GENERICO_S)==0) { %>
		      		<td>
				   		<% bp.getController().writeFormLabel(out,"stato_liquidazione");%>
				   		<% bp.getController().writeFormInput(out,null,"stato_liquidazione",false,null,"onChange=\"submitForm('doOnStatoLiquidazioneChange')\"");%>
				   	</td>
				   	<td> 
				   		<% bp.getController().writeFormLabel(out,"causale");%>
				   		<% bp.getController().writeFormInput(out,null,"causale",false,null,"onChange=\"submitForm('doOnCausaleChange')\"");%>
				   	</td>
				<% } %>	   	   	
			</tr>
		<% } else { %>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"tipo_doc_for_search");%>
				</td>
				<td colspan="3">
					<% bp.getController().writeFormInput(out, null,"tipo_doc_for_search",false,null,"");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"stato_cofiForSearch");%>
				</td>
				<td>
					<% boolean isInSpesaMode = (bp instanceof IrichiestaAmministrativoSpesaBP && ((IrichiestaAmministrativoSpesaBP)bp).isSpesaBP()) ? true : false;
						bp.getController().writeFormInput(out,null,"stato_cofiForSearch",isInSpesaMode,null,""); %>
				</td>
				<% bp.getController().writeFormField(out,"ti_associato_manrevForSearch");%>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"flagEnte");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"flagEnte",false,null,"onClick=\"submitForm('doOnFlagEnteChange')\"");%>
				</td>
			</tr>
			<tr>
		     	<td>
		      		<% bp.getController().writeFormLabel(out,"stato_pagamento_fondo_ecoForSearch");%>
		      	</td>      	
		     	<td>
		      		<% bp.getController().writeFormInput(out,null,"stato_pagamento_fondo_ecoForSearch", isInSpesaMode,null,""); %>
		      	</td>
		      	   	 
		      		<td> 
				   		<% bp.getController().writeFormLabel(out,"stato_liquidazione");%>
				   		<% bp.getController().writeFormInput(out,null,"stato_liquidazione",isInSpesaMode,null,"onChange=\"submitForm('doDefault')\"");%>
				   	</td>
				   	<td> 
				   		<% bp.getController().writeFormLabel(out,"causale");%>
				   		<% bp.getController().writeFormInput(out,null,"causale",false,null,"onChange=\"submitForm('doDefault')\"");%>
				   	</td>
			</tr>
		<% } %>
	</table>
</div>
<div class="Group">
	<table>
		<% if (!bp.isSearching()) { %>
			<tr>
		     	<td>
		      		<% bp.getController().writeFormLabel(out,"ti_istituz_commerc");%>
		      	</td>      	
		     	<td>
		      		<% bp.getController().writeFormInput(out,null,"ti_istituz_commerc",false,null,"onChange=\"submitForm('doOnIstituzionaleCommercialeChange')\"");%>
		      	</td>
			</tr>
			<tr>
				<% bp.getController().writeFormField(out,"data_registrazione"); %>
				<% bp.getController().writeFormField(out,"dt_scadenza");%>
			</tr>
		<% } else { %>
			<tr>
	      		<% bp.getController().writeFormField(out,"ti_istituz_commercSearch"); %>
			</tr>
			<tr>
				<% bp.getController().writeFormField(out,"data_registrazioneForSearch"); %>
				<% bp.getController().writeFormField(out,"dt_scadenza");%>
			</tr>
		<% } %>
		<tr>
			<% bp.getController().writeFormField(out,"im_totale");%>
		</tr>  
		<tr>      	
			<% bp.getController().writeFormField(out,"dt_da_competenza_coge");%>			  
			<% bp.getController().writeFormField(out,"dt_a_competenza_coge");%>			  
		</tr>
	</table>
	<table>
		<tr>
			<% bp.getController().writeFormField(out,"ds_richiesta_generico");%>
		</tr>  
	</table>
</div>
 
 <div class="Group">
    <table>
       <tr>
			<td>
				<% bp.getController().writeFormLabel(out,"valuta");%>
			</td>
			<% if (bp.isSearching()) { %>
				<td>
					<% bp.getController().writeFormInput(out,null,"valutaForSearch",false,null,"");%>
				</td>
			<% } else { %>
				<td>
					<% bp.getController().writeFormInput(out,null,"valuta",false,null,"onChange=\"submitForm('doSelezionaValuta')\"");%>
				</td>
				<td>
					<% bp.getController().writeFormLabel(out,"cambio");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"cambio",false,null,"");%>
				</td>
			<% } %>
      </tr>
    </table>
    </div>