<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.anagraf00.bp.*"
%>

<%
  CRUDAnagraficaBP bp = (CRUDAnagraficaBP)BusinessProcess.getBusinessProcess(request);
  UserContext uc = HttpActionContext.getUserContext(session);
%>
<%@page import="it.cnr.jada.UserContext"%>
<%@page import="javax.naming.Context"%>
<%@page import="it.cnr.contab.utenze00.bp.CNRUserContext"%>
<table>

	<% if((((it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk)bp.getModel()).isPersonaFisica())) {%>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"aliquota_fiscale");%></td>
			<td colspan="4"><% bp.getController().writeFormInput(out,"aliquota_fiscale");%></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"cd_attivita_inps");%></td>
			<td colspan="4"><% bp.getController().writeFormInput(out,"cd_attivita_inps");%>
				<% bp.getController().writeFormInput(out,"ds_attivita_inps");%>
				<% bp.getController().writeFormInput(out,"find_attivita_inps");%></td>
		</tr>		
		<tr>
			<td><% bp.getController().writeFormLabel(out,"altra_ass_previd_inps");%></td>
			<td colspan="4"><% bp.getController().writeFormInput(out,"altra_ass_previd_inps");%>
				<% bp.getController().writeFormInput(out,"ds_altra_ass_previd_inps");%>
				<% bp.getController().writeFormInput(out,"find_altra_ass_previd_inps");%></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"num_iscriz_cciaa");%></td>
			<td colspan="2"><% bp.getController().writeFormInput(out,"num_iscriz_cciaa");%></td>
			<td><% bp.getController().writeFormLabel(out,"num_iscriz_albo");%>
				<% bp.getController().writeFormInput(out,"num_iscriz_albo");%></td>
		</tr>
	<% } %>

	<tr>
		<td><% bp.getController().writeFormLabel(out,"dt_antimafia");%></td>
		<td colspan="4"><% bp.getController().writeFormInput(out,"dt_antimafia");%></td>
	</tr>

	<% if((((it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk)bp.getModel()).isPersonaFisica())) {%>
		<tr>
			<td><% bp.getController().writeFormLabel(out, "ente_appartenenza");%></td>
			<td colspan="4"><% bp.getController().writeFormInput(out, "ente_appartenenza");%>
				<% bp.getController().writeFormInput(out, "default", "descrizioneAnagrafica", true, "FormLabel", null);%>
				<% bp.getController().writeFormInput(out, "find_cd_ente_app");%></td>
		</tr>

		<tr>
			<td><% bp.getController().writeFormLabel(out,"dt_fine_rapporto");%></td>
			<td colspan="4"><% bp.getController().writeFormInput(out,null,"dt_fine_rapporto",true,null,"");%></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"causale_fine_rapporto");%></td>
			<td colspan="4"><% bp.getController().writeFormInput(out,"causale_fine_rapporto");%></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"codice_fiscale_caf");%></td>
			<td colspan="2"><% bp.getController().writeFormInput(out,"codice_fiscale_caf");%></td>
			<td><% bp.getController().writeFormLabel(out,"denominazione_caf");%>
				<% bp.getController().writeFormInput(out,"denominazione_caf");%></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"sede_inail");%></td>
			<td colspan="2"><% bp.getController().writeFormInput(out,"sede_inail");%></td>
			<td><% bp.getController().writeFormLabel(out,"matricola_inail");%>
				<% bp.getController().writeFormInput(out,"matricola_inail");%></td>
		</tr>

		<tr>
			<td><% bp.getController().writeFormLabel(out,"note");%></td>
			<td colspan="4"><% bp.getController().writeFormInput(out,"note");%></td>
		</tr>

		<tr>
		  <% if(bp.isGestiteDeduzioniIrpef(uc)) {%>
			<td><% bp.getController().writeFormLabel(out,"fl_notaxarea");%></td>
			<td colspan="2"><% bp.getController().writeFormInput(out,"fl_notaxarea");%></td>
		  <% } %>	
		  <% if(bp.isGestiteDetrazioniAltre(uc)) {%>
			<td colspan="2"><% bp.getController().writeFormLabel(out,"fl_no_detrazioni_altre");%>
				<% bp.getController().writeFormInput(out,"fl_no_detrazioni_altre");%></td>
		  <% } %>		
		</tr>
		<tr>
   		  <% if(bp.isGestiteDeduzioniFamily(uc)) {%>
			<td><% bp.getController().writeFormLabel(out,"fl_nofamilyarea");%></td>
			<td colspan="2"><% bp.getController().writeFormInput(out,"fl_nofamilyarea");%></td>
 		  <% } %>
 		  <% if(bp.isGestiteDetrazioniFamily(uc)) {%>		
			<td colspan="2"><% bp.getController().writeFormLabel(out,"fl_no_detrazioni_family");%>
			   <% bp.getController().writeFormInput(out,"fl_no_detrazioni_family");%></td>
		  <% } %>	   
		</tr>	
        <% if(bp.isGestiteDetrazioniAltre(uc)) {%>
          <tr>
			<td><% bp.getController().writeFormLabel(out,"fl_applica_detr_pers_max");%></td>
			<td colspan="2"><% bp.getController().writeFormInput(out,"fl_applica_detr_pers_max");%></td>
		  </tr>	
		<% } %>  			
		<tr>
			<td><% bp.getController().writeFormLabel(out,"im_reddito_complessivo");%></td>
			<td colspan="2"><% bp.getController().writeFormInput(out,"im_reddito_complessivo");%></td>
		</tr>				
		<% if(bp.isGestiteDeduzioniFamily(uc)) {%>
  		  <tr>
			<td><% bp.getController().writeFormLabel(out,"im_deduzione_family_area");%></td>
			<td colspan="4"><% bp.getController().writeFormInput(out,"im_deduzione_family_area");%></td>
		  </tr>
		<% } %>  
		  <% if(bp.isGestitoCreditoIrpef(uc)) {%>
          <tr>
			<td><% bp.getController().writeFormLabel(out,"fl_no_credito_irpef");%></td>
			<td colspan="2"><% bp.getController().writeFormInput(out,"fl_no_credito_irpef");%></td>
		  </tr>	
          <tr>
			<td><% bp.getController().writeFormLabel(out,"fl_no_credito_cuneo_irpef");%></td>
			<td colspan="2"><% bp.getController().writeFormInput(out,"fl_no_credito_cuneo_irpef");%></td>
		  </tr>
          <tr>
			<td><% bp.getController().writeFormLabel(out,"fl_no_detr_cuneo_irpef");%></td>
			<td colspan="2"><% bp.getController().writeFormInput(out,"fl_no_detr_cuneo_irpef");%></td>
		  </tr>
		<% } %>
		<% if(bp.isSupervisore()) {%> 
          <tr>
			<td><% bp.getController().writeFormLabel(out,"fl_detrazioni_altri_tipi");%></td>
			<td colspan="2"><% bp.getController().writeFormInput(out,"fl_detrazioni_altri_tipi");%></td>
		  </tr>	
		<% } %>    
	
	<% } %>    
</table>