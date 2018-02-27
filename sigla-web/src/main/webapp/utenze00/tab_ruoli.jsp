<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.utenze00.bp.*"
%>


<%  
		CRUDUtenzaBP bp = (CRUDUtenzaBP)BusinessProcess.getBusinessProcess(request);
%>
<div class="card p-2 mt-2">
    <table class="Form" width="100%">
      <tr>
          <%bp.getController().writeFormField( out, "find_uo_per_ruolo"); %>
      </tr>
    </table>
</div>
<div class="card p-2 mt-2">
    <table class="Form" width="100%">
      <tr>
            <td><span class="GroupLabel text-primary h2">Ruoli disponibili</span></td>
            <td></td>
            <td><span class="GroupLabel text-primary h2">Ruoli assegnati</span></td>
      </tr>
      <tr>
          <td rowspan="2"><%bp.getCrudRuoli_disponibili().writeHTMLTable(pageContext,null,false,false,false,"100%","300px"); %></td>
          <td>
            <% JSPUtils.button(out,
                  bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-right faa-passing" : "img/doublerightarrow24.gif",
                  bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-right" : "img/doublerightarrow24.gif",
                  null,
                  "javascript:submitForm('doAggiungiRuolo')",
                  "btn-outline-primary faa-parent animated-hover btn-block",
                  true,
                  bp.getParentRoot().isBootstrap()); %>
          </td>
          <td rowspan="2"><%bp.getCrudRuoli().writeHTMLTable(pageContext,null,false,false,false,"100%","300px"); %></td>
      </tr>
      <tr>
        <td>
            <% JSPUtils.button(out,
                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-left faa-passing-reverse" : "img/doubleleftarrow24.gif",
                bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-left" : "img/doubleleftarrow24.gif",
                null,
                 "javascript:submitForm('doRimuoviRuolo')",
                "btn-outline-primary faa-parent animated-hover btn-block",
                true,
                bp.getParentRoot().isBootstrap()); %>
        </td>
      </tr>
    </table>
</div>