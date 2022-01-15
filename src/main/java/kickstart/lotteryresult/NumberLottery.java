package kickstart.lotteryresult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
		LocalDate d = LocalDate.of(2022,2,10);
		LocalDate e = d.plusMonths(1);
		System.out.println(d.getDayOfWeek());
		System.out.println(e);
		System.out.println(e.getDayOfWeek());

		List<Integer> ml = new ArrayList<>();
		ml.add(4);
		ml.add(1);
		ml.add(67);
		ml.add(23);
		ml.add(34);
		List<Integer> ml2 = new ArrayList<>();
		ml2.add(67);
		ml2.add(23);
		ml2.add(34);
		ml2.add(4);
		ml2.add(12);

		System.out.println(ml);
		System.out.println(ml2);
		Collections.sort(ml2);
		boolean wert = true;
		for(int i: ml){
			if(!ml2.contains(i)){
				wert = false;
				break;
			}
		}
		System.out.println(ml2);
	}

 */







}
