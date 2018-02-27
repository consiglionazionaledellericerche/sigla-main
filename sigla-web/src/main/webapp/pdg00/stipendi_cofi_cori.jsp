<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*"
%>

<%
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
%>
<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Stipendi CoFiCORI</title>
</head>

<body class="Form">
<%
	bp.openFormWindow(pageContext);
%>

	 <table class="Panel">
    <%--  <table border="0" cellspacing="0" cellpadding="2" align=center> --%>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"esercizio");%></td>
        <td colspan="3"><% bp.getController().writeFormInput(out,"esercizio");%></td>
      </tr>      
      <tr>
        <td><% bp.getController().writeFormLabel(out,"mese");%></td>
        <td><% bp.getController().writeFormInput(out,"mese");%></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"find_tipo_contributo_ritenuta");%></td>
        <td>
        	<% bp.getController().writeFormInput(out,"find_tipo_contributo_ritenuta");%>
        </td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"ti_ente_percipiente");%></td>
        <td><% bp.getController().writeFormInput(out,"ti_ente_percipiente");%></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"ammontare");%></td>
        <td><% bp.getController().writeFormInput(out,"ammontare");%></td>
      </tr>      
      <tr>
        <td><% bp.getController().writeFormLabel(out,"dt_da_competenza_coge");%></td>
        <td><% bp.getController().writeFormInput(out,"dt_da_competenza_coge");%></td>
        <td align="right"><% bp.getController().writeFormLabel(out,"dt_a_competenza_coge");%></td>
        <td><% bp.getController().writeFormInput(out,"dt_a_competenza_coge");%></td>
      </tr>
      </table>