<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
	it.cnr.contab.compensi00.docs.bulk.*,
	it.cnr.contab.compensi00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Conguaglio</title>
</head>
<body class="Form"> 

<%	CRUDConguaglioBP bp = (CRUDConguaglioBP)BusinessProcess.getBusinessProcess(request);
	ConguaglioBulk conguaglio = (ConguaglioBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

<table>
  <tr>
	<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	<td><% bp.getController().writeFormInput( out, "esercizio"); %></td>
	<td><% bp.getController().writeFormLabel( out, "pgConguaglioPos"); %></td>
	<td><% bp.getController().writeFormInput( out, "pgConguaglioPos"); %></td>

	<%if(conguaglio.getDt_cancellazione()!=null)
	  {%>
		<td><% bp.getController().writeFormLabel(out, "dt_cancellazione"); %></td>
		<td><% bp.getController().writeFormInput(out, "dt_cancellazione"); %></td>
	<%} 
	  else
	  {%>
	  	<td colspan="2"></td>
	<%}%>  	
	
  </tr>
</table>

<table class="Panel" width="100%">
  <tr>
	<td><% JSPUtils.tabbed(
					pageContext,
					"tab",
					new String[][] {
							{ "tabConguaglio","Testata","/compensi00/tab_conguaglio.jsp" },
							{ "tabConguaglioTerzo","Terzo","/compensi00/tab_conguaglio_terzo.jsp" },
							{ "tabConguaglioDatiEsterni","Dati Esterni","/compensi00/tab_conguaglio_dati_esterni.jsp" },
							{ "tabConguaglioDett","Conguaglio","/compensi00/tab_conguaglio_dett.jsp" }
					},
					bp.getTab("tab"),
					"center",
					"100%",
					null ); %>

	</td>
  </tr>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>