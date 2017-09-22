package IGT;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Vector;

public class Decks {
	private class card{
		private int reward = 0;
		private int punish = 0;
		
		private int getReward() {
			return reward;
		}
		private void setReward(int reward) {
			this.reward = reward;
		}
		private int getPunish() {
			return punish;
		}
		private void setPunish(int punish) {
			this.punish = punish;
		}
	}
	
	int numberOfDecks = 4;
	int numberOfCards = 40;
	
	Vector<ArrayDeque<card>> decks;
	
	public Decks() {
		decks = new Vector<ArrayDeque<card>>();
		for (int i = 0; i < numberOfDecks; i++) {
			decks.add(new ArrayDeque<card>());
		}
		
		try {
			InputStream rewardInputStream = getClass().getResourceAsStream("/decksRewardSeed.csv");
			InputStream punishInputStream = getClass().getResourceAsStream("/decksPunishSeed.csv");
			
			BufferedReader reward = new BufferedReader(new InputStreamReader(rewardInputStream, "UTF-8"));
			BufferedReader punish = new BufferedReader(new InputStreamReader(punishInputStream, "UTF-8"));

			for (int i = 0; i < numberOfCards; i++) {
				String[] rewardWords = reward.readLine().split(",");
				String[] punishWords = punish.readLine().split(",");

				for (int j = 0; j < numberOfDecks; j++) {
					if (rewardWords[j].startsWith("\uFEFF"))
						rewardWords[j] = rewardWords[i].substring(1);
					if (punishWords[j].startsWith("\uFEFF"))
						punishWords[j] = rewardWords[i].substring(1);
					
					card tmpCard = new card();
					tmpCard.setReward(Integer.parseInt(rewardWords[j]));
					tmpCard.setPunish(Integer.parseInt(punishWords[j]));
					
					decks.get(j).addLast(tmpCard);
				}
			}

			reward.close();
			punish.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int[] getCard(int deckNumber) {
		card curCard = decks.get(deckNumber).pollFirst();
		int result[] = {curCard.getReward(), curCard.getPunish()};
		
		decks.get(deckNumber).addLast(curCard);
		return result;
	}
}
