<%@page import="it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.incarichi00.bp.CRUDIncarichiProceduraBP"%>
<%
	CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
    Incarichi_proceduraBulk model = (Incarichi_proceduraBulk)bp.getModel(); 
%>
<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
</head>

<body class="Form">
<% bp.openFormWindow(pageContext); %>
    <div class="card p-2 mb-2">
        <table class="Panel" align="left" cellspacing=4 cellpadding=4>
          <tr>
             <% bp.getController().writeFormField(out,"esercizio");%>
             <% bp.getController().writeFormField(out,"pg_procedura");%>
             <td><% bp.getController().writeFormInput(out,null,"statoText",true,"GroupLabel text-primary h3 inputFieldReadOnly font-italic",
                        bp.getParentRoot().isBootstrap()?null:"style=\"background: #F5F5DC;background-color:transparent;border-style:none;cursor:default;font-size:16px;font-style:italic;\"");%></td>
          </tr>
        </table>
    </div>
    <% JSPUtils.tabbed(
                   pageContext,
                   "tab",
                   bp.getTabs(),
                   bp.getTab("tab"),
                   "center",
                   "100%",
                   "100%" ); %>
<% bp.closeFormWindow(pageContext); %>
</body>
</html>
