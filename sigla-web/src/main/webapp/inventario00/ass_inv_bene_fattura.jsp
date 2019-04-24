<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario00.docs.bulk.*,
		it.cnr.contab.inventario00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Associa Righe Fattura - Beni</title>
</head>
<body class="Form">

<% AssBeneFatturaBP bp = (AssBeneFatturaBP)BusinessProcess.getBusinessProcess(request); 
   Ass_inv_bene_fatturaBulk associaz_bene_fatt = (Ass_inv_bene_fatturaBulk)bp.getModel();
   bp.openFormWindow(pageContext); %>
  
  <% bp.getDettagliFattura().writeHTMLTable(pageContext,"inventarioSet",false,false,false,"100%","auto"); %>
  <div class="Group card p-2">
	<table class="w-100">
	  <tr>
	  	<td colspan = "4">
		  <% bp.getRigheDaFattura().writeHTMLTable(
				pageContext,
				null,
				true,
				false,
				true,
				"100%",
				"140px",
				true); %>
		</td>
	  </tr>
	</table>
	<table>	
	  <tr>
		<td>
			<% bp.getRigheDaFattura().writeFormLabel(out,"codiceCompleto"); %>
		</td>
		<td>
			<% bp.getRigheDaFattura().writeFormInput(out,null,"codiceCompleto",true,null,null); %>
		</td>
	  </tr>	
	  <tr>
		<td>
			<% bp.getRigheDaFattura().writeFormLabel(out,"ds_bene"); %>
		</td>
		<td>
			<% bp.getRigheDaFattura().writeFormInput(out,null,"ds_bene",true,null,null); %>
		</td>
	  </tr>
   </table>
   <table>
	  <tr>
		<td>
			<% bp.getRigheDaFattura().writeFormLabel(out,"find_categoria_bene"); %>
		</td>
		<td>
			<% bp.getRigheDaFattura().writeFormInput(out,null,"find_categoria_bene",true,null,null); %>
		</td>
	  </tr>	
	  <tr>
		<td>
			<% bp.getRigheDaFattura().writeFormLabel(out,"ds_assegnatario"); %>
		</td>
		<td colspan="4">
			<% bp.getRigheDaFattura().writeFormInput(out,null,"cd_assegnatario",true,null,null); %>
		
			<% bp.getRigheDaFattura().writeFormInput(out,null,"ds_assegnatario",true,null,null); %>
		</td>
	  </tr>
	  <tr>
		<td>
			<% bp.getRigheDaFattura().writeFormLabel(out,"find_ubicazione"); %>
		</td>
		<td>
			<% bp.getRigheDaFattura().writeFormInput(out,null,"find_ubicazione",true,null,null); %>
		</td>
	  </tr>
   </table>
   <table>
	  <tr>
		<td>
			<% bp.getRigheDaFattura().writeFormLabel(out,"collocazione"); %>
		</td>
		<td>
			<% bp.getRigheDaFattura().writeFormInput(out,null,"collocazione",true,null,null); %>
		</td>
	  </tr>	 
	</table>
 </div>
<% bp.closeFormWindow(pageContext); %>	
</body>
</html>