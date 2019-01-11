<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*,
		it.cnr.contab.compensi00.tabrif.bulk.*,
		it.cnr.contab.compensi00.bp.*"				
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Gruppi Contributo Ritenuta</title>
</head>
<body class="Form">

<% 	CRUDGruppoCRBP bp = (CRUDGruppoCRBP)BusinessProcess.getBusinessProcess(request);
 	bp.openFormWindow(pageContext);
 	Gruppo_crBulk gruppo = (Gruppo_crBulk)bp.getModel(); 
    Gruppo_cr_detBulk riga = (Gruppo_cr_detBulk)bp.getDettagliGruppoBP().getModel();    
%>

<table class="Panel">
  <tr>
	<td><% bp.getController().writeFormLabel( out, "cd_gruppo_cr"); %></td>
	<td><% bp.getController().writeFormInput( out, "cd_gruppo_cr"); %></td>	
  </tr>			

  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_gruppo_cr"); %></td>
	<td colspan=3><% bp.getController().writeFormInput(out,"ds_gruppo_cr"); %></td>
  </tr>

  <tr>
	<td><% bp.getController().writeFormLabel(out,"fl_accentrato"); %></td>
	<td><% bp.getController().writeFormInput(out,"fl_accentrato"); %></td>
	
	<td><% bp.getController().writeFormLabel(out,"cd_tipo_riga_f24"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"cd_tipo_riga_f24",false,null,"onChange=\"submitForm('doSelezionaTipoFlusso')\""); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"fl_anno_prec"); %></td>
	<td><% bp.getController().writeFormInput(out,"fl_anno_prec"); %></td>
	<td><% bp.getController().writeFormLabel(out,"cd_tributo_erario"); %></td>
	<td><% bp.getController().writeFormInput(out,"cd_tributo_erario"); %></td>
	
	<% if (gruppo!=null && gruppo.getCd_tipo_riga_f24()!=null && gruppo.getCd_tipo_riga_f24().compareTo(Gruppo_crBulk.INPS)==0){%>
		<td><% bp.getController().writeFormLabel(out,"cd_matricola_inps"); %></td>
		<td><% bp.getController().writeFormInput(out,"cd_matricola_inps"); %></td>
	<%}%>	
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"fl_f24online"); %></td>
	<td><% bp.getController().writeFormInput(out,"fl_f24online"); %></td>
	<td><% bp.getController().writeFormLabel(out,"fl_f24online_previd"); %></td>
	<td><% bp.getController().writeFormInput(out,"fl_f24online_previd"); %></td>
  </tr>
</table>

   	<%bp.getDettagliGruppoBP().writeHTMLTable(pageContext,"default",bp.isInserting()||bp.isEditing(),false,bp.isInserting()||bp.isEditing(),"100%","200px"); %>
<table>			
  		
		<tr>	
			<td>
				<% bp.getDettagliGruppoBP().writeFormLabel(out,"find_regione"); %>
			</td>	
			<td colspan="3">	 
				<% bp.getDettagliGruppoBP().writeFormInput(out,null,"cd_regione",(riga!=null && riga.getCrudStatus()!=riga.TO_BE_CREATED) ,null,null); %>
				<% bp.getDettagliGruppoBP().writeFormInput(out,null,"ds_regione",(riga!=null && riga.getCrudStatus()!=riga.TO_BE_CREATED),null,null); %>
				<% bp.getDettagliGruppoBP().writeFormInput(out,null,"find_regione",(riga!=null && riga.getCrudStatus()!=riga.TO_BE_CREATED),null,null); %>
			</td>					
		</tr>
		<tr>
			<td>
				<% bp.getDettagliGruppoBP().writeFormLabel(out,"find_comune"); %>
			</td>
			<td colspan="3">
				<% bp.getDettagliGruppoBP().writeFormInput(out,null,"pg_comune",(riga!=null && riga.getCrudStatus()!=riga.TO_BE_CREATED),null,null); %>
				<% bp.getDettagliGruppoBP().writeFormInput(out,null,"ds_comune",(riga!=null && riga.getCrudStatus()!=riga.TO_BE_CREATED),null,null); %>
				<% bp.getDettagliGruppoBP().writeFormInput(out,null,"find_comune",(riga!=null && riga.getCrudStatus()!=riga.TO_BE_CREATED),null,null); %>
			</td>
		</tr>
		
		<tr>
			<td>
				<% bp.getDettagliGruppoBP().writeFormLabel(out,"terzo"); %>
			</td>
			<td colspan="3">
				<% bp.getDettagliGruppoBP().writeFormInput(out,"cd_terzo_versamento"); %>
				<% bp.getDettagliGruppoBP().writeFormInput(out,"ds_terzo"); %>
				<% bp.getDettagliGruppoBP().writeFormInput(out,"terzo"); %>			
			</td>			
		</tr>	
		<tr>
		 	<td><% bp.getDettagliGruppoBP().writeFormLabel(out,"modalita_pagamento");%></td>
		    <td colspan=3>
		    	<% bp.getDettagliGruppoBP().writeFormInput(out,null,"modalita_pagamento",false,null,"onChange=\"submitForm('doOnModalitaPagamentoChange')\"");%>
				<% if (riga != null && riga.getBanca() != null) bp.getDettagliGruppoBP().writeFormInput(out, "listaBanche"); %>
			</td>
		</tr>
	<tr>
 	<td colspan="4">
	<%	if (riga != null && riga.getBanca() != null) {
			if (Rif_modalita_pagamentoBulk.BANCARIO.equalsIgnoreCase(riga.getBanca().getTi_pagamento())) {
	 	     	bp.getDettagliGruppoBP().writeFormInput(out,"contoB");
			} else if (Rif_modalita_pagamentoBulk.POSTALE.equalsIgnoreCase(riga.getBanca().getTi_pagamento())) {
			   	bp.getDettagliGruppoBP().writeFormInput(out,"contoP");
			} else if (Rif_modalita_pagamentoBulk.QUIETANZA.equalsIgnoreCase(riga.getBanca().getTi_pagamento())) {
			   	bp.getDettagliGruppoBP().writeFormInput(out,"contoQ");
			} else if (Rif_modalita_pagamentoBulk.ALTRO.equalsIgnoreCase(riga.getBanca().getTi_pagamento())) { 
			   	bp.getDettagliGruppoBP().writeFormInput(out,"contoA");
			} else if (Rif_modalita_pagamentoBulk.IBAN.equalsIgnoreCase(riga.getBanca().getTi_pagamento())) { 
			   	bp.getDettagliGruppoBP().writeFormInput(out,"contoN");
			} else if (Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(riga.getBanca().getTi_pagamento()) && riga.getBanca().isTABB()) {
                bp.getDettagliGruppoBP().writeFormInput(out,"contoB");
            }
  		} else if (riga != null && riga.getModalitaPagamento() != null && (riga.getTerzo() != null && riga.getTerzo().getCrudStatus() != riga.getTerzo().UNDEFINED)) {%>
			<span class="FormLabel" style="color:red">
				Nessun riferimento trovato per la modalità di pagamento selezionata!
			</span>
	<%}%>
  	</td>
  </tr> 
  </table>
	<%	bp.closeFormWindow(pageContext); %>
	</body>
</html>