<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.doccont00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
	<head>
		<script language="JavaScript" src="scripts/util.js"></script>
		<% JSPUtils.printBaseUrl(pageContext);%>
	</head>
	<script language="javascript" src="scripts/css.js"></script>
	<title>Mandato di accreditamento CNR -> Cds</title>
	<body class="Form">

	<%  
			SituazioneCdSBP bp = (SituazioneCdSBP)BusinessProcess.getBusinessProcess(request);
			it.cnr.contab.doccont00.core.bulk.RicercaMandatoAccreditamentoBulk ricercaMandato = (it.cnr.contab.doccont00.core.bulk.RicercaMandatoAccreditamentoBulk)bp.getModel();
			bp.openFormWindow(pageContext); 
	%>

	<div class="Group">		
	<table class="Panel">
		<tr>
			<td><% bp.getController().writeFormLabel( out, "im_disp_cassa_CNR"); %></td>
			<td><% bp.getController().writeFormInput( out, "im_disp_cassa_CNR"); %></td>
		</tr>
	</table>
	</div>			
	<div class="Group">			
	<table class="Panel">	
		<tr>
			<td><% bp.getController().writeFormLabel( out, "dt_scadenza_obbligazioni"); %></td>
			<td><% bp.getController().writeFormInput( out, "dt_scadenza_obbligazioni"); %></td>
		</tr>
		<tr>
			<td>
				<% JSPUtils.button(out,bp.encodePath("img/find24.gif"), bp.encodePath("img/find24.gif"),"Ricerca impegni", "javascript:submitForm('doCercaObbligazioni')", true,bp.getParentRoot().isBootstrap()); %>
			</td>
			<td>
				<% JSPUtils.button(out,bp.encodePath("img/redo24.gif"),bp.encodePath("img/redo24.gif"), "Elenca tutti Cds", "javascript:submitForm('doCercaCds')",!((it.cnr.contab.doccont00.core.bulk.RicercaMandatoAccreditamentoBulk)bp.getModel()).isFlTuttiCdsCaricati(),bp.getParentRoot().isBootstrap()); %>
			</td>
		</tr>
	</table>		
	</div>					
	<table class="Panel">			
		<tr>
			<td colspan=2>
				<b ALIGN="CENTER"><font size=2>Centri di spesa</font></b>
				<%  bp.getCds().writeHTMLTable(pageContext,null,false,false,false,"100%","250px", false); %>
			</td>
		</tr>

		<tr>
				<td colspan=2> <center><% JSPUtils.button(out,bp.encodePath("img/export24.gif"),bp.encodePath("img/export24.gif"), bp.encodePath("Emetti mandati"),"javascript:submitForm('doApriMandatoWizard')" ,bp.isEmettiMandatiEnabled(),bp.getParentRoot().isBootstrap()); %>
				<% JSPUtils.button(out,bp.encodePath("img/zoom24.gif"),bp.encodePath("img/zoom24.gif"), "Visualizza entrate", "javascript:submitForm('doVisualizzaEntrate')",bp.isVisualizzaEntrateEnabled(),bp.getParentRoot().isBootstrap()); %>
				<% JSPUtils.button(out,bp.encodePath("img/zoom24.gif"),bp.encodePath("img/zoom24.gif"), "Visualizza spese", "javascript:submitForm('doVisualizzaSpese')",bp.isVisualizzaEntrateEnabled(), bp.getParentRoot().isBootstrap()); %>				
				</center></td>
		</tr>
	
	</table>
	<%	bp.closeFormWindow(pageContext); %>
</body>