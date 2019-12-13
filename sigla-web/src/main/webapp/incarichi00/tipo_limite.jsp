<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bp.*,
		it.cnr.jada.util.jsp.*"
%>

<%
CRUDConfigTipoLimiteBP bp = (CRUDConfigTipoLimiteBP)BusinessProcess.getBusinessProcess(request);
%>
<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
</head>

<body class="Form">
<%
	bp.openFormWindow(pageContext);
%>
<fieldset>
    <div class="card p-2">
        <table class="Panel w-100">
            <tr><% bp.getController().writeFormField(out,"cd_tipo_limite");%></tr>
            <tr><% bp.getController().writeFormField(out,"ds_tipo_limite");%></tr>
            <% if (bp.isEnteCNR()) { %>
                <tr><% bp.getController().writeFormField(out,"fl_cancellatoRO");%></tr>
            <% } else { %>
                <tr><% bp.getController().writeFormField(out,"fl_cancellato");%></tr>
            <% } %>
        </table>
    </div>
</fieldset>
<%bp.closeFormWindow(pageContext); %>
</body>
</html>