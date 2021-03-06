// @@author A0149063E

package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import entity.Display;
import entity.Task;
import entity.TaskDeadline;
import entity.TaskEvent;
import entity.TaskFloat;
import entity.TaskReserved;
import storage.Storage;

public class StorageTest {

	private static final String FILE_TEST = "src\\tests\\storageTests\\test.txt";
	private static final String FILE_TEST_INVALID = "src\\storageTests\\test.txt";
	private static final String FILE_TEST_EMPTY_DESCRIPTION = "src\\tests\\storageTests\\emptyDescriptionTest.txt";
	private static final String FILE_TEST_EMPTY_LOCATION = "src\\tests\\storageTests\\emptyLocationTest.txt";
	private static final String FILE_TEST_EMPTY_TAGS = "src\\tests\\storageTests\\emptyTagsTest.txt";
	private static final String FILE_TEST_EMPTY_DEADLINE = "src\\tests\\storageTests\\emptyDeadlineTest.txt";
	private static final String FILE_TEST_EMPTY_DATES = "src\\tests\\storageTests\\emptyDatesTest.txt";

	private Storage storage = Storage.getInstance();
	private Display display;

	/* Creates a display to be used for the unit tests */
	@Before
	public void setUpDisplay() {
		ArrayList<String> tags = new ArrayList<>(Arrays.asList("tag1, tag2"));
		Calendar date = Calendar.getInstance();

		TaskFloat floatTask = new TaskFloat("Test Floating", "NUS", tags);
		TaskDeadline deadlineTask = new TaskDeadline("Test Deadline", "NUS", date, tags);
		TaskEvent eventTask = new TaskEvent("Test Event", "NUS", date, date, tags);

		ArrayList<TaskFloat> floatTasks = new ArrayList<TaskFloat>();
		ArrayList<TaskDeadline> deadlineTasks = new ArrayList<TaskDeadline>();
		ArrayList<TaskEvent> eventTasks = new ArrayList<TaskEvent>();
		ArrayList<TaskReserved> reservedTasks = new ArrayList<TaskReserved>();
		ArrayList<Task> completedTasks = new ArrayList<Task>();

		floatTasks.add(floatTask);
		floatTasks.add(floatTask);

		deadlineTasks.add(deadlineTask);
		deadlineTasks.add(deadlineTask);

		eventTasks.add(eventTask);
		eventTasks.add(eventTask);

		display = new Display("", eventTasks, deadlineTasks, floatTasks, reservedTasks, completedTasks);
	}

	@Before
	public void prepareTextFileForTests() throws IOException {
		storage.createFile(FILE_TEST);
	}

	/* This is a case for the successful createFile() partition */
	@Test
	public void testfileExists() {
		File file = new File(FILE_TEST);
		assertTrue(file.exists());
	}

	/*
	 * This is a case for the createFile() partition proving that it creates a
	 * file in the correct location
	 */
	@Test
	public void testfileLocation() {
		File file = new File(FILE_TEST_INVALID);
		assertFalse(file.exists());
	}

	/* This is a case for the normal reading from file partition */
	@Test
	public void testSaveAndRead() throws IOException {
		storage.saveFile(display);
		assertEquals(display.toString(), storage.getDisplay(FILE_TEST).toString());
	}

	/*
	 * This is a case for the reading from a file with empty descriptions
	 * partition
	 */
	@Test
	public void testReadEmptyDescription() throws IOException {
		Display emptyDescriptionDisplay = storage.getDisplay(FILE_TEST_EMPTY_DESCRIPTION);

		ArrayList<TaskFloat> emptyFloat = emptyDescriptionDisplay.getFloatTasks();
		ArrayList<TaskDeadline> emptyDeadline = emptyDescriptionDisplay.getDeadlineTasks();
		ArrayList<TaskEvent> emptyEvent = emptyDescriptionDisplay.getEventTasks();

		assertEquals("undefined", emptyFloat.get(0).getDescription());
		assertEquals("undefined", emptyDeadline.get(0).getDescription());
		assertEquals("undefined", emptyEvent.get(0).getDescription());
	}

	/*
	 * This is a case for the reading from a file with empty locations partition
	 */
	@Test
	public void testReadEmptyLocation() throws IOException {
		Display emptyDescriptionDisplay = storage.getDisplay(FILE_TEST_EMPTY_LOCATION);

		ArrayList<TaskFloat> emptyFloat = emptyDescriptionDisplay.getFloatTasks();
		ArrayList<TaskDeadline> emptyDeadline = emptyDescriptionDisplay.getDeadlineTasks();
		ArrayList<TaskEvent> emptyEvent = emptyDescriptionDisplay.getEventTasks();

		assertEquals(null, emptyFloat.get(0).getLocation());
		assertEquals(null, emptyDeadline.get(0).getLocation());
		assertEquals(null, emptyEvent.get(0).getLocation());
	}

	/* This is a case for the reading from a file with empty tags partition */
	@Test
	public void testReadEmptyTags() throws IOException {
		Display emptyDescriptionDisplay = storage.getDisplay(FILE_TEST_EMPTY_TAGS);

		ArrayList<TaskFloat> emptyFloat = emptyDescriptionDisplay.getFloatTasks();
		ArrayList<TaskDeadline> emptyDeadline = emptyDescriptionDisplay.getDeadlineTasks();
		ArrayList<TaskEvent> emptyEvent = emptyDescriptionDisplay.getEventTasks();

		ArrayList<Task> emptyTagList = new ArrayList<Task>();

		assertEquals(emptyTagList, emptyFloat.get(0).getTags());
		assertEquals(emptyTagList, emptyDeadline.get(0).getTags());
		assertEquals(emptyTagList, emptyEvent.get(0).getTags());
	}

	/*
	 * This is a case for the reading from a file with empty deadlines partition
	 */
	@Test
	public void testReadEmptyDeadline() throws IOException {
		Display emptyDescriptionDisplay = storage.getDisplay(FILE_TEST_EMPTY_DEADLINE);
		ArrayList<TaskDeadline> emptyDeadline = emptyDescriptionDisplay.getDeadlineTasks();
		assertTrue(emptyDeadline.isEmpty());
	}

	/* This is a case for the reading from a file with empty dates partition */
	@Test
	public void testReadEmptyDates() throws IOException {
		Display emptyDescriptionDisplay = storage.getDisplay(FILE_TEST_EMPTY_DATES);
		ArrayList<TaskEvent> emptyEvent = emptyDescriptionDisplay.getEventTasks();
		assertTrue(emptyEvent.isEmpty());
	}

	@After
	public void closeDownAfterTests() {
		File file = new File(FILE_TEST);
		file.delete();
	}

}
