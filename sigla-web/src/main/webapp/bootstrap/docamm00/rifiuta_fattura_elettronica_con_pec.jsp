<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.docamm00.fatturapa.bulk.*"
%>

<%
	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	RifiutaFatturaBulk bulk = (RifiutaFatturaBulk)bp.getModel();
%>
<html>
    <head>
        <% JSPUtils.printBaseUrl(pageContext); %>
        <title>Rifiuta Fattura</title>
    </head>
<body class="Workspace">
<% bp.openForm(pageContext);%>
    <div class="col-md-9 mx-auto mt-3">
        <div class="card card-shadow">
          <h3 class="card-header text-primary">
            <i class="fa fa-envelope-open fa-fw fa-2x" aria-hidden="true"></i> Invia PEC
          </h3>
          <div class="card-block p-3">
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">
                    <% bp.getController().writeFormLabel(out,"emailPEC"); %>
                </label>
                <div class="col-sm-10">
                    <% bp.getController().writeFormInput(out,"emailPEC"); %>
                </div>
            </div>
            <%if (bulk.isDecorrenzaTermini()) { %>
                <div class="form-group row">
                    <label class="col-sm-2 col-form-label text-danger">
                        <% bp.getController().writeFormLabel(out,"message_option_nullable"); %>
                    </label>
                    <div class="col-sm-10">
                        <% bp.getController().writeFormInput(out,"message_option_nullable"); %>
                    </div>
                </div>
            <%}%>
            <%if (!bulk.isMessageOptionSelected()) { %>
                <div class="form-group row">
                    <label class="col-sm-2 col-form-label text-danger">
                        <% bp.getController().writeFormLabel(out,bulk.isNota() ? "message_text_nota": "message_text"); %>
                    </label>
                    <div class="col-sm-10">
                        <% bp.getController().writeFormInput(out,bulk.isNota() ? "message_text_nota": "message_text"); %>
                    </div>
                </div>
            <%}%>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">
                    <% bp.getController().writeFormLabel(out,"note"); %>
                </label>
                <div class="col-sm-10">
                    <% bp.getController().writeFormInput(out,"note"); %>
                </div>
            </div>
          </div>
          <div class="card-footer border bg-white">
            <input type="button" class="btn btn-outline-danger col-5 d-inline-block" name="comando.doAnnulla" value="Annulla" onclick="submitForm('doAnnulla')">
            <input type="button" class="btn btn-outline-primary col-5 d-inline-block pull-right" name="comando.doConferma" value="Conferma" onclick="submitForm('doConferma')">
          </div>
        </div>
    </div>
<% bp.closeForm(pageContext); %>
</body>