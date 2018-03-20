<%@ page pageEncoding="UTF-8"  session="false" import="it.cnr.jada.util.jsp.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//Dtd HTML 4.0 Transitional//EN">

<html>
	<head>
		<script language="JavaScript" src="scripts/util.js"></script>
		<% 	JSPUtils.printBaseUrl(pageContext); %>
		<script language="javascript" src="scripts/css.js"></script>
		<script>
			function loader() {
				window.top.location='<%= JSPUtils.getClusterAppRoot(request)%>'
			}
		</script>
	</head>

	<body onload="loader()">
		<a href="<%= JSPUtils.getClusterAppRoot(request)%>" target="_top">Attendere...</a>
	</body>

</html>