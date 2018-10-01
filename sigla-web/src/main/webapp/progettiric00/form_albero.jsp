<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.utenze00.bp.*,it.cnr.contab.utenze00.bulk.*,it.cnr.contab.progettiric00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<% SelezionatoreListaAlberoBP bp = (SelezionatoreListaAlberoBP)BusinessProcess.getBusinessProcess(request);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Progetti di Ricerca</title>
</head>
<body class="Form">
<%   bp.openFormWindow(pageContext); %>

    <table class="Panel w-100 card p-2 mb-2" width="100%">
        <tr>
            <td class="w-25">
                <% bp.writeHistoryLabel(pageContext); %>
            </td>
            <td width="100%" class="w-100">
                <% bp.writeHistoryField(pageContext,"cd_progetto"); %>
            </td>
        </tr>
    </table>
	<table class="Panel" width="100%" height="100%">
		<tr height="100%">
			<td colspan="2">
				<% bp.writeHTMLTable(pageContext,"100%","100%"); %>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<% bp.writeHTMLNavigator(out); %>
			</td>
		</tr>
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>