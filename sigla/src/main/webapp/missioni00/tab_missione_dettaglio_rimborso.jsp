<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*"%>

<%  
	CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);
%>
<div class="Group card p-2">
    <table width="100%">
        <tr>
        <td>
            <% bp.getController().writeFormLabel( out, "tipo_trattamento"); %>
            <% bp.getController().writeFormInput( out, null,"tipo_trattamento",false,null,"onChange=\"submitForm('doOnTipoTrattamentoChange')\"");%>
        </td>
        </tr>
    </table>
</div>
<div class="Group">
    <table width="100%">
        <tr>
            <td><% bp.getRimborsoController().writeHTMLTable(pageContext, "dettaglioRimborsoSet",false,false,false,"100%","150px");%></td>
        </tr>
    </table>
</div>

