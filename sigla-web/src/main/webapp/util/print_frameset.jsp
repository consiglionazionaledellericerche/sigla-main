<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.utenze00.bp.*,it.cnr.contab.utenze00.bulk.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<% AbstractPrintBP bp = (AbstractPrintBP)BusinessProcess.getBusinessProcess(request); %>
<title>Stampa</title>
  <frameset name="print_frameset" id="print_frameset" rows="51,*"> 
   <frame name="print_menu" id="print_menu" scrolling="NO" src="<%= BusinessProcess.encodeUrl(request,"util/print_menu.jsp")%>" marginwidth="0" marginheight="0" frameborder="NO">
   <frame name="print" id="print" src="<%= BusinessProcess.encodeUrl(request,"util/print.jsp")%>" marginwidth="4" marginheight="4" frameborder="YES">
  </frameset>

<%="</head>"%> 

</html>
