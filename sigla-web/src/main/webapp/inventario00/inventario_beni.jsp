<%@ page 
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
   // Il BENE selezionato è un bene accessorio: NON viene visualizzato il tab UTILIZZATORI
   if (bene != null && bene.isBeneAccessorio()){
	   JSPUtils.tabbed(
			pageContext,
			"tab",
			new String[][] {
				{ "tabInventarioBeniTestata","Bene","/inventario00/tab_inv_bene.jsp" },
				{ "tabInventarioBeniAmmortamento","Ammortamento","/inventario00/tab_inv_bene_ammortamento.jsp" }
			},
			bp.getTab("tab"),
			"center",
			"100%",
			null );
   }
   // Il BENE selezionato NON è un bene accessorio: viene visualizzato il tab UTILIZZATORI
   else {
	    JSPUtils.tabbed(
			pageContext,
			"tab",
			new String[][] {
				{ "tabInventarioBeniTestata","Bene","/inventario00/tab_inv_bene.jsp" },			
				{ "tabInventarioBeniUtilizzatori","Utilizzatori","/inventario00/tab_inv_bene_utilizzatori.jsp" },
				{ "tabInventarioBeniAmmortamento","Ammortamento","/inventario00/tab_inv_bene_ammortamento.jsp" }
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