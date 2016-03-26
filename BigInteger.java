//U10416012 劉宏德

import java.util.*;

public class BigInteger{

  private List<Integer> value;
	
	public BigInteger(String val) {
		
		//take digital part
		String v = val.charAt(0) == '-' ? val.substring(1) : val;
		
		//every four characters parsed as a int
		value = new ArrayList<>();
		for(int i = v.length() - 4; i > -4; i -= 4) {
			value.add(Integer.parseInt(v.substring(i >= 0 ? i : 0, i + 4)));
		}
		
		//complement, use 8 as a unit
		int valueLength = (value.size() / 8 + 1) * 8;
		for(int i = value.size(); i < valueLength; i++) {
			value.add(0);
		}
		
		//turn into negative to complement
		value = val.charAt(0) == '-' ? toComplement(value) : value;
	}
	
	private BigInteger(List<Integer> value) {
		this.value = value;
	}
	
	//complement,deal with overflow
	private static List<Integer> toComplement(List<Integer> v) {
		List<Integer> comp = new ArrayList<>();
		
		for(Integer i : v) {
			comp.add(9999 - i);
		}
		
		comp.set(0, comp.get(0) + 1);
		return comp;
	}
	
	public static void main(String[] args) {
	  
	  //creata a scanner
		Scanner input = new Scanner(System.in);
		
		System.out.print("enter the first number you want to calculate: ");
		String c = input.nextLine();
		System.out.print("enter the second number you want to calculate: ");
		String d = input.nextLine();
		
		//let the two number which the user input String change to BigInteger
		BigInteger a = new BigInteger(c);
		BigInteger b = new BigInteger(d);
		
		System.out.println(a.add(b));
		System.out.println(a.subtract(b));
		System.out.println(a.multiply(b));
		System.out.println(a.divide(b));
	}
}
