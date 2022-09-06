<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
		import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*, 
	        it.cnr.contab.config00.bp.*,
	        it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Gestione Piano dei Conti Econ./Patr. - Conto</title>
<body class="Form">

<% CRUDContoBP bp = (CRUDContoBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>
	<table class="Panel card p-2">
	<% if (!bp.isFlNuovoPdg()){%>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
            <td><% bp.getController().writeFormInputByStatus( out, "esercizio"); %></td>
        </tr>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "cd_voce_ep_padre"); %></td>
            <td>
                <% bp.getController().writeFormInput( out, "cd_voce_ep_padre"); %>
                    <% bp.getController().writeFormInput( out, "ds_voce_ep_padre"); %>
                    <% bp.getController().writeFormInput( out, "find_voce_ep_padre"); %>
                    <% bp.getController().writeFormInput( out, "creacontext"); %>
            </td>
        </tr>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "ds_gruppo"); %></td>
            <td><% bp.getController().writeFormInput( out, "ds_gruppo"); %></td>
        </tr>
	<% }%>
	<tr>
        <td><% bp.getController().writeFormLabel( out, "cd_proprio_voce_ep"); %></td>
        <td><% bp.getController().writeFormInputByStatus( out, "cd_proprio_voce_ep"); %></td>
	</tr>
	<tr>
        <td><% bp.getController().writeFormLabel( out, "ds_voce_ep"); %></td>
        <td><% bp.getController().writeFormInput( out, "ds_voce_ep"); %></td>
	</tr>
	<% if (!bp.isFlNuovoPdg()){%>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "natura_voce"); %></td>
            <td><% bp.getController().writeFormInput( out, "natura_voce"); %></td>
        </tr>
    <% }else {%>
        <tr>
          <td><% bp.getController().writeFormLabel(out,"find_classificazione_voci_ep");%></td>
          <td><% bp.getController().writeFormInput(out,"find_classificazione_voci_ep");%></td>
        </tr>
	<% }%> 
	<tr>
        <td><% bp.getController().writeFormLabel( out, "riepiloga_a"); %></td>
        <td><% bp.getController().writeFormInput( out, "riepiloga_a"); %></td>
	</tr>
	<tr>
        <td><% bp.getController().writeFormLabel( out, "ti_sezione"); %></td>
        <td><% bp.getController().writeFormInput( out, "ti_sezione"); %></td>
	</tr>
	<tr>
        <td><% bp.getController().writeFormInput( out, "fl_a_pareggio"); %></td>
        <td><% bp.getController().writeFormLabel( out, "fl_a_pareggio"); %></td>
	</tr>
	<tr>
        <td><% bp.getController().writeFormLabel( out, "conto_speciale"); %></td>
        <td><% bp.getController().writeFormInput( out, "conto_speciale"); %></td>
	</tr>
	<tr>
        <td><% bp.getController().writeFormLabel( out, "cd_riapre_a_conto"); %></td>
        <td>
            <% bp.getController().writeFormInput( out, "cd_riapre_a_conto"); %>
                <% bp.getController().writeFormInput( out, "ds_riapre_a_conto"); %>
                <% bp.getController().writeFormInput( out, "find_riapre_a_conto"); %>
        </td>
	</tr>
    <tr>
        <td><% bp.getController().writeFormLabel( out, "cd_voce_ep_contr"); %></td>
        <td>
            <% bp.getController().writeFormInput( out, "cd_voce_ep_contr"); %>
            <% bp.getController().writeFormInput( out, "ds_voce_ep_contr"); %>
            <% bp.getController().writeFormInput( out, "find_voce_ep_contr"); %>
        </td>
    </tr>
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>