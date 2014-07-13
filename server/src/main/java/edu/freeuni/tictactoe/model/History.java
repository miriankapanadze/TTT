package edu.freeuni.tictactoe.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Entity
public class History implements Serializable {

	private long id;

	private User firstUser;

	private User secondUser;

	private int result;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne
	public User getFirstUser() {
		return firstUser;
	}

	public void setFirstUser(User firstUser) {
		this.firstUser = firstUser;
	}

	@ManyToOne
	public User getSecondUser() {
		return secondUser;
	}

	public void setSecondUser(User secondUser) {
		this.secondUser = secondUser;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	@Transient
	public static JSONObject getJSONObject(History history) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", history.getId());
			jsonObject.put("firstUser_name", history.getFirstUser().getName());
			jsonObject.put("firstUser_username", history.getFirstUser().getUsername());
			jsonObject.put("secondUser_name", history.getSecondUser().getName());
			jsonObject.put("secondUser_username", history.getSecondUser().getUsername());
			jsonObject.put("result", history.getResult());

			return jsonObject;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transient
	public static JSONArray getJSONArray(List<History> histories) {
		JSONArray jsonArray = new JSONArray();
		for (History history : histories) {
			jsonArray.put(getJSONObject(history));
		}
		return jsonArray;
	}
}
