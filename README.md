# mail-classifier
Ontology based mail classifier.

Entities:
* Rule;
* Command;
* Mail:
 Spam-mail;
 Normal-mail;
 Suspicious-mail;
* Parameter;

It helps classify mail by using rules and relations with mail-types.

OWL file: [Mail agent ontology](../master/src/main/owl/root-ontology.owx)

Classifier Agent implementation: [Classifier Agent](../master/src/main/java/classifier/MailClassifierAgent.java)

OWLApi as dependency.
