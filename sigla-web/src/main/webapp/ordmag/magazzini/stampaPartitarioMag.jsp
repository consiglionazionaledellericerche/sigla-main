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
</head>
<title>Stampa Consumi</title>

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>
<body class="Form">
<div class="Group">
    <table class="Panel card p-2" width="100%">
        <tr>
            <td><% bp.getController().writeFormLabel(out,"findUnitaOperativaOrd"); %></td>
            <td colspan="5"><% bp.getController().writeFormInput(out,"findUnitaOperativaOrd"); %></td>
           </tr>
       <tr>
            <td><% bp.getController().writeFormLabel(out,"findMagazzino"); %></td>
            <td colspan="5"><% bp.getController().writeFormInput(out,"findMagazzino"); %></td>
       </tr>
       <tr>
            <td><% bp.getController().writeFormLabel(out,"daDataMovimento"); %></td>
            <td><% bp.getController().writeFormInput(out,"daDataMovimento"); %></td>
            <td><% bp.getController().writeFormLabel(out,"aDataMovimento"); %></td>
            <td><% bp.getController().writeFormInput(out,"aDataMovimento"); %></td>
        </tr>
        <tr>
            <td><% bp.getController().writeFormLabel(out,"daDataCompetenza"); %></td>
            <td><% bp.getController().writeFormInput(out,"daDataCompetenza"); %></td>
            <td><% bp.getController().writeFormLabel(out,"aDataCompetenza"); %></td>
            <td><% bp.getController().writeFormInput(out,"aDataCompetenza"); %></td>
        </tr>
        <tr>
            <td><%bp.getController().writeFormLabel(out, "findDaBeneServizio");%></td>
            <td><%bp.getController().writeFormInput(out, "findDaBeneServizio");%></td>
            <td><% bp.getController().writeFormLabel(out, "findABeneServizio");%></td>
            <td><%bp.getController().writeFormInput(out, "findABeneServizio");%></td>
        </tr>
          <tr>
            <td><%bp.getController().writeFormLabel(out, "findDaFornitore");%></td>
            <td><%bp.getController().writeFormInput(out, "findDaFornitore");%></td>
            <td><% bp.getController().writeFormLabel(out, "findAFornitore");%></td>
            <td><%bp.getController().writeFormInput(out, "findAFornitore");%></td>
          </tr>
    </table>
</div>

</body>