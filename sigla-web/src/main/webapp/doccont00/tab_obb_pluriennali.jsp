<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
	        it.cnr.jada.bulk.*,
	        it.cnr.jada.util.action.*,
	        it.cnr.jada.util.jsp.*,
	        it.cnr.contab.doccont00.bp.*,
	        java.util.*"
%>


<%  
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)BusinessProcess.getBusinessProcess(request);
		bp.getCrudObbligazione_pluriennale().writeHTMLTable(pageContext,"ColumnsDettObbPluriennale",true,false,true,"100%","150px");
%>

<div class="Group card p-2 mb-2">
        <table class="w-100">
            <tr>
                    <td><% bp.getCrudObbligazione_pluriennale().writeFormLabel(out,"anno");%></td>
                    <td><% bp.getCrudObbligazione_pluriennale().writeFormInput(out,"anno"); %></td>
                    <td><% bp.getCrudObbligazione_pluriennale().writeFormLabel(out,"importo");%></td>
                    <td><% bp.getCrudObbligazione_pluriennale().writeFormInput(out,"importo"); %></td>
             </tr>


        </table>
    </div>