package edu.freeuni.tictactoe.service;

import edu.freeuni.tictactoe.db.EMFactory;
import edu.freeuni.tictactoe.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

public class UsersManager {

	private static UsersManager instance;

	private EntityManager em;

	private Logger logger = LoggerFactory.getLogger(UsersManager.class);

	private UsersManager() {
		this.em = EMFactory.createEM();
	}

	public static UsersManager getInstance() {
		if (instance == null) {
			instance = new UsersManager();
		}
		return instance;
	}

	public void updateUser(User user) {
		em.getTransaction().begin();
		em.merge(user);
		em.getTransaction().commit();
	}
}
