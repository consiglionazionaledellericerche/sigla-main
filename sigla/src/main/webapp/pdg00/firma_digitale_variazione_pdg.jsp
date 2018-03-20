<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*,
			it.cnr.contab.reports.bp.*,
			it.cnr.contab.pdg00.bp.FirmaDigitalePdgVariazioniBP"
%>
<html>
    <head>
        <% JSPUtils.printBaseUrl(pageContext); %>
        <title>Risultato ricerca</title>
        <script language="JavaScript" src="scripts/util.js"></script>
        <script language="javascript" src="scripts/css.js"></script>
    </head>
    <body class="Form">
    <%
        FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)BusinessProcess.getBusinessProcess(request);
        bp.openFormWindow(pageContext);
    %>
        <table>
            <tr>
                <td class="pr-2">
                    <% bp.writeFormLabel(out,"ti_signed"); %>
                </td>
                <td align="left">
                    <% bp.writeFormInput(out,null,"ti_signed",false,null,"onclick=\"javascript:submitForm('doCambiaVisibilita')\""); %>
                </td>
            </tr>
        </table>
        <table class="Panel mt-2" width="100%" height="100%">
            <tr>
                <td>
                    <% bp.writeHTMLTable(pageContext,"100%","100%"); %>
                </td>
            </tr>
            <tr valign="top">
                <td>
                    <% bp.writeHTMLNavigator(out); %>
                </td>
            </tr>
        </table>
    <%	bp.closeFormWindow(pageContext); %>
    </body>
</html>