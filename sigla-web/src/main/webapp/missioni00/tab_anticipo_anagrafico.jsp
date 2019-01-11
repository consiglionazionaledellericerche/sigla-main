<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*, it.cnr.jada.bulk.*, it.cnr.contab.anagraf00.tabrif.bulk.*"%>

<%  
	CRUDAnticipoBP bp = (CRUDAnticipoBP)BusinessProcess.getBusinessProcess(request);
	AnticipoBulk anticipo = (AnticipoBulk) bp.getModel();

	if(anticipo == null)
		anticipo = new AnticipoBulk();
%>

<div class="Group" style="width:100%">
<table width="100%">
	<tr></tr>
	
	<tr>
	<td>
		<% bp.getController().writeFormLabel( out, "dt_registrazione"); %>
		<% bp.getController().writeFormInput( out, null, "dt_registrazione", !anticipo.isEditable(), null,""); %>
	</td>
	<td><% bp.getController().writeFormField( out, "dt_cancellazione"); %></td>
	</tr>
	
	<tr>
	<td>
		<% bp.getController().writeFormLabel( out, "ds_anticipo"); %>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<% bp.getController().writeFormInput( out, "ds_anticipo"); %>
	</td>
	<td></td>
	</tr>
		
	<tr></tr>
</table>
</div>

<div class="Group" style="width:100%">
<table width="100%">
	<tr></tr>

	<tr>
    <td colspan="4"><% bp.getController().writeFormInput(out,null,"ti_anagrafico",false,null,"onClick=\"submitForm('doOnTipoAnagraficoChange')\"");%></td>	
	</tr>

	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>	
	
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_terzo"); %></td>
	<td colspan="3">
		<% bp.getController().writeFormInput( out, "cd_terzo"); %>		
		<% bp.getController().writeFormInput( out, "find_terzo"); %>
	</td>
	</tr>

	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_precedente"); %></td>
	<td><% bp.getController().writeFormInput( out, "cd_precedente"); %></td>
	</tr>
		
	<tr>
	<td><% bp.getController().writeFormLabel(out,"nome");%></td>
	<td><% bp.getController().writeFormInput(out,"nome");%></td>	
	<td><% bp.getController().writeFormLabel(out,"cognome");%></td>
	<td><% bp.getController().writeFormInput(out,"cognome");%></td>
	</tr>

	<tr>
	<td><% bp.getController().writeFormLabel(out,"codice_fiscale"); %></td>
	<td><% bp.getController().writeFormInput(out,"codice_fiscale"); %></td>
	<td><% bp.getController().writeFormLabel(out,"partita_iva"); %></td>
	<td><% bp.getController().writeFormInput(out,"partita_iva"); %></td>	
	</tr>

	<tr>
	<td><% bp.getController().writeFormLabel(out,"ragione_sociale"); %></td>
	<td colspan="3"><% bp.getController().writeFormInput(out,"ragione_sociale"); %></td>
	</tr>
	
    <tr>
	<td><% bp.getController().writeFormLabel(out,"via_fiscale");%></td>
	<td><% bp.getController().writeFormInput(out,"via_fiscale");%></td>
	<td><% bp.getController().writeFormLabel(out,"num_civico");%></td>
	<td><% bp.getController().writeFormInput(out,"num_civico");%></td>
    </tr>
    
   	<tr>
	<td><% bp.getController().writeFormLabel(out,"ds_comune");%></td>
	<td><% bp.getController().writeFormInput(out,"ds_comune");%></td>
	<td><% bp.getController().writeFormLabel(out,"ds_provincia");%></td>
	<td><% bp.getController().writeFormInput(out,"ds_provincia");%></td>
    </tr>

	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr></tr>	
	
 	<tr>
 	<td><% bp.getController().writeFormLabel(out,"termini_pagamento");%></td>
 	<td colspan="3"><% bp.getController().writeFormInput(out, null, "termini_pagamento", !anticipo.isEditable(), null, "");%></td> 	
   	</tr>

 	<tr>
 	<td><% bp.getController().writeFormLabel(out,"modalita_pagamento");%></td>
    <td colspan="3"><% bp.getController().writeFormInput(out,null,"modalita_pagamento", !anticipo.isEditable(),null,"onChange=\"submitForm('doOnModalitaPagamentoChange')\"");%>
		<% 	if (anticipo.getBanca() != null) 
			{
				bp.getController().writeFormInput(out, null, "listabanche", false, null, "");
			} %>
   	</td>    
   	</tr>
   	
    <tr>
 	<td colspan="4">
	<%	if (anticipo.getBanca() != null) 
		{
			if (Rif_modalita_pagamentoBulk.BANCARIO.equalsIgnoreCase(anticipo.getBanca().getTi_pagamento())) 
			{
	 	     	bp.getController().writeFormInput(out,"contoB");
			} 
			else if (Rif_modalita_pagamentoBulk.POSTALE.equalsIgnoreCase(anticipo.getBanca().getTi_pagamento())) 
			{
			   	bp.getController().writeFormInput(out,"contoP");
			} 
			else if (Rif_modalita_pagamentoBulk.QUIETANZA.equalsIgnoreCase(anticipo.getBanca().getTi_pagamento())) 
			{
			   	bp.getController().writeFormInput(out,"contoQ");
			} 
			else if (Rif_modalita_pagamentoBulk.ALTRO.equalsIgnoreCase(anticipo.getBanca().getTi_pagamento())) 
			{ 
			   	bp.getController().writeFormInput(out,"contoA");
			}
			else if (Rif_modalita_pagamentoBulk.IBAN.equalsIgnoreCase(anticipo.getBanca().getTi_pagamento())) 
			{ 
			   	bp.getController().writeFormInput(out,"contoN");
			}
			else if (Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(anticipo.getBanca().getTi_pagamento()) && anticipo.getBanca().isTABB())
            {
                bp.getController().writeFormInput(out,"contoB");
            }
  		} 
		else if (anticipo.getModalita_pagamento() != null && (anticipo.getV_terzo() != null && anticipo.getV_terzo().getCrudStatus() != anticipo.getV_terzo().UNDEFINED)) 
		{ %>
			<span class="FormLabel" style="color:red">
				Nessun riferimento trovato per la modalità di pagamento selezionata!
			</span>
	<%	} %>
  	</td>
	</tr>
 	
	<tr></tr>
</table>
</div>