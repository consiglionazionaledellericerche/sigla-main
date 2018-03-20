<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.contab.inventario00.docs.bulk.*,
		it.cnr.contab.inventario00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Associa Buoni Documento per aumento valore</title>
</head>
<body class="Form">		

<% AssBeneFatturaBP bp = (AssBeneFatturaBP)BusinessProcess.getBusinessProcess(request); 
   Ass_inv_bene_fatturaBulk associaz_bene_fatt = (Ass_inv_bene_fatturaBulk)bp.getModel();
   bp.openFormWindow(pageContext);


   JSPUtils.tabbed(
		pageContext,
		"tab",
		new String[][] {							
			{ "tabTestata","Testata","/inventario00/tab_ass_bene_fattura_per_aumento.jsp" },
			{ "tabDettaglio","Dettaglio","/inventario00/tab_ass_bene_fattura_per_aumentoDettDoc.jsp" }							
		},
		bp.getTab("tab"),
		"center",
		"100%",
		null );
   
	bp.closeFormWindow(pageContext); 
%>
</body>
</html>