<%@ page pageEncoding="UTF-8"
        import="it.cnr.jada.action.*,
                it.cnr.jada.bulk.*,
                it.cnr.jada.util.action.*,
                it.cnr.jada.util.jsp.*,
                it.cnr.contab.progettiric00.bp.*,
                it.cnr.contab.progettiric00.core.bulk.*"
%>

<%
    TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP) BusinessProcess.getBusinessProcess(request);
    bp.getCrudProgetto_anagrafico().writeHTMLTable(pageContext,"ColumnsDettProgAnag",true,false,true,"100%","150px");
%>

<div class="Group card p-2 mb-2">
        <table class="w-100">
            <tr>

                <td><% bp.getCrudProgetto_anagrafico().writeFormLabel(out, "findAnagrafico"); %></td>
                <td colspan="5"><% bp.getCrudProgetto_anagrafico().writeFormInput(out, "findAnagrafico"); %></td>
            </tr>
            <tr>
                <td><% bp.getCrudProgetto_anagrafico().writeFormLabel(out,"importo");%></td>
                <td><% bp.getCrudProgetto_anagrafico().writeFormInput(out,null,"importo"); %></td>
                <td><% bp.getCrudProgetto_anagrafico().writeFormLabel(out,"dataInizio");%></td>
                <td><% bp.getCrudProgetto_anagrafico().writeFormInput(out,null,"dataInizio"); %></td>
                <td><% bp.getCrudProgetto_anagrafico().writeFormLabel(out,"dataFine");%></td>
                <td><% bp.getCrudProgetto_anagrafico().writeFormInput(out,null,"dataFine"); %></td>


            </tr>
        </table>
    </div>