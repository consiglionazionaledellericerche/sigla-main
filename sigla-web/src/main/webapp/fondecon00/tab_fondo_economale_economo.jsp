<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		java.util.*,
		it.cnr.contab.fondecon00.bp.*,
		it.cnr.contab.fondecon00.core.bulk.*,
		it.cnr.contab.anagraf00.core.bulk.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*"
%>

<%
	FondoEconomaleBP bp = (FondoEconomaleBP)BusinessProcess.getBusinessProcess(request);
	Fondo_economaleBulk fondo = (Fondo_economaleBulk)bp.getModel();
	TerzoBulk economo = fondo.getEconomo();
	boolean roField = fondo.isOnlyForClose();
%>

<div class="Group p-2" style="width:100%">
    <table width="100%">
        <tr>
            <% bp.getController().writeFormField( out, "economo"); %>
        </tr>
        <tr>
            <% bp.getController().writeFormField( out, "cd_precedente_economo"); %>
        </tr>
        <tr>
            <% bp.getController().writeFormField( out, "codice_fiscale_economo"); %>
        </tr>
        <tr>
            <% bp.getController().writeFormField( out, "partita_iva_economo"); %>
        </tr>
        <tr>
            <td>
                <% bp.getController().writeFormLabel(out,"modalita_pagamento"); %>
            </td>
            <td>
                <% bp.getController().writeFormInput(out,null,"modalita_pagamento",roField,null,"onChange=\"submitForm('doOnModalitaPagamentoChange')\""); %>
                <% 	if (fondo.getBanca() != null) { %>
                        <% bp.getController().writeFormInput(out, null, "listabanche", false, null, ""); %>
                <%	} %>
            </td>
        </tr>
    </table>
    <table>
        <tr>
            <td>
                <%	if (fondo.getBanca() != null) { %>
                        <div class="Group card p-2 mt-2 border-info" style="width:100%">
                            <%	if (Rif_modalita_pagamentoBulk.BANCARIO.equalsIgnoreCase(fondo.getBanca().getTi_pagamento())) {
                                        bp.getController().writeFormInput(out,"contoB");
                                } else if (Rif_modalita_pagamentoBulk.POSTALE.equalsIgnoreCase(fondo.getBanca().getTi_pagamento())) {
                                        bp.getController().writeFormInput(out,"contoP");
                                } else if (Rif_modalita_pagamentoBulk.QUIETANZA.equalsIgnoreCase(fondo.getBanca().getTi_pagamento())) {
                                        bp.getController().writeFormInput(out,"contoQ");
                                } else if (Rif_modalita_pagamentoBulk.ALTRO.equalsIgnoreCase(fondo.getBanca().getTi_pagamento())) {
                                        bp.getController().writeFormInput(out,"contoA");
                                } else if (Rif_modalita_pagamentoBulk.IBAN.equalsIgnoreCase(fondo.getBanca().getTi_pagamento())) {
                                    bp.getController().writeFormInput(out,"contoN");
                                } else if (Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(fondo.getBanca().getTi_pagamento()) && fondo.getBanca().isTABB()) {
                                    bp.getController().writeFormInput(out,"contoN");
                                }
                            %>
                        </div>
                            <%
                            } else if (fondo.getModalita_pagamento() != null && (economo != null && economo.getCrudStatus() != economo.UNDEFINED)) { %>
                                <td colspan="5">
                                <span class="FormLabel" style="color:red">
                                    Nessun riferimento trovato per la modalità di pagamento selezionata!
                                </span>
                        <%	} %>
            </td>
        </tr>
    </table>
</div>
