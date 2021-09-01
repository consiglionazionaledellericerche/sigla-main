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
<title>Gestione Mandato</title>
<%  
		CRUDMandatoBP bp = (CRUDMandatoBP)BusinessProcess.getBusinessProcess(request);
%>
<script language="JavaScript">
function doVisualizzaContabile() {	
  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getContabileFileName()%>?methodName=scaricaContabile&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
function doVisualizzaMandato() {	
  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getMandatoFileName()%>?methodName=scaricaMandato&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>

<body class="Form">
<%
    bp.openFormWindow(pageContext);
    JSPUtils.tabbed(
                pageContext,
                "tab",
                bp.getTabs(),
                bp.getTab("tab"),
                "center",
                "100%","100%");
    bp.closeFormWindow(pageContext);
%>
</body>