<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bp.*,
		it.cnr.contab.incarichi00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<% 	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
 %>

<div class="Group card p-2" style="width:100%">
    <div class="GroupLabel h3 text-primary">
        Testata
    </div>
    <table width="100%">
      <tr>
              <td><% bp.getController().writeFormLabel(out, "id"); %>	</td>
                  <td><% bp.getController().writeFormInput(out,"id");%></td>
      </tr>
      <tr>
                  <td>AAA <% bp.getController().writeFormLabel(out, "find_ScadenzarioDottorati"); %></td>
                  <td>AAA <% bp.getController().writeFormInput(out, "find_ScadenzarioDottorati"); %></td>
            </tr>
              <tr>
                  <td>AA<% bp.getController().writeFormField(out,"cdCds"); %></td>
                  <td>AA<% bp.getController().writeFormField(out,"cdCds"); %></td>
              </tr>
            <tr>
              <td><% bp.getController().writeFormLabel(out,"cdUnitaOrganizzativa"); %></td>
              <td><% bp.getController().writeFormInput(out,"cdUnitaOrganizzativa"); %></td>
            </tr>
            <tr>
              <td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
              <td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
                </tr>
                    <tr>
              <td><% bp.getController().writeFormLabel(out,"pgRata"); %></td>
              <td><% bp.getController().writeFormInput(out,"pgRata"); %></td>
            </tr>
            <tr>
              <td><% bp.getController().writeFormLabel(out,"pgMandato"); %></td>
              <td><% bp.getController().writeFormInput(out,"pgMandato"); %></td>
                </tr>
                    <tr>
              <td><% bp.getController().writeFormLabel(out,"dtInizioRata"); %></td>
              <td><% bp.getController().writeFormInput(out,"dtInizioRata"); %></td>
            </tr>
            <tr>
              <td><% bp.getController().writeFormLabel(out,"dtFineRata"); %></td>
              <td><% bp.getController().writeFormInput(out,"dtFineRata"); %></td>
                </tr>
                    <tr>
              <td><% bp.getController().writeFormLabel(out,"dtScadenza"); %></td>
              <td><% bp.getController().writeFormInput(out,"dtScadenza"); %></td>
            </tr>
            <tr>
              <td><% bp.getController().writeFormLabel(out,"imRata"); %></td>
              <td><% bp.getController().writeFormInput(out,"imRata"); %></td>
            </tr>
    </table>
</div>