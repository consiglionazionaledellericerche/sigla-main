<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.utenze00.bp.*"
%>


<%  
		CRUDUtenzaBP bp = (CRUDUtenzaBP)BusinessProcess.getBusinessProcess(request);
		UtenteBulk bulk = (UtenteBulk)bp.getModel();

%>

	<%@page import="it.cnr.contab.utenze00.bulk.UtenteBulk"%>
	<div class="Group card p-2">
        <table cellspacing="0" cellpadding="2" class="w-100">
        <tr>
        <td><% bp.getController().writeFormLabel( out, "cd_utente"); %></td>
        <td><% bp.getController().writeFormInput( out, "cd_utente"); %></td>
        </tr>
        <tr>
        <td><% bp.getController().writeFormLabel( out, "cd_utente_uid"); %></td>
        <td><% bp.getController().writeFormInput( out, "cd_utente_uid"); %></td>
        </tr>
        <tr>
        <td><% bp.getController().writeFormLabel( out, "fl_autenticazione_ldap"); %></td>
        <td><% bp.getController().writeFormInputByStatus( out, "fl_autenticazione_ldap"); %></td>
        </tr>
        <tr>
        <td><% bp.getController().writeFormLabel( out, "ds_utente"); %></td>
        <td><% bp.getController().writeFormInput( out, "ds_utente"); %></td>
        </tr>
        <tr>
        <td><% bp.getController().writeFormLabel( out, "nome"); %></td>
        <td><% bp.getController().writeFormInput( out, "nome"); %></td>
        </tr>
        <tr>
        <td><% bp.getController().writeFormLabel( out, "cognome"); %></td>
        <td><% bp.getController().writeFormInput( out, "cognome"); %></td>
        </tr>
        <tr>
        <td><% bp.getController().writeFormLabel( out, "indirizzo"); %></td>
        <td><% bp.getController().writeFormInput( out, "indirizzo"); %></td>
        </tr>
        <tr>
        <td><% bp.getController().writeFormLabel( out, "dt_inizio_validita"); %></td>
        <td><% bp.getController().writeFormInput( out, "dt_inizio_validita"); %></td>
        </tr>
        <tr>
        <td><% bp.getController().writeFormLabel( out, "dt_fine_validita"); %></td>
        <td><% bp.getController().writeFormInput( out, "dt_fine_validita"); %></td>
        </tr>
        <tr>
         <% if (bulk != null && bulk.getFl_autenticazione_ldap()) {%>
        		<td><% JSPUtils.button(out,
           			bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-scissors" : bp.encodePath("img/cut24.gif"),
   					bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-scissors" : bp.encodePath("img/cut24.gif"),
        			"Riattiva utenza inutilizzata","javascript:submitForm('doResetInutilizzo')",
        			"btn-outline-danger btn-title btn-block faa-parent animated-hover",
					bp.isEditing(),bp.getParentRoot().isBootstrap()); %></td>
         <%}else{ %>  	
	        <td><% JSPUtils.button(out,
	            bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-scissors" : bp.encodePath("img/cut24.gif"),
	            bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-scissors" : bp.encodePath("img/cut24.gif"),
	            "Annulla password",
	            "javascript:submitForm('doResetPassword')",
	            "btn-outline-danger btn-title btn-block faa-parent animated-hover",
	            bp.isEditing(),
	            bp.getParentRoot().isBootstrap()); %>
	        </td>
	        <td><% bp.getController().writeFormInput( out, "fl_password_change"); %>
	                <% bp.getController().writeFormLabel( out, "fl_password_change"); %></td>
	        
         <%} %>
        </tr>
        <tr><% bp.getController().writeFormField( out, "find_cdr"); %></tr>
        <% if (bp.isCdrConfiguratoreAll(HttpActionContext.getUserContext(session))) {%>
            <tr><% bp.getController().writeFormField( out, "dipartimento"); %></tr>
        <%} %>
        <tr>
        <td><% bp.getController().writeFormLabel( out, "find_template"); %></td>
        <td>
            <% bp.getController().writeFormInput( out, "find_template"); %>
        </td>
        </tr>
        <% if (bp.isCdrConfiguratoreAll(HttpActionContext.getUserContext(session))) {%>
            <tr>
                <td><% bp.getController().writeFormLabel( out, "fl_supervisore"); %></td>
                <td><% bp.getController().writeFormInput( out, "fl_supervisore"); %></td>
            </tr>
            <tr>
                <td><% bp.getController().writeFormLabel( out, "ruolo_supervisore"); %></td>
                <td><% bp.getController().writeFormInput( out, "ruolo_supervisore"); %></td>
            </tr>
        <%} %>
        <% if (bulk != null && bulk.getFl_autenticazione_ldap()) {%>
            <tr>
                <td colspan="2" align="center">
                    <% JSPUtils.button(out,
                        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-user-circle-o" : bp.encodePath("img/properties24.gif"),
                        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-user-circle-o" : bp.encodePath("img/properties24.gif"),
                        bp.encodePath("Abilitazione accesso in SIGLA"),
                        "javascript:submitForm('doVerificaAbilitazioneUtenteLdap')",
                        "btn-outline-primary btn-title",
                        bulk!=null && bulk.getCd_utente_uid()!=null,
                        bp.getParentRoot().isBootstrap()); %>
                </td>
            </tr>
        <% } %>
        </table>
    </div>
