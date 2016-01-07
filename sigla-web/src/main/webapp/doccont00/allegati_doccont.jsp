<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.*,
		it.cnr.contab.doccont00.bp.*,
		it.cnr.contab.util00.bp.*"
%>
<%	
	AllegatiDocContBP bp = (AllegatiDocContBP)BusinessProcess.getBusinessProcess(request);
%>
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Allegati al <%=bp.getModel().toString()%></title>
<script language="JavaScript">
function doScaricaFile() {	
  doPrint('genericdownload/<%=bp.getNomeAllegato()%>?methodName=scaricaAllegatoGenerico&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
</head>
<body class="Form">
<%	bp.openFormWindow(pageContext); %>
<%  bp.getCrudArchivioAllegati().writeHTMLTable(pageContext,"default",true,false,true,"100%","150px"); %>  
<div class="Group">
  <table>
  	<% bp.getCrudArchivioAllegati().writeForm(out, bp.getAllegatiFormName());  %>
  </table>
</div> 
<% bp.closeFormWindow(pageContext); %>
</body>