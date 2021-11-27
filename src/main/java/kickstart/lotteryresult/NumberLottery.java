package kickstart.lotteryresult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NumberLottery {
	private List<Integer> winNumbers = new ArrayList<>();
<<<<<<< HEAD
	private int additionalN;
=======
>>>>>>> 44b366b5d9e497327ffebb5e9cd9df9c9218b003

	/*
	This is an empty constructor
	 */
	public NumberLottery(){
		this.winNumbers = generate_nums();
<<<<<<< HEAD
		this.additionalN = winNumbers.get(6);
		this.winNumbers.remove(additionalN);
=======
>>>>>>> 44b366b5d9e497327ffebb5e9cd9df9c9218b003
	}

	public List<Integer> generate_nums(){
		List<Integer> result = new ArrayList<>();
		Random random = new Random();

		while(result.size() != 7){
			int num =Math.abs((random.nextInt() % 49)) + 1;
			if(!result.contains(num)){
				result.add(num);
			}
		}
		return result;
	}

	public List<Integer> getWinNumbers() {
		return winNumbers;
	}
<<<<<<< HEAD

	public int getAdditionalN() {
		return additionalN;
	}
=======
>>>>>>> 44b366b5d9e497327ffebb5e9cd9df9c9218b003
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
