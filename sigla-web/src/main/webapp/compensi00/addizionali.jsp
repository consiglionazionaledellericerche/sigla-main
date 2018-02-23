<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.compensi00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title> Addizionali Comunali </title>
</head>
<body class="Form"> 

<%	CRUDAddizionaliBP bp = (CRUDAddizionaliBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>


	<table style="width:100%">
       <tr>
    	<td>
		  <span class="FormLabel" style="color:blue">File da caricare</span>
		</td>
		<td>
			<input type="file" name="file">			
		</td>
		<td>
			<% JSPUtils.button(out,null,null,
					"Carica Aggiornamenti","javascript:submitForm('doCarica')", 
					bp.getDettagliCRUDController()==null||bp.getDettagliCRUDController().countDetails()==0,
					bp.getParentRoot().isBootstrap()); %>
		</td>
	</tr>
	
   <tr>
  	<td colspan = "4">
		  <% bp.getDettagliCRUDController().writeHTMLTable(
				pageContext,
				"default",
				false,
				false,
				true,
				null,
				"385px",
				bp.getDettagliCRUDController()!=null); %>
	</td>
  </tr>
</table>	
<% bp.closeFormWindow(pageContext); %>

</body>
</html>