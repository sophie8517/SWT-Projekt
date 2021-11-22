package kickstart.lotteryresult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NumberLottery {
	//private List<Integer> win_numbers = new ArrayList<>();

	/*
	This is an empty constructor
	 */
	public NumberLottery(){}

	public List<Integer> generate_nums(){
		List<Integer> result = new ArrayList<>();
		Random random = new Random();

		while(result.size() < 6){
			int num =Math.abs((random.nextInt() % 49)) + 1;
			if(!result.contains(num)){
				result.add(num);
			}
		}
		return result;
	}
/*
	public static void main(String[] args) {
		NumberLottery l = new NumberLottery();
		List<Integer> liste = l.generate_nums();
		System.out.println(liste);
		LocalDate d = LocalDate.now();
		System.out.println(d.getDayOfWeek().getValue());
	}

 */

}
