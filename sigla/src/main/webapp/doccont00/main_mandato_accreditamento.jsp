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
<title>Gestione Mandato Accreditamento CNR - CdS</title>
<body class="Form">

<%  
		CRUDMandatoAccreditamentoBP bp = (CRUDMandatoAccreditamentoBP)BusinessProcess.getBusinessProcess(request);
		bp.openFormWindow(pageContext); 
%>
<script language="JavaScript">
function doVisualizzaContabile() {	
  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getContabileFileName()%>?methodName=scaricaContabile&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
<table class="Panel">
	<tr><td colspan=2>
	<%
			JSPUtils.tabbed(
						pageContext,
						"tab",
						new String[][] {
								{ "tabMandato","Mandato","/doccont00/tab_mandato_accreditamento.jsp" },
	/*								{ "tabSospesi","Sospesi","/doccont00/tab_mandato_sospesi.jsp" }, */
								{ "tabReversali","Reversali","/doccont00/tab_mandato_reversali.jsp" }								
								},
						bp.getTab("tab"),
						"center",
						null,null);
	%>
	</td></tr>	
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>