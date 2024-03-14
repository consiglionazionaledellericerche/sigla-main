<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*,
			it.cnr.contab.reports.bp.*,
			it.cnr.contab.docamm00.bp.SelezionatoreFattureLiquidazioneSospesaBP"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% 
	JSPUtils.printBaseUrl(pageContext); 
    SelezionatoreFattureLiquidazioneSospesaBP bp = (SelezionatoreFattureLiquidazioneSospesaBP)BusinessProcess.getBusinessProcess(request);
%>
<title>Risultato ricerca</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<body class="Form">

<% bp.openFormWindow(pageContext); %>
<table class="Panel" width="100%" height="100%">
	<tr>
		<td>
		    <div class="card border-primary p-2">
			<fieldset>
			<% bp.writeFormLabel(out,"stato_liquidazione"); %>
			<% bp.writeFormInput(out,null,"stato_liquidazione",false,null,"onchange=\"javascript:submitForm('doCambiaVisibilita')\""); %>
			</fieldset>
			</div>
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
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>