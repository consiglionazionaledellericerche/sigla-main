<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario00.bp.*"
%>
		
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Tipo Carico/Scarico</title>
</head>
<body class="Form"> 

<% CRUDTipoCaricoScaricoBP bp = (CRUDTipoCaricoScaricoBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); 
	 Tipo_carico_scaricoBulk tipoCS = (Tipo_carico_scaricoBulk)bp.getModel(); %>

	<table class="Panel">
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"cd_tipo_carico_scarico");%>
		  </td>
		  <td>
			<% bp.getController().writeFormInput(out,"cd_tipo_carico_scarico");%>
		  </td>
		</tr>
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"ds_tipo_carico_scarico");%>
		  </td>
		  <td>
			<% bp.getController().writeFormInput(out,null,"ds_tipo_carico_scarico",(tipoCS!=null && !tipoCS.isCancellabile()),null,null);%>
		  </td>
		</tr>
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"ti_documento");%>
		  </td>
		  <td>
			<% bp.getController().writeFormInput(out,null,"ti_documento",bp.isEditing(),null,"onClick=\"submitForm('doOnFlCaricoScarico')\"");%>
		  </td>
		</tr>
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"fl_fatturabile");%>
		  </td>
		  <td>
			<% bp.getController().writeFormInput(out,null,"fl_fatturabile",bp.isEditing()||((tipoCS!=null) && tipoCS.isTrasferibile()),null,"onClick=\"submitForm('doOnFlFatturabileChange')\"");%>
												
		  </td>
		</tr>
		<% if (bp.isMovimentoCarico()){ %>
			<tr>
			  <td>
				<% bp.getController().writeFormLabel(out,"fl_aumento_valore");%>
			  </td>
			  <td>
				<% bp.getController().writeFormInput(out,null,"fl_aumento_valore",bp.isEditing()||((tipoCS!=null) && tipoCS.isTrasferibile()),null,"onClick=\"submitForm('doOnFlAumentoValoreChange')\"");%>
			  </td>
			</tr>
			<tr>
			  <td>
				<% bp.getController().writeFormLabel(out,"fl_elabora_buono_coge");%>
			  </td>
			  <td>
				<% bp.getController().writeFormInput(out,null,"fl_elabora_buono_coge",bp.isEditing()||((tipoCS!=null) && tipoCS.isTrasferibile()),null,"onClick=\"submitForm('doOnFlElaboraCOGEChange')\"");%>
			  </td>
			</tr>
		<% } else { %>		
		  <tr>
			  <td>
				<% bp.getController().writeFormLabel(out,"fl_storno_fondo");%>
			  </td>
			  <td>
				<% bp.getController().writeFormInput(out,null,"fl_storno_fondo",bp.isEditing()||((tipoCS!=null) && tipoCS.isTrasferibile()),null,null);%>
			  </td>
		  </tr>	  
		  <tr>
			  <td>
				<% bp.getController().writeFormLabel(out,"fl_chiude_fondo");%>
			  </td>
			  <td>
				<% bp.getController().writeFormInput(out,null,"fl_chiude_fondo",bp.isEditing()||((tipoCS!=null) && tipoCS.isTrasferibile()),null,null);%>
			  </td>
		  </tr>	  
		  <tr>
			  <td>
				<% bp.getController().writeFormLabel(out,"fl_vendita");%>
			  </td>
			  <td>
				<% bp.getController().writeFormInput(out,null,"fl_vendita",bp.isEditing()||((tipoCS!=null) && tipoCS.isTrasferibile()),null,null);%>
			  </td>
		  </tr>	  
		  <tr>
			  <td>
				<% bp.getController().writeFormLabel(out,"fl_da_ordini");%>
			  </td>
			  <td>
				<% bp.getController().writeFormInput(out,null,"fl_da_ordini",bp.isEditing()||((tipoCS!=null) && tipoCS.isTrasferibile()),null,null);%>
			  </td>
		  </tr>
		<% } %>
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"fl_buono_per_trasferimento");%>
		  </td>
		  <td>
			<% bp.getController().writeFormInput(out,null,"fl_buono_per_trasferimento",bp.isEditing()||((tipoCS!=null) && tipoCS.isFatturabile()),null,"onClick=\"submitForm('doOnFlTrasferimentoChange')\"");%>
		  </td>
		</tr>
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"dt_cancellazione");%>
		  </td>
		  <td>
			<% bp.getController().writeFormInput(out,null,"dt_cancellazione",true,null,null);%>
		  </td>
		</tr>
	</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>