package owl;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.mansyntax.ManchesterOWLSyntaxParser;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public class DLQueryParser {

    @Nonnull
    private final OWLOntology rootOntology;
    @Nonnull
    private final BidirectionalShortFormProvider bidiShortFormProvider;

    /**
     * Constructs a DLQueryParser using the specified ontology and short form
     * provider to map entity IRIs to short names.
     * 
     * @param rootOntology
     *        The root ontology. This essentially provides the domain vocabulary
     *        for the query.
     * @param shortFormProvider
     *        A short form provider to be used for mapping back and forth
     *        between entities and their short names (renderings).
     */
    DLQueryParser(@Nonnull OWLOntology rootOntology, @Nonnull ShortFormProvider shortFormProvider) {
        this.rootOntology = rootOntology;
        OWLOntologyManager manager = rootOntology.getOWLOntologyManager();
        Set<OWLOntology> importsClosure = rootOntology.getImportsClosure();
        // Create a bidirectional short form provider to do the actual mapping.
        // It will generate names using the input
        // short form provider.
        bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(manager, importsClosure, shortFormProvider);
    }

    /**
     * Parses a class expression string to obtain a class expression.
     * 
     * @param classExpressionString
     *        The class expression string
     * @return The corresponding class expression if the class expression string
     *         is malformed or contains unknown entity names.
     */
    @Nonnull
    public OWLClassExpression parseClassExpression(@Nonnull String classExpressionString) {
        // Set up the real parser
        ManchesterOWLSyntaxParser parser = OWLManager.createManchesterParser();
        parser.setStringToParse(classExpressionString);
        parser.setDefaultOntology(rootOntology);
        // Specify an entity checker that wil be used to check a class
        // expression contains the correct names.
        OWLEntityChecker entityChecker = new ShortFormEntityChecker(bidiShortFormProvider);
        parser.setOWLEntityChecker(entityChecker);
        // Do the actual parsing
        return parser.parseClassExpression();
    }

    @Nonnull
    public List<OWLObjectPropertyExpression> parsePropertyExpression(@Nonnull String propertyExpressionString) {
        // Set up the real parser
        ManchesterOWLSyntaxParser parser = OWLManager.createManchesterParser();
        parser.setStringToParse(propertyExpressionString);
        parser.setDefaultOntology(rootOntology);
        // Specify an entity checker that wil be used to check a class
        // expression contains the correct names.
        OWLEntityChecker entityChecker = new ShortFormEntityChecker(bidiShortFormProvider);
        parser.setOWLEntityChecker(entityChecker);
        // Do the actual parsing
        return parser.parseObjectPropertyChain();
    }
}

