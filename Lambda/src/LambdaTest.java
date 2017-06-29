import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class LambdaTest {

	FlexibleTreeNode<Person> root;
	FlexibleTreeNode<Person> parent1, parent1_1, parent1_2, parent2, parent2_1, parent2_2;
	FlexibleTreeNode<Person> leaf1_1, leaf1_2, leaf2, leaf2_1, leaf2_2;
	List<String> lstSentEmails;
	
	@Before
	public void setUp(){
		lstSentEmails = new ArrayList<String>();
		root = new FlexibleTreeNode<Person>(new Person("001", "Präsident", HSPersonaltyp.Praesident, "bt@hs-rm.de"));
			parent1 = new FlexibleTreeNode<Person>(new Person("002", "Dekan", HSPersonaltyp.Dekan, "bt@hs-rm.de"));
				leaf1_1 = new FlexibleTreeNode<Person>(new Person("003", "Student", HSPersonaltyp.Student, "bt@hs-rm.de"));
				leaf1_2 =  new FlexibleTreeNode<Person>(new Person("004", "Student", HSPersonaltyp.Student, "bt@hs-rm.de"));
			parent2 = new FlexibleTreeNode<Person>(new Person("003", "Dekan", HSPersonaltyp.Dekan, "bt@hs-rm.de"));
				leaf1_1 = new FlexibleTreeNode<Person>(new Person("005", "Student", HSPersonaltyp.Student, "bt@hs-rm.de"));
				leaf1_2 =  new FlexibleTreeNode<Person>(new Person("006", "Student", HSPersonaltyp.Student, "bt@hs-rm.de"));
				
		root.add(parent1);
		root.add(parent2);
			parent1.add(leaf1_1);
			parent1.add(leaf1_2);
			parent2.add(leaf2_1);
			parent2.add(leaf2_2);
	}
	
	//Aufgabe 1b
	@SuppressWarnings("unused")
	private void sendMail(String strEmailAdress){
		lstSentEmails.add(strEmailAdress);
	}
	
	@Test
	public void testForEach(){
		root.forEach((t,u) -> lstSentEmails.add(u.getEmail()));
		assertTrue( lstSentEmails.contains(root.getUserObject().getEmail()) );
		assertEquals(8, lstSentEmails.size());
	}
	@Test
	public void testForEach1(){
		parent2.forEach((t,u) -> lstSentEmails.add(u.getEmail()));
		assertTrue( lstSentEmails.contains(leaf2_1.getUserObject().getEmail()) );
		assertFalse( lstSentEmails.contains(root.getUserObject().getEmail()) );
		assertEquals(2, lstSentEmails.size());
	}
	
	@Test
	public void testSentEmail(){
		root.forEach((t,u) ->sendMail(u.getEmail()));

		
		root.forEach((t,u) -> lstSentEmails.add(u.getEmail()));
		assertTrue( lstSentEmails.contains(root.getUserObject().getEmail()) );
	}
	
	@Test
	public void testStream(){
		List<Person> lstPersons = new LinkedList<Person>();
		root.forEach((t,u) ->lstPersons.add(u));
		

		lstPersons.stream().forEach((u) -> sendMail(u.getEmail()));
		

		
		assertTrue( lstPersons.contains(root.getUserObject()) );
		
		assertTrue(lstSentEmails.contains(lstPersons.get(0).getEmail()));
		assertTrue(lstSentEmails.contains(root.getUserObject().getEmail() ));
		
	}
	

	@After
	public void tearDown(){
		lstSentEmails.clear();
	}

}
