<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bp.*,
		it.cnr.contab.incarichi00.bulk.*,
		it.cnr.contab.incarichi00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<% 	CRUDScadenzarioDottoratiBP bp = (CRUDScadenzarioDottoratiBP)BusinessProcess.getBusinessProcess(request);
	ScadenzarioDottoratiBulk scadenzarioDottorati = (ScadenzarioDottoratiBulk)bp.getModel();
     %>

<div class="Group card p-2" style="width:100%">
    <div class="GroupLabel h3 text-primary">
        Testata
    </div>
    <table width="100%">
      <tr>
      		<% bp.getController().writeFormField(out,"findTerzo"); %>
      </tr>
      <tr>
      		<td colspan="2"><% bp.getController().writeFormInput(out,"find_percipiente"); %></td>
      	</tr>
      	<tr>
      		<% bp.getController().writeFormField(out,"cd_precedente"); %>
      	</tr>
      	<tr>
      		<% bp.getController().writeFormField(out,"nome"); %>
      		<% bp.getController().writeFormField(out,"cognome"); %>
      	</tr>
      	<tr>
      		<% bp.getController().writeFormField(out,"ragioneSociale"); %></td>
      	</tr>
      	<tr>
      		<% bp.getController().writeFormField(out,"codiceFiscale"); %>
      		<% bp.getController().writeFormField(out,"partitaIva"); %>
      	</tr>
    </table>
    <div class="GroupLabel h3 text-primary">
            Termini_pagamento
    </div>
    	<table>
            <tr>
                <td><% bp.getController().writeFormLabel(out,"cdTerminiPag");%></td>
                <td><% bp.getController().writeFormInput(out,null,"cdTerminiPag",false,null,"");%></td>
            </tr>
            <tr>
            <td><% bp.getController().writeFormLabel(out,"cdModalitaPag");%></td>
                <td>
                <% bp.getController().writeFormInput(out,null,"cdModalitaPag",false,null,"onChange=\"submitForm('doOnCdModalitaPagChange')\"");%>
                </td>

            </tr>
            <tr>
                <td>
			        <%
					bp.getController().writeFormInput(out,"pgBanca");
				    %>
   		        </td>
            </tr>
    </table>
    <table>
       	  <tr>
       	  <td><% bp.getController().writeFormField(out,"termini_pagamento"); %>
       	  </td>
       	  <td><% bp.getController().writeFormField(out,"modalita_pagamento"); %>
                 	  </td>
       	  </tr>
       	     <tr>
            	<td>
        	     	<% bp.getController().writeFormLabel(out,"modalita_pagamento");%>
        	     	<% bp.getController().writeFormInput(out,"pagamento");%>

             	</td>
            	<td>
       	      	<% bp.getController().writeFormInput(out,null,"modalita_pagamento",false,null,"onChange=\"submitForm('doOnModalitaPagamentoChange')\"");%>
             	</td>
       		<td>
       			<% 	if (scadenzarioDottorati.getBanca() != null) {
       					bp.getController().writeFormInput(out, null, "listaBanche", false, null, "");
       				} %>
          		</td>
             </tr>
           </table>
</div>
<div class="Group card p-2" style="width:100%">
    <div class="GroupLabel h3 text-primary">
        Mini carriera di origine
    </div>
    <table width="100%">
      <tr>
        <% bp.getController().writeFormField(out,"id"); %>
        <% bp.getController().writeFormField(out,"esercizio_scad_dott_ori"); %>
      </tr>
    </table>
</div>