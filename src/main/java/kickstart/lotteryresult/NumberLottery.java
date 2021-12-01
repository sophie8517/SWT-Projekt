package kickstart.lotteryresult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NumberLottery {
	private List<Integer> winNumbers = new ArrayList<>();
	private int additionalN;

	/*
	This is an empty constructor
	 */
	public NumberLottery(){
		this.winNumbers = generate_nums();
		this.additionalN = generateAdditionalNumber();
	}

	public List<Integer> generate_nums(){
		List<Integer> result = new ArrayList<>();
		Random random = new Random();

		while(result.size() != 6){
			int num =Math.abs((random.nextInt() % 49)) + 1;
			if(!result.contains(num)){
				result.add(num);
			}
		}
		return result;
	}

	public int generateAdditionalNumber(){
		Random random = new Random();
		int num = Math.abs(random.nextInt() % 10);
		return num;
	}

	public List<Integer> getWinNumbers() {
		return winNumbers;
	}

	public int getAdditionalN() {
		return additionalN;
	}
	/*
	public static void main(String[] args) {
		LocalDate d = LocalDate.of(2021,12,28);
		LocalDate e = d.plusDays(7);
		System.out.println(d.getDayOfWeek());
		System.out.println(e);
		System.out.println(e.getDayOfWeek());
	}

	 */



}
