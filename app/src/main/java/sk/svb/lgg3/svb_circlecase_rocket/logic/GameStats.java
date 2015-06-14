package sk.svb.lgg3.svb_circlecase_rocket.logic;

import com.orm.SugarRecord;

public class GameStats extends SugarRecord<GameStats> {

	public static final int GAME_LR = 0;
	public static final int GAME_RT = 1;
	
	private int score = 0;
	private int type = 0;
	
	private String date = "";
	private String name = "";

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
