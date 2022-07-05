import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/*
 * This class includes test cases for the basic/normal functionality of the 
 * FriendFinder.findClassmates method, but does not check for any error handling.
 */

public class FindClassmatesTest {

	protected FriendFinder ff;

	protected ClassesDataSource defaultClassesDataSource = new ClassesDataSource() {

		@Override
		public List<String> getClasses(String studentName) {

			if (studentName.equals("A")) {
				return List.of("1", "2", "3");
			} else if (studentName.equals("B")) {
				return List.of("1", "2", "3");
			} else if (studentName.equals("C")) {
				return List.of("2", "4");
			} else
				return null;

		}

	};

	protected StudentsDataSource defaultStudentsDataSource = new StudentsDataSource() {

		@Override
		public List<Student> getStudents(String className) {

			Student a = new Student("A", 101);
			Student b = new Student("B", 102);
			Student c = new Student("C", 103);

			if (className.equals("1")) {
				return List.of(a, b);
			} else if (className.equals("2")) {
				return List.of(a, b, c);
			} else if (className.equals("3")) {
				return List.of(a, b);
			} else if (className.equals("4")) {
				return List.of(c);
			} else
				return null;
		}

	};

	@Test
	public void testFindOneFriend() {

		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertEquals(1, response.size());
		assertTrue(response.contains("B"));

	}

	@Test
	public void testFindNoFriends() {

		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("C", 103));
		assertNotNull(response);
		assertTrue(response.isEmpty());

	}

	@Test
	public void testClassesDataSourceReturnsNullForInputStudent() {

		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("D", 104));
		assertNotNull(response);
		assertTrue(response.isEmpty());

	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullInput() {
		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullInputName() {
		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student(null, 105));
	}

	@Test(expected = IllegalStateException.class)
	public void testNullClassesDataSource() {
		ff = new FriendFinder(null, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("A", 101));
	}

	@Test(expected = IllegalStateException.class)
	public void testNullStudentsDataSource() {
		ff = new FriendFinder(defaultClassesDataSource, null);
		Set<String> response = ff.findClassmates(new Student("A", 101));
	}

	protected ClassesDataSource returnNullClassesDataSource = new ClassesDataSource() {

		@Override
		public List<String> getClasses(String studentName) {

			if (studentName.equals("A")) {
				return List.of("1", "2");
			} else if (studentName.equals("B")) {
				return List.of("1", "2");
			} else
				return null;

		}

	};

	protected StudentsDataSource returnNullStudentsDataSource = new StudentsDataSource() {

		@Override
		public List<Student> getStudents(String className) {

			Student a = new Student("A", 101);
			Student b = new Student("B", 102);

			if (className.equals("1")) {
				return List.of(a, b);
			} else
				return null;
		}

	};

	@Test
	public void testStudentsDataSourceReturnsNull() {
		ff = new FriendFinder(returnNullClassesDataSource, returnNullStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertTrue(response.size() == 1);
		assertTrue(response.contains("B"));
	}

	@Test
	public void testStudentsDataSourceOutputContainsNullValue() {
		ff = new FriendFinder(returnNullClassesDataSource, new StudentsDataSource() {
			@Override
			public List<Student> getStudents(String className) {
				Student a = new Student("A", 101);
				Student b = new Student("B", 102);
				List<Student> ret = new ArrayList<>();
				ret.add(a);
				ret.add(b);
				ret.add(null);
				return ret;
			}
		});
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertTrue(response.size() == 1);
		assertTrue(response.contains("B"));
	}

	@Test
	public void testClassesDataSourceOutputContainsNullValue() {
		ff = new FriendFinder(new ClassesDataSource() {
			@Override
			public List<String> getClasses(String studentName) {
				List<String> ret = new ArrayList<>();
				ret.add("1");
				ret.add("2");
				ret.add(null);
				return ret;
			}
		}, new StudentsDataSource() {
			@Override
			public List<Student> getStudents(String className) {
				Student a = new Student("A", 101);
				Student b = new Student("B", 102);
				List<Student> ret = new ArrayList<>();
				ret.add(a);
				ret.add(b);
				return ret;
			}
		});
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertTrue(response.size() == 1);
		assertTrue(response.contains("B"));
	}

	@Test
	public void testOtherClassesDataSourceReturnsNull() {
		ff = new FriendFinder(returnNullClassesDataSource, new StudentsDataSource() {
			@Override
			public List<Student> getStudents(String className) {
				Student a = new Student("A", 101);
				Student b = new Student("B", 102);
				Student c = new Student("C", 103);
				return List.of(a, b, c);
			}
		});
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertTrue(response.size() == 1);
		assertTrue(response.contains("B"));
	}

	@Test
	public void testOtherStudentsWithNullName() {
		ff = new FriendFinder(returnNullClassesDataSource, new StudentsDataSource() {
			@Override
			public List<Student> getStudents(String className) {
				Student a = new Student("A", 101);
				Student b = new Student(null, 102);
				return List.of(a, b);
			}
		});
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertTrue(response.isEmpty());
	}

	@Test
	public void testNotCheckNullValueContainedInClassesList() {
		ff = new FriendFinder(new ClassesDataSource() {
			@Override
			public List<String> getClasses(String studentName) {
				List<String> ret = new ArrayList<>();
				ret.add("1");
				ret.add("2");
				if (studentName.equals("A"))
					ret.add(null);
				return ret;
			}
		}, new StudentsDataSource() {
			@Override
			public List<Student> getStudents(String className) {
				Student a = new Student("A", 101);
				Student b = new Student("B", 102);
				List<Student> ret = new ArrayList<>();
				ret.add(a);
				ret.add(b);
				return ret;
			}
		});
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertTrue(response.size() == 1);
		assertTrue(response.contains("B"));
	}
	
}
