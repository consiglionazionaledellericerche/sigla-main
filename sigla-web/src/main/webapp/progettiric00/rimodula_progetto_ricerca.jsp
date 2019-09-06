<%@ page pageEncoding="UTF-8"
        import="it.cnr.jada.action.*,
                it.cnr.jada.bulk.*,
                it.cnr.jada.util.action.*,
                it.cnr.jada.util.jsp.*,
                it.cnr.contab.progettiric00.bp.*,
                it.cnr.contab.progettiric00.core.bulk.*"
%>

<%!     static String[][] tabs = null;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Rimodulazione Progetti</title>
</head>

<body class="Form">
<%  RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
    bp.openFormWindow(pageContext);   

	JSPUtils.tabbed(
                   pageContext,
                   "tab",
                   bp.getTabs(session),
                   bp.getTab("tab"),
                   "center",
                   "100%",
                   "100%" );
    bp.closeFormWindow(pageContext); 
%>
</body>
