<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.cori00.docs.bulk.*,
		it.cnr.contab.cori00.bp.*"
%>
		
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Liquidazione CORI</title>
</head>
<body class="Form"> 

<% CRUDLiquidazioneCORIBP bp = (CRUDLiquidazioneCORIBP)BusinessProcess.getBusinessProcess(request);	
	 Liquid_coriBulk liquid_cori = (Liquid_coriBulk)bp.getModel();
     bp.openFormWindow(pageContext); 

	JSPUtils.tabbed(
		pageContext,
		"tab",
		new String[][] {
			{ "tabLiquidCoriTestata","Testata","/cori00/tab_liquid_cori_testata.jsp" },			
			{ "tabLiquidCoriCapitoli","Capitoli","/cori00/tab_liquid_cori_capitoli.jsp" },			
		},
		bp.getTab("tab"),
		"center",
		"100%",
		null );
   
	bp.closeFormWindow(pageContext); 
%>
</body>
</html>