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
<title>Stipendi Impegni</title>
</head>

<body class="Form">
<%
	bp.openFormWindow(pageContext);
%>
	<fieldset>	
	 <table class="Panel">
    <%--  <table border="0" cellspacing="0" cellpadding="2" align=center> --%>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"esercizio");%></td>
        <td colspan="3"><% bp.getController().writeFormInput(out,"esercizio");%></td>
      </tr>      
      <tr>
        <td><% bp.getController().writeFormLabel(out,"mese");%></td>
        <td><% bp.getController().writeFormInput(out,"mese");%></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"find_stipendi_obb");%></td>
        <td><% bp.getController().writeFormInput(out,"find_stipendi_obb");%></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"im_totale");%></td>
        <td><% bp.getController().writeFormInput(out,"im_totale");%></td>
      </tr>      
      </table>
	</fieldset>
<%bp.closeFormWindow(pageContext); %>
</body>
</html> 	