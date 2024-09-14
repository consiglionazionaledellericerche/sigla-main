<%@page import="it.cnr.contab.doccont00.bp.StampaElencoProgettiBP"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa elenco Gruppo di Azioni Elementari</title>
</head>
<body class="Form">

<%	StampaElencoProgettiBP bp = (StampaElencoProgettiBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);  %>
<div class="Group card p-2" style="width:100%">
    <table>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
        <td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"flg_pdg"); %></td>
        <td><% bp.getController().writeFormInput(out,"flg_pdg"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"flg_impegno"); %></td>
        <td><% bp.getController().writeFormInput(out,"flg_impegno"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"findProgettoForPrint"); %></td>
        <td><% bp.getController().writeFormInput(out,"findProgettoForPrint"); %></td>
      </tr>

      <tr>
        <td><% bp.getController().writeFormLabel(out,"findCommessaForPrint"); %></td>
        <td><% bp.getController().writeFormInput(out,"findCommessaForPrint"); %></td>
      </tr>
        <% if (bp.getParametriCnr()!=null && !bp.getParametriCnr().getFl_nuovo_pdg()) { %>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"findModuloForPrint"); %></td>
        <td><% bp.getController().writeFormInput(out,"findModuloForPrint"); %></td>
      </tr>
       <%}%>
    </table>
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>