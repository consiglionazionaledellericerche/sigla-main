<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Causale Annullamento Consegna </title>
</head>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
   bp.openFormWindow(pageContext);%>
	<div class="Group">	
		<table class="Panel">
			<% bp.getController().writeForm(out); %>
		</table>
	</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>