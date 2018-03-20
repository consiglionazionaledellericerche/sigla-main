<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.prevent00.bulk.*,
		it.cnr.contab.prevent00.action.*,
		it.cnr.contab.prevent00.bp.*"
%>

<%	CRUDDettagliEtrBilancioPrevCnrBP bp = (CRUDDettagliEtrBilancioPrevCnrBP)BusinessProcess.getBusinessProcess(request);%>

<table border="0" cellspacing="0" cellpadding="2" align=center>

	<tr><% bp.getController().writeFormField( out, "im_stanz_iniziale_a1");%></tr>
	<tr><% bp.getController().writeFormField( out, "variazioni_piu");%></tr>
	<tr><% bp.getController().writeFormField( out, "variazioni_meno");%></tr>
	<tr><td><span class="FormLabel">Saldo accertamenti</span></td><td><% bp.getController().writeFormInput( out, "im_obblig_imp_acr");%></td></tr>
	<tr><td><span class="FormLabel">Saldo reversali</span></td><td><% bp.getController().writeFormInput( out, "im_mandati_reversali");%></td></tr>		
	<tr><td><span class="FormLabel">Saldo incassi</span></td><td><% bp.getController().writeFormInput( out, "im_pagamenti_incassi");%></td></tr>				
	
</table>