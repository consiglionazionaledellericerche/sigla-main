<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.anagraf00.core.bulk.*"
%>

<%	CRUDAnagraficaBP bp = (CRUDAnagraficaBP)BusinessProcess.getBusinessProcess(request);%>

<% bp.getCrudDichiarazioni_intento().writeHTMLTable(pageContext,null,true,false,true,"100%","100px"); %>

<table class="Form">
	<tr>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"esercizio");%></td>
<!--		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"id_dichiarazione");%></td> -->
	</tr>
	<tr>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"dt_comunicazione_dic");%></td>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"dt_comunicazione_rev");%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"dt_ini_validita");%></td>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"dt_fin_validita");%></td>
	</tr>
	<tr>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"dt_inserimento_registro");%></td>
		<td><% bp.getCrudDichiarazioni_intento().writeFormField(out,"dt_revoca_registro");%></td>
	</tr>
</table>