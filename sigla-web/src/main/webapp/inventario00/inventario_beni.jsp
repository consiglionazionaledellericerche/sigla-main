<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario00.docs.bulk.*,
		it.cnr.contab.inventario00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Gestione Beni</title>
</head>
<body class="Form">		

<% CRUDInventarioBeniBP bp = (CRUDInventarioBeniBP)BusinessProcess.getBusinessProcess(request);
   Inventario_beniBulk bene = (Inventario_beniBulk)bp.getModel(); 
   bp.openFormWindow(pageContext); %>

  <div class="Group">
	<table>			
		<tr>				
			<td>
				<% bp.getController().writeFormLabel(out,"pg_inventario"); %>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,null,"pg_inventario",true,null,null); %>
			</td>
		</tr>
		<% if (bene.getNr_inventario() != null){ %>
			<tr>				 
				<td>
					<span class="FormLabel">Stato del bene:</span>
				</td>
				<td>
   				    <span class="FormLabel" style="color:blue">
					<% if (bene.isTotalmenteScaricato()){ %>
						totalmente scaricato
					<% } else { %>
						normale
					<% } 
					   if (bene.isMigrato()){ %>
						(Bene Migrato)
					<% } %>
					</span>
				</td>
			</tr>
		<% } %>		
	</table>
	</div> 
<%
   JSPUtils.tabbed(
			pageContext,
			"tab",
			bp.getTabs(),
			bp.getTab("tab"),
			"center",
			"100%",
			null );
	bp.closeFormWindow(pageContext);
%>
</body>
</html>