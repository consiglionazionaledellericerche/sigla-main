<%@ page 
	import="it.cnr.jada.action.*,
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
<%
	bp.openFormWindow(pageContext);
%>
	<fieldset>
	 <div class="card p-2">	
	 <table class="Panel w-100">
    <%--  <table border="0" cellspacing="0" cellpadding="2" align=center> --%>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"esercizio");%></td>
        <td colspan="3"><% bp.getController().writeFormInput(out,"esercizio");%></td>
      </tr>      
      <tr>
        <td><% bp.getController().writeFormLabel(out,"find_tipo_incarico");%></td>
        <td><% bp.getController().writeFormInput(out,"find_tipo_incarico");%></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"find_tipo_attivita");%></td>
        <td><% bp.getController().writeFormInput(out,"find_tipo_attivita");%></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"tipo_natura");%></td>
        <td><% bp.getController().writeFormInput(out,"tipo_natura");%></td>
      </tr>      
      <tr>
        <td><% bp.getController().writeFormLabel(out,"find_tipo_limite");%></td>
        <td><% bp.getController().writeFormInput(out,"find_tipo_limite");%></td>
      </tr>      
      </table>
      </div>
	</fieldset>
<%bp.closeFormWindow(pageContext); %>
</body>
</html>