<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.prevent01.bulk.*,
		it.cnr.contab.prevent01.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa Situazione Sintetica Progetto</title>
</head>
<body class="Form">

<%	bp.openFormWindow(pageContext); %>

<table width=100%>
 <tr>
  <td>
	<div class="Group">
	<table>
<!-- 
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
		<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
	  </tr>	
  	  <tr>
	     <td><% bp.getController().writeFormLabel(out,"centro_responsabilita"); %></td>
		 <td><% bp.getController().writeFormInput(out,null,"centro_responsabilita",true,null,null); %></td>
	  </tr>
 -->
 	  <tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"cd_progetto");%>
		  </td>
		   <td colspan="3">	
			<% bp.getController().writeFormInput(out,null,"cd_progetto",false,null,null); %>
			<% bp.getController().writeFormInput(out,null,"ds_progetto",false,null,null); %>
			<% bp.getController().writeFormInput(out,null,"find_nodo_padre",false,null,null); %>
		  </td>
	  </tr>	  
	</table>
	</div>
   </td>
  </tr>
</table>


<% bp.closeFormWindow(pageContext); %>

</body>
</html>