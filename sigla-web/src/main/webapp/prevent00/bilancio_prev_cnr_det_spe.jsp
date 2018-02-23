<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.prevent00.bulk.*,
		it.cnr.contab.prevent00.action.*,
		it.cnr.contab.prevent00.bp.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Dettagli bilancio / SPESE</title>
</head>
<body class="Form">

<%	CRUDDettagliSpeBilancioPrevCnrBP bp = (CRUDDettagliSpeBilancioPrevCnrBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	
	Voce_f_saldi_cmpBulk dettSpesa = (Voce_f_saldi_cmpBulk)bp.getModel();
%>

<table class="Panel">

	<tr></tr>
	<tr></tr>
	<td colspan=2 ALIGN="CENTER">
			<% bp.getController().writeFormLabel( out, "esercizio"); %>
			<% bp.getController().writeFormInputByStatus( out, "esercizio"); %>
			<span class="FormLabel">Ente</span>
			<% bp.getController().writeFormInputByStatus( out, "cd_cds"); %>							
	</td>

	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>	

	<tr>
	  <td><span class="FormLabel">Spese CNR</span></td>
	  <td></td>
    </tr>
	
	<tr>
	<!--<td>	<% bp.getController().writeFormLabel( out, "cd_voce"); %></td>-->
	<td><span class="FormLabel">Capitolo/Articolo</span></td>
	<td>	<% bp.getController().writeFormInput( out, "cd_voce"); %>
			<% bp.getController().writeFormInput( out, "ds_voce"); %>
			<% bp.getController().writeFormInput( out, "find_voce"); %></td>				 
	</tr>

   	<!--<tr><% 	bp.getController().writeFormField( out, "ti_gestione"); %></tr>-->

   	
	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_parte"); %></td>
		
		<!-- 	"onChange=\"submitForm('doDefault')\"" serve per far ridisegnare la Jsp ogni volta che modifico
				il valore del campo "cd_parte"
		-->
		<td><% bp.getController().writeFormInput(out,null,"cd_parte",false,null,"onChange=\"submitForm('doDefault')\"");%></td>		
	</tr>
      	
	<tr><% bp.getController().writeFormField( out, "cd_categoria");%></tr>
   	<tr><% bp.getController().writeFormField( out, "cd_funzione"); %></tr>	         
   	<tr><% bp.getController().writeFormField( out, "cd_natura"); %></tr>	     

	<tr><td colspan=2>
		<%JSPUtils.tabbed(
				pageContext,
				"tabDettaglioSpesa",
				new String[][]{																
						{ "tabEsercizio",String.valueOf(dettSpesa.getEsercizio().intValue()),"/prevent00/tab_det_spe_bilancio_cnr_esercizio.jsp" },
						{ "tabEsercizio2",String.valueOf(dettSpesa.getEsercizio().intValue() + 1),"/prevent00/tab_det_spe_bilancio_cnr_esercizio2.jsp" },
						{ "tabEsercizio3",String.valueOf(dettSpesa.getEsercizio().intValue() + 2),"/prevent00/tab_det_spe_bilancio_cnr_esercizio3.jsp" }
					      },
				bp.getTab("tabDettaglioSpesa"),
				"center",
				"600px",
				"300px" );
		%>
	</td></tr>
	
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>