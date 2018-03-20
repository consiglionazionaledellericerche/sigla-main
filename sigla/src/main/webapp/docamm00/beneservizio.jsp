<!-- 
 ?ResourceName "beneservizio.jsp"
 ?ResourceTimestamp "09/08/01 16.54.00"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<SCRIPT LANGUAGE="Javascript">
function escludi(index){
	var names=new Array();
	var num_flags=2;
	names[1]="main.fl_gestione_inventario";
	names[2]="main.fl_gestione_magazzino";
	
	for (i=1; i<=num_flags;i++)
		if (index!=i)
			document.mainForm.elements(names[i]).checked=false;	
}
</SCRIPT>
<script language="javascript" src="scripts/css.js"></script>
<title>Bene Servizio</title>
</head>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<div class="Group">
		<table class="Panel">
			<tr>
				<td><% bp.getController().writeFormLabel( out, "cd_bene_servizio"); %></td>
				<td><% bp.getController().writeFormInput( out, "cd_bene_servizio"); %></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel( out, "ds_bene_servizio"); %></td>
				<td><% bp.getController().writeFormInput( out, "ds_bene_servizio"); %></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel( out, "ti_bene_servizio"); %></td>
				<td>
					<% bp.getController().writeFormInput( out,
										((bp.isSearching())? 
											"ti_bene_servizioForSearch" :
											"ti_bene_servizio")); %>
				</td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel( out, "tipoServizio"); %></td>
				<td>
					<% bp.getController().writeFormInput( out,
										((bp.isSearching())? 
											"tipoServizioForSearch" :
											"tipoServizio")); %>
				</td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel( out, "tipoManutenzione"); %></td>
				<td>
					<% bp.getController().writeFormInput( out,
										((bp.isSearching())? 
											"tipoManutenzioneForSearch" :
											"tipoManutenzione")); %>
				</td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel( out, "tipoGestione"); %></td>
				<td>
					<% bp.getController().writeFormInput( out,
										((bp.isSearching())? 
											"tipoGestioneForSearch" :
											"tipoGestione")); %>
				</td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel( out, "cd_categoria_gruppo"); %></td>
				<td>
					<% bp.getController().writeFormInput(out,"cd_categoria_gruppo");%>
					<% bp.getController().writeFormInput(out,"ds_categoria_gruppo");%>
					<% bp.getController().writeFormInput(out,"categoria_gruppo");%>
				</td>
			</tr>	   
			<tr>
				<td><% bp.getController().writeFormLabel( out, "cd_voce_iva"); %></td>
				<td><% bp.getController().writeFormInput(out,"cd_voce_iva");%>
				<% bp.getController().writeFormInput(out,"ds_voce_iva");%>
				<% bp.getController().writeFormInput(out,"voce_iva");%>
				</td>
			</tr> 
			<tr>
				<td><% bp.getController().writeFormInput( out, null,"fl_gestione_inventario",false,null,"onclick=\"escludi(1);submitForm('doDefault')\"");%>
				<% bp.getController().writeFormLabel( out, "fl_gestione_inventario"); %></td>
				<td><% bp.getController().writeFormInput( out, null,"fl_gestione_magazzino",false,null,"onclick=\"escludi(2);submitForm('doDefault')\"");%>
				<% bp.getController().writeFormLabel( out, "fl_gestione_magazzino"); %></td>
			</tr>	
			<tr>
				<%
					bp.getController().writeFormField(out, "unitaMisura");
				%>
			</tr>	
			<tr>
				<%
					bp.getController().writeFormField(out, "tipoArticolo");
				%>
			</tr>	
			<tr>
				<%
					bp.getController().writeFormField(out, "gruppoMerceologico");
				%>
			</tr>
			<tr>
				<td><% bp.getController().writeFormInput( out, "fl_obb_intrastat_acq"); %>
					<% bp.getController().writeFormLabel( out, "fl_obb_intrastat_acq"); %></td>
				<td><% bp.getController().writeFormInput( out, "fl_obb_intrastat_ven"); %>
					<% bp.getController().writeFormLabel( out, "fl_obb_intrastat_ven"); %></td>
			</tr>	
			<tr>
				<td><% bp.getController().writeFormInput( out, "fl_autofattura"); %>
					<% bp.getController().writeFormLabel( out, "fl_autofattura"); %></td>	
				<td><% bp.getController().writeFormInput( out, "fl_bollo"); %>
					<% bp.getController().writeFormLabel( out, "fl_bollo"); %></td>	
			</tr>
			<tr>
				<td><% bp.getController().writeFormInput( out, "flScadenza"); %>
					<% bp.getController().writeFormLabel( out, "flScadenza"); %></td>
				<td><% bp.getController().writeFormInput( out, "scontiApplicInvent"); %>
					<% bp.getController().writeFormLabel( out, "scontiApplicInvent"); %></td>
			</tr>	
			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "fl_valido");
					%>
				</td>
				<td colspan="4">
					<%
						bp.getController().writeFormInput(out, "fl_valido");
					%>
				</td>
			</tr>
			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "note");
					%>
				</td>
				<td colspan="4">
					<%
						bp.getController().writeFormInput(out, "note");
					%>
				</td>
			</tr>
		</table> 
	</div>
	<%	bp.closeFormWindow(pageContext); %>
</body>
</html>