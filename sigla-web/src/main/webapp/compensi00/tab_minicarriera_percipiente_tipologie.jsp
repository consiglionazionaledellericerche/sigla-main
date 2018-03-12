<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.contab.compensi00.docs.bulk.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*"
%>

<%	CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)BusinessProcess.getBusinessProcess(request);
	MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();
	it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk percipiente = carriera.getPercipiente();
%>

<div class="Group card p-2" style="width:100%">
    <table width="100%">
        <tr>
            <td width="25%"><% bp.getController().writeFormLabel(out,"fl_escludi_qvaria_deduzione");%></td>
            <td width="75%"><% bp.getController().writeFormInput(out,null,"fl_escludi_qvaria_deduzione",false,null,""); %></td>
        </tr>
        <tr>
            <% if (!bp.isSearching()) { %>
                <td>
                    <% bp.getController().writeFormLabel(out,"ti_istituz_commerc");%>
                </td>
                <td>
                    <% bp.getController().writeFormInput(out,null,"ti_istituz_commerc",false,null,"onChange=\"submitForm('doOnIstituzionaleCommercialeChange')\"");%>
                </td>
            <% } else { %>
                <td>
                    <% bp.getController().writeFormLabel(out,"ti_istituz_commercSearch");%>
                </td>
                <td>
                    <% bp.getController().writeFormInput(out,null,"ti_istituz_commercSearch",false,null,"onChange=\"submitForm('doOnIstituzionaleCommercialeChange')\"");%>
                </td>
            <% } %>
        </tr>
        <tr>
            <td width="25%"><% bp.getController().writeFormLabel(out,"fl_tassazione_separata");%></td>
            <td width="75%"><% bp.getController().writeFormInput(out,null,"fl_tassazione_separata",false,null,"onClick=\"submitForm('doOnFlTassazioneSeparataChange')\""); %></td>
        </tr>
        <tr>
            <td width="25%"><% bp.getController().writeFormLabel(out,"tipo_rapporto");%></td>
            <td width="75%"><% bp.getController().writeFormInput(out,null,"tipo_rapporto",false,null,"onChange=\"submitForm('doOnTipoRapportoChange')\""); %></td>
        </tr>
        <tr>
            <td width="25%"><% bp.getController().writeFormLabel(out,"tipo_trattamento");%></td>
            <td width="75%"><% bp.getController().writeFormInput(out,null,"tipo_trattamento",false,null,"onChange=\"submitForm('doOnTipoTrattamentoChange')\""); %></td>
        </tr>
        <% if (carriera.isVisualizzaPrestazione()) { %>
        <tr>
            <td width="25%"><% bp.getController().writeFormLabel(out,"tipoPrestazioneCompenso");%></td>
            <td width="75%"><% bp.getController().writeFormInput(out,null,"tipoPrestazioneCompenso",false,null,"onChange=\"submitForm('doOnTipoPrestazioneCompensoChange')\""); %></td>
        </tr>
        <% } %>


        <% if (carriera.isVisualizzaIncarico()) { %>
        <tr>
            <td colspan="2">
                <div class="Group" style="width:100%">
                    <fieldset class="fieldset">
                        <% if (carriera!=null && carriera.getCd_tipo_rapporto()!=null && carriera.getCd_tipo_rapporto().equals(new String ("BORS"))) { %>
                        <legend class="GroupLabel">Borsa di studio</legend>
                        <% } else { %>
                            <% if (carriera!=null && carriera.getCd_tipo_rapporto()!=null && carriera.getCd_tipo_rapporto().equals(new String ("ASS"))) { %>
                            <legend class="GroupLabel h3 text-primary">Assegno di ricerca</legend>
                            <% } else {%>
                                <legend class="GroupLabel h3 text-primary">Incarico</legend>
                            <% } %>
                        <% } %>
                        <table width="100%" class="p-1">
                            <tr><% bp.getController().writeFormField(out,"incarichi_repertorio"); %></tr>
                        </table>
                    </fieldset>
                </div>
            </td>
        </tr>
        <% } %>
    </table>
</div>
<div class="Group card p-2" style="width:100%">
    <table width="100%">
        <tr>
            <% bp.getController().writeFormField(out, "imponibile_irpef_eseprec1"); %>
            <% bp.getController().writeFormField(out, "imponibile_irpef_eseprec2"); %>
        </tr>
        <tr>
            <td colspan="2">
                <% JSPUtils.button(out,
                    bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-calculator" : "img/new16.gif",
                    bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-calculator" : "img/new16_d.gif",
                    "Calcola aliquota media",
                    "if (disableDblClick()) javascript:submitForm('doCalcolaAliquotaMedia')",
                    "btn-outline-primary btn-block btn-title",
                    ((bp.isInserting() || bp.isEditing()) && !carriera.isROImportiIrpef()),
                    bp.getParentRoot().isBootstrap()); %>
            </td>
            <td colspan="2">
                <% JSPUtils.button(out,
                    bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-eraser" : "img/new16.gif",
                    bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-eraser" : "img/new16_d.gif",
                    "Azzera aliquota media",
                    "if (disableDblClick()) javascript:submitForm('doAzzeraAliquotaMedia')",
                    "btn-outline-danger btn-block btn-title",
                    ((bp.isInserting() || bp.isEditing()) && !carriera.isROBottoniImportiIrpef()),
                    bp.getParentRoot().isBootstrap()); %>
            </td>
        </tr>
        <tr>
            <% bp.getController().writeFormField(out, "aliquota_irpef_media"); %>
        </tr>
    </table>
</div>