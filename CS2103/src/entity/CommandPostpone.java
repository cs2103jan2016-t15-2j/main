/*
 * @@author A0139995E
 */
package entity;

/**
 * This command is for postponing times of events and deadlines
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandPostpone implements Command {
    private int _taskNumber;
    private ArrayList<String> _parameters;
    private Calendar _time;
    private String _msg;
    private boolean _updateFile = true;;
    private boolean _saveHistory = true;
    private Logger _logger = GlobalLogger.getLogger();

    public CommandPostpone() {
        this._taskNumber = -1;
    }

    public CommandPostpone(Integer taskNumber, Calendar time, ArrayList<String> parameters) {
        assert parameters != null;
        assert taskNumber != null;
        this._taskNumber = taskNumber - 1;
        this._time = time;
        this._parameters = parameters;
    }
    
    public Display execute(Display display) {
        assert display != null: "Postpone: null display";
        if (isInvalidCommand(display)) {
            _logger.log(Level.INFO, "Postpone: Invalid parameters");
            setInvalidDisplay(display);
            return display;
        }
        postpone(display);
        display.setMessage(_msg);
        return display;
    }

    public void postpone(Display display) {
        if (_taskNumber < display.getVisibleDeadlineTasks().size()) {
            _logger.log(Level.INFO, "Postpone: Postpone deadline");
            postponeDeadline(display);
        } else {
            _taskNumber -= display.getVisibleDeadlineTasks().size();
            _logger.log(Level.INFO, "Postpone: Postpone event");
            postponeEvent(display);
        }
    }

    /**
     * checks which parameters are being postponed
     * and increment them accordingly
     */
    public void postponeEvent(Display display) {
        TaskEvent task = display.getVisibleEvents().remove(_taskNumber);
        display.getEventTasks().remove(task);
        for (int i = 0; i < _parameters.size(); i++) {
            String parameter = _parameters.get(i).trim().toLowerCase();
            switch (parameter) {
            case GlobalConstants.YEAR:
                addYear(task.getStartDate());
                addYear(task.getEndDate());
                break;
            case GlobalConstants.MONTH:
                addMonth(task.getStartDate());
                addMonth(task.getEndDate());
                break;
            case GlobalConstants.DAY:
                addDay(task.getStartDate());
                addDay(task.getEndDate());
                break;
            case GlobalConstants.HOUR:
                addHour(task.getStartDate());
                addHour(task.getEndDate());
                break;
            case GlobalConstants.MINUTE:
                addMinute(task.getStartDate());
                addMinute(task.getEndDate());
                break;
            }
        }
        new CommandAddEvent(task).execute(display);
        _msg = GlobalConstants.MESSAGE_POSTPONED + task.getDescription();
        setOverdue(task);
    }

    /**
     * checks which parameters are being postponed
     * and increment them accordingly
     */
    public void postponeDeadline(Display display) {
        TaskDeadline task = display.getVisibleDeadlineTasks().remove(_taskNumber);
        display.getDeadlineTasks().remove(task);
        for (int i = 0; i < _parameters.size(); i++) {
            String parameter = _parameters.get(i).trim().toLowerCase();
            switch (parameter) {
            case GlobalConstants.YEAR:
                addYear(task.getEndDate());
                break;
            case GlobalConstants.MONTH:
                addMonth(task.getEndDate());
                break;
            case GlobalConstants.DAY:
                addDay(task.getEndDate());
                break;
            case GlobalConstants.HOUR:
                addHour(task.getEndDate());
                break;
            case GlobalConstants.MINUTE:
                addMinute(task.getEndDate());
                break;
            }
        }
        new CommandAddDeadlineTask(task).execute(display);
        _msg = GlobalConstants.MESSAGE_POSTPONED + task.getDescription();
        setOverdue(task);
    }

    /**
     * This method resets the overdue flag
     * of the postponed task
     */
    private void setOverdue(TaskEvent task) {
        if(task.getEndDate().before(Calendar.getInstance())){
            task.setIsOverdue(true);
        }else{
            task.setIsOverdue(false);
        }
    }

    private void setOverdue(TaskDeadline task) {
        if(task.getEndDate().before(Calendar.getInstance())){
            task.setIsOverdue(true);
        }else{
            task.setIsOverdue(false);
        }
    }

    private void addMinute(Calendar oldTime) {
        oldTime.set(Calendar.MINUTE,
                oldTime.get(Calendar.MINUTE) + _time.get(Calendar.MINUTE));
    }

    private void addHour(Calendar oldTime) {
        oldTime.set(Calendar.HOUR_OF_DAY,
                oldTime.get(Calendar.HOUR_OF_DAY) + _time.get(Calendar.HOUR_OF_DAY));
    }

    private void addDay(Calendar oldTime) {
        oldTime.set(Calendar.DATE, oldTime.get(Calendar.DATE) + _time.get(Calendar.DATE));
    }

    private void addMonth(Calendar oldTime) {
        oldTime.set(Calendar.MONTH, oldTime.get(Calendar.MONTH) + _time.get(Calendar.MONTH));
    }

    private void addYear(Calendar oldTime) {
        oldTime.set(Calendar.YEAR, oldTime.get(Calendar.YEAR) + _time.get(Calendar.YEAR));
    }

    private void setInvalidDisplay(Display display) {
        _updateFile = false;
        _saveHistory = false;
        display.setMessage(_msg);
        display.setTaskIndices(null);
        display.setConflictingTasksIndices(null);
    }

    private boolean isInvalidCommand(Display display) {
        if (hasInvalidTaskNumber(display)) {
            return true;
        } else if (hasInvalidTime()) {
            return true;
        }
        return false;
    }

    private boolean hasInvalidTime() {
        return false;
    }

    /**
     * this method takes into accounts that only
     * deadline tasks and events can be postponed
     */
    private boolean hasInvalidTaskNumber(Display display) {
        if (_taskNumber < 0) {
            _msg = GlobalConstants.MESSAGE_ERROR_INVALID_INDEX;
            return true;
        } else {
            int minIndex = 0;
            int maxIndex = display.getVisibleDeadlineTasks().size() + display.getVisibleEvents().size() - 1;
            if ((_taskNumber < minIndex) || (_taskNumber > maxIndex)) {
                _msg = GlobalConstants.MESSAGE_ERROR_POSTPONE_INVALID_TASK_TYPES;
                return true;
            }
        }
        return false;
    }

    public boolean requiresSaveHistory() {
        return _saveHistory;
    }

    public boolean requiresUpdateFile() {
        return _updateFile;
    }
}
