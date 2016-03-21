/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 2:50am
 * CS2103
 */
package bean;

import java.util.ArrayList;
import java.util.Calendar;

import logic.Logic;

public class CommandAddReserved implements Command{
    private TaskReserved task;
    private boolean updateFile = true;
    private boolean saveHistory = true;

    public CommandAddReserved() {
        task = null;
    }

    public CommandAddReserved(String description, String location, ArrayList<Calendar> startDates,
            ArrayList<Calendar> endDates, ArrayList<String> tags) {
        task = new TaskReserved(description, location, startDates, endDates, tags);
    }

    public Display execute(Display display) {
        if (task.getDescription() == null) {
            updateFile = false;
            return (new Display(Logic.MESSAGE_NO_DESCRIPTION));
        }
        ArrayList<TaskReserved> reservedTasks = addReservedTask(display.getReservedTasks());
        display.setReservedTasks(reservedTasks);
        return display;
    }

    public ArrayList<TaskReserved> addReservedTask(ArrayList<TaskReserved> taskList) {
        int index = getIndex(taskList);
        taskList.add(index, task);
        return taskList;
    }

    /*
     * This method searches for the index to slot the deadline task in since we
     * are sorting the list in order of earliest start date of the first time
     * slot.
     */
    public int getIndex(ArrayList<TaskReserved> taskList) {
        int i = 0;
        Calendar addedTaskStartDate = task.getStartDates().get(0);
        for (i = 0; i < taskList.size(); i++) {
            Calendar taskInListStartDate = taskList.get(i).getStartDates().get(0);
            if (addedTaskStartDate.compareTo(taskInListStartDate) < 0) {
                break;
            }
        }
        return i;
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}
