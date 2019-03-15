<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.cdip.bulk.*,
		it.cnr.contab.pdg00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Rendiconti Finanziari CNR per CDS</title>
</head>
<body class="Form">

<%
	StampaRendFinanziarioCNRBP bp = (StampaRendFinanziarioCNRBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>
<div class="card p-2">
    <table class="w-100">
      <tr>
        <td><% bp.getController().writeFormLabel(out,"findCds"); %></td>
        <td><% bp.getController().writeFormInput(out,"findCds"); %></td>
      </tr>
    </table>
    <table>
        <tr>
            <td colspan=2 class="text-info"><b>Stampa Entrate (importi diversi da zero)</b></td>
        </tr>
        <tr>
            <td>
                <%JSPUtils.button(out,
                    bp.getParentRoot().isBootstrap() ? "fa fa-2x fa-print" : "img/print16.gif",
                    "Per capitolo",
                    "if (disableDblClick()) javascript:submitForm('doStampaEntrateCDSPerCapitolo')",
                    "btn-outline-info btn-title",
                    bp.getParentRoot().isBootstrap());%>
            </td>
            <td>
                <%JSPUtils.button(out,
                    bp.getParentRoot().isBootstrap() ? "fa fa-2x fa-print" : "img/print16.gif",
                    "Per articolo",
                    "if (disableDblClick()) javascript:submitForm('doStampaEntrateCDSPerArticolo')",
                    "btn-outline-info btn-title",
                    bp.getParentRoot().isBootstrap());%>
            </td>
        </tr>
        <tr>
            <td colspan=2 class="text-primary"><b>Stampa Spese (importi diversi da zero)</b></td>
        </tr>
        <tr>
            <td>
                <%JSPUtils.button(out,
                    bp.getParentRoot().isBootstrap() ? "fa fa-2x fa-print" : "img/print16.gif",
                    "Per capitolo",
                    "if (disableDblClick()) javascript:submitForm('doStampaSpeseCDSPerCapitolo')",
                    "btn-outline-primary btn-title",
                    bp.getParentRoot().isBootstrap());%>
            </td>
            <td>
                <%JSPUtils.button(out,
                    bp.getParentRoot().isBootstrap() ? "fa fa-2x fa-print" : "img/print16.gif",
                    "Per articolo",
                    "if (disableDblClick()) javascript:submitForm('doStampaSpeseCDSPerArticolo')",
                    "btn-outline-primary btn-title",
                    bp.getParentRoot().isBootstrap());%>
            </td>
        </tr>
    </table>
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>