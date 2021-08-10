<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bp.*,
		it.cnr.contab.incarichi00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<% 	CRUDScadenzarioDottoratiBP bp = (CRUDScadenzarioDottoratiBP)BusinessProcess.getBusinessProcess(request);
    bp.getRateCRUDController().writeHTMLTable(pageContext,null,true,false,true,"100%","200px"); %>

<div class="Group card p-2" style="width:100%">
    <div class="GroupLabel h3 text-primary">
        Testata
    </div>
    <table width="100%">
      <tr>
              <td><% bp.getRateCRUDController().writeFormLabel(out, "id"); %>	</td>
                  <td><% bp.getRateCRUDController().writeFormInput(out,"id");%></td>
      </tr>
      <tr>
                  <td>AAA <% bp.getRateCRUDController().writeFormLabel(out, "find_ScadenzarioDottorati"); %></td>
                  <td>AAA <% bp.getRateCRUDController().writeFormInput(out, "find_ScadenzarioDottorati"); %></td>
            </tr>
              <tr>
                  <td>AA<% bp.getRateCRUDController().writeFormField(out,"cdCds"); %></td>
                  <td>AA<% bp.getRateCRUDController().writeFormField(out,"cdCds"); %></td>
              </tr>
            <tr>
              <td><% bp.getRateCRUDController().writeFormLabel(out,"cdUnitaOrganizzativa"); %></td>
              <td><% bp.getRateCRUDController().writeFormInput(out,"cdUnitaOrganizzativa"); %></td>
            </tr>
            <tr>
              <td><% bp.getRateCRUDController().writeFormLabel(out,"esercizio"); %></td>
              <td><% bp.getRateCRUDController().writeFormInput(out,"esercizio"); %></td>
                </tr>
                    <tr>
              <td><% bp.getRateCRUDController().writeFormLabel(out,"pgRata"); %></td>
              <td><% bp.getRateCRUDController().writeFormInput(out,"pgRata"); %></td>
            </tr>
            <tr>
              <td><% bp.getRateCRUDController().writeFormLabel(out,"pgMandato"); %></td>
              <td><% bp.getRateCRUDController().writeFormInput(out,"pgMandato"); %></td>
                </tr>
                    <tr>
              <td><% bp.getRateCRUDController().writeFormLabel(out,"dtInizioRata"); %></td>
              <td><% bp.getRateCRUDController().writeFormInput(out,"dtInizioRata"); %></td>
            </tr>
            <tr>
              <td><% bp.getRateCRUDController().writeFormLabel(out,"dtFineRata"); %></td>
              <td><% bp.getRateCRUDController().writeFormInput(out,"dtFineRata"); %></td>
                </tr>
                    <tr>
              <td><% bp.getRateCRUDController().writeFormLabel(out,"dtScadenza"); %></td>
              <td><% bp.getRateCRUDController().writeFormInput(out,"dtScadenza"); %></td>
            </tr>
            <tr>
              <td><% bp.getRateCRUDController().writeFormLabel(out,"imRata"); %></td>
              <td><% bp.getRateCRUDController().writeFormInput(out,"imRata"); %></td>
            </tr>
    </table>
</div>