<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*, 
	        it.cnr.jada.util.action.*, 
	        it.cnr.contab.utenze00.bp.*"
%>
<%  
		CRUDUtenzaBP bp = (CRUDUtenzaBP)BusinessProcess.getBusinessProcess(request);
        bp.getCrudUtente_indirizzi_mail().writeHTMLTable(pageContext,null,true,false,true,"100%","100px"); 
%>
	<table class="Form">
	<tr>
	<td><% bp.getCrudUtente_indirizzi_mail().writeFormLabel( out, "indirizzo_mail"); %></td>
	<td><% bp.getCrudUtente_indirizzi_mail().writeFormInput( out, "indirizzo_mail"); %></td>
	</tr>
	<tr>
	<td colspan=2><% bp.getCrudUtente_indirizzi_mail().writeFormLabel( out, "fl_err_appr_var_bil_cnr_res"); %>
				  <% bp.getCrudUtente_indirizzi_mail().writeFormInput( out, "fl_err_appr_var_bil_cnr_res"); %></td>
	</tr>
	<tr>
	<td colspan=2><% bp.getCrudUtente_indirizzi_mail().writeFormLabel( out, "fl_com_app_var_stanz_res"); %>
	              <% bp.getCrudUtente_indirizzi_mail().writeFormInput( out, "fl_com_app_var_stanz_res"); %></td>
	</tr>
	<tr>
	<td colspan=2><% bp.getCrudUtente_indirizzi_mail().writeFormLabel( out, "fl_err_appr_var_bil_cnr_comp"); %>
	              <% bp.getCrudUtente_indirizzi_mail().writeFormInput( out, "fl_err_appr_var_bil_cnr_comp"); %></td>
	</tr>
	<tr>
	<td colspan=2><% bp.getCrudUtente_indirizzi_mail().writeFormLabel( out, "fl_com_app_var_stanz_comp"); %>
	              <% bp.getCrudUtente_indirizzi_mail().writeFormInput( out, "fl_com_app_var_stanz_comp"); %></td>
	</tr>
	<tr>
	<td colspan=2><% bp.getCrudUtente_indirizzi_mail().writeFormLabel( out, "flEsitoPosFattElettr"); %>
	              <% bp.getCrudUtente_indirizzi_mail().writeFormInput( out, "flEsitoPosFattElettr"); %></td>
	</tr>
	<tr>
	<td colspan=2><% bp.getCrudUtente_indirizzi_mail().writeFormLabel( out, "flEsitoNegFattElettr"); %>
	              <% bp.getCrudUtente_indirizzi_mail().writeFormInput( out, "flEsitoNegFattElettr"); %></td>
	</tr>
	<tr>
	<td colspan=2><% bp.getCrudUtente_indirizzi_mail().writeFormLabel( out, "flFepNotificaRicezione"); %>
	              <% bp.getCrudUtente_indirizzi_mail().writeFormInput( out, "flFepNotificaRicezione"); %></td>
	</tr>
	</table>