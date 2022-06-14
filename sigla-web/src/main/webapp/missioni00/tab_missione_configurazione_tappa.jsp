<!--
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*"%>

<%
    CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);
%>

<table width="100%">
    <tr>
        <td>
             <% bp.getTappaController().setEnabled( !bp.isEditingTappa());
                if((bp.isEditable()) && (!bp.isSearching()))
                {
                    bp.getTappaController().writeHTMLTable(pageContext, null,true,false,true,"100%","150px");
                }
                else
                {
                    // Visualizzazione missione
                    bp.getTappaController().writeHTMLTable(pageContext, null,false,false,false,"100%","150px");
                }
            %>
        </td>
    </tr>
</table>

<div class="Group" >
    <div class="Group card p-2 mt-2" style="width:100%">
        <table>
            <tr>
                <td><% bp.getTappaController().writeFormLabel( out, "dt_inizio_tappa"); %></td>
                <td><% bp.getTappaController().writeFormInput( out,"default","dt_inizio_tappa",!bp.isEditingTappa(),null, "onChange=\"submitForm('doCambiaDataTappa')\""); %></td>
            </tr>
        </table>
    </div>
    <div class="Group card p-2" style="width:100%">
        <table width="100%">
            <tr>
            <td><% bp.getTappaController().writeFormInput(out,"default","comuneRadioGroup",!bp.isEditingTappa(),null,"onClick=\"submitForm('doSetNazioneDivisaCambioItalia')\"");%></td>
            </tr>
        </table>

        <table width="100%">
            <tr>
            <td><% bp.getTappaController().writeFormLabel( out, "pg_nazione");%></td>
            <td><% bp.getTappaController().writeFormInput(out,"default","pg_nazione",bp.isNazioneReadOnly(),null, null);%>
                <% bp.getTappaController().writeFormInput(out,"default","ds_nazione",bp.isNazioneReadOnly(),null, null);%>
                <% bp.getTappaController().writeFormInput(out,"default","find_nazione",bp.isNazioneReadOnly(),null, null);%>
            </tr>

            <tr>
            <td><% bp.getTappaController().writeFormLabel( out, "cd_divisa_tappa");%></td>
            <td><% bp.getTappaController().writeFormInput(out,"cd_divisa_tappa");%>
                <% bp.getTappaController().writeFormInput(out,"ds_divisa_tappa");%>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <% bp.getTappaController().writeFormLabel( out, "cambio_tappa");%>
                <% bp.getTappaController().writeFormInput(out,"default","cambio_tappa",bp.isCambioTappaReadOnly(),null, null);%>
            </td>
            </tr>
        </table>
    </div>

    <div class="Group card p-2" style="width:100%">
        <table width="100%">
            <tr>
            <td><% bp.getTappaController().writeFormInput(out,"default","vittoAlloggioNavigazioneRadioGroup",!bp.isEditingTappa(),null, null);%></td>
            </tr>
        </table>
        <table width="100%">
            <tr>
            <td><% bp.getTappaController().writeFormLabel( out, "fl_no_diaria"); %>
                <% bp.getTappaController().writeFormInput(out,"default","fl_no_diaria",!bp.isEditingTappa()||!bp.isDiariaEditable(HttpActionContext.getUserContext(session)),null, "onChange=\"submitForm('doOnFlDiariaChange')\"");%></td>
                <% if (bp.isRimborsoVisible(HttpActionContext.getUserContext(session))) { %>
                     <td><% bp.getTappaController().writeFormInput(out,"default","tipoRimborsoRadioGroup",!bp.isEditingTappa()||!bp.isRimborsoEditable(HttpActionContext.getUserContext(session)),null, null);%></td>
                <% } %>
            </tr>
        </table>
    </div>
    <div class="Group card p-2" style="width:100%">
        <table width="100%">
            <tr>
                <td>
                    <% bp.getTappaController().writeFormLabel( out, "dt_ingresso_estero"); %>
                    <% bp.getTappaController().writeFormInput(out,"default","dt_ingresso_estero",bp.isDataIngressoAbilitata(),null, null);%>
                </td>
                <td>
                    <% bp.getTappaController().writeFormLabel( out, "dt_uscita_estero"); %>
                    <% bp.getTappaController().writeFormInput(out,"default","dt_uscita_estero",bp.isDataIngressoAbilitata(),null, null);%>
                </td>
            </tr>
        </table>
    </div>
    <div class="card w-100">
        <table width="100%">
            <tr>
                <td ALIGN="CENTER">
                        <div class="btn-group m-2" role="group">
                        <% JSPUtils.button(out,
                                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-pencil-square-o text-success" : "img/edit24.gif",
                                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-pencil-square-o text-success" : "img/edit24.gif",
                                "Modifica",
                                "javascript:submitForm('doEditaTappa')",
                                "btn-outline-secondary btn-title",
                                bp.isEditTappaButtonEnabled(),
                                bp.getParentRoot().isBootstrap()); %>
                        <% JSPUtils.button(out,
                                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/save24.gif",
                                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/save24.gif",
                                "Conferma",
                                "javascript:submitForm('doConfermaTappa')",
                                "btn-outline-secondary btn-title",
                                bp.isConfermaTappaButtonEnabled(),
                                bp.getParentRoot().isBootstrap()); %>
                        <% JSPUtils.button(out,
                                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-undo text-warning" : "img/undo24.gif",
                                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-undo text-warning" : "img/undo24.gif",
                                "Annulla",
                                "javascript:submitForm('doUndoTappa')",
                                "btn-outline-secondary btn-title",
                                bp.isUndoTappaButtonEnabled(),
                                bp.getParentRoot().isBootstrap()); %>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</div>