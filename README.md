# mail-classifier
Ontology based mail classifier.

Entities:
* Rule;
* Command;
* Mail:
  ** Spam-mail;
  ** Normal-mail;
  ** Suspicious-mail;
* Parameter

It helps classify mail by using rules and relations with mail-types.

src/main/owl/root-ontology.owl - mail agent ontology.
Classifier Agent implementation -
[https://github.com/vic-volk/mail-classifier/blob/master/src/main/java/classifier/MailClassifierAgent.java]

OWLApi as dependency
