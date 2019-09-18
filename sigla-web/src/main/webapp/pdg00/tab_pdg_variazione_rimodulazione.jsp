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
<div class="Group card">
<fieldset class="fieldset">
	<% if (!bp.getParametriCnr().getFl_regolamento_2006().booleanValue() && bk.getDesTipoVariazione()!=null) { %>
		<legend class="GroupLabel cardLabel card-header text-primary"><% bp.getController().writeFormInput(out,null,"desTipoVariazione",true,"GroupLabel cardLabel card-header text-primary","style=\"border-style : none; cursor:default;\"");%></legend>
	<% } %> 
<BR>
<div class="GroupLabel font-weight-bold text-primary ml-2">Rimodulazione Progetto</div>
<div class="Group card p-3 m-1 w-100" style="width:100%">
	<table class="Panel w-100" cellspacing=1 cellpadding=1>
	  <tr>
        <TD><% bp.getController().writeFormLabel(out,"findProgettoRimodulato");%></TD>
	    <TD colspan="4"><% bp.getController().writeFormInput(out,"default","findProgettoRimodulato",isFieldEnabled,null,null);%></TD>      
      </tr>
	  <tr>
        <TD><% bp.getController().writeFormLabel(out,"pgGenRimodulazione");%></TD>
        <TD><% bp.getController().writeFormInput(out,"default","pgGenRimodulazione",true,null,null);%></TD>
        <TD><% bp.getController().writeFormLabel(out,"pgRimodulazione");%>
        	<% bp.getController().writeFormInput(out,"default","pgRimodulazione",true,null,null);%></TD>
      </tr>
   	</table>
</div>	
</fieldset>
</div>