<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	it.cnr.contab.compensi00.docs.bulk.*,
	it.cnr.contab.compensi00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title> Bonus </title>
</head>
<body class="Form"> 
<%
   CRUDBonusBP bp = (CRUDBonusBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	BonusBulk bonus=(BonusBulk)bp.getModel();
%>
	<table>
	<tr>
	<td><% bp.getController().writeFormField(out,"esercizio");%></td>
	<td><% bp.getController().writeFormField(out,"pg_bonus");%></td>
	<td><% bp.getController().writeFormField(out,"dt_registrazione");%></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormField(out,"dt_richiesta");%></td>
	<td><% bp.getController().writeFormField(out,"codice_fiscale");%></td>
	<td><% bp.getController().writeFormField(out,"cd_terzo");%></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormField(out,"cognome");%></td>
	<td><% bp.getController().writeFormField(out,"nome");%></td>
	</tr>
	
	<tr>
	<td><%bp.getController().writeFormField(out,"ti_sesso");%></td>
	<td><%bp.getController().writeFormField(out,"dt_nascita");%></td>
	<td><%bp.getController().writeFormField(out,"ds_comune");%></td>
	<td><%bp.getController().writeFormField(out,"cd_provincia");%></td>
	</tr>
	
	<tr>
	<td><%bp.getController().writeFormField(out,"im_reddito");%></td>
	<td><%bp.getController().writeFormField(out,"im_reddito_nucleo_f");%></td>
	<td><%bp.getController().writeFormField(out,"im_bonus");%></td>
	</tr>
	<tr>
	<td><%bp.getController().writeFormField(out,"esercizio_imposta");%></td>
	<td><%bp.getController().writeFormField(out,"fl_trasmesso");%></td>
	</tr>
	</table>
	<table>
	<tr>
	<td><%bp.getController().writeFormField(out,"ds_tipo_rapporto");%></td>
	<td><%bp.getController().writeFormField(out,"cd_trattamento");%></td>
	</tr>
	<tr>
	<td><%bp.getController().writeFormField(out,"ds_condizione");%></td>
	</tr>
	
	</table>
	
		<%bp.getCrudBonusNucleoFamBP().writeHTMLTable(pageContext,"default",((bp.isInserting()||bp.isEditing())&&bonus.isModificabile()),false,((bp.isInserting()||bp.isEditing())&&(bonus.isModificabile())),"100%","200px"); %>
		<table>
		<tr>	 
			<td  colspan="3">
				<% bp.getCrudBonusNucleoFamBP().writeFormLabel(out,"cf_componente_nucleo"); %>
				<% bp.getCrudBonusNucleoFamBP().writeFormInput(out,"cf_componente_nucleo"); %>
				<% bp.getCrudBonusNucleoFamBP().writeFormLabel(out,"im_reddito_componente"); %>
				<% bp.getCrudBonusNucleoFamBP().writeFormInput(out,"im_reddito_componente"); %>
				<% bp.getCrudBonusNucleoFamBP().writeFormLabel(out,"tipo_componente_nucleo"); %>
				<% bp.getCrudBonusNucleoFamBP().writeFormInput(out,null,"tipo_componente_nucleo",false,null,"onChange=\"submitForm('doOnTipoComponenteNucleoChange()')\""); %> 
			</td>
			<td>	
				<% bp.getCrudBonusNucleoFamBP().writeFormLabel(out,"fl_handicap"); %>
				<% bp.getCrudBonusNucleoFamBP().writeFormInput(out,null,"fl_handicap",!bp.isAbilitato(),null,"onClick=\"submitForm('doOnFlHandicapChange()')\""); %>
			</td>					
		</tr>
	</table>
	
	<table>
		<tr>	
			<td>
				<% JSPUtils.button(out,null,null,"Completa bonus",
						"javascript:submitForm('doCompletaBonus')",
						bonus.isModificabile()&&!bonus.getFl_trasmesso(),
						bp.getParentRoot().isBootstrap()); %>
			</td>
			<td>
				<% JSPUtils.button(out,null,null,"Crea compenso"," javascript:submitForm('doCreaCompenso')",
						bonus.isModificabile()&&!bonus.getFl_trasmesso() &&bonus.getPg_bonus()!=null &&!bp.isDirty(),
						bp.getParentRoot().isBootstrap()); %>
			</td>
			
			<td>
				<% JSPUtils.button(out,null,null, "Visualizza compenso"," javascript:submitForm('doVisualizzaCompenso')", 
						bonus.getPg_bonus()!=null && !bonus.isModificabile(),
						bp.getParentRoot().isBootstrap()); %>
			</td>
			
		</tr>
	</table>	

<% bp.closeFormWindow(pageContext); %>

</body>
</html>

