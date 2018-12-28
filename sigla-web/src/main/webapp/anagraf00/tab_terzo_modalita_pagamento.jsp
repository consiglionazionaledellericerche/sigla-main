<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.anagraf00.core.bulk.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*"
%>

<%
	CRUDTerzoBP bp = (CRUDTerzoBP)BusinessProcess.getBusinessProcess(request);

  	String ti_pagamento = bp.getTiPagamento();
	boolean isIbanNullable=bp.getCrudBanche()!=null && 
			             bp.getCrudBanche().getModel()!=null &&
					     ((BancaBulk)bp.getCrudBanche().getModel()).getTi_pagamento()!=null &&
				 		 ((BancaBulk)bp.getCrudBanche().getModel()).getTi_pagamento().equals(Rif_modalita_pagamentoBulk.IBAN);
%>

<table border="0" cellspacing="0" cellpadding="2" width="100%">
  <tr>
	<td colspan="4"><% bp.getCrudModalita_pagamento().writeHTMLTable(pageContext,null,true,false,true,"100%","200px"); %></td>
  </tr>
</table>

<% if(bp.getCrudModalita_pagamento().getModel() != null && 
		((Modalita_pagamentoBulk)bp.getCrudModalita_pagamento().getModel()).isPerCessione()) { %>
<table border="0" cellspacing="0" cellpadding="2" width="100%">			
  <tr>
  	<td>
  		<% bp.getCrudModalita_pagamento().writeFormLabel(out,"cd_terzo_delegato"); %>
  		<% bp.getCrudModalita_pagamento().writeFormInput(out,"cd_terzo_delegato"); %>
		<% bp.getCrudModalita_pagamento().writeFormInput(out,"ds_terzo_delegato"); %>
  		<% bp.getCrudModalita_pagamento().writeFormInput(out,"find_terzo_delegato"); %>
  	</td>
  </tr>
</table>  
<% } %> 

<% if(bp.isCrudModalita_pagamentoVisible()) { %>
	<fieldset>
		<legend><% bp.getCrudModalita_pagamento().writeFormInput(out,"default","ds_lista_banche",true,"GroupLabel",null); %></legend>

		<table class="Form w-100">
	  	<tr>
	  		<td width="100%" valign="top">
	  		    <table class="Form w-100">
	  		        <tr>
	  		            <td><% bp.getCrudBanche().writeHTMLTable(pageContext,ti_pagamento,true,false,!bp.isOrigineBancaPerStipendi(),"100%","200px"); %></td>
	  		        </tr>
                    <% if (bp.getCrudBanche()!=null && bp.getCrudBanche().getModel() != null &&
                           (ti_pagamento.equals(Rif_modalita_pagamentoBulk.BANCARIO) || ti_pagamento.equals(Rif_modalita_pagamentoBulk.IBAN))) {%>
                    <tr><td><% bp.getCrudBanche().writeFormLabel(out,"nazione_iban"+(isIbanNullable?"_nullable":"")); %>
                            <% bp.getCrudBanche().writeFormInput(out,"nazione_iban"+(isIbanNullable?"_nullable":"")); %>
                            <%if (((BancaBulk)bp.getCrudBanche().getModel()).getNazione_iban()!=null) {
                                for (int i = 0; i<((BancaBulk)bp.getCrudBanche().getModel()).getNazione_iban().getStrutturaIbanNrLivelli();i++) {
                            %>
                              &nbsp;-&nbsp;
                            <%
                                bp.getCrudBanche().writeFormInput(out,"codice_iban_parte"+(i+1)+(isIbanNullable?"_nullable":""));
                                }
                            }
                            %>
                    </td></tr>
                    <% } %>
	  		    </table>
	  		</td>
	  		<td valign="top">
	  			<table class="Form"><%bp.getCrudBanche().writeForm(out,ti_pagamento); %></table>
	  		<td>
	  	</tr>
	  </table>
	</fieldset>
<%} %>