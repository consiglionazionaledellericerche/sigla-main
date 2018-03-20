<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.*,		
		it.cnr.contab.compensi00.bp.*,
		it.cnr.contab.compensi00.docs.bulk.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*"
%>

<% CRUDConguaglioBP bp = (CRUDConguaglioBP)BusinessProcess.getBusinessProcess(request);
	ConguaglioBulk conguaglio = (ConguaglioBulk)bp.getModel(); 
	UserContext uc = HttpActionContext.getUserContext(session);%>

<div class="Group" style="width:100%">
<table>

  <% if (bp.isSearching()) { %>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_registrazioneForSearch"); %></td>
	<td colspan=3><% bp.getController().writeFormInput(out,"dt_registrazioneForSearch"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"dt_da_competenza_cogeForSearch"); %></td>
	<td><% bp.getController().writeFormInput(out,"dt_da_competenza_cogeForSearch"); %>
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
	<td><% bp.getController().writeFormInput(out,"dt_da_competenza_coge"); %>
		<% bp.getController().writeFormLabel(out,"dt_a_competenza_coge"); %>
		<% bp.getController().writeFormInput(out,"dt_a_competenza_coge"); %></td>
  </tr>
  <% } %>  

  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_conguaglio"); %></td>
	<td colspan=3><% bp.getController().writeFormInput(out,"ds_conguaglio"); %></td>
  </tr>
</table>

<table>
<% if(bp.isGestiteDeduzioniIrpef(uc)) {%>
  <tr>   
	<td>
		<% bp.getController().writeFormLabel(out,"fl_escludi_qvaria_deduzione");%>
		<% bp.getController().writeFormInput(out,null,"fl_escludi_qvaria_deduzione",false,null,"onclick=\"submitForm('doOnFlagChange')\"");%>		
	</td>
	<td>
		<% bp.getController().writeFormLabel(out,"fl_intera_qfissa_deduzione"); %>
		<% bp.getController().writeFormInput(out,null,"fl_intera_qfissa_deduzione",false,null,"onclick=\"submitForm('doOnFlagChange')\"");%>				
	</td>		
  </tr>
<% } %>
  <tr>
	<td colspan=2>
		<% bp.getController().writeFormLabel(out,"fl_accantona_add_terr"); %>	
		<% bp.getController().writeFormInput(out,null,"fl_accantona_add_terr",false,null,"onclick=\"submitForm('doOnFlagChange')\"");%>						
	</td>		
  </tr>  
<% if(bp.isGestiteDetrazioniFamily(uc)) {%>  
  <tr>
	<td colspan=2>
		<% bp.getController().writeFormLabel(out,"fl_detrazioni_fam_intero_anno"); %>	
		<% bp.getController().writeFormInput(out,null,"fl_detrazioni_fam_intero_anno",false,null,"onclick=\"submitForm('doOnFlagChange')\"");%>						
	</td>		
  </tr>
<% } %>  
</table>

</div>

<fieldset>
<legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px; color:blue">Compenso associato</legend>
<div class="Panel" style="width:100%">
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_cds_compenso"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_cds_compenso"); %></td>
	<td><% bp.getController().writeFormLabel(out,"esercizio_compenso"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio_compenso"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"cd_uo_compenso"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_uo_compenso"); %></td>
	<td><% bp.getController().writeFormLabel(out,"pg_compenso"); %></td>
	<td><% bp.getController().writeFormInput(out,"pg_compenso"); %></td>
  </tr>

<div class="Panel" style="width:100%">
<table>
  <tr>
	<td><% JSPUtils.button(out,null,null,"Visualizza Compenso","javascript:submitForm('doVisualizzaCompenso')",null,bp.isBottoneVisualizzaCompensoEnabled(), bp.getParentRoot().isBootstrap());%></td>
  </tr>
</table>
</div>

</table>
</div>
</fieldset>