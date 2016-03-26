//U10416012 劉宏德

import java.util.*;

public class BigInteger{

  private List<Integer> value;
	
	public BigInteger(String value1) {
		
		//take digital part
		String v = value1.charAt(0) == '-' ? value1.substring(1) : value1;
		
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
		value = value1.charAt(0) == '-' ? toComplement(value) : value;
	}
	
	private BigInteger(List<Integer> value) {
		this.value = value;
	}
	
	//add
	public BigInteger add(BigInteger t) {
		//analyzing the number is positive or negative
		if(isNegative(t.value)) {
			return subtract(new BigInteger(toComplement(t.value)));
		}
		
		//align the digit
		int length = Math.max(value.size(), t.value.size());
		List<Integer> num1 = copyOf(value, length);
		List<Integer> num2 = copyOf(t.value, length);
		List<Integer> result = new ArrayList<>();
		
		int carry = 0;
		for(int i = 0; i < length - 1; i++) {
			int c = num1.get(i) + num2.get(i) + carry;
			
			if(c < 10000) {
				carry = 0;
			}
			
			else {
				c -= 10000;
				carry = 1;
			}
			
			result.add(c);
		}
		
		if(carry == 1) {	//deal with overflow
			
			if(isPositive(op1)) {
				result.add(1);
			}
			
			for(int i = 0; i < 8; i++) {
				result.add(0);	//increase eight digit
			}
		}
		
		else {		//complement, if positive number add 0, if negative number add 9999
			result.add(isPositive(num1) ? 0 : 9999);
		}
		
		return new BigInteger(result);
	}
	
	//subtract
	public BigInteger subtract(BigInteger t) {
	
		if(isNegative(t.value)) {
			return add(new BigInteger(toComplement(t.value)));
		}
		
		//align the digit
		int length = Math.max(value.size(), t.value.size());
		List<Integer> op1 = copyOf(value, length);
		List<Integer> op2 = copyOf(t.value, length);
		List<Integer> result = new ArrayList<>();
		
		int borrow = 0;
		for(int i = 0; i < length - 1; i++) {
			int c = num1.get(i) - num2.get(i) - borrow;
			
			if(c > -1) {
				borrow = 0;
			}
			
			else {		//borrow place
				c += 10000;
				borrow = 1;
			}
			
			result.add(c);
		}
		
		if(borrow == 1) {
			if(isNegative(num1)) {
				result.add(9998);
			}
			
			else {		//when overflow on the positive integer subtract another positive integer, the result is zero
				result.clear();
			}
			
			for(int i = 0; i < 8; i++) {
				result.add(9999);
			}
		}
		
		else {		//complement, if the number which the user input is a positive integer, and then add 0, if it is a negative integer, add 9999 
			result.add(isNegative(num1) ? 9999 : 0);
		}
		
		return new BigInteger(result);
	}
	
	//multiply
	private BigInteger multiply(int value1, int shift) {
		List<Integer> result = new ArrayList<>();
		
		for(int i = 0; i < shift; i++) {
			result.add(0);		//if the result overflow, and add 0 at a first digit
		}
		
		int carry = 0;
		for(int i= 0; i < value.size() - 1; i++) {
			int tmp= value.get(i) * value1 + carry;
			result.add(tmp % 10000);
			carry = tmp / 10000;
		}
		
		if(carry != 0) {
			result.add(carry);
			
			for(int i = 0; i < 8; i++) {
				result.add(0);
			}
		}
		
		else {
			result.add(0);
		}
		
		return new BigInteger(result);
	}
	
	public BigInteger multiply(BigInteger t) {
		
		//change to positive number
		BigInteger num1 = isNegative(value) ? new BigInteger(toComplement(value)) : this;
		List<Integer> num2 = isNegative(t.value) ? toComplement(t.value) : t.value;
		
		//calculate with every digit
		List<BigInteger> rs = new ArrayList<>();
		for(int i = 0; i < num2.size() - 1; i++) {
			rs.add(num1.multiply(num2.get(i), i));
		}
		
		//add every digit 
		BigInteger result = rs.get(0);
		for(int i = 1; i < rs.size(); i++) {
			result = result.add(rs.get(i));
		}
		
		//judge positive or negative
		return getLast(value) + getLast(t.value) == 9999 ? new BigInteger(toComplement(result.value)) : result;
	}
	
	//judge result is greater or equal the number which the user input
	public boolean greaterOrEquals(BigInteger t) {
		return isNegative(subtract(t).value) ? false : true;
	}
	
	//judge result is less or equal the number which the user input
	private boolean islessOrEqualsToQuotient(BigInteger num1, BigInteger num2) {
		return num1.greaterOrEquals(multiply(num2)) ? true : false;
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
	
	//if the number which the user input is a negative number, then add 9999
	private static boolean isNegative(List<Integer> list) {
		return getLast(list) == 9999;
	}
	
	//if the number which the number input is a positive number, then add 0
	private static boolean isPositive(List<Integer> list) {
		return getLast(list) == 0;
	}
	
	//judge the number which the user input is or not zero
	private static boolean isZero(List<Integer> list) {
		for(Integer i : list) {
			if(i != 0) {
				return false;
			}
		}
		
		return true;
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
