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
          <h3 class="card-header">
            <i class="fa fa-question-circle fa-fw fa-2x text-info" aria-hidden="true"></i> Inserire il motivo di rifiuto della fattura
          </h3>
          <div class="card-block p-3">
            <div class="form-group row">
                <div class="col-sm-12">
                  <%if (bulk.isMotivoLibero()) { %>
                    <% bp.getController().writeFormInput(out,"message_text"); %>
                  <%} else {%>
                    <% bp.getController().writeFormInput(out,"message_option"); %>
                  <%}%>
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