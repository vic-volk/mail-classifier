package owl;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.util.ShortFormProvider;

import javax.annotation.Nonnull;
import java.util.Set;

public class DLQueryPrinter {

    private final DLQueryEngine dlQueryEngine;
    private final ShortFormProvider shortFormProvider;

    /**
     * @param engine
     *        the engine
     * @param shortFormProvider
     *        the short form provider
     */
    public DLQueryPrinter(DLQueryEngine engine, ShortFormProvider shortFormProvider) {
        this.shortFormProvider = shortFormProvider;
        dlQueryEngine = engine;
    }

    /**
     * @param classExpression
     *        the class expression to use for interrogation
     */
    public void askQuery(@Nonnull String classExpression) {
        if (classExpression.isEmpty()) {
            System.out.println("No class expression specified");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("\n--------------------------------------------------------------------------------\n");
            sb.append("QUERY:   ");
            sb.append(classExpression);
            sb.append('\n');
            sb.append("--------------------------------------------------------------------------------\n\n");
            // Ask for the subclasses, superclasses etc. of the specified
            // class expression. Print out the results.
            Set<OWLClass> superClasses = dlQueryEngine.getSuperClasses(classExpression, true);
            printEntities("SuperClasses", superClasses, sb);
            Set<OWLClass> equivalentClasses = dlQueryEngine.getEquivalentClasses(classExpression);
            printEntities("EquivalentClasses", equivalentClasses, sb);
            Set<OWLClass> subClasses = dlQueryEngine.getSubClasses(classExpression, true);
            printEntities("SubClasses", subClasses, sb);
            Set<OWLNamedIndividual> individuals = dlQueryEngine.getInstances(classExpression, true);
            printEntities("Instances", individuals, sb);
            System.out.println(sb);
        }
    }

    private void printEntities(@Nonnull String name, @Nonnull Set<? extends OWLEntity> entities,
        @Nonnull StringBuilder sb) {
        sb.append(name);
        int length = 50 - name.length();
        for (int i = 0; i < length; i++) {
            sb.append('.');
        }
        sb.append("\n\n");
        if (!entities.isEmpty()) {
            for (OWLEntity entity : entities) {
                assert entity != null;
                sb.append('\t');
//                sb.append(shortFormProvider.getShortForm(entity));
                sb.append(entity.getIRI().getShortForm());
                sb.append('\n');
            }
        } else {
            sb.append("\t[NONE]\n");
        }
        sb.append('\n');
    }
}
