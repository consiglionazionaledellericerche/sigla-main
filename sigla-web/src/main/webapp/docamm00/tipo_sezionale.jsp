<!-- 
 ?ResourceName "tipo_se4zionale.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Tipo Sezionale </title>
</head>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	Tipo_sezionaleBulk tipoSezionale = (Tipo_sezionaleBulk)bp.getModel();
	 bp.openFormWindow(pageContext); %>

	<div class="Group">
		<table class="Panel" border="0" cellspacing="0" cellpadding="2">
		   <tr>
				<% bp.getController().writeFormField(out,"cd_tipo_sezionale");%>							
		   </tr>
		   <tr>
				<% bp.getController().writeFormField(out,"ds_tipo_sezionale");%>							
		   </tr>	
		   <tr>
			<td>
				<% bp.getController().writeFormLabel( out, "ti_bene_servizio"); %>
			</td>
			<td>
				<% bp.getController().writeFormInput( out,
											((bp.isSearching())? 
												"ti_bene_servizioForSearch" :
												"ti_bene_servizio")); %>
			</td>
		    <tr>
				<% bp.getController().writeFormField(out,"ordina");%>							
		    </tr>
		   </tr>
		</table>
		<table class="Panel" border="0" cellspacing="0" cellpadding="2">
		   <tr>
				<td>&nbsp;</td>
				<td class="Group">
					<% bp.getController().writeFormInput(out,null,"ti_acquisti_vendite",false,null,"onclick=\"submitForm('doOnTiAcquistiVenditeChange')\"");%>						
				</td>
				<td class="Group">
					<% bp.getController().writeFormInput(out,null,"ti_istituz_commerc",false,null,"onclick=\"submitForm('doDefault')\"");%>						
				</td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormInput( out, "fl_ordinario"); %>
					<% bp.getController().writeFormLabel( out, "fl_ordinario"); %></td>
				<td><% bp.getController().writeFormInput( out, "fl_intra_ue"); %>
					<% bp.getController().writeFormLabel( out, "fl_intra_ue"); %></td>
				<td><% bp.getController().writeFormInput( out, "fl_extra_ue"); %>
					<% bp.getController().writeFormLabel( out, "fl_extra_ue"); %></td>
				<td><% bp.getController().writeFormInput( out, "fl_split_payment"); %>
					<% bp.getController().writeFormLabel( out, "fl_split_payment"); %></td>
				<td><% bp.getController().writeFormInput( out, "fl_reg_tardiva"); %>
					<% bp.getController().writeFormLabel( out, "fl_reg_tardiva"); %></td>	
			</tr>		
			<tr>
				<td><% bp.getController().writeFormInput( out, "fl_san_marino_con_iva"); %>
					<% bp.getController().writeFormLabel( out, "fl_san_marino_con_iva"); %></td>
				<td><% bp.getController().writeFormInput( out, "fl_san_marino_senza_iva"); %>
					<% bp.getController().writeFormLabel( out, "fl_san_marino_senza_iva"); %></td>
				<td><% bp.getController().writeFormInput( out, null, "fl_autofattura", false, null, "onclick=\"submitForm('doOnFlAutofatturaChange')\""); %>
					<% bp.getController().writeFormLabel( out, "fl_autofattura"); %></td>
				<td><% bp.getController().writeFormInput( out, "fl_servizi_non_residenti"); %>
					<% bp.getController().writeFormLabel( out, "fl_servizi_non_residenti"); %></td>	
			</tr>		
		</table>

		<table class="Panel" border="0" cellspacing="0" cellpadding="2">
			<% 
			   if (!tipoSezionale.isIstituzionale()){%>		
				<tr>
					<% bp.getController().writeFormField(out,"cd_attivita_commerciale");%>
					<% bp.getController().writeFormField(out,"ds_attivita_commerciale");%>
					<td>
						<% bp.getController().writeFormInput(out,"attivita_commerciale");%>
					</td>
				</tr>
			<% if (tipoSezionale.isAcquisti()){%>		
				<tr>
					<% bp.getController().writeFormField(out,"sezionale_ven_liquidazione");%>
					<% bp.getController().writeFormField(out,"ds_sezionale_ven_liquidazione");%>
					<td>
						<% bp.getController().writeFormInput(out,"find_sezionale_ven_liquidazione");%>
					</td>
			   </tr>
			<% }
			}%>
		</table>
	</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>