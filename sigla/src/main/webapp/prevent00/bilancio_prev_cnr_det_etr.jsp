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
<title>Dettagli bilancio / ENTRATE</title>
</head>
<body class="Form">

<%	CRUDDettagliEtrBilancioPrevCnrBP bp = (CRUDDettagliEtrBilancioPrevCnrBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	
	Voce_f_saldi_cmpBulk dettEntrata = (Voce_f_saldi_cmpBulk)bp.getModel();
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
	  <td><span class="FormLabel">Entrate CNR</span></td>
	  <td></td>
    </tr>

	<tr>
	<!--<td>	<% bp.getController().writeFormLabel( out, "cd_voce"); %></td>-->
	<td><span class="FormLabel">Articolo</span></td>
	<td>	<% bp.getController().writeFormInput( out, "cd_voce"); %>
			<% bp.getController().writeFormInput( out, "ds_voce"); %>
			<% bp.getController().writeFormInput( out, "find_voce"); %></td>				 
	</tr>

   	<!--<tr><% 	bp.getController().writeFormField( out, "ti_gestione"); %></tr>-->

    <!-- tolgo queste parti non gestite dall'entrata
	<tr>
		<td><% bp.getController().writeFormLabel( out, "cd_parte"); %></td>
    -->		
		<!-- 	"onChange=\"submitForm('doDefault')\"" serve per far ridisegnare la Jsp ogni volta che modifico
				il valore del campo "cd_parte"
		-->

	<!-- tolgo queste parti non gestite dall'entrata
		<td><% bp.getController().writeFormInput(out,null,"cd_parte",false,null,"onChange=\"submitForm('doDefault')\"");%></td>		
	</tr>
	-->

	<tr>
	</tr>
	<tr>
	</tr>
      	
	<!-- tolgo queste parti non gestite dall'entrata
   	<tr><% bp.getController().writeFormField( out, "cd_funzione"); %></tr>	         
   	<tr><% bp.getController().writeFormField( out, "cd_natura"); %></tr>	     
	-->

	<tr><% bp.getController().writeFormField( out, "cd_categoria");%></tr>

	<tr><td colspan=2>
		<%JSPUtils.tabbed(
				pageContext,
				"tabDettaglioEntrata",
				new String[][]{																
						{ "tabEsercizio",String.valueOf(dettEntrata.getEsercizio().intValue()),"/prevent00/tab_det_etr_bilancio_cnr_esercizio.jsp" },
						{ "tabEsercizio2",String.valueOf(dettEntrata.getEsercizio().intValue() + 1),"/prevent00/tab_det_etr_bilancio_cnr_esercizio2.jsp" },
						{ "tabEsercizio3",String.valueOf(dettEntrata.getEsercizio().intValue() + 2),"/prevent00/tab_det_etr_bilancio_cnr_esercizio3.jsp" }
					      },
				bp.getTab("tabDettaglioEntrata"),
				"center",
				"600px",
				"300px" );
		%>
	</td></tr>
	
</table>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>