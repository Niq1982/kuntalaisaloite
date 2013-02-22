package fi.om.municipalityinitiative.json;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class JsonJokuParseriTest {

    @Test
    public void jotain() {

        String json = "{\"participantCount\":{\"franchise\":{\"publicNames\":0,\"privateNames\":1,\"total\":1},\"noFranchise\":{\"publicNames\":2,\"privateNames\":0,\"total\":2},\"total\":3},\"publicParticipants\":{\"franchise\":[],\"noFranchise\":[{\"name\":\"Kikka kokko\",\"participateDate\":\"2013-02-14\",\"homeMunicipality\":{\"name\":\"Hartola\",\"id\":29}},{\"name\":\"Pauli Kärpänoja\",\"participateDate\":\"2013-02-14\",\"homeMunicipality\":{\"name\":\"Tampere\",\"id\":289}}]},\"name\":\"asdsdFADSFADFGAD\",\"id\":\"https://localhost:8443/api/v1/initiatives/1\",\"collectable\":true,\"municipality\":{\"name\":\"Helsinki\",\"id\":35},\"sentTime\":\"2013-02-14\",\"proposal\":\"Kuntalaisaloitteen otsikko\\r\\n\\r\\nKirjoita kuntalaisaloitteen otsikko. Otsikon tulee olla selkeä ja varsinaista sisältöä kuvaava.\\r\\n\\r\\nAloitteen sisältö\\r\\n\\r\\nKirjoita tähän varsinainen aloiteteksti - ...Kuntalaisaloitteen otsikko\\r\\n\\r\\nKirjoita kuntalaisaloitteen otsikko. Otsikon tulee olla selkeä ja varsinaista sisältöä kuvaava.\\r\\n\\r\\nAloitteen sisältö\\r\\n\\r\\nKirjoita tähän varsinainen aloiteteksti - ...Kuntalaisaloitteen otsikko\\r\\n\\r\\nKirjoita kuntalaisaloitteen otsikko. Otsikon tulee olla selkeä ja varsinaista sisältöä kuvaava.\\r\\n\\r\\nAloitteen sisältö\\r\\n\\r\\nKirjoita tähän varsinainen aloiteteksti - ...\",\"authorName\":\"Pauli Kärpänoja\",\"createTime\":\"2013-02-14\"}";

        for (JsonJokuParseri.IndentedString s : JsonJokuParseri.toParts(json)) {
            System.out.println(s.getIndent() + ": "+StringUtils.repeat(" ", 3* s.getIndent()) + s.getValue() + s.getLocalizationKey());
        }

    }
}
