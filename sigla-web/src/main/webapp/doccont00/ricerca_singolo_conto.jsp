<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.doccont00.singconto.bulk.*,
		it.cnr.contab.doccont00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Ricerca singoli conti</title>
</head>

<body class="Form">
<%	RicercaSingoloContoBP bp = (RicercaSingoloContoBP)BusinessProcess.getBusinessProcess(request);
	V_voce_f_sing_contoBulk filtro = (V_voce_f_sing_contoBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

	<div class="Group card p-2">
		<table>
		<% if ( !filtro.isEnteInScrivania() ) { %>
			<tr>
				<td><% bp.getController().writeFormLabel( out, "fl_ente"); %></td>		
				<td><% bp.getController().writeFormInput(out,"default","fl_ente",false,null,"onclick=\"submitForm('doOnFl_enteChange')\""); %></td>
			</tr>
			<tr>
				<td><% bp.getController().writeFormLabel( out, "ti_gestione"); %></td>		
				<td><% bp.getController().writeFormInput( out, "default","ti_gestione",filtro.getFl_ente().booleanValue(),null,"onChange=\"submitForm('doOnTi_gestioneChange')\""); %></td>		
			</tr>
			<% if ( filtro.getFl_ente().booleanValue()) { 
					if (!filtro.isEntrata()) { %>	
						<tr>
							<td><% bp.getController().writeFormLabel( out,"fl_partita_giro"); %></td>		
							<td><% bp.getController().writeFormInput( out,"default","fl_partita_giro",false,null,"onclick=\"submitForm('doOnFl_partita_giroChange')\""); %></td>		
						</tr>
						<tr>
						   <td><% bp.getController().writeFormLabel( out, "cd_elemento_voce"); %></td>	 	
						   <td><% bp.getController().writeFormInput( out,"default","find_elemento_voce");%></td>
						</tr>   
		  				<tr>
						   <td><% bp.getController().writeFormLabel( out, "cd_voce"); %></td>		
						   <td><% bp.getController().writeFormInput( out, null,"find_voce_f",(filtro.getFl_partita_giro()!=null && filtro.getFl_partita_giro().booleanValue()),null,"");%></td>
						</tr>					
		  			<% } %>
  			<% } else {%>
  						<tr>
							<td><% bp.getController().writeFormLabel( out, "fl_partita_giro"); %></td>		
							<td><% bp.getController().writeFormInput( out,"default","fl_partita_giro",false,null,"onclick=\"submitForm('doOnFl_partita_giroChange')\""); %></td>		
						</tr>
						<tr>
						   <td><% bp.getController().writeFormLabel( out, "cd_elemento_voce"); %></td>		
						   <td><% bp.getController().writeFormInput( out, "find_elemento_voce");%></td>
						</tr>
						<tr>
						   <td><% bp.getController().writeFormLabel( out, "cd_voce"); %></td>		
						   <td><% bp.getController().writeFormInput( out, null,"find_voce_f",(filtro.getFl_partita_giro()!=null && filtro.getFl_partita_giro().booleanValue()),null,"");%></td>
						</tr>
			<% } %>
	  	<% } else { %>
			<tr>
				<td><% bp.getController().writeFormLabel( out, "ti_gestione"); %></td>		
				<td><% bp.getController().writeFormInput( out, "default","ti_gestione",false,null,"onChange=\"submitForm('doDefault')\""); %></td>		
			</tr>
			<% if (filtro.isEntrata()) { %>
				
				<tr>
					<td><% bp.getController().writeFormLabel( out, "fl_partita_giro"); %></td>		
					<td><% bp.getController().writeFormInput( out,"default","fl_partita_giro",false,null,"onclick=\"submitForm('doOnFl_partita_giroChange')\""); %></td>		
				<tr>
				   <td><% bp.getController().writeFormLabel( out, "cd_unita_organizzativa"); %></td>		
				   <td><% bp.getController().writeFormInput( out, "find_unita_organizzativa");%></td>
				</tr>
				<tr>
				   <td><% bp.getController().writeFormLabel( out, "cd_voce"); %></td>		
				   <td><% bp.getController().writeFormInput( out, null,"find_voce_f",(filtro.getFl_partita_giro()!=null && filtro.getFl_partita_giro().booleanValue()),null,"");%></td>
				</tr>
  			<% } else { %>
				<tr>
				   <td><% bp.getController().writeFormLabel( out, "cd_natura"); %></td>		
				   <td><% bp.getController().writeFormInput( out, "cd_natura");
				          bp.getController().writeFormInput( out, "find_natura");%></td>
				</tr>
				<tr>
					<td><% bp.getController().writeFormLabel( out, "fl_partita_giro"); %></td>		
					<td><% bp.getController().writeFormInput( out,"default","fl_partita_giro",false,null,"onclick=\"submitForm('doOnFl_partita_giroChange')\"");%></td>		
				</tr>
				<tr>
				   <td><% bp.getController().writeFormLabel( out, "cd_cds_proprio"); %></td>		
				   <td><% bp.getController().writeFormInput( out, "cd_cds_proprio");
				          bp.getController().writeFormInput( out, "find_cds");%></td>
				</tr>
				<tr>
				   <td><% bp.getController().writeFormLabel( out, "cd_proprio_voce");%></td>
				   <td><%bp.getController().writeFormInput( out, "cd_proprio_voce");
			       		 bp.getController().writeFormInput( out, "find_area_ricerca"); %></td>
				</tr>
				<tr>
					   <td><% bp.getController().writeFormLabel( out, "cd_voce"); %></td>		
					   <td><% bp.getController().writeFormInput( out, null,"find_voce_f",(filtro.getFl_partita_giro()!=null && filtro.getFl_partita_giro().booleanValue()),null,"");%></td>
				</tr>
			<% } %>
		<% } %>	   
		</table>	
	</div>
	<% bp.closeFormWindow(pageContext); %>
	
</body>