<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%@page import="it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk"%>
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="JavaScript" src="scripts/disableRightClick.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
 <% it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP bp2=(it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP)BusinessProcess.getBusinessProcess(request); %>
<% if (bp2.isFlusso()) { %>
	<title>Gestione Distinta cassiere - Flusso Ordinativo </title>
<% } else if (bp2.isSepa()) {%>
	<title>Gestione Distinta Sepa</title>
<% } else if (bp2.isAnnulli()) {%>
	<title>Gestione Distinta Annulli</title>	
<% } else {%>
	<title>Gestione Distinta cassiere</title>
<% } %>

<body class="Form">
<% 	it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP bp = (it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>
	<% if (bp.isUoEnte() && bp.getModel()!=null && bp.getDistinteCassCollegateDet().countDetails()!=0)
			JSPUtils.tabbed(
						pageContext,
						"tab",
						new String[][] {
								{ "tabDistinta","Distinta","/doccont00/tab_distinta.jsp" },
								{ "tabDistinta_dettaglio","Mandati/Reversali","/doccont00/tab_distinta_dettaglio.jsp" },
								{ "tabDistinte_collegate_dettaglio","Distinte Collegate","/doccont00/tab_distinte_collegate_dettaglio.jsp" },
								{ "tabAllegati",bp.getParentRoot().isBootstrap() ? "<i class=\"fa fa-fw fa-file\" aria-hidden=\"true\"></i> Allegati" : "Allegati","/util00/tab_allegati.jsp" }
								},
						bp.getTab("tab"),
						"center",
						"100%",null);
	   else		
			JSPUtils.tabbed(
						pageContext,
						"tab",
						new String[][] {
								{ "tabDistinta","Distinta","/doccont00/tab_distinta.jsp" },
								{ "tabDistinta_dettaglio","Mandati/Reversali","/doccont00/tab_distinta_dettaglio.jsp" },
								{ "tabAllegati",bp.getParentRoot().isBootstrap() ? "<i class=\"fa fa-fw fa-file\" aria-hidden=\"true\"></i> Allegati" : "Allegati","/util00/tab_allegati.jsp" }
								},
						bp.getTab("tab"),
						"center",
						"100%",null);
	%>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>