<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.consultazioni.bp.*,
		it.cnr.contab.pdg00.consultazioni.bulk.Param_cons_costi_personaleBulk"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Consultazione Costi del Personale Mensile</title>
</head>
<body class="Form">

<% 	ConsCostiDelPersonaleMensileBP bp = (ConsCostiDelPersonaleMensileBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); 
	%>
<table>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
	<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
  </tr>
  <tr>	
	<td><% bp.getController().writeFormLabel(out,"cds"); %></td>
	<td><% bp.getController().writeFormInput(out,"cds"); %></td>
  </tr>
  <tr>
    	<td><% bp.getController().writeFormLabel( out, "find_uo"); %></td>		
		<td colspan=4><%bp.getController().writeFormInput( out, "find_uo");%></td>
  </tr>
   <tr>
	<td><% bp.getController().writeFormLabel(out,"mese"); %></td>
	<td><% bp.getController().writeFormInput(out,"mese"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"find_commessa"); %></td>
	<td><% bp.getController().writeFormInput(out,"find_commessa"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"find_modulo"); %></td>
	<td><% bp.getController().writeFormInput(out,"find_modulo"); %></td>
  </tr>  
  <tr>
	<td><% bp.getController().writeFormLabel(out,"find_dipendente"); %></td>
	<td><% bp.getController().writeFormInput(out,"find_dipendente"); %></td>
  </tr>    
 
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>