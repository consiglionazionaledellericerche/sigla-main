<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Gestione sospesi CNR</title>
<body class="Form">

<%  it.cnr.contab.doccont00.bp.CRUDSospesoCNRBP bp = (it.cnr.contab.doccont00.bp.CRUDSospesoCNRBP)BusinessProcess.getBusinessProcess(request);
    it.cnr.contab.doccont00.core.bulk.SospesoBulk sospeso = (it.cnr.contab.doccont00.core.bulk.SospesoBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>
   
	<div class="Group card p-2 w-100">
        <table class="Panel">
            <tr>
                <td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
                <td><% bp.getController().writeFormInput( out, "esercizio"); %></td>
                <td><% bp.getController().writeFormLabel( out, "cd_sospeso"); %></td>
                <td><% bp.getController().writeFormInput( out, "cd_sospeso"); %></td>
                <td><% bp.getController().writeFormLabel( out, "im_sospeso"); %></td>
                <td><% bp.getController().writeFormInput( out,"im_sospeso"); %></td>
            </tr>
            <tr>
                <td><% bp.getController().writeFormLabel( out, "ti_entrata_spesa"); %></td>
                <td><% bp.getController().writeFormInput( out, "ti_entrata_spesa"); %></td>
                <td><% bp.getController().writeFormLabel( out, "dt_registrazione"); %></td>
                <td><% bp.getController().writeFormInput( out, "dt_registrazione"); %></td>
                <td><% bp.getController().writeFormLabel( out, "ti_cc_bi");%></td>
                <td><% bp.getController().writeFormInput( out, "ti_cc_bi");%></td>
            </tr>
            <tr>
                <td><% bp.getController().writeFormLabel( out, "causale"); %></td>
                <td colspan=5><% bp.getController().writeFormInput(out,null,"causale",true,null,null);  %></td>
            </tr>
            <tr>
                <td><% bp.getController().writeFormLabel( out, "ds_anagrafico"); %></td>
                <td colspan=5><% bp.getController().writeFormInput(out,null,"ds_anagrafico",true,null,null); %></td>
            </tr>
        </table>
    </div>
    <table border="0" cellspacing="0" cellpadding="2" class="w-100">
        <tr>
            <td><% bp.getSospesiFigli().writeHTMLTable(pageContext,"SospesiCNR2", true,false,true,"100%","100px", true); %></td>
        </tr>
    </table>
    <div class="Group card p-2 w-100">
        <table border="0" cellspacing="0" cellpadding="2">
            <tr>
                <td><% bp.getSospesiFigli().writeFormLabel( out, "im_sospeso_figlio"); %></td>
                <td><% bp.getSospesiFigli().writeFormInput( out,"im_sospeso_figlio"); %></td>
            </tr>
            <tr>
                <td><% bp.getSospesiFigli().writeFormLabel( out, "stato_sospesoCNR"); %></td>
                <td><% bp.getSospesiFigli().writeFormInput( out,null,"stato_sospesoCNR",false,null,"onchange=\"submitForm('doCambiaStatoCNR')\""); %></td>
            </tr>
            <tr>
                <td><% bp.getSospesiFigli().writeFormLabel( out, "cd_cds_origine"); %></td>
                <td><% bp.getSospesiFigli().writeFormInput( out, "find_cds_origine"); %></td>
            </tr>
            <tr>
                <td><% bp.getSospesiFigli().writeFormLabel( out, "find_mandato_riaccredito"); %></td>
                <td><% bp.getSospesiFigli().writeFormInput( out, "find_mandato_riaccredito"); %></td>
            </tr>
            <tr>
                <td><% bp.getSospesiFigli().writeFormLabel( out, "cd_avviso_pagopa"); %></td>
                <td colspan=5><% bp.getSospesiFigli().writeFormInput(out,null,"cd_avviso_pagopa",true,null,null); %></td>
            </tr>
        </table>
	</div>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>