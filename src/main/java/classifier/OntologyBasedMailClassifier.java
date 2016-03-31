package classifier;

import model.Mail;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;
import owl.DLQueryEngine;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class OntologyBasedMailClassifier implements MailClassifier {

    private OWLOntology ontology;
    private DLQueryEngine dlQueryEngine;
    private OWLOntologyManager manager;
    private OWLDataFactory factory;
    private OWLDataProperty hasText;

    final private String prefix = "http://webprotege.stanford.edu/";

    private String ruleExpression = "mailClassifierRule";

    public OntologyBasedMailClassifier(OWLOntology ontology) {
        manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();
        hasText = factory.getOWLDataProperty(IRI.create(prefix, "hasText"));
        OWLReasoner reasoner = createReasoner(ontology);
        ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
        dlQueryEngine = new DLQueryEngine(reasoner, shortFormProvider);
        this.ontology = ontology;
    }

    public String classify(Mail mail) {
        //Get classification rules
        Set<OWLNamedIndividual> individuals = dlQueryEngine.getInstances(ruleExpression, true);
        Collection<OWLLiteral> rules = new ArrayList<>();
        individuals.forEach(i -> rules.addAll(EntitySearcher.getDataPropertyValues(i, hasText, ontology)));
        List<String> values = new ArrayList<>();
        rules.forEach(r -> values.add(r.getLiteral()));
        //Determine mail by rules

        //Invoke commands

        return "test command";
    }

    @Nonnull
    private OWLReasoner createReasoner(@Nonnull OWLOntology rootOntology) {
        // We need to create an instance of OWLReasoner. An OWLReasoner provides
        // the basic query functionality that we need, for example the ability
        // obtain the subclasses of a class etc. To do this we use a reasoner
        // factory.
        // Create a reasoner factory.
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        return reasonerFactory.createReasoner(rootOntology);
    }
}
