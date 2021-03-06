package owl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNodeSet;
import org.semanticweb.owlapi.util.CollectionFactory;
import org.semanticweb.owlapi.util.ShortFormProvider;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

/**
 * This example shows how to perform a "dlquery". The DLQuery view/tab in
 * Protege 4 works like this.
 */
public class DLQueryEngine {

    @Nonnull
    private final OWLReasoner reasoner;
    @Nonnull
    private final DLQueryParser parser;

    /**
     * Constructs a DLQueryEngine. This will answer "DL queries" using the
     * specified reasoner. A short form provider specifies how entities are
     * rendered.
     * 
     * @param reasoner
     *        The reasoner to be used for answering the queries.
     * @param shortFormProvider
     *        A short form provider.
     */
    public DLQueryEngine(@Nonnull OWLReasoner reasoner, @Nonnull ShortFormProvider shortFormProvider) {
        this.reasoner = reasoner;
        OWLOntology rootOntology = reasoner.getRootOntology();
        parser = new DLQueryParser(rootOntology, shortFormProvider);
    }

    /**
     * Gets the superclasses of a class expression parsed from a string.
     * 
     * @param classExpressionString
     *        The string from which the class expression will be parsed.
     * @param direct
     *        Specifies whether direct superclasses should be returned or not.
     * @return The superclasses of the specified class expression If there was a
     *         problem parsing the class expression.
     */
    @Nonnull
    public Set<OWLClass> getSuperClasses(@Nonnull String classExpressionString, boolean direct) {
        if (classExpressionString.trim().isEmpty()) {
            return CollectionFactory.emptySet();
        }
        OWLClassExpression classExpression = parser.parseClassExpression(classExpressionString);
        NodeSet<OWLClass> superClasses = reasoner.getSuperClasses(classExpression, direct);
        return superClasses.getFlattened();
    }

    /**
     * Gets the equivalent classes of a class expression parsed from a string.
     * 
     * @param classExpressionString
     *        The string from which the class expression will be parsed.
     * @return The equivalent classes of the specified class expression If there
     *         was a problem parsing the class expression.
     */
    @Nonnull
    public Set<OWLClass> getEquivalentClasses(@Nonnull String classExpressionString) {
        if (classExpressionString.trim().isEmpty()) {
            return CollectionFactory.emptySet();
        }
        OWLClassExpression classExpression = parser.parseClassExpression(classExpressionString);
        Node<OWLClass> equivalentClasses = reasoner.getEquivalentClasses(classExpression);
        Set<OWLClass> result;
        if (classExpression.isAnonymous()) {
            result = equivalentClasses.getEntities();
        } else {
            result = equivalentClasses.getEntitiesMinus(classExpression.asOWLClass());
        }
        return result;
    }

    /**
     * Gets the subclasses of a class expression parsed from a string.
     * 
     * @param classExpressionString
     *        The string from which the class expression will be parsed.
     * @param direct
     *        Specifies whether direct subclasses should be returned or not.
     * @return The subclasses of the specified class expression If there was a
     *         problem parsing the class expression.
     */
    @Nonnull
    public Set<OWLClass> getSubClasses(@Nonnull String classExpressionString, boolean direct) {
        if (classExpressionString.trim().isEmpty()) {
            return CollectionFactory.emptySet();
        }
        OWLClassExpression classExpression = parser.parseClassExpression(classExpressionString);
        NodeSet<OWLClass> subClasses = reasoner.getSubClasses(classExpression, direct);
        return subClasses.getFlattened();
    }

    /**
     * Gets the instances of a class expression parsed from a string.
     * 
     * @param classExpressionString
     *        The string from which the class expression will be parsed.
     * @param direct
     *        Specifies whether direct instances should be returned or not.
     * @return The instances of the specified class expression If there was a
     *         problem parsing the class expression.
     */
    @Nonnull
    public Set<OWLNamedIndividual> getInstances(@Nonnull String classExpressionString, boolean direct) {
        if (classExpressionString.trim().isEmpty()) {
            return CollectionFactory.emptySet();
        }
        OWLClassExpression classExpression = parser.parseClassExpression(classExpressionString);
        NodeSet<OWLNamedIndividual> individuals = reasoner.getInstances(classExpression, direct);
        return individuals.getFlattened();
    }

    @Nonnull
    public Set<OWLNamedIndividual> getRelated(OWLNamedIndividual individual, @Nonnull String propertyExpressionString) {
        if (propertyExpressionString.trim().isEmpty()) {
            return CollectionFactory.emptySet();
        }
        List<OWLObjectPropertyExpression> propertyExpressions
                = parser.parsePropertyExpression(propertyExpressionString);
        Set<OWLNamedIndividual> individuals = Sets.newHashSet();
        propertyExpressions.forEach(ps ->
                individuals.addAll(reasoner.getObjectPropertyValues(individual, ps).getFlattened()));
        return individuals;
    }
}

