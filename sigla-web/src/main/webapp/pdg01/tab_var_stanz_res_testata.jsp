<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.varstanz00.bp.*,
		it.cnr.contab.varstanz00.bulk.*"
%>

<%
	CRUDVar_stanz_resBP bp = (CRUDVar_stanz_resBP)BusinessProcess.getBusinessProcess(request);
	Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)bp.getModel();	
	boolean isTableEnabled = (bp.isCdrScrivania() && var_stanz_res.isPropostaProvvisoria());
	boolean isFieldEnabled = !isTableEnabled;
//	boolean isAbilitato = !(bp.isCdrScrivania() && bp.isAbilitatoModificaDescVariazioni());
	boolean isAbilitato =( isFieldEnabled && !(bp.isCdrScrivania() && bp.isAbilitatoModificaDescVariazioni()));
%>
<table class="Panel">	
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"esercizio");%></TD>
   <TD><% bp.getController().writeFormInput(out,"esercizio");%></TD>
   <TD><% bp.getController().writeFormLabel(out,"pg_variazione");%></TD>
   <TD><% bp.getController().writeFormInput(out,"pg_variazione");%></TD> 
  </TR>
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"centroDiSpesa");%></TD>
   <TD colspan=3><% bp.getController().writeFormInput(out,"centroDiSpesa");%></TD>
  </TR>  
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"centroDiResponsabilita");%></TD>
   <TD colspan=3><% bp.getController().writeFormInput(out,"centroDiResponsabilita");%></TD>
  </TR>  
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"stato");%></TD>
   <TD colspan=3><% bp.getController().writeFormInput(out,"stato");%></TD>
  </TR>
	<tr>
		<td><% bp.getController().writeFormLabel(out,"esercizio_res"); %></td>
		<td colspan=3><% bp.getController().writeFormInput(out,"default","esercizio_res",isFieldEnabled,null,null);%></TD>
	</tr>
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"tipologia");%></TD>
   <TD colspan=3><% bp.getController().writeFormInput(out,"default","tipologia",bp.isROTipologia(),null,null);%></TD>
  </TR>
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"tipologia_fin");%></TD>
   <TD colspan=3><% bp.getController().writeFormInput(out,"tipologia_fin");%></TD>
  </TR>
  
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"dt_apertura");%></TD>
   <TD><% bp.getController().writeFormInput(out,"dt_apertura");%></TD>
   <TD><% bp.getController().writeFormLabel(out,"dt_approvazione");%></TD>
   <TD><% bp.getController().writeFormInput(out,"dt_approvazione");%></TD>   
  </TR>
  <TR>  
   <TD><% bp.getController().writeFormLabel(out,"dt_chiusura");%></TD>
   <TD><% bp.getController().writeFormInput(out,"dt_chiusura");%></TD>
   <TD><% bp.getController().writeFormLabel(out,"dt_annullamento");%></TD>
   <TD><% bp.getController().writeFormInput(out,"dt_annullamento");%></TD>      
  </TR>
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"ds_delibera");%></TD>
   <TD colspan=3><% bp.getController().writeFormInput(out,"default","ds_delibera",isAbilitato,null,null);%></TD>
  </TR>
  <TR>
   <TD><% bp.getController().writeFormLabel(out,"ds_variazione");%></TD>
   <TD colspan=3><% bp.getController().writeFormInput(out,"default","ds_variazione",isAbilitato,null,null);%></TD>
  </TR>
</table>
<% if (var_stanz_res != null && var_stanz_res.getStato() != null && 
         var_stanz_res.getStato().equalsIgnoreCase(Var_stanz_resBulk.STATO_APPROVATA)){ %>
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
