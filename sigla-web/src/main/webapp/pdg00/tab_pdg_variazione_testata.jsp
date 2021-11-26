<%@ page pageEncoding="UTF-8"
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
	boolean isFieldStoEnabled = !((bp.isSearching())||(bp.isCdrScrivania()&&bk.isPropostaProvvisoria())&& bk.isStorno()&&bk.isGestioneSpeseEnable());
	boolean isFieldNonApprovaEnabled = !(bp.isSearching()||bk.isPropostaDefinitiva());
//	boolean isAbilitato = !(bp.isCdrScrivania() &&bk.isApprovata() && bp.isAbilitatoModificaDescVariazioni());
	boolean isAbilitato =( isFieldEnabled && !(bp.isCdrScrivania() &&bk.isApprovata() && bp.isAbilitatoModificaDescVariazioni()));
%>
<fieldset class="fieldset card">
	<% if (!bp.getParametriCnr().getFl_regolamento_2006().booleanValue() && bk.getDesTipoVariazione()!=null) { %>
		<legend class="GroupLabel cardLabel card-header text-primary"><% bp.getController().writeFormInput(out,null,"desTipoVariazione",true,"GroupLabel cardLabel card-header text-primary","style=\"border-style : none; cursor:default;\"");%></legend>
	<% } %> 
<table class="card-block m-2">
  <TR>
   	<% bp.getController().writeFormField(out,"esercizio");%>
    <TD colspan="4">
    	<% bp.getController().writeFormLabel(out,"pg_variazione_pdg");%>
    	<% bp.getController().writeFormInput(out,"pg_variazione_pdg");%>
    </TD>
  </TR>
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"centro_responsabilita");%></TD>
   <TD colspan="5"><% bp.getController().writeFormInput(out,"default","centro_responsabilita");%></TD>
  </TR>  
  <TR>
   	<TD><% bp.getController().writeFormLabel(out,"stato");%></TD>
   	<TD colspan="2"><% bp.getController().writeFormInput(out,"default","stato",isFieldEnabled,null,null);%></TD>
    <TD colspan="3">
    	<% bp.getController().writeFormLabel(out,"stato_invio");%>  
    	<% bp.getController().writeFormInput(out,"default","stato_invio",!(bp.isUoEnte()&&(bk.isPropostaDefinitiva()||bp.isSearching())),null,null);%>
    </TD>
  </TR> 
<% if (bp instanceof CRUDPdgVariazioneGestionaleBP) {  %>
  	<TR>
   	  <TD><% bp.getController().writeFormLabel(out,"tipo_variazione");%></TD>
   	  <TD colspan=5><% bp.getController().writeFormInput(out,"tipo_variazione");%></TD>
	</TR>
	<% if (bp.isAttivaGestioneVariazioniTrasferimento() && (bp.isSearching() ||  
			bk!=null && bk.getTipo_variazione()!=null && bk.getTipo_variazione().getFl_variazione_trasferimento())) { %>
    <TR>
	  <TD><% bp.getController().writeFormLabel( out, "mapMotivazioneVariazione"); %></TD>
	  <% if (bp.isSearching()) { %>
		<TD colspan="2"><% bp.getController().writeFormInput(out,"default","tiMotivazioneVariazione");%></TD>
	  <% } else { %>
		<TD colspan="2"><% bp.getController().writeFormInput(out,"default","mapMotivazioneVariazione",!bp.isSearching()&&(isAbilitato||!bk.isPropostaProvvisoria()),null,null);%></TD>
	  <% } %>	  
	  
	  <% if (bp.isSearching() || bk.isMotivazioneVariazioneBandoPersonale()) {%>
		 <TD><% bp.getController().writeFormLabel( out, "idBando"); %></TD>
		 <TD><% bp.getController().writeFormInput( out, "default","idBando",!bp.isSearching()&&(isAbilitato||!bk.isPropostaProvvisoria()),null,null); %></TD>
	 	 <%	if (bk.isApprovata()||bk.isApprovazioneFormale()) { %>
		 <TD>
		 	<%
		 		bp.getController().writeFormLabel( out, "idMatricola");
		 	    bp.getController().writeFormInput( out, "default","idMatricola",!bp.isSearching()&&!(bp.isCdrScrivania()||bp.isUoEnte())||bk.getIdMatricola()!=null,null,null);
		 	%>
		 </TD>
		 <% } %>
	  <% } 
	     if (bp.isSearching() || (bk.isMotivazioneVariazioneProrogaPersonale() || bk.isMotivazioneVariazioneAltreSpesePersonale())) { %>
		 <TD <% if (!bp.isSearching()){ %>colspan="3"<% } %>>
		 	<%	
		 		bp.getController().writeFormLabel( out, "idMatricola");
	 	    	bp.getController().writeFormInput( out, "default","idMatricola",!bp.isSearching()&&(isAbilitato||!bk.isPropostaProvvisoria()),null,null);
		 	%>
		 </TD>
	  <% }%>
    </TR>
    <% } %>
	<% if (bp.isMovimentoSuFondi()) { %>
    <TR>
	  <TD><% bp.getController().writeFormLabel( out, "find_fondo_spesa"); %></TD>
	  <TD colspan="5"><% bp.getController().writeFormInput( out, "find_fondo_spesa"); %></TD>
    </TR>
    <% } %>
    <TR>
      <TD><% bp.getController().writeFormLabel(out,"tipologia_fin");%></TD>
      <TD colspan="2"><% bp.getController().writeFormInput(out,"tipologia_fin");%></TD>
      <TD><% bp.getController().writeFormLabel(out,"ds_causale");%></TD>
     <TD colspan="2">	  
    	<% bp.getController().writeFormInput(out,"default","ds_causale",isFieldStoEnabled,null,null);%>
    </TD>
    </TR>
<% } %>
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"dt_apertura");%></TD>
   <TD colspan="2"><% bp.getController().writeFormInput(out,"default","dt_apertura",isFieldEnabled,null,null);%></TD>
   <TD><% bp.getController().writeFormLabel(out,"dt_approvazione");%></TD>
   <TD colspan="2"><% bp.getController().writeFormInput(out,"default","dt_approvazione",isFieldEnabled,null,null);%></TD>   
  </TR>
  <TR>  
   <TD><% bp.getController().writeFormLabel(out,"dt_chiusura");%></TD>
   <TD colspan="2"><% bp.getController().writeFormInput(out,"default","dt_chiusura",isFieldEnabled,null,null);%></TD>
   <TD><% bp.getController().writeFormLabel(out,"dt_annullamento");%></TD>
   <TD colspan="2"><% bp.getController().writeFormInput(out,"default","dt_annullamento",isFieldEnabled,null,null);%></TD>      
  </TR>
<% if (!(bp instanceof CRUDPdgVariazioneGestionaleBP)) {  %>
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"ds_delibera");%></TD>
   <TD colspan="5"><% bp.getController().writeFormInput(out,"default","ds_delibera",isAbilitato,null,null);%></TD>
  </TR>
<% } %>
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"ds_variazione");%></TD>
   <TD colspan="5"><% bp.getController().writeFormInput(out,"default","ds_variazione",isAbilitato,null,null);%></TD>
  </TR>
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"riferimenti");%></TD>
   <TD colspan="5"><% bp.getController().writeFormInput(out,"default","riferimenti",isAbilitato,null,null);%></TD>
  </TR>
</table>
</fieldset>

<% if (bp.isUoEnte() && (bp.isSearching() || !bk.isPropostaProvvisoria())) { %>
    <% if (bp.getParent().isBootstrap()) { %><BR><% } %>
    <fieldset class="fieldset card">
        <table class="card-block m-2">
           <tr><% bp.getController().writeFormField(out,"fl_cda");%></tr>
           <tr>
                <td><% bp.getController().writeFormLabel(out,"assegnazione");%></td>
                <td><% bp.getController().writeFormInput(out,"assegnazione");
                       if (!bp.isSearching())
                       	    JSPUtils.button(out,
                       				bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-link text-success" : "img/edit16.gif",
                       				bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-link text-success" : "img/edit16.gif",
                       				null,
                       				"javascript:submitForm('doPrendiInCarico')",
                       				"btn-secondary btn-outline-secondary btn-title",
                       				true,
                       				bp.getParentRoot().isBootstrap());
		             %>
		        </td>
		   </tr>
           <tr><% bp.getController().writeFormField(out,"im_incassato");%></tr>
           <tr><% bp.getController().writeFormField(out,"note");%></tr>
        </table>
    </fieldset>
<% } %>

<% if ((bp.isSearching())||(bk.isRespinta())||(bk.isPropostaDefinitiva()&&bp.isUoEnte())) { %>
<fieldset class="fieldset card">
    <legend class="GroupLabel card-header h3 text-primary">Motivazione della Mancata Approvazione</legend>
	<table class="card-block m-2">
          <tr>
            <TD><% bp.getController().writeFormLabel(out,"cd_causale_respinta");%></TD>
            <TD colspan="5"><% bp.getController().writeFormInput(out,"default","cd_causale_respinta",isFieldNonApprovaEnabled,null,null);%></TD>
          </tr>
          <tr>
            <TD><% bp.getController().writeFormLabel(out,"ds_causale_respinta");%></TD>
            <TD colspan="5"><% bp.getController().writeFormInput(out,"default","ds_causale_respinta",isFieldNonApprovaEnabled,null,null);%></TD>
          </tr>
    </table>
</fieldset>
<% } %>

<% if (bp.getParent().isBootstrap()) {%><BR><% } %>
<fieldset class="fieldset card">
	<table class="card-block m-2">
	  <tr>
	     <td></td>
	     <td align=center><span class="GroupLabel font-weight-bold text-primary">Variazioni +</span></td>
	     <td align=center><span class="GroupLabel font-weight-bold text-primary">Variazioni -</span></td>
	     <td align=center><span class="GroupLabel font-weight-bold text-primary">Saldo differenza</span></td>
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
</fieldset>
