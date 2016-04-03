package classifier;

import model.Mail;

import java.util.List;

public interface MailClassifier {

    List<String> classify(Mail mail);
}
