package edu.freeuni.tictactoe.service;

import edu.freeuni.tictactoe.db.EMFactory;
import edu.freeuni.tictactoe.model.History;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

public class HistoryManager {

	private static HistoryManager instance;

	private EntityManager em;

	@SuppressWarnings("UnusedDeclaration")
	private Logger logger = LoggerFactory.getLogger(UsersManager.class);

	private HistoryManager() {
		this.em = EMFactory.createEM();
	}

	public static HistoryManager getInstance() {
		if (instance == null) {
			instance = new HistoryManager();
		}
		return instance;
	}

	public void addHistory(History history) {
		em.getTransaction().begin();
		em.persist(history);
		em.getTransaction().commit();

		UsersManager.getInstance().updateUserRanksByHistory(history);
	}
}
