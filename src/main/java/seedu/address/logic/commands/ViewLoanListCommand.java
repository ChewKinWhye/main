package seedu.address.logic.commands;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import seedu.address.MainApp;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.storage.XmlAdaptedLoanList;
import seedu.address.storage.XmlAdaptedLoanerDescription;

//@@author ChewKinWhye

/**
 * Lists all the entries in the loan list to the user.
 */

public class ViewLoanListCommand extends Command {
    public static final String COMMAND_WORD = "viewLoanList";
    public static final String MESSAGE_SUCCESS = "Loan list displayed";
    public static final String MESSAGE_EMPTY = "Loan list is currently empty";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {

        if (!model.getLoginStatus()) {
            throw new CommandException(MESSAGE_LOGIN);
        }

        if (!MainApp.getLoanListFile().exists()) {
            throw new CommandException(MESSAGE_EMPTY);
        }
        String messageOutput = getMessageOutput(MainApp.getLoanListFile());
        return new CommandResult(messageOutput);
    }

    public String getMessageOutput(File loanListFile) throws CommandException {
        try {
            Integer counter = 1;
            String messageOutput = new String();
            messageOutput += MESSAGE_SUCCESS + "\n";
            JAXBContext context = JAXBContext.newInstance(XmlAdaptedLoanList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            XmlAdaptedLoanList xmlAdaptedLoanList = (XmlAdaptedLoanList) unmarshaller
                    .unmarshal(loanListFile);
            if (xmlAdaptedLoanList.getLoanList().size() == 0) {
                throw new CommandException(MESSAGE_EMPTY);
            }
            for (XmlAdaptedLoanerDescription loanerDescription : xmlAdaptedLoanList.getLoanList()) {
                messageOutput += counter + ". ";
                messageOutput += loanerDescription.getLoanerName() + ": ";
                messageOutput += loanerDescription.getItemName() + " ";
                messageOutput += loanerDescription.getQuantity() + "\n";
                counter++;
            }
            return messageOutput;
        } catch (JAXBException e) {
            System.out.println(e.toString());
        }
        return null;
    }
}
