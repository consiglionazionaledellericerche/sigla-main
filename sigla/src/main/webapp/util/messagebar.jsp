<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
	session="false"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>

<body style="background: buttonface;">
	<table cellspacing="0" cellpadding="0" style="font-family : sans-serif;font-size : 12px; height:100%; width: 100%">
			<tr>
				<td class="MessageBar" style="width: 16px;"><img id="img" width="16" height="16" src="img/spacer.gif"></td>
				<td class="MessageBar" id="message" style="width: 100%;">&nbsp;</td>
			</tr>
		</table>
</body>
<script>
window.top.MessageText = getElementById(document,"message");
window.top.MessageImg = getElementById(document,"img");
</script>
</html>