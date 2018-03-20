<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*,
	        it.cnr.jada.util.action.*,
	        it.cnr.contab.utenze00.bp.*,
	        it.cnr.contab.utenze00.bulk.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript">
function apriExcel() {
  try{
    window.excel = window.open('util/download_excel.jsp', 'Excel', 'toolbar=no,width=200,height=100');
    window.excel.focus();  
    window.excel.location.reload();
    doChiudiForm();
  }
  catch (e)
  {
    doChiudiForm();
    window.alert("Attenzione Popup bloccato!");
  }    
}
</script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<% BulkListPrintBP bp = (BulkListPrintBP)BusinessProcess.getBusinessProcess(request);
   session.setAttribute("BP",bp);
   bp.openForm(pageContext,"FormAction","workspace");
   bp.closeForm(pageContext);
%>
<body class="Print">
  <form name=excel action="javascript:apriExcel();"></form>
<script>    
  document.excel.submit();
</script>
</body>
</html>