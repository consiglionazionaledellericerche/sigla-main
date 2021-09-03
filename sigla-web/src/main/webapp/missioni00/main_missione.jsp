<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->
 
<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*, it.cnr.jada.action.*, it.cnr.jada.bulk.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext); %>
</head>
<script language="javascript" src="scripts/css.js"></script>
<title>Missione</title>
<body class="Form">

<%  
	CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	MissioneBulk missione = (MissioneBulk) bp.getModel();

	if(missione == null)
		missione = new MissioneBulk();	
%>
<div class="card mb-2 p-1">
    <table width="100%">
        <tr>
            <% bp.getController().writeFormField( out, "esercizio"); %>
            <% bp.getController().writeFormField( out, "pg_missione"); %>
        </tr>
        <tr>
            <% bp.getController().writeFormField( out, "cd_cds");%>
            <% bp.getController().writeFormField( out, "cd_unita_organizzativa"); %>
        </tr>
        <% if (missione.isRiportataInScrivania() && missione.isLabelRiportoToShow() && !bp.isSearching()) { %>
        <tr>
                <td colspan="4"><span class="FormLabel text-danger" style="color:red">Documento Riportato</span></td>
        </tr>
        <% } %>
    </table>
</div>
<table class="Panel" width="100%">
	<tr><td>
		<%	
				JSPUtils.tabbed(
						pageContext,
						"tab",
						bp.getTabs(),
						bp.getTab("tab"),
						"center", 
						"100%", 
						null,
						!(bp.isEditingTappa() || bp.getSpesaController().isEditingSpesa()));
		%>
	</td></tr>	
</table>

<%	bp.closeFormWindow(pageContext); %>

</body>
</html>