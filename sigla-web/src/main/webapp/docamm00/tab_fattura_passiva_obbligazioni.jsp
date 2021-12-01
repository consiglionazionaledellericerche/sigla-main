<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.doccont00.core.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<%	CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP)BusinessProcess.getBusinessProcess(request);
	Fattura_passivaBulk fatturaPassiva = (Fattura_passivaBulk)bp.getModel(); %>
	<div class="Group card" style="width:100%">
		<table width="100%">
			<tr>
			  	<td colspan="3">
				  	<span class="FormLabel" style="color:black">Impegni</span>
			  	</td>
			</tr>
			<tr>
			  	<td colspan="3">
					<%	boolean canAdd = true;

						if (fatturaPassiva instanceof Nota_di_creditoBulk || fatturaPassiva instanceof Nota_di_debitoBulk || fatturaPassiva.isDaOrdini())
							canAdd = false;
						bp.getObbligazioniController().writeHTMLTable(pageContext,"default",canAdd,false,!fatturaPassiva.isDaOrdini(),"100%","100px"); %>
			  	</td>
			</tr>
			<tr>
			  	<td>
		   			<% bp.writeFormLabel(out,"importoTotalePerObbligazione"); %>
			  	</td>
			  	<td>
				  	<% bp.writeFormInput(out,null,"importoTotalePerObbligazione",false,null,"style=\"color:black\"");%>
			  	</td>

   <% if (bp.isCIGVisible(((Obbligazione_scadenzarioBulk)bp.getObbligazioniController().getModel()))) { %>
    <div class="Group card">
        <table class="w-100">
           <tr>
                <td><% bp.getObbligazioniController().writeFormLabel(out,"cig");%></td>
		        <td>
		         	<% bp.getObbligazioniController().getBulkInfo().writeFormInput(out,bp.getObbligazioniController().getModel(),null, "cig", false,null,
	                        "",
	                        bp.getObbligazioniController().getInputPrefix(),
	                        bp.getObbligazioniController().getStatus(),
	                        bp.getObbligazioniController().getFieldValidationMap(),
	                        bp.getParentRoot().isBootstrap());%>
		        </td>
                <td><% bp.getObbligazioniController().writeFormLabel(out,"motivo_assenza_cig");%></td>
                <td><% bp.getObbligazioniController().getBulkInfo().writeFormInput(
                        out,
                        bp.getObbligazioniController().getModel(),
                        null,
                        "motivo_assenza_cig",
                        false,
                        null,
                        "",
                        bp.getObbligazioniController().getInputPrefix(),
                        bp.getObbligazioniController().getStatus(),
                        bp.getObbligazioniController().getFieldValidationMap(),
                        bp.getParentRoot().isBootstrap()
                    );
                %></td>
          </tr>
        </table>
    </div>
    <% } %>

			  	<td>
					<% if (fatturaPassiva.quadraturaInDeroga()) { %>
						<span class="FormLabel" style="color:red">
							Quadratura in deroga
						</span>
					<%	} %>
			  	</td>
	   		</tr>
		</table>
	</div>
	<div class="Group card" style="width:100%">
   		<table width="100%">
			<tr>
			  	<td>
		   			<%	Obbligazione_scadenzarioBulk obbl = (Obbligazione_scadenzarioBulk)bp.getObbligazioniController().getModel();
		   				if (obbl == null) { %>
						  	<span class="FormLabel" style="color:black">Dettagli <%=(fatturaPassiva instanceof Nota_di_creditoBulk)?"nota di credito stornati":(fatturaPassiva instanceof Nota_di_debitoBulk)?"nota di debito addebitati":"fattura contabilizzati"%></span>
		   			<%	} else { %>
						  	<span class="FormLabel" style="color:black">Dettagli <%=(fatturaPassiva instanceof Nota_di_creditoBulk)?"nota di credito stornati":(fatturaPassiva instanceof Nota_di_debitoBulk)?"nota di debito addebitati":"fattura contabilizzati"%> su "<%=obbl.getDs_scadenza()%>"</span>
		   			<%	} %>
			  	</td>
			</tr>
			<tr>
			  	<td>
					<% bp.getDettaglioObbligazioneController().writeHTMLTable(pageContext,"righiSet",!fatturaPassiva.isDaOrdini(),false,!fatturaPassiva.isDaOrdini(),"100%","150px"); %>
			  	</td>
			</tr>
   		</table>
	</div>