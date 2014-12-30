package net.sourceforge.pinemup.core.io.notes.stream.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

public class NotesXmlConverter {
   private static final Logger LOG = LoggerFactory.getLogger(NotesXmlConverter.class);

   public String getNotesFileVersion(InputStream inputStream) {
      String version = null;

      XMLStreamReader parser = null;
      try {
         XMLInputFactory myFactory = XMLInputFactory.newInstance();
         parser = myFactory.createXMLStreamReader(inputStream, NotesXmlMetaData.NOTESFILE_ENCODING);

         int event;
         while (parser.hasNext()) {
            event = parser.next();
            switch (event) {
            case XMLStreamConstants.START_ELEMENT:
               String ename = parser.getLocalName();
               if (ename.equals("notesfile")) {
                  for (int i = 0; i < parser.getAttributeCount(); i++) {
                     if (parser.getAttributeLocalName(i).equals("version")) {
                        version = parser.getAttributeValue(i);
                     }
                  }
               }
               break;
            default:
               // do nothing
               break;
            }
         }
      } catch (XMLStreamException e) {
         LOG.error("Error during attempt to retrieve notesfile version.", e);
      } finally {
         if (parser != null) {
            try {
               parser.close();
            } catch (XMLStreamException e) {
               LOG.error("Error during attempt to close stream.", e);
            }
         }
      }
      return version;
   }
}
