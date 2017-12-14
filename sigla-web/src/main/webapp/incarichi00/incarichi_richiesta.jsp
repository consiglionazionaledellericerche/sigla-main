<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
	    it.cnr.contab.incarichi00.bulk.*,
	    it.cnr.contab.incarichi00.bp.*"
	 pageEncoding="ISO-8859-1"
%>

<%
	CRUDIncarichiRichiestaBP bp = (CRUDIncarichiRichiestaBP)BusinessProcess.getBusinessProcess(request);
 	Incarichi_richiestaBulk  model = (Incarichi_richiestaBulk)bp.getModel();
%>
<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Verifica professionalità interne</title>
</head>

<body class="Form">
<% bp.openFormWindow(pageContext);%>
<% if (model.getNrContrattiAttivati().compareTo(new Integer(0))==0) {%>
<table class="Panel">
    <tr><td>
	<jsp:include page="/incarichi00/tab_incarichi_richiesta_testata.jsp"/>
	</td></tr>
</table>
<% } else {%>
<fieldset class="fieldset">
    <legend class="GroupLabel"><% bp.getController().writeFormInput(out,null,"statoText",true,"GroupLabel","style=\"background: #F5F5DC;background-color:transparent;border-style : none; cursor:default;font-size : 16px;\"");%></legend>    
	<table class="Panel">
	<tr>
		<td width="100%">
		    <table class="ToolBar" width="100%" cellspacing="0" cellpadding="2"><tr><td>
			<table class="Panel" align="left" cellspacing=4 cellpadding=4>
			  <tr>
		         <td><% bp.getController().writeFormField(out,"esercizio");%></td>
		         <td><% bp.getController().writeFormField(out,"pg_richiesta");%></td>
		      </tr>  
		   	</table>
		   	</td></tr>
		   	</table>	
		</td>
	</tr>
 	<tr><td>&nbsp;</td></tr>
	<tr>
		<td height="100%">
		   <% JSPUtils.tabbed(
		                   pageContext,
		                   "tab",
		   				   new String[][]{																
			   						{ "tabTestata","Verifica Professionalità","/incarichi00/tab_incarichi_richiesta_testata.jsp" },
			   						{ "tabIncarichiProcedura","Elenco Procedure Incarichi Associate","/incarichi00/tab_incarichi_richiesta_procedura.jsp" }},
					       bp.getTab("tab"),
		                   "center",
		                   "100%",
		                   "100%" ); %>
		</td>
	</tr>
</table>
</fieldset>
<%}%>
<% bp.closeFormWindow(pageContext); %>
</body>
</html>
