<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.utenze00.bp.*,it.cnr.contab.utenze00.bulk.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<script>
window.top.PrintFrame=window
</script>
</head>
<% AbstractPrintBP bp = (AbstractPrintBP)BusinessProcess.getBusinessProcess(request); %>
<body class="Print">
<% bp.print(pageContext); %>
</body>
<script language="JavaScript">
	for (i = 0;i < window.top.PrintForm.elements.length;i++)
		window.top.PrintForm.elements[i].disabled=false
</script>
</html>