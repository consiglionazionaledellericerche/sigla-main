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
		it.cnr.contab.prevent00.bulk.*,
		it.cnr.contab.prevent00.action.*,
		it.cnr.contab.prevent00.bp.*"
%>

<%	CRUDDettagliSpeBilancioPrevCnrBP bp = (CRUDDettagliSpeBilancioPrevCnrBP)BusinessProcess.getBusinessProcess(request);%>

<table border="0" cellspacing="0" cellpadding="2" align=center>

	<tr><% bp.getController().writeFormField( out, "im_stanz_iniziale_a1");%></tr>
	<tr><% bp.getController().writeFormField( out, "variazioni_piu");%></tr>
	<tr><% bp.getController().writeFormField( out, "variazioni_meno");%></tr>
	<tr><% bp.getController().writeFormField( out, "im_obblig_imp_acr");%></tr>
	<tr><% bp.getController().writeFormField( out, "im_mandati_reversali");%></tr>		
	<tr><% bp.getController().writeFormField( out, "im_pagamenti_incassi");%></tr>				
	<tr><% bp.getController().writeFormField( out, "im_1210");%></tr>				
	
</table>