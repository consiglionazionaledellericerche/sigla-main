<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        it.cnr.jada.util.action.*"
%>
<%
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
<body class="Form">

<%  
		bp.openFormWindow(pageContext); 
%>

<table class="Panel">
	<tr><td colspan=2>
	<%
	JSPUtils.tabbed(pageContext,
					"tab",
					(bp.isSearching()?new String[][] {
										{ "tabIncarichiArchivio","Archivio","/incarichi00/tab_incarichi_archivio_xml_fp.jsp" }}
									 :new String[][] {
										{ "tabIncarichiArchivio","Archivio","/incarichi00/tab_incarichi_archivio_xml_fp.jsp" },
										{ "tabIncarichiEstrazione","Estrazione","/incarichi00/tab_incarichi_estrazione_xml_fp.jsp" } }),						
						bp.getTab("tab"),
						"center",
						"100%", 
						"100%");
	%>
	</td></tr>	
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>