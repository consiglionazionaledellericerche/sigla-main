<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.utenze00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Gestione Accessi per Ruolo</title>
<body class="Form">

<% CRUDRuoloBP bp = (CRUDRuoloBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>
    <div class="card p-2 mt-2">
        <table class="Panel w-100">
            <tr>
                <td><% bp.getController().writeFormLabel( out, "cd_ruolo"); %></td>
                <td><% bp.getController().writeFormInput( out, "cd_ruolo"); %></td>
            </tr>
            <tr>
                <td><% bp.getController().writeFormLabel( out, "ds_ruolo"); %></td>
                <td><% bp.getController().writeFormInput( out, "ds_ruolo"); %></td>
            </tr>
            <tr>
                <td><% bp.getController().writeFormLabel( out, "tipo_ruolo"); %></td>
                <td><% bp.getController().writeFormInput( out, "tipo_ruolo"); %></td>
            </tr>
            <tr>
                 <td><% bp.getController().writeFormLabel( out, "find_cds"); %></td>
                 <td><% bp.getController().writeFormInput( out, null, "find_cds",!bp.isCdsFieldEnabled(),null,null); %></td>
            </tr>
        </table>
    </div>
    <div class="card p-2 mt-2">
        <table class="Form" width="100%">
          <tr>
                <td><span class="GroupLabel text-primary h2">Accessi disponibili</span></td>
                <td></td>
                <td><span class="GroupLabel text-primary h2">Accessi assegnati</span></td>
          </tr>
          <tr>
                <td rowspan="2">
                    <%	bp.getCrudAccessi_disponibili().writeHTMLTable(pageContext,null,false,true,false,"100%","300px"); %>
                </td>
                <td>
                    <% JSPUtils.button(out,
                          bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-right faa-passing" : "img/doublerightarrow24.gif",
                          bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-right" : "img/doublerightarrow24.gif",
                          null,
                          "javascript:submitForm('doAggiungiAccesso')",
                          "btn-outline-primary faa-parent animated-hover btn-block",
                          true,
                          bp.getParentRoot().isBootstrap()); %>
                </td>
                <td rowspan="2">
                    <%	bp.getCrudAccessi().writeHTMLTable(pageContext,null,false,false,false,"100%","300px"); %>
                </td>
            </tr>
            <tr>
                <td>
                    <% JSPUtils.button(out,
                        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-left faa-passing-reverse" : "img/doubleleftarrow24.gif",
                        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-left" : "img/doubleleftarrow24.gif",
                        null,
                         "javascript:submitForm('doRimuoviAccesso')",
                        "btn-outline-primary faa-parent animated-hover btn-block",
                        true,
                        bp.getParentRoot().isBootstrap()); %>
                </td>
            </tr>
        </table>
    </div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>