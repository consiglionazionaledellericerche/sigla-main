<%@ page pageEncoding="UTF-8"
	import = "it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.ordini.bp.CRUDOrdineAcqBP,
		it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk,
		it.cnr.contab.ordmag.anag00.*"
%>

<%  
CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP)BusinessProcess.getBusinessProcess(request);
	OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk)bp.getRighe().getModel();
	String collapseIconClass = bp.isDettaglioContrattoCollapse() ? "fa-chevron-circle-down" : "fa-chevron-circle-up";
%>
<div class="Group card p-2 mb-2">
	<table class="w-100">
		<tr>
			<td><% bp.getRighe().writeFormLabel(out, "findBeneServizio"); %></td>
			<td colspan="5"><% bp.getRighe().writeFormInput(out, "findBeneServizio"); %></td>
		</tr>
		<tr>
			<td><% bp.getRighe().writeFormLabel(out, "findUnitaMisura");%></td>
			<td colspan="3"><% bp.getRighe().writeFormInput(out, "findUnitaMisura");%></td>
			<td colspan="2">
				<% bp.getRighe().writeFormLabel(out,"coefConv");%>
				<% bp.getRighe().writeFormInput(out,null,"coefConv",riga!=null&&riga.isROCoefConv(),null,""); %>
			</td>
		</tr>
		<tr>
			<td><% bp.getRighe().writeFormLabel(out, "voce_iva"); %></td>
			<td colspan="5"><% bp.getRighe().writeFormInput(out, "voce_iva"); %></td>
		</tr>
		<tr>
			<% bp.getRighe().writeFormField(out, "dspQuantita"); %>
			<% bp.getRighe().writeFormField(out,"prezzoUnitario");%>
		</tr>
		<tr>
			<% bp.getRighe().writeFormField(out,"sconto1");%>      	
			<% bp.getRighe().writeFormField(out,"sconto2");%>
			<% bp.getRighe().writeFormField(out,"sconto3");%>      	
		</tr>
        <tr>      	
			<td><% bp.getRighe().writeFormLabel(out,"notaRiga");%></td>      	
			<td colspan="5"><% bp.getRighe().writeFormInput(out,"notaRiga");%></td>
        </tr>
        <% if (riga != null && riga.getDettaglioContratto() != null) { %>
            <%if (bp.getParentRoot().isBootstrap()) { %>
            <div>
                <h6 class="mb-0">
                    <a onclick="submitForm('doToggle(dettaglioContratto)')" class="text-primary"><i aria-hidden="true" class="fa <%=collapseIconClass%>"></i> Dati Dettaglio Contratto</a>
                </h6>
            </div>
            <% } %>
            <% if (riga.getDettaglioContratto() != null && (!bp.isDettaglioContrattoCollapse() || !bp.getParentRoot().isBootstrap())) { %>
                     <tr>
                        <% bp.getRighe().writeFormField(out,"quantitaMin");%>
                        <% bp.getRighe().writeFormField(out,"quantitaMax");%>
                        <% bp.getRighe().writeFormField(out,"quantitaOrdinata");%>
                    </tr>
            <% } %>
        <% } %>
	</table>
</div>

<div class="Group card p-2 mb-2">
	<table class="w-100">
		<tr>
			<% bp.getRighe().writeFormField(out, "tipoConsegna"); %>
			<td colspan="4">
				<% bp.getRighe().writeFormLabel(out, "dtPrevConsegna"); %>
				<% bp.getRighe().writeFormInput(out, "dtPrevConsegna"); %>
			</td>
			<td colspan="4">
				<% bp.getRighe().writeFormLabel(out, "statoConsegne"); %>
				<% bp.getRighe().writeFormInput(out, "statoConsegne"); %>
			</td>
		</tr>
		<tr>
			<td><% bp.getRighe().writeFormLabel(out, "findMagazzino");%></td>
			<td colspan="3"><% bp.getRighe().writeFormInput(out, "findMagazzino");%></td>
			<td><% bp.getRighe().writeFormLabel(out, "findLuogoConsegnaMag");%></td>
			<td colspan="3"><% bp.getRighe().writeFormInput(out, "findLuogoConsegnaMag");%></td>
		</tr>
		<tr>
			<td><% bp.getRighe().writeFormLabel(out, "findUnitaOperativaOrdDest"); %></td>
			<td colspan="7"><% bp.getRighe().writeFormInput(out, "findUnitaOperativaOrdDest"); %></td>
		</tr>
		<tr>
			<%
				bp.getRighe().writeFormField(out, "imImponibile");
			    bp.getRighe().writeFormField(out, "imIva");
			    bp.getRighe().writeFormField(out, "imIvaD");
			    bp.getRighe().writeFormField(out, "imTotaleRiga");
			%>
		</tr>
		<tr>
			<td><% bp.getRighe().writeFormLabel(out, "cercaDspConto"); %></td>
			<td colspan="7"><% bp.getRighe().writeFormInput(out, "cercaDspConto"); %></td>
		</tr>
	</table>
</div>