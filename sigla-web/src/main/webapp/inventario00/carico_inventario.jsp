<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario01.bulk.*,
		it.cnr.contab.inventario01.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Carico Inventario</title>
</head>
<body class="Form">		

<% CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)BusinessProcess.getBusinessProcess(request);
   Buono_carico_scaricoBulk buonoCarico = (Buono_carico_scaricoBulk)bp.getModel(); 
   bp.openFormWindow(pageContext);
    if (buonoCarico.isPerAumentoValore() && !buonoCarico.isByFatturaPerAumentoValore()){
		JSPUtils.tabbed(
						pageContext,
						"tab",
						new String[][] {
							{ "tabCaricoInventarioTestata","Testata","/inventario00/tab_testata_buono.jsp" },
							{ "tabCaricoInventarioDettaglio","Dettaglio","/inventario00/tab_dettaglio_aumentoValore.jsp" }							
						},
						bp.getTab("tab"),
						"center",
						"100%",
						null );
	   }

   else if (bp.isTabUtilizzatoriEnabled()){	  
		JSPUtils.tabbed(
						pageContext,
						"tab",
						new String[][] {
							{ "tabCaricoInventarioTestata","Testata","/inventario00/tab_testata_buono.jsp" },
							{ "tabCaricoInventarioDettaglio","Dettaglio","/inventario00/tab_dettaglio_buono.jsp" },					
							{ "tabCaricoInventarioUtilizzatori","Utilizzatori","/inventario00/tab_utilizzatori.jsp" },
							{ "tabCaricoInventarioAmmortamento","Ammortamento","/inventario00/tab_ammortamento.jsp" }
						},
						bp.getTab("tab"),
						"center",
						"100%",
						null );
   
	   
   }   
   else{
	   JSPUtils.tabbed(
			pageContext,
			"tab",
			new String[][] {
				{ "tabCaricoInventarioTestata","Testata","/inventario00/tab_testata_buono.jsp" },
				{ "tabCaricoInventarioDettaglio","Dettaglio","/inventario00/tab_dettaglio_buono.jsp" }
			},
			bp.getTab("tab"),
			"center",
			"100%",
			null );
   }
	bp.closeFormWindow(pageContext); 
%>
</body>
</html>