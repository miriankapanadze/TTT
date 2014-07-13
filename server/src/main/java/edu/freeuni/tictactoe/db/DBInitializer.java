package edu.freeuni.tictactoe.db;

import edu.freeuni.tictactoe.model.History;
import edu.freeuni.tictactoe.model.User;

import javax.persistence.EntityManager;

public class DBInitializer {

	public static void main(String [] args) {
		EntityManager em = EMFactory.createEM();
        em.getTransaction().begin();

		User firstUser = new User();
		firstUser.setName("Zura");
		firstUser.setUsername("zux");
		firstUser.setPassword("1234");

		User secondUser = new User();
		secondUser.setName("Mirian");
		secondUser.setUsername("mirian");
		secondUser.setPassword("1234");

		History history = new History();
		history.setFirstUser(firstUser);
		history.setSecondUser(secondUser);
		history.setResult(1);

		em.persist(firstUser);
		em.persist(secondUser);
		em.persist(history);

		em.getTransaction().commit();
		EMFactory.close();
	}
}