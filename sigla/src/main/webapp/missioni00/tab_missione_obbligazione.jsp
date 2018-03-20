<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*, it.cnr.jada.bulk.*, it.cnr.contab.docamm00.bp.*" %>

<%  
    CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);
    MissioneBulk missione = (MissioneBulk) bp.getModel();

    if(missione == null)
        missione = new MissioneBulk();
%>

<div class="Group card p-2" style="width:100%">
    <table>
     <% if (bp.isSearching())
        {
          boolean isInSpesaMode = (bp instanceof IDocumentoAmministrativoSpesaBP && ((IDocumentoAmministrativoSpesaBP)bp).isSpesaBP()) ? true : false; %>
            <tr>
            <td><% bp.getController().writeFormLabel(out,"stato_pagamento_fondo_ecoForSearch");%></td>
            <td><% bp.getController().writeFormInput(out,null,"stato_pagamento_fondo_ecoForSearch", isInSpesaMode, null,""); %></td>
            </tr>

      <% } else { %>

            <tr>
            <td><% bp.getController().writeFormLabel(out,"stato_pagamento_fondo_eco");%></td>
            <td><% bp.getController().writeFormInput(out,null,"stato_pagamento_fondo_eco",false,null,"onChange=\"submitForm('doDefault')\"");%></td>
            </tr>

            <tr>
            <td><% bp.getController().writeFormLabel(out,"dt_pagamento_fondo_eco"); %></td>
            <td><% bp.getController().writeFormInput(out,"dt_pagamento_fondo_eco"); %></td>
            </tr>
      <% } %>
    </table>
</div>

<fieldset class="mt-2">
    <legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px; color:blue"><span class="h4 text-info ml-2">Scadenza Impegno<span></legend>
    <div class="Panel card p-2" style="width:100%">
        <table width="100%">
            <tr>
                <td><% bp.getController().writeFormLabel( out, "esercizio_ori_obbligazione"); %></td>
                <td><% bp.getController().writeFormInput( out, "esercizio_ori_obbligazione"); %></td>
                <td><% bp.getController().writeFormLabel( out, "pg_obbligazione"); %></td>
                <td><% bp.getController().writeFormInput( out, "pg_obbligazione"); %></td>
            </tr>
            <tr>
                <td><% bp.getController().writeFormLabel(out, "pg_obbligazione_scadenzario");%></td>
                <td><% bp.getController().writeFormInput(out, "pg_obbligazione_scadenzario");%></td>
                <td><% bp.getController().writeFormLabel(out, "ds_scadenza_obbligazione");%></td>
                <td><% bp.getController().writeFormInput(out, "ds_scadenza_obbligazione");%></td>
                <td><% bp.getController().writeFormLabel(out, "im_scadenza");%></td>
                <td><% bp.getController().writeFormInput(out, "im_scadenza");%></td>
            </tr>

            <tr>
                <td><% bp.getController().writeFormLabel(out, "dt_scadenza");%></td>
                <td><% bp.getController().writeFormInput(out, "dt_scadenza");%></td>
                <td><% bp.getController().writeFormLabel(out, "esercizio_obbligazione");%></td>
                <td><% bp.getController().writeFormInput(out, "esercizio_obbligazione");%></td>
                <td><% bp.getController().writeFormLabel(out, "cd_cds_obbligazione");%></td>
                <td><% bp.getController().writeFormInput(out, "cd_cds_obbligazione");%></td>
            </tr>
        </table>
        <table width="100%" class="mt-2">
            <tr>
                <td ALIGN="CENTER">
                    <% JSPUtils.button(out,
                            bp.getParentRoot().isBootstrap()?"fa fa-fw fa-plus":"img/new24.gif",
                            bp.getParentRoot().isBootstrap()?"fa fa-fw fa-plus":"img/new24.gif",
                            bp.getParentRoot().isBootstrap()?"Crea/Ricerca impegno":"Crea/Ricerca<br>impegno",
                            "if (disableDblClick()) submitForm('doRicercaScadenzaObbligazione')",
                            "btn-outline-primary btn-title",
                            bp.areBottoniObbligazioneAbilitati(),
                            bp.getParentRoot().isBootstrap());%>
                    <% JSPUtils.button(out,
                            bp.getParentRoot().isBootstrap()?"fa fa-fw fa-trash":"img/remove24.gif",
                            bp.getParentRoot().isBootstrap()?"fa fa-fw fa-trash":"img/remove24.gif",
                            bp.getParentRoot().isBootstrap()?"Elimina impegno":"Elimina<br>impegno",
                            "if (disableDblClick()) submitForm('doEliminaScadenzaObbligazione')",
                            "btn-outline-danger btn-title",
                            bp.areBottoniObbligazioneAbilitati(),
                            bp.getParentRoot().isBootstrap());%>
                    <% JSPUtils.button(out,
                            bp.getParentRoot().isBootstrap()?"fa fa-fw fa-repeat":"img/redo24.gif",
                            bp.getParentRoot().isBootstrap()?"fa fa-fw fa-repeat":"img/redo24.gif",
                            bp.getParentRoot().isBootstrap()?"Aggiorna in manuale":"Aggiorna in<br>manuale",
                            "if (disableDblClick()) submitForm('doOpenObbligazioniWindow')",
                            "btn-outline-info btn-title",
                            bp.isBottoneObbligazioneAggiornaManualeAbilitato(),
                            bp.getParentRoot().isBootstrap());%>
                    <% JSPUtils.button(out,
                            bp.getParentRoot().isBootstrap()?"fa fa-fw fa-refresh":"img/refresh24.gif",
                            bp.getParentRoot().isBootstrap()?"fa fa-fw fa-refresh":"img/refresh24.gif",
                            bp.getParentRoot().isBootstrap()?"Aggiorna in automatico":"Aggiorna in<br>automatico",
                            "if (disableDblClick()) submitForm('doModificaScadenzaInAutomatico')",
                            "btn-outline-secondary btn-title",
                            bp.areBottoniObbligazioneAbilitati(),
                            bp.getParentRoot().isBootstrap());%>
                </td>
            </tr>
        </table>
    </div>
</fieldset>

<fieldset class="mt-2">
    <legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px; color:blue"><span class="h4 text-info ml-2">Compenso</span></legend>
    <div class="Panel card p-2" style="width:100%">
        <table width="100%">
            <tr>
                <td><% bp.getController().writeFormLabel( out, "pg_compenso"); %></td>
                <td><% bp.getController().writeFormInput( out, "pg_compenso"); %></td>
                <td><% bp.getController().writeFormLabel( out, "esercizio_compenso"); %></td>
                <td><% bp.getController().writeFormInput( out, "esercizio_compenso"); %></td>
            </tr>

            <tr>
                <td><% bp.getController().writeFormLabel( out, "cd_uo_compenso"); %></td>
                <td><% bp.getController().writeFormInput( out, "cd_uo_compenso"); %></td>
                <td><% bp.getController().writeFormLabel( out, "cd_cds_compenso"); %></td>
                <td><% bp.getController().writeFormInput( out, "cd_cds_compenso"); %></td>
            </tr>
        </table>
        <table width="100%" class="mt-2">
            <tr>
                <td ALIGN="CENTER">
                    <% JSPUtils.button(out,
                        bp.getParentRoot().isBootstrap()?"fa fa-fw fa-eye":null,
                        bp.getParentRoot().isBootstrap()?"fa fa-fw fa-eye":null,
                        "Visualizza compenso",
                        "javascript:submitForm('doVisualizzaCompenso')",
                        "btn-outline-primary btn-title",
                        true,
                        bp.getParentRoot().isBootstrap()); %>
                </td>
            </tr>
        </table>
    </div>
</fieldset>

<fieldset class="mt-2">
<legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px; color:blue"><span class="h4 text-info ml-2">Anticipo</span></legend>
    <div class="Panel card p-2" style="width:100%">
        <table width="100%">
            <tr>
                <td><% bp.getController().writeFormLabel( out, "pg_anticipo"); %></td>
                <td><% bp.getController().writeFormInput(out,null,"find_anticipo", bp.areCampiAnticipoReadonly(),null,""); %></td>
                <td><% bp.getController().writeFormLabel(out, "im_anticipo");%></td>
                <td><% bp.getController().writeFormInput(out, "im_anticipo");%></td>
            </tr>
            <tr>
                <td><% bp.getController().writeFormLabel(out, "esercizio_anticipo");%></td>
                <td><% bp.getController().writeFormInput(out, "esercizio_anticipo");%></td>
                <td><% bp.getController().writeFormLabel(out, "cd_cds_anticipo");%></td>
                <td><% bp.getController().writeFormInput(out, "cd_cds_anticipo");%></td>
                <td><% bp.getController().writeFormLabel(out, "cd_uo_anticipo");%></td>
                <td><% bp.getController().writeFormInput(out, "cd_uo_anticipo");%></td>
            </tr>
        </table>
        <table width="100%" class="mt-2">
            <tr>
            <td ALIGN="CENTER">
                <% JSPUtils.button(out,
                    bp.getParentRoot().isBootstrap()?"fa fa-fw fa-eye": null,
                    bp.getParentRoot().isBootstrap()?"fa fa-fw fa-eye": null,
                    "Visualizza anticipo",
                    "javascript:submitForm('doVisualizzaAnticipo')",
                    "btn-outline-primary btn-title",
                    true,
                    bp.getParentRoot().isBootstrap()); %>
            </td>
            </tr>
        </table>
    </div>
</fieldset>