<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*,
		it.cnr.jada.util.jsp.*,  
		it.cnr.contab.anagraf00.core.bulk.*"
%>

<%	CRUDAnagraficaBP bp = (CRUDAnagraficaBP)BusinessProcess.getBusinessProcess(request);
    AnagraficoBulk model = (AnagraficoBulk)bp.getModel();%>

<% bp.getCrudDichiarazioni_intento().writeHTMLTable(pageContext,null,model.isUo_ente(),false,model.isUo_ente(),"100%","150px"); %>

<table class="Form">
	  
	<tr>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"anno_dich");%></td>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"num_dich");%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"esercizio");%></td>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"progr");%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"id_dichiarazione");%></td> 
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"dt_comunicazione_dic");%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"anno_rif");%></td>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"fl_acquisti");%></td>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"fl_importazioni");%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"dt_inizio_val_dich");%></td>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"dt_fine_val_dich");%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"im_limite_sing_op");%></td>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"im_limite_op");%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"dt_ini_validita");%></td>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"dt_fin_validita");%></td>
	</tr>
</table>