<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bilaterali00.bulk.Blt_accordiBulk,
		it.cnr.contab.bilaterali00.bp.CRUDBltAccordiBP"%>
<%
  CRUDBltAccordiBP bp = (CRUDBltAccordiBP)BusinessProcess.getBusinessProcess(request);
  Blt_accordiBulk  model = (Blt_accordiBulk)bp.getModel();
%>
	<fieldset class="fieldset">
		<legend class="GroupLabel">Ente Straniero</legend>
		<table>
			<tr>
				<% bp.getController().writeFormField(out,"nome_ente_str");%>
				<% bp.getController().writeFormField(out,"acronimo_ente_str");%>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"nazioneStr");%></td>
				<td colspan=3><% bp.getController().writeFormInput(out,"nazioneStr");%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"citta_ente_str");%></td>
				<td><% bp.getController().writeFormInput(out,"citta_ente_str");%></td>
				<td><% bp.getController().writeFormLabel(out,"indirizzo_ente_str");%></td>
				<td><% bp.getController().writeFormInput(out,"indirizzo_ente_str");%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"ds_nazione_eng");%></td>
				<td><% bp.getController().writeFormInput(out,"ds_nazione_eng");%></td>
				<td><% bp.getController().writeFormLabel(out,"ds_aggettivo_nazional");%></td>
				<td><% bp.getController().writeFormInput(out,"ds_aggettivo_nazional");%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"email_trasmissione_passaporto");%></td>
				<td colspan=3><% bp.getController().writeFormInput(out,"email_trasmissione_passaporto");%></td>
			</tr>
		</table>
	</fieldset>

	<fieldset class="fieldset">
		<legend class="GroupLabel">Dati Generali Accordo</legend>
		<table>
		  	<tr>
		    	<td><% bp.getController().writeFormLabel(out,"anno_ini");%></td>
		    	<td><% bp.getController().writeFormInput(out,"anno_ini");%></td>
		    	<td><% bp.getController().writeFormLabel(out,"anno_fin");%></td>
		    	<td><% bp.getController().writeFormInput(out,"anno_fin");%></td>
	      	</tr>  	
		  	<tr>
		    	<td><% bp.getController().writeFormLabel(out,"fl_conv_fiscale");%></td>
		    	<td><% bp.getController().writeFormInput(out,"fl_conv_fiscale");%></td>
		    	<td><% bp.getController().writeFormLabel(out,"num_articolo_prop_intel");%></td>
		    	<td><% bp.getController().writeFormInput(out,"num_articolo_prop_intel");%></td>
	      	</tr>  	
		  	<tr>
		    	<td><% bp.getController().writeFormLabel(out,"fl_pagamento_ente");%></td>
		    	<td><% bp.getController().writeFormInput(out,"fl_pagamento_ente");%></td>
				<td><% bp.getController().writeFormLabel(out,"cd_terzo_ente");%></td>
				<td><% bp.getController().writeFormInput(out,"cd_terzo_ente");%>
				    <% bp.getController().writeFormInput(out,"ds_terzo_ente");%>
				    <% bp.getController().writeFormInput(out,"find_terzo_ente");%>
				</td>
			</tr>
		  	<tr>
		    	<td><% bp.getController().writeFormLabel(out,"fl_lettera_invito");%></td>
		    	<td><% bp.getController().writeFormInput(out,"fl_lettera_invito");%></td>
		    	 	<td><% bp.getController().writeFormLabel(out,"fl_atti_amministrativi");%></td>
		    	<td><% bp.getController().writeFormInput(out,"fl_atti_amministrativi");%></td>
		   
		    </tr>  	
	      	<tr>
		    	<td><% bp.getController().writeFormLabel(out,"data_firma_accordo");%></td>
		    	<td><% bp.getController().writeFormInput(out,"data_firma_accordo");%></td>
		    	<td><% bp.getController().writeFormLabel(out,"data_firma_addendum");%></td>
		    	<td><% bp.getController().writeFormInput(out,"data_firma_addendum");%></td>
	      	</tr>  	
			<tr>
				<td><% bp.getController().writeFormLabel(out,"num_prot_dec_contr");%></td>
				<td><% bp.getController().writeFormInput(out,"num_prot_dec_contr");%></td>
				<td><% bp.getController().writeFormLabel(out,"data_prot_dec_contr");%></td>
				<td><% bp.getController().writeFormInput(out,"data_prot_dec_contr");%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"num_prot_parere_commissione");%></td>
				<td><% bp.getController().writeFormInput(out,"num_prot_parere_commissione");%></td>
				<td><% bp.getController().writeFormLabel(out,"dt_prot_parere_commissione");%></td>
				<td><% bp.getController().writeFormInput(out,"dt_prot_parere_commissione");%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"num_prot_lista_congiunta");%></td>
				<td><% bp.getController().writeFormInput(out,"num_prot_lista_congiunta");%></td>
				<td><% bp.getController().writeFormLabel(out,"dt_prot_lista_congiunta");%></td>
				<td><% bp.getController().writeFormInput(out,"dt_prot_lista_congiunta");%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"find_elemento_voce");%></td>
				<td colspan=3><% bp.getController().writeFormInput(out,"find_elemento_voce");%></td>
			</tr>
		</table>
	</fieldset>

	<fieldset class="fieldset">
		<legend class="GroupLabel">Responsabile Italiano</legend>
		<table width="70%">
			<tr>
				<td><% bp.getController().writeFormLabel(out,"find_responsabile_ita");%></td>
				<td colspan=3><% bp.getController().writeFormInput(out,"find_responsabile_ita");%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"telef_respons_ita");%></td>
				<td><% bp.getController().writeFormInput(out,"telef_respons_ita");%></td>
				<td><% bp.getController().writeFormLabel(out,"fax_respons_ita");%></td>
				<td><% bp.getController().writeFormInput(out,"fax_respons_ita");%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"email_respons_ita");%></td>
				<td colspan=3><% bp.getController().writeFormInput(out,"email_respons_ita");%></td>
			</tr>
		</table>
	</fieldset>

	<fieldset class="fieldset">
		<legend class="GroupLabel">Informazioni Economiche Italiani all'Estero</legend>
		<table>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"importo_diaria_ita");%>
				<% bp.getController().writeFormInput(out,"importo_diaria_ita");%></td>
				<td><% bp.getController().writeFormLabel(out,"importo_mensile_ita");%>
				<% bp.getController().writeFormInput(out,"importo_mensile_ita");%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"im_diaria_contrib_sogg_ita");%></td>
				<td><% bp.getController().writeFormInput(out,"im_diaria_contrib_sogg_ita");%></td>
				<td><% bp.getController().writeFormLabel(out,"im_mensile_contrib_sogg_ita");%></td>
				<td><% bp.getController().writeFormInput(out,"im_mensile_contrib_sogg_ita");%></td>
			</tr>
		    <tr>
				<td><% bp.getController().writeFormLabel(out,"find_divisa_ita"); %></td>
				<td colspan=3><% bp.getController().writeFormInput(out,"find_divisa_ita"); %></td>
		    </tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"spese_viaggio_ita");%></td>
				<td><% bp.getController().writeFormInput(out,"spese_viaggio_ita");%></td>
				<td><% bp.getController().writeFormLabel(out,"fl_rimborso_treno");%>
					<% bp.getController().writeFormInput(out,"fl_rimborso_treno");%></td>
				<td><% bp.getController().writeFormLabel(out,"fl_spese_visto");%>
					<% bp.getController().writeFormInput(out,"fl_spese_visto");%></td>
				<td><% bp.getController().writeFormLabel(out,"fl_viaggi_interni");%></td>
		    	<td><% bp.getController().writeFormInput(out,"fl_viaggi_interni");%></td>
			</tr>
		</table>
	</fieldset>

	<fieldset class="fieldset">
		<legend class="GroupLabel">Responsabile Straniero</legend>
		<table width="70%">
			<tr>
				<% bp.getController().writeFormField(out,"cognome_respons_str");%>
				<% bp.getController().writeFormField(out,"nome_respons_str");%>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"telef_respons_str");%></td>
				<td><% bp.getController().writeFormInput(out,"telef_respons_str");%></td>
				<td><% bp.getController().writeFormLabel(out,"fax_respons_str");%></td>
				<td><% bp.getController().writeFormInput(out,"fax_respons_str");%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"email_respons_str");%></td>
				<td colspan=3><% bp.getController().writeFormInput(out,"email_respons_str");%></td>
			</tr>
		</table>
	</fieldset>

	<fieldset class="fieldset">
		<legend class="GroupLabel">Informazioni Economiche Stranieri in Italia</legend>
		<table width="70%">
			<tr>
				<td><% bp.getController().writeFormLabel(out,"importo_diaria_str");%></td>
				<td><% bp.getController().writeFormInput(out,"importo_diaria_str");%></td>
				<td><% bp.getController().writeFormLabel(out,"importo_mensile_str");%></td>
				<td><% bp.getController().writeFormInput(out,"importo_mensile_str");%></td>
				<td><% bp.getController().writeFormLabel(out,"diaria_lungo_periodo");%></td>
				<td><% bp.getController().writeFormInput(out,"diaria_lungo_periodo");%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"prc_anticipo");%></td>
				<td><% bp.getController().writeFormInput(out,"prc_anticipo");%></td>
				<td><% bp.getController().writeFormLabel(out,"importo_max_anticipo");%></td>
				<td><% bp.getController().writeFormInput(out,"importo_max_anticipo");%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"prc_oneri_fiscali");%></td>
				<td><% bp.getController().writeFormInput(out,"prc_oneri_fiscali");%></td>
				<td><% bp.getController().writeFormLabel(out,"prc_oneri_contributivi");%></td>
				<td><% bp.getController().writeFormInput(out,"prc_oneri_contributivi");%></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel(out,"fl_salta_convenzione");%></td>
		    	<td><% bp.getController().writeFormInput(out,"fl_salta_convenzione");%></td>
			</tr>
		</table>
	</fieldset>
