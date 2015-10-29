<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.config00.sto.bulk.*,
		it.cnr.contab.prevent01.action.*,
		it.cnr.contab.prevent01.bp.*,
		it.cnr.contab.prevent01.bulk.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Piano di Gestione Preliminare</title>
</head>
<body class="Form">

<%
	CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = ((CRUDPdGAggregatoModuloBP)bp).getCrudDettagli();
	bp.openFormWindow(pageContext);
	CdrBulk cdr = (CdrBulk)bp.getModel();
	boolean pdg_selezionato = ((Pdg_moduloBulk)controller.getModel()) != null && ((Pdg_moduloBulk)controller.getModel()).getCrudStatus() == OggettoBulk.NORMAL;
%>

<div class="Group">
	<table class="Panel" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><% bp.getController().writeFormField(out,"cd_cdr_ro");%></td>
			<td><% bp.getController().writeFormField(out,"ds_cdr_ro");%></td>
		</tr>
	</table>
</div>

<div class="Group">
	<table border="0" cellspacing="0" cellpadding="0" width="80%">
		<td>
		<%	if (bp.getParametriCnr().getFl_nuovo_pdg()) 
				controller.writeHTMLTable(pageContext,"prg_liv2",true,false,true,"100%","180px");
			else
				controller.writeHTMLTable(pageContext,null,true,false,true,"100%","180px");
		%>
		</td>
	</table>
</div>

<div class="Group">
	<table border="0" cellspacing="0" cellpadding="2">
		<tr>
			<td>
			<%	if (bp.getParametriCnr().getFl_nuovo_pdg()) 
					controller.writeFormField(out,"searchtool_progetto_liv2");
				else
					controller.writeFormField(out,"searchtool_progetto");
			%>
			</td>
		</tr>
	</table>
	<table border="0" cellspacing="0" cellpadding="2">
		<tr>
			<td colspan=5>
				<div class="GroupLabel">Stato del PdG</div>
				<div class="Group">
					<table>
						<tr>
							<td><% controller.writeFormLabel(out,"cambia_stato");%></td>
							<td><% controller.writeFormInput( out, null, "cambia_stato", bp.isROStato(), null, null);%></td>
							<td><center><%JSPUtils.button(out, "img/import24.gif", "img/import24.gif", "Cambia Stato", "if (disableDblClick()) javascript:submitForm('doCambiaStato')", null, !bp.isROStato());%></center></td>
						</tr>					
					</table>
				</div>
			</td>
			<td>
				<div class="GroupLabel">Contrattazione</div>
				<div class="Group" style="width: 360; height: 56">
					<table>
						<tr>
							<td halign="middle" width="178"><%JSPUtils.button(out, "img/compressed.gif", "img/compressed.gif", "Contrattazione Entrate", "if (disableDblClick()) submitForm('doContrattazioneEntrate')","width:178; height:46",pdg_selezionato);%></td>
							<td halign="middle" width="171"><%JSPUtils.button(out, "img/transfer.gif", "img/transfer.gif", "Contrattazione Spese", "if (disableDblClick()) submitForm('doContrattazioneSpese')","width:178; height:46",pdg_selezionato);%></td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan=5>
			</td>
			<td>
				<div class="GroupLabel">Gestionale</div>
				<div class="Group" style="width: 360; height: 56">
					<table  width="361">
						<tr>
							<td halign="middle" width="178"><%JSPUtils.button(out, "img/compressed.gif", "img/compressed.gif", "Gestionale Entrate", "if (disableDblClick()) submitForm('doGestionaleEntrate')","width:178; height:46",pdg_selezionato&&bp.isGestionaleAccessibile());%></td>
							<td halign="middle" width="171"><%JSPUtils.button(out, "img/transfer.gif", "img/transfer.gif", "Gestionale Spese", "if (disableDblClick()) submitForm('doGestionaleSpese')","width:178; height:46",pdg_selezionato&&bp.isGestionaleAccessibile());%></td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>
</div>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>