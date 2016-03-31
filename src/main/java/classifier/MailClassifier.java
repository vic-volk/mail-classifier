package classifier;

import model.Mail;

public interface MailClassifier {

    String classify(Mail mail);
}
