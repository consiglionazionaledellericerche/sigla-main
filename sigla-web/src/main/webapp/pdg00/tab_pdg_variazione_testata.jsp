<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.pdg00.bp.*,
		it.cnr.contab.pdg01.bp.*,
        it.cnr.contab.pdg00.bulk.*"
%>

<%
	PdGVariazioneBP bp = (PdGVariazioneBP)BusinessProcess.getBusinessProcess(request);
	Pdg_variazioneBulk bk = (Pdg_variazioneBulk) bp.getModel();
	boolean isFieldEnabled = !((bp.isSearching())||(bp.isCdrScrivania()&&bk.isPropostaProvvisoria()));
	boolean isFieldNonApprovaEnabled = !(bp.isSearching()||bk.isPropostaDefinitiva());
//	boolean isAbilitato = !(bp.isCdrScrivania() &&bk.isApprovata() && bp.isAbilitatoModificaDescVariazioni());
	boolean isAbilitato =( isFieldEnabled && !(bp.isCdrScrivania() &&bk.isApprovata() && bp.isAbilitatoModificaDescVariazioni()));
%>
<div class="Group">
<fieldset class="fieldset card">
	<% if (!bp.getParametriCnr().getFl_regolamento_2006().booleanValue() && bk.getDesTipoVariazione()!=null) { %>
		<legend class="GroupLabel cardLabel card-header text-primary"><% bp.getController().writeFormInput(out,null,"desTipoVariazione",true,"GroupLabel cardLabel card-header text-primary","style=\"border-style : none; cursor:default;\"");%></legend>
	<% } %> 
<table class="Panel">	
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"esercizio");%></TD>
   <TD><% bp.getController().writeFormInput(out,"esercizio");%></TD>
   <TD><% bp.getController().writeFormLabel(out,"pg_variazione_pdg");%></TD>
   <TD><% bp.getController().writeFormInput(out,"pg_variazione_pdg");%></TD> 
  </TR>
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"centro_responsabilita");%></TD>
   <TD colspan=5><% bp.getController().writeFormInput(out,"default","centro_responsabilita");%></TD>
  </TR>  
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"stato");%></TD>
   <TD colspan=2><% bp.getController().writeFormInput(out,"default","stato",isFieldEnabled,null,null);%></TD>
    <TD colspan=3><% bp.getController().writeFormLabel(out,"stato_invio");%>  
    	<% bp.getController().writeFormInput(out,"default","stato_invio",!(bp.isUoEnte()&&(bk.isPropostaDefinitiva()||bp.isSearching())),null,null);%></TD>
   </TR> 
<% if (bp instanceof CRUDPdgVariazioneGestionaleBP) {  %>
  	<TR>
   	  <TD><% bp.getController().writeFormLabel(out,"tipo_variazione");%></TD>
   	  <TD colspan=5><% bp.getController().writeFormInput(out,"tipo_variazione");%></TD>
	</TR>
	<% if (bp.isMovimentoSuFondi()) { %>
    <TR>
	  <TD><% bp.getController().writeFormLabel( out, "find_fondo_spesa"); %></TD>
	  <TD colspan=5><% bp.getController().writeFormInput( out, "find_fondo_spesa"); %></TD>
    </TR>
    <% } %>
    <TR>
      <TD><% bp.getController().writeFormLabel(out,"tipologia_fin");%></TD>
      <TD colspan=5><% bp.getController().writeFormInput(out,"tipologia_fin");%></TD>
    </TR>
<% } %>
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"dt_apertura");%></TD>
   <TD><% bp.getController().writeFormInput(out,"default","dt_apertura",isFieldEnabled,null,null);%></TD>
   <TD><% bp.getController().writeFormLabel(out,"dt_approvazione");%></TD>
   <TD><% bp.getController().writeFormInput(out,"default","dt_approvazione",isFieldEnabled,null,null);%></TD>   
  </TR>
  <TR>  
   <TD><% bp.getController().writeFormLabel(out,"dt_chiusura");%></TD>
   <TD><% bp.getController().writeFormInput(out,"default","dt_chiusura",isFieldEnabled,null,null);%></TD>
   <TD><% bp.getController().writeFormLabel(out,"dt_annullamento");%></TD>
   <TD><% bp.getController().writeFormInput(out,"default","dt_annullamento",isFieldEnabled,null,null);%></TD>      
  </TR>
<% if (!(bp instanceof CRUDPdgVariazioneGestionaleBP)) {  %>
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"ds_delibera");%></TD>
   <TD colspan=3><% bp.getController().writeFormInput(out,"default","ds_delibera",isAbilitato,null,null);%></TD>
  </TR>
<% } %>
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"ds_variazione");%></TD>
   <TD colspan=3><% bp.getController().writeFormInput(out,"default","ds_variazione",isAbilitato,null,null);%></TD>
  </TR>
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"riferimenti");%></TD>
   <TD colspan=3><% bp.getController().writeFormInput(out,"default","riferimenti",isAbilitato,null,null);%></TD>
  </TR>
</table>
<%
if ((bp.isSearching())||(bk.isRespinta())||(bk.isPropostaDefinitiva()&&bp.isUoEnte())) { 
%>
<BR>
<div class="GroupLabel">Motivazione della Mancata Approvazione</div>
<div class="Group" style="width:80%">
	<table class="Panel" align="left" cellspacing=1 cellpadding=1>
	  <tr>
        <TD><% bp.getController().writeFormLabel(out,"cd_causale_respinta");%></TD>
        <TD colspan=3><% bp.getController().writeFormInput(out,"default","cd_causale_respinta",isFieldNonApprovaEnabled,null,null);%></TD>
      </tr>
	  <tr>
        <TD><% bp.getController().writeFormLabel(out,"ds_causale_respinta");%></TD>
        <TD colspan=3><% bp.getController().writeFormInput(out,"default","ds_causale_respinta",isFieldNonApprovaEnabled,null,null);%></TD>
      </tr>
   	</table>
</div>	
<%
}
%>
</fieldset>
</div>
<% if (bp.getParent().isBootstrap()) {%><BR><% } %>
<div class="Group card">
	<table class="Panel" cellspacing=1 cellpadding=1>
	  <tr>
	     <td></td>
	     <td align=center><span class="GroupLabel">Variazioni +</span></td>
	     <td align=center><span class="GroupLabel">Variazioni -</span></td>
	     <td align=center><span class="GroupLabel">Saldo differenza</span></td>
	     <td></td>
	  </tr>
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"somma_spesa_var_piu");%></td>
         <td><% bp.getController().writeFormInput(out,"somma_spesa_var_piu");%></td>
         <td><% bp.getController().writeFormInput(out,"somma_spesa_var_meno");%></td>
         <td><% bp.getController().writeFormInput(out,"somma_spesa_diff");%></td>
		 <td><% bp.getController().writeFormInput(out,"assestatoSpese"); %></td>
      </tr>  
<% if (!(bp instanceof CRUDPdgVariazioneGestionaleBP)) { %>
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"somma_costi_var_piu");%></td>
         <td><% bp.getController().writeFormInput(out,"somma_costi_var_piu");%></td>
         <td><% bp.getController().writeFormInput(out,"somma_costi_var_meno");%></td>
         <td><% bp.getController().writeFormInput(out,"somma_costi_diff");%></td>
		 <td><% bp.getController().writeFormInput(out,"assestatoCosti"); %></td>
      </tr>  
<% } %>
      <tr><td colspan=4>&nbsp;</td></tr>
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"somma_entrata_var_piu");%></td>
         <td><% bp.getController().writeFormInput(out,"somma_entrata_var_piu");%></td>
         <td><% bp.getController().writeFormInput(out,"somma_entrata_var_meno");%></td>
         <td><% bp.getController().writeFormInput(out,"somma_entrata_diff");%></td>
		 <td><% bp.getController().writeFormInput(out,"assestatoEntrate"); %></td>
      </tr>  
<% if (!(bp instanceof CRUDPdgVariazioneGestionaleBP)) { %>
	  <tr>
         <td><% bp.getController().writeFormLabel(out,"somma_ricavi_var_piu");%></td>
         <td><% bp.getController().writeFormInput(out,"somma_ricavi_var_piu");%></td>
         <td><% bp.getController().writeFormInput(out,"somma_ricavi_var_meno");%></td>
         <td><% bp.getController().writeFormInput(out,"somma_ricavi_diff");%></td>
		 <td><% bp.getController().writeFormInput(out,"assestatoRicavi"); %></td>
      </tr>  
<% } %>
   	</table>
</div>	
<% if (bk != null && bk.getStato() != null && 
       bk.getVar_bilancio() != null && bk.getVar_bilancio().getPg_variazione() != null){ %>
<BR>
<div class="GroupLabel">Variazione al bilancio dell'Ente</div>          
<div class="Group">
	<table>      
		<tr>
			<td><% bp.getController().writeFormField(out,"pg_variazione_bilancio");%></td>
			<td><% bp.getController().writeFormInput(out,"ds_variazione_bilancio");%></td>         
		</tr>
	</table>
</div>
<%}%>
