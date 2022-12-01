<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.doccont00.bp.*"
%>


<%  
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = (it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk)bp.getModel();
%>

<table class="Form" width="100%">
  <tr>
        <td>
            <b><big class="text-primary">CdR</big></b>
            <% JSPUtils.button(out,
                    bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/conferma.gif",
                    bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/conferma.gif",
                    bp.getParentRoot().isBootstrap() ?"Conferma" : null,
                    "javascript:submitForm('doCaricaLineeAttivita')",
                    "btn-outline-secondary btn-title",
                    obbligazione.isConfermaCentriDiResponsabilitaEnabled() && bp.isEditable() && !bp.isViewing(),
                    bp.getParentRoot().isBootstrap()); %>
            <% Button.write(out,
                    bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-hand-lizard-o fa-flip-horizontal text-primary" : "img/zoom16.gif",
                    bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-hand-lizard-o fa-flip-horizontal text-primary" : "img/zoom16.gif",
                    null,
                    "javascript:submitForm('doVisualizzaSpeseCdr')",
                    "btn-outline-secondary btn-title",
                    "Prospetto situazione spese",
                    bp.isVisualizzaSpeseCdrButtonEnabled(),
                    bp.getParentRoot().isBootstrap()); %>

        </td>
        <td></td>
	</tr>
	<tr>
	    <td colspan=2><% bp.getCentriDiResponsabilita().writeHTMLTable(pageContext,"cdr",false,false,false,"100%","100px"); %></td>
	</tr>
	<tr>
        <td colspan=2>
                <b ALIGN="CENTER" class="text-primary"><big>GAE</big></b>
                <% JSPUtils.button(out,
                        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/conferma.gif",
                        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/conferma.gif",
                        bp.getParentRoot().isBootstrap() ?"Conferma" : null,
                        "javascript:submitForm('doConfermaLineeAttivita')",
                        "btn-outline-secondary btn-title",
                        obbligazione.isConfermaLineeAttivitaEnabled() && bp.isEditable() && !bp.isViewing(),
                        bp.getParentRoot().isBootstrap()); %>
        </td>
	</tr>
	<tr>
        <td>
            <b class="h5 text-info">GAE da PdG approvato</b>
        </td>
	    <td>
	        <b class="h5 text-info">Altri GAE</b>
	    </td>
	</tr>
	<tr>
		<td>
		    <%bp.getLineeDiAttivita().writeHTMLTable(pageContext,bp.isFlNuovoPdg()?"nuovoPdg":null,false,false,false,"100%","200px"); %>
		</td>
		<td>
		    <%bp.getNuoveLineeDiAttivita().writeHTMLTable(pageContext,bp.isFlNuovoPdg()?"lattNuovoPdg":"latt",obbligazione.isAddNuoveLattEnabled() && bp.isEditable(),false,true,"100%","200px", false); %>
		</td>
	</tr>
</table>