<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.utenze00.bp.*,it.cnr.contab.utenze00.bulk.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script>
function doStampa() {
	if (confirm('Vuoi continuare?')) {
		window.top.PrintFrame.focus();
		window.top.PrintFrame.print();
	}
}
</script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<body style="background:buttonface">
<% AbstractPrintBP bp = (AbstractPrintBP)BusinessProcess.getBusinessProcess(request);
		bp.openForm(pageContext,bp.getDefaultAction(),"workspace"); %>
<%		bp.writeToolbar(out); %>
<%		bp.closeForm(pageContext); %>
</body>
</html>