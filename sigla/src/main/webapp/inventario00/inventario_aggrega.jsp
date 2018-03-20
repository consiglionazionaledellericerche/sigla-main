<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario01.bulk.*,
		it.cnr.contab.inventario01.bp.*"
%>
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Buono Carico Inventario</title>
</head>
<body class="Form">
<% CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)BusinessProcess.getBusinessProcess(request);
	Buono_carico_scaricoBulk buonoCarico = (Buono_carico_scaricoBulk)bp.getModel();
	Buono_carico_scarico_dettBulk riga_inventario = (Buono_carico_scarico_dettBulk) bp.getRigheInventarioDaFattura().getModel();
	 bp.openFormWindow(pageContext); %>
	
	<% bp.getDettagliFattura().writeHTMLTable(pageContext,"inventarioSet",false,false,false,"100%","200px"); %>	
  	<div class="Group">	
	<table>			
		<tr>
			<td>
				<% bp.getDettagliFattura().writeFormLabel(out,"ds_bene_servizio"); %>
				<% bp.getDettagliFattura().writeFormInput(out,"ds_bene_servizio"); %>
			</td>
			<td>	
				<% bp.getDettagliFattura().writeFormLabel(out,"quantita"); %>
 				<% bp.getDettagliFattura().writeFormInput(out,null,"quantita",true,null,null); %>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<% bp.getRigheInventarioDaFattura().writeFormLabel(out,"gruppi"); %>	
				<% if (bp.isNumGruppiErrato()){ %>
					<input type=text name="groups" class=null maxLength=3 size=10 style="background: coral">
				<% } else { %>
					<input type=text name="groups" class=null maxLength=3 size=10 style="background: #F5F5DC">
				<% } %>
			</td>
			
		</tr>
	</table>
	<table>
		<tr>	
			<td>
				<% JSPUtils.button(out,null,"Crea Gruppi","javascript:submitForm('doCreaGruppi')", bp.getParentRoot().isBootstrap()); %>
			</td>
		</tr>
	</table>
	</div>
	<div class="Group">	
	<% bp.getRigheInventarioDaFattura().writeHTMLTable(pageContext,"righeSetDaFattura",false,false,false,"100%","200px"); %>
	<table>		
		<tr>
			<td>
				<% bp.getRigheInventarioDaFattura().writeFormLabel(out,"ds_bene"); %>
			</td>
			<td>
				<% bp.getRigheInventarioDaFattura().writeFormInput(out,"ds_bene"); %>
			</td>
			<td>
				<% bp.getRigheInventarioDaFattura().writeFormLabel(out,"quantita"); %>
			</td>
			<td>
				<% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"quantita",(!bp.isQuantitaEnabled()),null,null); %>
			</td>			
		</tr>
		<tr>
			<td>
				<% bp.getRigheInventarioDaFattura().writeFormLabel(out,"fl_accessorio_contestuale"); %>
			</td>
			<td colspan = "3">
				<% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"fl_accessorio_contestuale",bp.isEditing(),null,(riga_inventario != null && riga_inventario.isAccessorioContestuale()?"onClick=\"submitForm('doDeselezionaAccessoriContestualiByFattura')\"":"onClick=\"submitForm('doFindAccessoriContestualiByFattura')\"")); %>																															
			</td>		
		</tr>		
		<% if (riga_inventario != null && riga_inventario.isAccessorioContestuale()){ %>		
			<tr>
				<td colspan="4">
					<% bp.getRigheInventarioDaFattura().writeFormLabel(out,"ds_bene_principale_contestuale"); %>
					<% bp.getRigheInventarioDaFattura().writeFormInput(out,null,"ds_bene_principale_contestuale",true,null,null); %>
				</td>
			</tr>
		<% } %>
		<tr>
			<td colspan="4">
				<% JSPUtils.button(out,null,"Crea Dettagli","javascript:submitForm('doCreaDettagli')", bp.getParentRoot().isBootstrap()); %>
			</td>
		</tr>
	</table>
	</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>