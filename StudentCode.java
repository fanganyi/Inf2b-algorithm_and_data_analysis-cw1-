package imult;

/*
 * Class StudentCode: class for students to implement
 * the specific methods required by the assignment:
 * add()
 * sub()
 * koMul()
 * koMulOpt()
 * (See coursework handout for full method documentation)
 */
import java.io.*;
import java.lang.reflect.Field;

public class StudentCode {
	public static BigInt add(BigInt A, BigInt B) {
		// create return value
		BigInt r = new BigInt();
		// initial carry
		Unsigned C = new Unsigned(0);

		// length of return value could be one larger than max length between A
		// and B.
		// do addition from the least significant value to the most significant
		// value.
		// at the mean time, track the carry and save the result in return
		// value.
		for (int i = 0; i <= 1 + Math.max(A.length(), B.length()); i++) {
			DigitAndCarry d = Arithmetic.addDigits(A.getDigit(i), B.getDigit(i), C);
			C = d.getCarry();
			r.setDigit(i, d.getDigit());
		}
		return r;
	}

	public static BigInt sub(BigInt A, BigInt B) {
		// create return value
		BigInt r = new BigInt();
		// initial carry
		Unsigned C = new Unsigned(0);

		// max length of return value could be max length between A and B.
		// do subtraction from the least significant value to the most
		// significant value.
		// at the mean time, track the carry and save the result in return
		// value.
		for (int i = 0; i <= Math.max(A.length(), B.length()); i++) {
			DigitAndCarry d = Arithmetic.subDigits(A.getDigit(i), B.getDigit(i), C);
			C = d.getCarry();
			r.setDigit(i, d.getDigit());
		}
		return r;
	}

	public static BigInt koMul(BigInt A, BigInt B) {
		// find A B maximum length
		int length = Math.max(A.length(), B.length());
		// initial return value r and l,m,h
		BigInt r = new BigInt();
		BigInt l = new BigInt();
		BigInt m = new BigInt();
		BigInt h = new BigInt();
		// if max length is 1, do one digit multiply
		if (length <= 1) {
			Unsigned a = Arithmetic.mulDigits(A.getDigit(0), B.getDigit(0)).getDigit();
			Unsigned b = Arithmetic.mulDigits(A.getDigit(0), B.getDigit(0)).getCarry();
			r.setDigit(0, a);
			r.setDigit(1, b);
			return r;
		} else {
			// otherwise split A B and do koMul separately until max length is 1,
			// and give a return at the end
			BigInt a0 = A.split(0, length / 2 - 1);
			BigInt a1 = A.split(length / 2, length - 1);
			BigInt b0 = B.split(0, length / 2 - 1);
			BigInt b1 = B.split(length / 2, length - 1);

			l = koMul(a0, b0);
			h = koMul(a1, b1);
			m = sub(sub(koMul(add(a0, a1), add(b0, b1)), l), h);
		}
		m.lshift(length / 2);
		h.lshift(2 * (length / 2));
		r = add(add(l, m), h);
		return r;
	}

	public static BigInt koMulOpt(BigInt A, BigInt B) {
		// find minimum length of A B
		int length = Math.min(A.length(), B.length());
		// if minimum length is less than 10, do schoolMul, otherwise do koMul
		// once, then do koMulOpt within koMul
		if (length < 10) {

			return Arithmetic.schoolMul(A, B);

		} else {

			BigInt r = new BigInt();
			BigInt l = new BigInt();
			BigInt m = new BigInt();
			BigInt h = new BigInt();

			BigInt a0 = A.split(0, length / 2 - 1);
			BigInt a1 = A.split(length / 2, length - 1);
			BigInt b0 = B.split(0, length / 2 - 1);
			BigInt b1 = B.split(length / 2, length - 1);

			l = koMulOpt(a0, b0);
			h = koMulOpt(a1, b1);
			m = sub(sub(koMulOpt(add(a0, a1), add(b0, b1)), l), h);

			m.lshift(length / 2);
			h.lshift(2 * (length / 2));
			r = add(add(l, m), h);
			return r;
		}
	}

	public static void main(String argv[]) throws java.io.FileNotFoundException {
		// create a file
		File file = new File("koMulOptTimes.txt");

		BigIntMul.getRunTimes(new Unsigned(1), new Unsigned(10), new Unsigned(90), file, true);

		BigIntMul.getRatios(new Unsigned(1), new Unsigned(10), new Unsigned(90), new File("fout.txt"),
				new Unsigned(90));

		// c and a are obtained from file "fout.txt"
		double c = 0.0033809986931094952;
		double a = 0.002914204935557974;
		BigIntMul.plotRunTimes(c, a, file);
	}
} // end StudentCode class
