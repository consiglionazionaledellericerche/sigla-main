<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*"
%>

<%
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
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
        <table class="Panel w-100">
          <% bp.getController().writeForm(out, "prima");%>
        </table>
   	</div>
    <div class="card p-2 mt-1 w-100">
        <div class="row">
            <div class="col-6">
                <table class="Panel w-100">
                  <% bp.getController().writeForm(out, "seconda");%>
                </table>
            </div>
            <div class="col-6">
                <table class="Panel w-100">
                  <% bp.getController().writeForm(out, "terza");%>
                </table>
            </div>
        </div>
   	</div>
    <div class="card p-2 mt-1 w-100">
        <div class="row">
            <div class="col-6">
                <table class="Panel w-100">
                  <% bp.getController().writeForm(out, "quarta");%>
                </table>
            </div>
            <div class="col-6">
                <table class="Panel w-100">
                  <% bp.getController().writeForm(out, "quinta");%>
                </table>
            </div>
        </div>
    </div>
<% bp.closeFormWindow(pageContext); %>
</body>