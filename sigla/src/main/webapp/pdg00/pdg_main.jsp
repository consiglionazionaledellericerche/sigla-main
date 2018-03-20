<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.bulk.*,
		it.cnr.contab.pdg00.action.*,
		it.cnr.contab.pdg00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>  Piano di Gestione</title>
</head>
<body class="Form">

<%
	PdGPreventivoBP bp = (PdGPreventivoBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	Pdg_preventivoBulk pdg = (Pdg_preventivoBulk)bp.getModel();
    boolean isCdrSet = (pdg.getCentro_responsabilita().getCd_centro_responsabilita() != null);
%>

<table class="Panel" height="150px">
	<tr>
		<td rowspan=2><% bp.getController().writeFormLabel(out,"centro_responsabilita");%></td>
		<td rowspan=2><% bp.getController().writeFormInput(out,"default","centro_responsabilita",false,null,"if (disableDblClick()) onchange=\"submitForm('doCambiaCdR')\"");%></td>
        <td><center><%JSPUtils.button(out, "img/edit16.gif","img/edit16.gif", "Dettagli Spese", "if (disableDblClick()) javascript:submitForm('doDettagliSpePdG')",null,isCdrSet, bp.getParentRoot().isBootstrap());%></center></td>
	</tr>
	<tr>
		<td><center><%JSPUtils.button(out, "img/edit16.gif","img/edit16.gif", "Dettagli Entrate", "if (disableDblClick()) javascript:submitForm('doDettagliEtrPdG')",null,isCdrSet, bp.getParentRoot().isBootstrap());%></center></td>
		<td colspan=2><center><%JSPUtils.button(out, "img/print16.gif", "img/print16.gif", "Stampe", "if (disableDblClick()) javascript:submitForm('doApriStampe')", null,isCdrSet, bp.getParentRoot().isBootstrap());%></center></td>
	</tr>


<% if(isCdrSet) { %>
	<tr>
		<td colspan=5>
			<div class="GroupLabel">Stato del PdG</div>
			<div class="Group">
				<table>
					<tr>
						<td><% bp.getController().writeFormLabel(out,"stato");%></td>
						<td><% bp.getController().writeFormInput( out, null, "stato", bp.isROStato(), null, null);%></td>
						<td><center><%JSPUtils.button(out, "img/import24.gif", "img/import24.gif", "Cambia Stato", "if (disableDblClick()) javascript:submitForm('doModificaStato')", null, !bp.isROStato(), bp.getParentRoot().isBootstrap());%></center></td>
						<td>
							<div class="GroupLabel"><center>Discrepanze tra PdG e<BR>Aggregato</center></div>
							<div class="Group">
								<table width=90%>
									<tr>
									  <td align=left><%JSPUtils.button(out, "img/book_opened.gif", "img/book_closed.gif", "Entrata", "if (disableDblClick()) javascript:submitForm('doConsPDGEntrata')", null, !bp.isROConsPDGEntrata(), bp.getParentRoot().isBootstrap());%></td>
									  <td align=right><%JSPUtils.button(out, "img/book_opened.gif", "img/book_closed.gif", "Spesa", "if (disableDblClick()) javascript:submitForm('doConsPDGSpesa')", null, !bp.isROConsPDGSpesa(), bp.getParentRoot().isBootstrap());%></td>
									</tr>					
								</table>
							</div>
						</td>
					</tr>					
				</table>
			</div>
		</td>
<% } %>
	</tr>
</table>
<table width="100%">
	<tr>
		<td colspan="2"><hr></td>
	</tr>
	<tr>
		<td><%   if(isCdrSet) {bp.getController().writeFormLabel(out,"annotazioni");}%></td>
		<td><%   if(isCdrSet) {
		          bp.getController().writeFormInput( out, null, "annotazioni", bp.isROStato(), null, null);
		         }
		    %></td>
	</tr>
	<tr>
		<td colspan="2"><hr></td>
	</tr>
	<tr><td colspan="2">
		<center><table>
		<tr>
			<td><center><%JSPUtils.button(out, "img/edit16.gif", "img/edit16.gif", "Spese caricate da altra UO", "if (disableDblClick()) javascript:submitForm('doCostiCaricati')", null, !bp.isROStato() && isCdrSet, bp.getParentRoot().isBootstrap());%></center></td>
			<td><center><%JSPUtils.button(out, "img/edit16.gif", "img/edit16.gif", "Spese scaricate verso altro UO", "if (disableDblClick()) javascript:submitForm('doCostiScaricatiSpe')", null, true && isCdrSet, bp.getParentRoot().isBootstrap());%></center></td>
			<td><center>
				<% 
				if (pdg.getCentro_responsabilita().getLivello() != null && pdg.getCentro_responsabilita().getLivello().intValue() == 1) {
						JSPUtils.button(out, "img/edit16.gif", "img/edit16.gif", "Ribaltamento su Area", "if (disableDblClick()) javascript:submitForm('doRibaltamento')", null, !bp.isROStato(), bp.getParentRoot().isBootstrap());
				} 
				
				%>
			</center></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td><center>
				<% if (pdg.getCentro_responsabilita().getLivello() != null && pdg.getCentro_responsabilita().getLivello().intValue() == 1) {
					bp.getController().writeFormInput(out,"fl_ribaltato_su_area");
				} %>
			</center></td>
		</tr>
		<tr>
			<td><center><%JSPUtils.button(out, "img/edit16.gif", "img/edit16.gif", "Ricavi caricati da altro CdR", "if (disableDblClick()) javascript:submitForm('doEntrateFigurative')", null, !bp.isROStato() && isCdrSet, bp.getParentRoot().isBootstrap());%></center></td>
			<td><center><%JSPUtils.button(out, "img/edit16.gif", "img/edit16.gif", "Costi scaricati verso altro CdR", "if (disableDblClick()) javascript:submitForm('doCostiScaricatiEtr')", null, true && isCdrSet, bp.getParentRoot().isBootstrap());%></center></td>
		</tr>
		<tr>
			<td colspan="3"><hr></td>
		</tr>
		<tr>
			<td><center><%JSPUtils.button(out, "img/edit16.gif", "img/edit16.gif", "Scarica Dipendenti su PdG", "if (disableDblClick()) javascript:submitForm('doScaricaDipendenti')", null, !bp.isROStato() && isCdrSet, bp.getParentRoot().isBootstrap());%></center></td>
			<td><center><%JSPUtils.button(out, "img/edit16.gif", "img/edit16.gif", "Annulla Scarica Dipendenti", "if (disableDblClick()) javascript:submitForm('doAnnullaScaricaDip')", null, !bp.isROStato() && isCdrSet, bp.getParentRoot().isBootstrap());%></center></td>
			<td><center><%JSPUtils.button(out, "img/edit16.gif", "img/edit16.gif", "Eliminazione dettagli<BR>per GAE", "if (disableDblClick()) javascript:submitForm('doEliminazionePerLineaAttivita')", null, !bp.isROStato() && isCdrSet, bp.getParentRoot().isBootstrap());%></center></td>
		</tr>
		</table></center>
	</td></tr>
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>