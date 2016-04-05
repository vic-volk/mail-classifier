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

[Mail agent ontology]
(../blob/master/src/main/owl/root-ontology.owl)

Classifier Agent implementation: 
[Classifier Agent](../blob/master/src/main/java/classifier/MailClassifierAgent.java)

OWLApi as dependency
