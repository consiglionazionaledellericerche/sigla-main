<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.prevent01.bulk.*,
		it.cnr.contab.pdg01.bp.*,
		it.cnr.contab.pdg01.bulk.*"
%>
<body class="Form">
<%
	CRUDPdgModuloSpeseGestBP bp = (CRUDPdgModuloSpeseGestBP)BusinessProcess.getBusinessProcess(request);
	boolean isDettaglioGestionaleEnable = !bp.isDettaglioGestionaleEnable((Pdg_modulo_spese_gestBulk)bp.getCrudDettagliGestionali().getModel());
	boolean isDettagliGestionaliEnable = !bp.isDettagliGestionaliEnable((Pdg_modulo_speseBulk)bp.getModel());
    bp.getCrudDettagliGestionali().writeHTMLTable(
				pageContext,
				"insertGestionale",
				!isDettagliGestionaliEnable,
				false,
				!isDettagliGestionaliEnable&&!isDettaglioGestionaleEnable,
				"100%",
				"200px",
				true);
%>
<div class="card">
<table class="w-100">
	<tr>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"find_cdr_assegnatario");%></td>
		<td colspan=3><% bp.getCrudDettagliGestionali().writeFormInput(out,null,"find_cdr_assegnatario",isDettaglioGestionaleEnable,null,null);%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"find_linea_attivita");%></td>
		<td colspan=3><% bp.getCrudDettagliGestionali().writeFormInput( out,"default","find_linea_attivita",isDettaglioGestionaleEnable,null,null);%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"find_elemento_voce");%></td>
		<td colspan=3><% bp.getCrudDettagliGestionali().writeFormInput(out,null,"find_elemento_voce",isDettaglioGestionaleEnable,null,null);%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"im_spese_gest_decentrata_int");%></td>
		<td><% bp.getCrudDettagliGestionali().writeFormInput(out,null,"im_spese_gest_decentrata_int",isDettaglioGestionaleEnable,null,null);%></td>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"im_spese_gest_accentrata_int");%></td>
		<td><% bp.getCrudDettagliGestionali().writeFormInput(out,null,"im_spese_gest_accentrata_int",isDettaglioGestionaleEnable,null,null);%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"im_spese_gest_decentrata_est");%></td>
		<td><% bp.getCrudDettagliGestionali().writeFormInput(out,null,"im_spese_gest_decentrata_est",isDettaglioGestionaleEnable,null,null);%></td>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"im_spese_gest_accentrata_est");%></td>
		<td><% bp.getCrudDettagliGestionali().writeFormInput(out,null,"im_spese_gest_accentrata_est",isDettaglioGestionaleEnable,null,null);%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"im_pagamenti");%></td>
		<td><% bp.getCrudDettagliGestionali().writeFormInput(out,null,"im_pagamenti",isDettaglioGestionaleEnable,null,null);%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDettagliGestionali().writeFormLabel(out,"descrizione");%></td>
		<td colspan=3><% bp.getCrudDettagliGestionali().writeFormInput(out,null,"descrizione",isDettaglioGestionaleEnable,null,null);%></td>
	</tr>
</table>
</div>
</body>