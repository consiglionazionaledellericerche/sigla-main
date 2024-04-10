<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.bp.*,
		it.cnr.jada.util.jsp.*"
%>

<%
	AllegatiMultipliFatturaPassivaBP bp = (AllegatiMultipliFatturaPassivaBP)BusinessProcess.getBusinessProcess(request);
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
    <div class="card w-100 card-shadow">
        <h3 class="card-header text-truncate text-primary"><i class="fa fa-question-circle fa-fw fa-2x text-info" aria-hidden="true"></i> <%=bp.getLabel()%></h3>
        <div class="card-body p-2">
            <table class="Panel p-2 w-100">
              <% bp.getController().writeForm(out, bp.getAllegatiFormName());%>
            </table>
        </div>
   	</div>
<% bp.closeFormWindow(pageContext); %>
</body>