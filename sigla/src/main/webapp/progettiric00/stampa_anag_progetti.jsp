<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.progettiric00.core.bulk.*,
		it.cnr.contab.progettiric00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa anagrafica progetti</title>
</head>
<body class="Form">

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	Stampa_anag_progettiVBulk bulk = (Stampa_anag_progettiVBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>
<table>
	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"unita_organizzativaForPrint");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"unita_organizzativaForPrint");%>
	  </TD></TR>

          <TR><TD>
		<% bp.getController().writeFormLabel(out,"cd_progetto");%>
	        </TD><TD>
		<% bp.getController().writeFormInput(out,"cd_progetto");%>
		<% bp.getController().writeFormInput(out,"ds_progetto");%>
		<% bp.getController().writeFormInput(out,"find_nodo_padre");%>
	  </TD></TR>
  	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"stato");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"stato");%>
	  </TD></TR>
  	  <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"livello");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"livello");%>
	  </TD></TR>
	   <TR><TD>
	  	<% bp.getController().writeFormLabel(out,"tipo_fase");%>
	  	</TD><TD colspan="3">
	  	<% bp.getController().writeFormInput(out,"tipo_fase");%>
	  </TD></TR>
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>