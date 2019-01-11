<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*, it.cnr.jada.bulk.*, it.cnr.contab.anagraf00.tabrif.bulk.*"%>

<%  
	CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);
	MissioneBulk missione = (MissioneBulk) bp.getModel();

	if(missione == null)
		missione = new MissioneBulk();
%>

<div class="Group card p-2" style="width:100%">
<table width="100%">
	<tr>
        <td colspan="4"><% bp.getController().writeFormInput(out,null,"ti_anagrafico",false,null,"onClick=\"submitForm('doOnTipoAnagraficoChange')\"");%></td>
	</tr>
	
	<tr>
        <td><% bp.getController().writeFormLabel( out, "find_terzo"); %></td>
        <td colspan="3">
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

 	<tr>
        <td><% bp.getController().writeFormLabel(out,"termini_pagamento");%></td>
        <td colspan="3"><% bp.getController().writeFormInput(out,null,"termini_pagamento",false,null,"onChange=\"submitForm('doOnTerminiPagamentoChange')\"");%>
   	</tr>

 	<tr>
        <td><% bp.getController().writeFormLabel(out,"modalita_pagamento");%></td>
        <td colspan="3"><% bp.getController().writeFormInput(out,null,"modalita_pagamento",false,null,"onChange=\"submitForm('doOnModalitaPagamentoChange')\"");%>
            <% 	if (missione.getBanca() != null)
                {
                    bp.getController().writeFormInput(out, null, "listabanche", false, null, "onChange=\"submitForm('doOnBancaChange')\"");
                } %>
        </td>
   	</tr>
  	<tr>
        <td colspan="4">
            <%	if (missione.getBanca() != null) {%>
                    <div class="Group card p-2 mt-2 border-info" style="width:100%">
                    <%
                    if (Rif_modalita_pagamentoBulk.BANCARIO.equalsIgnoreCase(missione.getBanca().getTi_pagamento()))
                    {
                        bp.getController().writeFormInput(out,"contoB");
                    }
                    else if (Rif_modalita_pagamentoBulk.POSTALE.equalsIgnoreCase(missione.getBanca().getTi_pagamento()))
                    {
                        bp.getController().writeFormInput(out,"contoP");
                    }
                    else if (Rif_modalita_pagamentoBulk.QUIETANZA.equalsIgnoreCase(missione.getBanca().getTi_pagamento()))
                    {
                        bp.getController().writeFormInput(out,"contoQ");
                    }
                    else if (Rif_modalita_pagamentoBulk.ALTRO.equalsIgnoreCase(missione.getBanca().getTi_pagamento()))
                    {
                        bp.getController().writeFormInput(out,"contoA");
                    }
                    else if (Rif_modalita_pagamentoBulk.IBAN.equalsIgnoreCase(missione.getBanca().getTi_pagamento()))
                    {
                        bp.getController().writeFormInput(out,"contoN");
                    }
                    else if (Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(missione.getBanca().getTi_pagamento()) && missione.getBanca().isTABB())
                    {
                        bp.getController().writeFormInput(out,"contoB");
                    }
                %>
                    </div>
                <%
                } else if (missione.getModalita_pagamento() != null && (missione.getV_terzo() != null && missione.getV_terzo().getCrudStatus() != missione.getV_terzo().UNDEFINED)){ %>
                    <span class="FormLabel" style="color:red">
                        Nessun riferimento trovato per la modalità di pagamento selezionata!
                    </span>
            <%	} %>
        </td>
	</tr>
</table>
</div>

<div class="Group card p-2" style="width:100%">
    <table width="100%">
        <tr>
            <td class="text-right"><% bp.getController().writeFormLabel( out, "tipo_rapporto"); %></td>
            <td><% bp.getController().writeFormInput( out, null,"tipo_rapporto",false,null,"onChange=\"submitForm('doOnTipoRapportoChange')\"");%></td>
            <td class="text-right"><% bp.getController().writeFormLabel( out, "rif_inquadramento"); %></td>
            <td><% bp.getController().writeFormInput( out, null,"rif_inquadramento",false,null,"onChange=\"submitForm('doOnInquadramentoChange')\"");%></td>
        </tr>
    </table>
</div>