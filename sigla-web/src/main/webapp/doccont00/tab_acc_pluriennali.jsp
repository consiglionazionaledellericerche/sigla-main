<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.doccont00.bp.*,it.cnr.contab.doccont00.action.*, it.cnr.contab.doccont00.core.bulk.*,
			it.cnr.contab.doccont00.core.bulk.*"
%>


<%  
	CRUDAccertamentoBP bp = (CRUDAccertamentoBP)BusinessProcess.getBusinessProcess(request);
	bp.getCrudAccertamento_pluriennale().writeHTMLTable(pageContext,"ColumnsDettAccPluriennale",true,false,true,"100%","150px");
%>

<div class="Group card p-2 mb-2">
        <table class="w-100">
            <tr>
                    <td><% bp.getCrudAccertamento_pluriennale().writeFormLabel(out,"anno");%></td>
                    <td><% bp.getCrudAccertamento_pluriennale().writeFormInput(out,"anno"); %></td>
                    <td><% bp.getCrudAccertamento_pluriennale().writeFormLabel(out,"importo");%></td>
                    <td><% bp.getCrudAccertamento_pluriennale().writeFormInput(out,"importo"); %></td>
             </tr>


        </table>
    </div>

	