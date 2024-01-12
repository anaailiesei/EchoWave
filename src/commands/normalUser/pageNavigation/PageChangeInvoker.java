package commands.normalUser.pageNavigation;

import commands.ActionCommand;

import java.util.LinkedList;

public final class PageChangeInvoker {
    private LinkedList<ActionCommand> pageUndoHistory = new LinkedList<>();
    private LinkedList<ActionCommand> pageRedoHistory = new LinkedList<>();
    private String message;

    /**
     * Clears the redo history when a change command is done
     */
    public void clearRedoHistory() {
        pageRedoHistory.clear();
    }

    /**
     * Executes the given operation
     *
     * @param actionCommand The command to be executed
     */
    public void executeOperation(final ActionCommand actionCommand) {
        actionCommand.execute();
        pageUndoHistory.push(actionCommand);
    }

    /**
     * Redo the last operation that was undone
     *
     * @return A string with the output message of this operation
     */
    public String redoOperation() {
        if (pageRedoHistory.isEmpty()) {
            return null;
        }
        ActionCommand actionCommand = pageRedoHistory.pop();
        actionCommand.redo();
        message = actionCommand.getMessage();
        pageUndoHistory.push(actionCommand);
        return message;
    }

    /**
     * Undo the last operation performed
     *
     * @return A string with the output message of thi soperation
     */
    public String undoOperation() {
        if (pageUndoHistory.isEmpty()) {
            return null;
        }
        ActionCommand actionCommand = pageUndoHistory.pop();
        actionCommand.undo();
        message = actionCommand.getMessage();
        pageRedoHistory.push(actionCommand);
        return message;
    }
}
