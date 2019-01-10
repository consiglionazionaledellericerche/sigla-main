<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*"
%>
<%
	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
%>
<html>
    <head>
        <% JSPUtils.printBaseUrl(pageContext); %>
        <title>Firma tramite OTP</title>
    </head>
<body class="Workspace">
<% bp.openForm(pageContext);%>
    <div class="col-md-6 mx-auto">
        <div class="card">
          <h3 class="card-header"><i class="fa fa-question-circle fa-fw fa-2x text-info" aria-hidden="true"></i> Inserire l'utente, la password e l'otp per la firma del documento.</h3>
          <div class="card-block p-3">
            <div class="form-group row">
                <label class="col-sm-3 col-form-label"><% bp.getController().writeFormLabel(out,"userNameOTP"); %></label>
                <div class="col-sm-9">
                  <% bp.getController().writeFormInput(out,"userNameOTP"); %>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-3 col-form-label"><% bp.getController().writeFormLabel(out,"passwordOTP"); %></label>
                <div class="col-sm-9">
                  <% bp.getController().writeFormInput(out,"passwordOTP");%>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-3 col-form-label"><% bp.getController().writeFormLabel(out,"otp"); %></label>
                <div class="col-sm-9">
                  <% bp.getController().writeFormInput(out,"otp");%>
                </div>
            </div>
          </div>
        </div>
        <div class="card-footer border bg-white">
            <input type="button" class="btn btn-outline-danger col-5 d-inline-block" name="comando.doAnnulla" value="Annulla" onclick="submitForm('doAnnulla')">
            <input type="button" class="btn btn-outline-primary col-5 d-inline-block pull-right" name="comando.doConferma" value="Conferma" onclick="submitForm('doConferma')">
        </div>
    </div>
<% bp.closeForm(pageContext); %>
</body>