<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>

	<head>

		<% JSPUtils.printBaseUrl(pageContext); %>

		<script language="JavaScript" src="scripts/util.js"></script>
		<script language="JavaScript">
			function selezionaCondizione(riga) {
				document.mainForm.rigaSelezionata.value = riga;
				submitForm("doSelezionaCondizione");
		}
		</script>

		<script language="javascript" src="scripts/css.js"></script>

		<title>Ricerca guidata</title>

	</head>

	<body class="Form">

<% 	RicercaLiberaBP bp = (RicercaLiberaBP)BusinessProcess.getBusinessProcess(request); 
	CondizioneRicercaBulk condizioneRicerca = bp.getCondizioneCorrente();
	bp.openFormWindow(pageContext); %>

	<table class="Panel card p-2" width="100%" height="100%">
	<tr><td>
		<input type=hidden name="rigaSelezionata">
		<% if (bp.getParentRoot().isBootstrap()) { %>
		<div class="form-inline">
		    <div class="form-group mx-sm-3 mb-2">
                <label class="col-form-label text-primary font-weight-bold">Condizione:</label>
            </div>
            <%	if (!condizioneRicerca.isPrimaCondizione()) { %>
                <div class="form-group mx-sm-1 mb-2">
                <%condizioneRicerca.writeFormInput(out, null, "logicalOperator", false,
                        "form-control", null, null, FormController.EDIT,bp.getFieldValidationMap(), bp.getParentRoot().isBootstrap());%>
                </div>
            <%	}
            if (condizioneRicerca instanceof CondizioneSempliceBulk) {
                CondizioneSempliceBulk condizione = (CondizioneSempliceBulk)condizioneRicerca;%>
                <div class="form-group mx-sm-1 mb-2">
                            <% condizione.writeFormInput(bp.getParent(),
                                    out,
                                    "default",
                                    "findFieldProperty",
                                    false,
                                    "form-control",
                                    "onchange=\"submitForm('doImpostaAttributo')\"",
                                    null,
                                    FormController.EDIT,
                                    bp.getFieldValidationMap(),
                                    bp.getParentRoot().isBootstrap());%>
                </div>
                <div class="form-group mx-sm-1 mb-2">
                            <% condizione.writeFormInput(out,
                                        "default",
                                        "operator",
                                        condizione.getFindFieldProperty() == null,
                                        "form-control",
                                        "onchange=\"submitForm('doImpostaAttributo')\"",
                                        null,
                                        FormController.EDIT,
                                        bp.getFieldValidationMap(),
                                        bp.getParentRoot().isBootstrap());
                                        %>
                </div>
                <% if (condizione.getOperator() != null && condizione.getOperator().intValue() != it.cnr.jada.persistency.sql.SQLBuilder.ISNULL &&
                                condizione.getOperator().intValue() != it.cnr.jada.persistency.sql.SQLBuilder.ISNOTNULL) { %>
                                <div class="form-group mx-sm-1 mb-2">
                                <%
                                        condizione.getFindFieldProperty().writeInput(out,
                                                    condizione.getPrototype(),
                                                    condizione.getValue(),
                                                    false,
                                                    "form-control",
                                                    null,
                                                    null,
                                                    FormController.FREESEARCH,
                                                    bp.getFieldValidationMap(),
                                                    bp.getParentRoot().isBootstrap());
                                %>
                                </div>
                <%	} %>
            <%	} %>
        </div>
		<% } else { %>
		<table class="ToolBar" width="100%">
			<tr>
				<td><span class="FormLabel">Condizione:</span></td>
					<%	if (!condizioneRicerca.isPrimaCondizione()) { %>
						<td><%condizioneRicerca.writeFormInput(out, null, "logicalOperator", false,
								null, null, null, FormController.EDIT,bp.getFieldValidationMap(), bp.getParentRoot().isBootstrap());%>
						</td>
					<%	}
						if (condizioneRicerca instanceof CondizioneSempliceBulk) {
							CondizioneSempliceBulk condizione = (CondizioneSempliceBulk)condizioneRicerca;
					%>
									<td>
									    <% condizione.writeFormInput(bp.getParent(),
									            out,
									            "default",
									            "findFieldProperty",
									            false,
									            null,
									            "onchange=\"submitForm('doImpostaAttributo')\"",
									            null,
									            FormController.EDIT,
									            bp.getFieldValidationMap(),
									            bp.getParentRoot().isBootstrap());%>
									</td>
									<td>
									    <% condizione.writeFormInput(out,
									            "default",
									            "operator",
									            condizione.getFindFieldProperty() == null,
									            null,
									            "onchange=\"submitForm('doImpostaAttributo')\"",
									            null,
									            FormController.EDIT,
									            bp.getFieldValidationMap(),
									            bp.getParentRoot().isBootstrap());
									            %>
									</td>
									<td><% if (condizione.getOperator() != null && condizione.getOperator().intValue() != it.cnr.jada.persistency.sql.SQLBuilder.ISNULL && condizione.getOperator().intValue() != it.cnr.jada.persistency.sql.SQLBuilder.ISNOTNULL)
												condizione.getFindFieldProperty().writeInput(out,
												            condizione.getPrototype(),
												            condizione.getValue(),
												            false,
												            null,
												            null,
												            null,
												            FormController.FREESEARCH,
												            bp.getFieldValidationMap(),
												            bp.getParentRoot().isBootstrap());
								        %>
								    </td>
					<%	} %>
				<td width="100%">&nbsp;</td>
			</tr>
		</table>
		<% } %>
	</td></tr>
	<tr valign="top"><td height="100%">
		<% if (bp.getParentRoot().isBootstrap()) { %>
			<div>
				<%	JSPUtils.treeBootstrap(
						out,
						bp.getNumeroLivelli(),
						"selezionaCondizione",
						"fa fw fa-angle-double-right text-primary",
						bp.getRigaSelezionata(),
						bp.getRighe()); %>
			</div>
		<%} else { %>
		<div style="width:100%; height:100%; overflow:auto; background-color: white; border: thin inset; border-color:buttonface;">
			<table bgcolor=white cellspacing="0" cellpadding="0" border=0>
			<tr valign="top"><td>
			<%	JSPUtils.tree(
					out,
					bp.getNumeroLivelli(),
					"selezionaCondizione",
					"img/treejoinl24.gif",
					"img/treejoint24.gif",
					"img/treejoini24.gif",
					"img/clausola16.gif",
					null,
					null,
					bp.getRigaSelezionata(),
					bp.getRighe(),
					bp.getParentRoot().isBootstrap()); %>
			</td></tr>
			</table>
		</div>
		<% } %>
	</td></tr>
	</table>
	<%	bp.closeFormWindow(pageContext); %>
	</body>
</html>