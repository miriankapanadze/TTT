package edu.freeuni.tictactoe.service;

import edu.freeuni.tictactoe.db.EMFactory;
import edu.freeuni.tictactoe.model.History;
import edu.freeuni.tictactoe.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class UsersManager {

	private static UsersManager instance;

	private EntityManager em;

	@SuppressWarnings("UnusedDeclaration")
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

	public void registerUser(User user) throws Exception {
		updateUser(user);
	}

	public User updateUser(User user) throws Exception {
		String userQL = (user.getId() == 0) ? "" : "AND u.id != " + user.getId();
		Query q = em.createQuery("SELECT COUNT(*) FROM User u WHERE u.username = :username " + userQL);
		q.setParameter("username", user.getUsername());
		if ((Long) q.getSingleResult() > 0) {
			throw new Exception("usernameAlreadyUsed");
		} else {
			em.getTransaction().begin();
			em.merge(user);
			em.getTransaction().commit();
		}
		return user;
	}

	public User findUserById(int id) throws Exception {
		try {
			return (User) em.createQuery("SELECT u FROM User u WHERE u.id = :id")
					.setParameter("id", id)
					.getSingleResult();

		} catch (NoResultException e) {
			throw new Exception("userNotFound");
		}
	}

	public User findUserByCredentials(User user) throws Exception {
		try {
			return (User) em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
					.setParameter("username", user.getUsername())
					.setParameter("password", user.getPassword())
					.getSingleResult();

		} catch (NoResultException e) {
			throw new Exception("invalidCredentials");
		}
	}

	public List<User> getOpponents(User user) {
		//noinspection unchecked
		return em.createQuery("SELECT u FROM User u WHERE u.id <> :id")
				.setParameter("id", user.getId())
				.getResultList();
	}

	public List<History> getUserHistory(User user) {
		//noinspection unchecked
		return em.createQuery("SELECT h FROM History h WHERE h.firstUser.id = :id OR h.secondUser.id = :id")
				.setParameter("id", user.getId())
				.getResultList();
	}

	public void updateUserRanksByHistory(History history) {
		updateUserRank(history.getFirstUser(), history.getResult());
		updateUserRank(history.getSecondUser(), history.getResult());
	}

	private void updateUserRank(User user, int plusRank) {
		int rank = user.getRank() + plusRank;
		rank = rank > 0 ? rank : 0;

		em.createQuery("UPDATE User u SET u.rank = :rank WHERE u.id = :id")
				.setParameter("id", user.getId())
				.setParameter("rank", rank)
				.executeUpdate();
	}
}
