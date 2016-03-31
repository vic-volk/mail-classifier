package classifier;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

public class OntologyBasedMailClassifier implements MailClassifier {

    private OWLOntology ontology;
    private DLQueryEngine dlQueryEngine;
    private OWLOntologyManager manager;
    private OWLDataFactory factory;
    private OWLDataProperty hasText;

    final private String prefix = "http://webprotege.stanford.edu/";

    private String ruleExpression = "mailClassifierRule";
    private String relatedTo = "relatedTo";

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
        Collection<OWLLiteral> rules = newArrayList();
        individuals.forEach(i -> rules.addAll(EntitySearcher.getDataPropertyValues(i, hasText, ontology)));

        Map<OWLNamedIndividual, Collection<OWLLiteral>> literalsMap = Maps.newHashMap();
        individuals.forEach(i -> literalsMap.put(i, EntitySearcher.getDataPropertyValues(i, hasText, ontology)));

        //Determine mail by rules
        List<OWLClass> mailTypes = Lists.newArrayList();
        literalsMap
                .forEach((k, lc) ->
                        lc.forEach(rule -> {
                            if (searchInText(rule.getLiteral(), mail.getText()))
                                mailTypes.addAll(getMailType(k));
                        }));

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

    private boolean searchInText(String rule, String text) {
        return text.contains(rule);
    }

    private Set<OWLClass> getMailType(OWLNamedIndividual rule) {
        return dlQueryEngine.getDomains(relatedTo, true);
    }

    private Set<OWLClass> getCommandByMailType(OWLClass mailType) {
        return dlQueryEngine.getEquivalentClasses(relatedTo);
    }
}
