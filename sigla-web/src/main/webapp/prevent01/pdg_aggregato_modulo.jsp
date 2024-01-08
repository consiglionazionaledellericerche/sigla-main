<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.config00.sto.bulk.*,
		it.cnr.contab.prevent01.action.*,
		it.cnr.contab.prevent01.bp.*,
		it.cnr.contab.prevent01.bulk.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Piano di Gestione Preliminare</title>
</head>
<body class="Form">

<%
	CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = ((CRUDPdGAggregatoModuloBP)bp).getCrudDettagli();
	bp.openFormWindow(pageContext);
	CdrBulk cdr = (CdrBulk)bp.getModel();
	boolean pdg_selezionato = ((Pdg_moduloBulk)controller.getModel()) != null && ((Pdg_moduloBulk)controller.getModel()).getCrudStatus() == OggettoBulk.NORMAL;
%>

<div class="Group">
	<table class="Panel card p-2 w-100">
		<tr>
		    <td><% bp.getController().writeFormLabel(out,"cd_cdr_ro");%></td>
		    <td class="w-100">
		        <div class="input-group input-group-searchtool w-100">
		            <% bp.getController().writeFormInput(out,"cd_cdr_ro");%>
		            <% bp.getController().writeFormInput(out,"ds_cdr_ro");%>
		        </div>
		    </td>
		</tr>
	</table>
</div>

<div class="Group">
	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<td>
		<%	if (bp.getParametriCnr().getFl_nuovo_pdg()) 
				controller.writeHTMLTable(pageContext,"prg_liv2",true,false,true,"100%","auto;max-height:50vh");
			else
				controller.writeHTMLTable(pageContext,null,true,false,true,"100%","auto;max-height:50vh");
		%>
		</td>
	</table>
</div>

<div class="Group card">
	<table border="0" cellspacing="0" cellpadding="2" class="w-100">
		<tr>
			<td>
			<%	if (bp.getParametriCnr().getFl_nuovo_pdg()) 
					controller.writeFormField(out,"searchtool_progetto_liv2");
				else
					controller.writeFormField(out,"searchtool_progetto");
			%>
			</td>
		</tr>
	</table>
	<table border="0" cellspacing="0" cellpadding="2" width="100%">
		<tr>
			<td colspan="5" width="45%">
                <div class="card">
                    <div class="GroupLabel card-header"><span class="font-weight-bold text-primary d-flex justify-content-center">Stato del PdG</span></div>
                    <div class="Group card-block p-2">
                        <table>
                            <tr>
                                <td><% controller.writeFormLabel(out,"cambia_stato");%></td>
                                <td><% controller.writeFormInput( out, null, "cambia_stato", bp.isROStato(), null, null);%></td>
                                <td>
                                    <center>
                                    <%JSPUtils.button(out,
                                        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-undo fa-flip-horizontal" : "img/import24.gif",
                                        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-undo fa-flip-horizontal" : "img/import24.gif",
                                        "Cambia Stato",
                                        "if (disableDblClick()) javascript:submitForm('doCambiaStato')",
                                        "btn-outline-info btn-title btn-block",
                                        !bp.isROStato(),
                                        bp.getParentRoot().isBootstrap());%>
                                    </center>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
			</td>
			<td>
                <div class="card">
                    <div class="GroupLabel card-header"><span class="font-weight-bold text-primary d-flex justify-content-center">Contrattazione</span></div>
                    <div class="Group card-block p-2">
                        <table width="100%" style="text-align: center;">
                            <tr>
                                <td>
                                    <%JSPUtils.button(out,
                                            bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-money" : "img/compressed.gif",
                                            bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-money" : "img/compressed.gif",
                                            "Entrate",
                                            "if (disableDblClick()) submitForm('doContrattazioneEntrate')",
                                            "btn-outline-primary btn-title btn-block",
                                            pdg_selezionato && bp.isPrevEntrataEnable(HttpActionContext.getUserInfo(request)),
                                            bp.getParentRoot().isBootstrap());%>
                                </td>
                                <td>
                                    <%JSPUtils.button(out,
                                            bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-briefcase" : "img/transfer.gif",
                                            bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-briefcase" : "img/transfer.gif",
                                            "Spese",
                                            "if (disableDblClick()) submitForm('doContrattazioneSpese')",
                                            "btn-outline-primary btn-title btn-block",
                                            pdg_selezionato && bp.isPrevSpesaEnable(HttpActionContext.getUserInfo(request)),
                                            bp.getParentRoot().isBootstrap());%>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
			</td>
		</tr>
		<tr>
			<td colspan=5>
                <div class="GroupLabel">
                    <span class="font-weight-bold text-primary d-flex justify-content-center animated bounceInLeft"><%=bp.getHintProgetto(HttpActionContext.getUserInfo(request))%></span>
                </div>
			</td>
			<td>
                <div class="card">
                    <div class="GroupLabel card-header"><span class="font-weight-bold text-primary d-flex justify-content-center">Gestionale</span></div>
                    <div class="Group card-block p-2">
                        <table width="100%" style="text-align: center;">
                            <tr>
                                <td>
                                    <%JSPUtils.button(out,
                                            bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-money" : "img/compressed.gif",
                                            bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-money" : "img/compressed.gif",
                                            "Entrate",
                                            "if (disableDblClick()) submitForm('doGestionaleEntrate')",
                                            "btn-outline-primary btn-title btn-block",
                                            pdg_selezionato && bp.isGestionaleAccessibile() && bp.isPrevEntrataEnable(HttpActionContext.getUserInfo(request)),
                                            bp.getParentRoot().isBootstrap());%>
                                </td>
                                <td>
                                    <%JSPUtils.button(out,
                                            bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-briefcase" : "img/transfer.gif",
                                            bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-briefcase" : "img/transfer.gif",
                                            "Spese",
                                            "if (disableDblClick()) submitForm('doGestionaleSpese')",
                                            "btn-outline-primary btn-title btn-block",
                                            pdg_selezionato && bp.isGestionaleAccessibile() && bp.isPrevSpesaEnable(HttpActionContext.getUserInfo(request)),
                                            bp.getParentRoot().isBootstrap());%>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
			</td>
		</tr>
	</table>
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>