<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.bp.*,
		it.cnr.contab.config00.contratto.bulk.*"
%>

<%
	CRUDConfigAnagContrattoBP bp = (CRUDConfigAnagContrattoBP)BusinessProcess.getBusinessProcess(request);
	        ContrattoBulk contratto = (ContrattoBulk)bp.getModel();
    		Dettaglio_contrattoBulk riga = (Dettaglio_contrattoBulk)bp.getCrudDettaglio_contratto().getModel();
    		if ( ContrattoBulk.DETTAGLIO_CONTRATTO_ARTICOLI.equals( contratto.getTipo_dettaglio_contratto()))
    		    bp.getCrudDettaglio_contratto().writeHTMLTable(pageContext,"ColumnsDettArticoli",true,false,true,"100%","140px");
    		 if ( ContrattoBulk.DETTAGLIO_CONTRATTO_CATGRP.equals( contratto.getTipo_dettaglio_contratto()))
    		    bp.getCrudDettaglio_contratto().writeHTMLTable(pageContext,"ColumnsDettArtGrp",true,false,true,"100%","140px");
%>
<% if ( ContrattoBulk.DETTAGLIO_CONTRATTO_ARTICOLI.equals( contratto.getTipo_dettaglio_contratto())){%>
    <div class="Group card p-2 mb-2">
        <table class="w-100">
            <tr>
                <td ><% bp.getCrudDettaglio_contratto().writeFormLabel(out, "findBeneServizio"); %></td>
                <td colspan="4"><% bp.getCrudDettaglio_contratto().writeFormInput(out, "findBeneServizio"); %></td>
            </tr>
            <tr>
                <td><% bp.getCrudDettaglio_contratto().writeFormLabel(out, "findUnitaMisura");%></td>
                <td colspan="2"><% bp.getCrudDettaglio_contratto().writeFormInput(out, "findUnitaMisura");%></td>
                <td colspan="4">
                    <% bp.getCrudDettaglio_contratto().writeFormLabel(out,"coefConv");%>
                    <% bp.getCrudDettaglio_contratto().writeFormInput(out,null,"coefConv",riga!=null&&riga.isROCoefConv(),null,""); %>
                </td>
            </tr>
            <tr>

                <td><% bp.getCrudDettaglio_contratto().writeFormLabel(out,"prezzoUnitario");%></td>
                <td><% bp.getCrudDettaglio_contratto().writeFormInput(out,null,"prezzoUnitario"); %></td>
                <td><% bp.getCrudDettaglio_contratto().writeFormLabel(out,"importoOrdinato");%></td>
                <td><% bp.getCrudDettaglio_contratto().writeFormInput(out,null,"importoOrdinato"); %></td>
            </tr>
            <tr>
                <td><% bp.getCrudDettaglio_contratto().writeFormLabel(out,"quantitaMin");%></td>
                <td><% bp.getCrudDettaglio_contratto().writeFormInput(out,null,"quantitaMin"); %></td>

                <td><% bp.getCrudDettaglio_contratto().writeFormLabel(out,"quantitaMax");%></td>
                <td><% bp.getCrudDettaglio_contratto().writeFormInput(out,null,"quantitaMax"); %></td>
                <td><% bp.getCrudDettaglio_contratto().writeFormLabel(out,"quantitaOrdinata");%></td>
                 <td><% bp.getCrudDettaglio_contratto().writeFormInput(out,null,"quantitaOrdinata"); %></td>
             </tr>
        </table>
    </div>
 <%}%>
<% if ( ContrattoBulk.DETTAGLIO_CONTRATTO_CATGRP.equals( contratto.getTipo_dettaglio_contratto())){%>
    <div class="Group card p-2 mb-2">
        <table class="w-100">
            <tr>
                <td><% bp.getCrudDettaglio_contratto().writeFormLabel(out, "findCategoriaGruppo"); %></td>
                <td colspan="5"><% bp.getCrudDettaglio_contratto().writeFormInput(out, "findCategoriaGruppo"); %></td>
            </tr>
            <tr>
                <td><% bp.getCrudDettaglio_contratto().writeFormLabel(out,"importoOrdinato");%></td>
                <td><% bp.getCrudDettaglio_contratto().writeFormInput(out,null,"importoOrdinato"); %></td>
            </tr>
        </table>
    </div>
 <%}%>
