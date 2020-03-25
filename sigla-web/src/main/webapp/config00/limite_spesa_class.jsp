<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.config00.pdcfin.cla.bulk.*,
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

<% 	CRUDLimiteSpesaClassBP bp = (CRUDLimiteSpesaClassBP)BusinessProcess.getBusinessProcess(request);
 	bp.openFormWindow(pageContext);
 	V_classificazione_vociBulk lim = (V_classificazione_vociBulk)bp.getModel();
 	LimiteSpesaClassBulk riga = (LimiteSpesaClassBulk)bp.getDettagli().getModel();
%>

<table class="Panel">
  <tr>
	<td><% bp.getController().writeFormLabel( out, "cd_classificazione"); %></td>
	<td colspan="3">
		<% bp.getController().writeFormInput( out, "cd_classificazione"); %>
		<% bp.getController().writeFormInput( out, "ds_classificazione"); %>
	</td>
  </tr>
 <tr>
	<td><% bp.getController().writeFormLabel( out, "im_limite_assestato"); %></td>
	<td><% bp.getController().writeFormInput( out, "im_limite_assestato"); %></td>
	<td><% bp.getController().writeFormLabel( out, "imLimiteAssestatoRipartito"); %></td>
	<td><% bp.getController().writeFormInput( out, "imLimiteAssestatoRipartito"); %></td>
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
			<td><% bp.getDettagli().writeFormLabel( out, "im_limite_assestato"); %>
				<% bp.getDettagli().writeFormInput( out, "im_limite_assestato"); %>
			</td>
			<td><% bp.getDettagli().writeFormLabel( out, "impegni_assunti"); %>
				<% bp.getDettagli().writeFormInput( out, "impegni_assunti"); %>
			</td>
		</tr>
		</table>	 
   		<%	bp.closeFormWindow(pageContext); %>
	</body>
</html>