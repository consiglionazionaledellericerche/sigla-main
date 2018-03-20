<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.config00.pdcfin.bulk.*,
		it.cnr.contab.config00.bp.*"				
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Limite Spesa</title>
</head>
<body class="Form">

<% 	CRUDLimiteSpesaBP bp = (CRUDLimiteSpesaBP)BusinessProcess.getBusinessProcess(request);
 	bp.openFormWindow(pageContext);
 	LimiteSpesaBulk lim = (LimiteSpesaBulk)bp.getModel(); 
 	LimiteSpesaDetBulk riga = (LimiteSpesaDetBulk)bp.getDettagli().getModel();    
%>

<table class="Panel">
  <tr>
	<td colspan="4"><% bp.getController().writeFormLabel( out, "findElementoVoce"); %>
		<% bp.getController().writeFormInput( out, "cd_elemento_voce"); %>
		<% bp.getController().writeFormInput( out, "ds_elemento_voce"); %>
		<% bp.getController().writeFormInput( out, "findElementoVoce"); %></td>
	<td><% bp.getController().writeFormLabel( out, "fonte"); %>
		<% bp.getController().writeFormInput( out, "fonte"); %></td>	
  </tr>			 
 <tr>
 		<td><% bp.getController().writeFormLabel( out, "importo_limite"); %>
			<% bp.getController().writeFormInput( out, "importo_limite"); %></td>
		<td><% bp.getController().writeFormLabel( out, "importo_assegnato"); %>
			<% bp.getController().writeFormInput( out, "importo_assegnato"); %></td>
 </tr>
</table>
   	<%bp.getDettagli().writeHTMLTable(pageContext,"dettagli",true,false,true,"100%","200px"); %>
   	<table>
   		<tr>
			<td>
				<% bp.getDettagli().writeFormLabel(out,"cd_cds"); %>
				<% bp.getDettagli().writeFormInput(out,null,"cd_cds",(riga!=null &&(riga.isUtilizzato()||!riga.isToBeCreated())),null,""); %>
				<% bp.getDettagli().writeFormInput(out,"ds_cds"); %> 
				<% bp.getDettagli().writeFormInput(out,null,"findCds",(riga!=null &&(riga.isUtilizzato()||!riga.isToBeCreated())),null,""); %>
			</td>
			<td><% bp.getDettagli().writeFormLabel( out, "importo_limite"); %>
				<% bp.getDettagli().writeFormInput( out, "importo_limite"); %>
			</td>
			<td><% bp.getDettagli().writeFormLabel( out, "impegni_assunti"); %>
				<% bp.getDettagli().writeFormInput( out, "impegni_assunti"); %>
			</td>
		</tr>
		</table>	 
   		<%	bp.closeFormWindow(pageContext); %>
	</body>
</html>