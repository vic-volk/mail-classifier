import classifier.OntologyBasedMailClassifier;
import model.Mail;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;
import owl.DLQueryEngine;
import owl.DLQueryExample;
import owl.DLQueryPrinter;

import java.io.File;
import java.io.IOException;

public class MailClassifierTest {

    final String path = "src/main/owl/root-ontology.owx";

    @Test
    public void testOntologyLoading() throws OWLOntologyCreationException, IOException {
        File ontologyXmlFile = new File(path);
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new FileDocumentSource(ontologyXmlFile));

        System.out.println("Loaded ontology: " + ontology.getOntologyID());

        OWLReasoner reasoner = DLQueryExample.createReasoner(ontology);
        ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
        // Create the owl.DLQueryPrinter helper class. This will manage the
        // parsing of input and printing of results
        DLQueryPrinter dlQueryPrinter = new DLQueryPrinter(new DLQueryEngine(reasoner, shortFormProvider),
                shortFormProvider);

        DLQueryExample.doQueryLoop(dlQueryPrinter);
    }

    @Test
    public void testMailClassifier() throws OWLOntologyCreationException {
        OntologyBasedMailClassifier classifier = new OntologyBasedMailClassifier(getOntology());
        Mail mail = new Mail();
        mail.setText("spam");
        String command = classifier.classify(mail);
    }

    private OWLOntology getOntology() throws OWLOntologyCreationException {
        File ontologyXmlFile = new File(path);
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        return manager.loadOntologyFromOntologyDocument(new FileDocumentSource(ontologyXmlFile));
    }
}

