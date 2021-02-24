<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.doccont00.bp.*,
		it.cnr.jada.util.jsp.*"
%>

<%
	AllegatiMultipliDocContBP bp = (AllegatiMultipliDocContBP)BusinessProcess.getBusinessProcess(request);
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
</head>

<body class="Form">
<% bp.openFormWindow(pageContext);%>
    <div class="card p-2 w-100">
        <h3 class="text-primary bg-white"><%=bp.getLabel()%></h3>
        <table class="Panel w-100">
          <% bp.getController().writeForm(out, "onlyinsert");%>
        </table>
   	</div>
<% bp.closeFormWindow(pageContext); %>
</body>