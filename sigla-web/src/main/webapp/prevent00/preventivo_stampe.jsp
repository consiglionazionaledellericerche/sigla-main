<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.prevent00.bulk.*,
		it.cnr.contab.prevent00.action.*,
		it.cnr.contab.prevent00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Preventivo finanziario CNR</title>
</head>
<body class="Form">

<%
	BilancioStampePreventivoBP bp = (BilancioStampePreventivoBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
%>

<table class="Panel">
    <tr>
		<td colspan=3> </td>
	</tr>
	<tr>
		<td colspan=3><b>Stampa spese</b>
		</td>
	</tr>
	<tr>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per capitolo", "if (disableDblClick()) javascript:submitForm('doStampaSpesaCapitolo')");%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per sottoarticolo", "if (disableDblClick()) javascript:submitForm('doStampaSpesaSottoarticolo')");%></td>
        <td></td>
	</tr>
		<td colspan=3> </td>
	<tr>

	<tr>
		<td colspan=3><b>Stampa spese (importi diversi da 0)</b>
		</td>
	</tr>
	<tr>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per capitolo", "if (disableDblClick()) javascript:submitForm('doStampaSpesaCapitoloDZ')");%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per sottoarticolo", "if (disableDblClick()) javascript:submitForm('doStampaSpesaSottoarticoloDZ')");%></td>
        <td></td>
	</tr>
		<td colspan=3> </td>
	<tr>
	</tr>
		<td colspan=3> </td>
	<tr>

	<tr>
		<td colspan=3><b>Stampa entrate</b>
		</td>
	</tr>
	<tr>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per capitolo", "if (disableDblClick()) javascript:submitForm('doStampaEntrataCapitolo')");%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per articolo", "if (disableDblClick()) javascript:submitForm('doStampaEntrataArticolo')");%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per sottoarticolo", "if (disableDblClick()) javascript:submitForm('doStampaEntrataSottoarticolo')");%></td>
	</tr>

	<tr>
		<td colspan=3> </td>
	</tr>

	<tr>
		<td colspan=3><b>Stampa entrate (importi diversi da 0)</b>
		</td>
	</tr>
	<tr>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per capitolo", "if (disableDblClick()) javascript:submitForm('doStampaEntrataCapitoloDZ')");%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per articolo", "if (disableDblClick()) javascript:submitForm('doStampaEntrataArticoloDZ')");%></td>
		<td><%JSPUtils.button(out, "img/print16.gif", "Per sottoarticolo", "if (disableDblClick()) javascript:submitForm('doStampaEntrataSottoarticoloDZ')");%></td>
	</tr>
	
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>