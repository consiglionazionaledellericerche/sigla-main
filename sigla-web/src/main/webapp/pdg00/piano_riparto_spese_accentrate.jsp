<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*,
	        it.cnr.jada.util.action.*,
	        it.cnr.contab.config00.bp.*,
			it.cnr.contab.prevent00.bp.*"
%>
<%
	CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = ((CRUDPdgPianoRipSpeseAccentBP)bp).getCrudPdgPianoRipartoSpese();
	boolean isTableEnabled = !((CRUDPdgPianoRipSpeseAccentBP)bp).isPdgPianoRipartoDefinitivo();
	boolean isFieldEnabled = !isTableEnabled;
	boolean isSearchVisible = ((CRUDPdgPianoRipSpeseAccentBP)bp).isSearchCrudPdgPianoRipartoSpeseEnabled();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Piano di Riparto delle Spese Accentrate</title>
<body class="Form">
<% bp.openFormWindow(pageContext);%>
	<table class="Panel card p-2">
        <tr>
            <td><% bp.getController().writeFormLabel( out, "cd_classificazione"); %></td>
            <td><% bp.getController().writeFormInput( out, "cd_classificazione");%> </td>
            </tr>
            <tr>
            <td><% bp.getController().writeFormLabel( out, "ds_classificazione"); %></td>
            <td><% bp.getController().writeFormInput( out, "ds_classificazione");%> </td>
        </tr>
	</table>
	<div class="card p-1">
	    <%controller.writeHTMLTable(pageContext,"pdgPianoRipartoSpese",isTableEnabled,isSearchVisible,isTableEnabled,"100%","170px"); %>
	</div>
	<table class="Panel card p-2" cellspacing=2>
	<tr>
		<td><% controller.writeFormLabel(out,"default","find_centro_responsabilita"); %></td>
		<td><% controller.writeFormInput(out,"default","find_centro_responsabilita"); %></td>			 
	</tr>
	<tr>
  	    <td><% controller.writeFormLabel(out,"default","im_tot_spese_acc"); %></td>
		<td><% controller.writeFormInput(out,"default","im_tot_spese_acc", isFieldEnabled, null, ""); %></td>
	</tr>	
</table>
<BR>
<table style="width:100%" class="card p-2">
 	<tr>
  		<td style="width:50%">
			<div class="Group" style="width:100%">
				<table class="Panel" align="left" cellspacing=1 cellpadding=1>
			  	<tr>
		   	 		<td><% bp.writeFormLabel(out,"tot_imp_piano_riparto_spese");%></td>
		         	<td><% bp.writeFormInput(out,"tot_imp_piano_riparto_spese");%></td>
		      	</tr>  
			   	</table>
			</div>	
		</td>
  		<td style="width:10%"></td>
  		<td style="width:40%">
			<div class="Group" style="width:100%">
				<table class="Panel" align="center" cellspacing=1 cellpadding=1>
			  	<tr>
					<td><center><%JSPUtils.button(out, "img/edit16.gif", "img/edit16.gif", "Carica Struttura", "if (disableDblClick()) javascript:submitForm('doCaricaStruttura')", null, isTableEnabled, bp.getParentRoot().isBootstrap());%></center></td>
					<td><center><%JSPUtils.button(out, "img/edit16.gif", "img/edit16.gif", "Inizializza Importi", "if (disableDblClick()) javascript:submitForm('doInitImTotSpeseAcc')", null, isTableEnabled, bp.getParentRoot().isBootstrap());%></center></td>
		      	</tr>  
			   	</table>
			</div>	
  		</td>
   	</tr>  
</table>
<% bp.closeFormWindow(pageContext); %>
</body>
</html>
