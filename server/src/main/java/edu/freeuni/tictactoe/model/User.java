package edu.freeuni.tictactoe.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Entity
public class User implements Serializable {

	private int id;

	private String name;

	private String username;

	private String password;

	private int rank;

	public User() {}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User(String name, String username, String password) {
		this.name = name;
		this.username = username;
		this.password = password;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Transient
	public static JSONObject getJSONObject(User user) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", user.getId());
			jsonObject.put("name", user.getName());
			jsonObject.put("username", user.getUsername());
			jsonObject.put("password", user.getPassword());
			jsonObject.put("rank", user.getRank());

			return jsonObject;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transient
	public static JSONArray getJSONArray(List<User> users) {
		JSONArray jsonArray = new JSONArray();
		for (User user : users) {
			jsonArray.put(getJSONObject(user));
		}
		return jsonArray;
	}
}
