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
<title>Transito Beni da Ordini</title>
</head>
<body class="Form">		

<% CRUDTransitoBeniOrdiniBP bp = (CRUDTransitoBeniOrdiniBP)BusinessProcess.getBusinessProcess(request);
   Transito_beni_ordiniBulk bene = (Transito_beni_ordiniBulk)bp.getModel();
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
			<td>
				<% bp.getController().writeFormLabel(out,"stato"); %>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,null,"stato",true,null,null); %>
			</td>
		</tr>
	</table>
	</div> 
<%
	    JSPUtils.tabbed(
			pageContext,
			"tab",
			new String[][] {
				{ "tabInventarioBeniTestata","Bene","/inventario00/tab_bene_inv_ord.jsp" }
			},
			bp.getTab("tab"),
			"center",
			"100%",
			null );
	bp.closeFormWindow(pageContext);
%>
</body>
</html>