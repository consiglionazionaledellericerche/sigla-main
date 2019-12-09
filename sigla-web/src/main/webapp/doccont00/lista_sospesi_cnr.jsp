<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.doccont00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext);%>
</head>
<script language="javascript" src="scripts/css.js"></script>

<% ListaSospesiCNRBP bp = (ListaSospesiCNRBP)BusinessProcess.getBusinessProcess(request); %>

<title>Modifica stato Sospesi CNR</title>

<body class="Form">

<% bp.openFormWindow(pageContext); %>
<table class="Panel" width="100%" height="100%">
	<tr>
		<td>
			<fieldset>
				<% bp.writeFormLabel(out,"statoTextForSearch"); %>
				<% bp.writeFormInput(out,null,"statoTextForSearch",false,null,"onclick=\"javascript:submitForm('doCambiaVisibilita')\""); %>
			</fieldset>
		</td>
	</tr>
	<%	if (!bp.getParentRoot().isBootstrap()) { %>
        <tr height="100%">
            <td colspan="4">
                <% bp.writeHTMLTable(pageContext,"100%","100%"); %>
            </td>
        </tr>
        <tr>
            <td colspan="4">
                <% bp.writeHTMLNavigator(out); %>
            </td>
        </tr>
	<% } %>
</table>
<%	if (bp.getParentRoot().isBootstrap()) {
        bp.writeHTMLTable(pageContext,"100%","65vh");
        bp.writeHTMLNavigator(out);
    }
%>
<%bp.closeFormWindow(pageContext); %>
</body>