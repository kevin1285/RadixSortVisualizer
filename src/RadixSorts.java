import java.util.*;
class RadixSorts{
	public static void main(String args[]) {
		int[] A = new int[50];
		for(int i=0; i<50; i++)
			A[i] = (int)(Math.random()*50 + 900);
		sortMSD(A);
		System.out.println(Arrays.toString(A));
	}
	public static void sortLSD(int[] A) {
		int max = Arrays.stream(A).max().getAsInt();
		for(int exp=1; max/exp>0; exp*=10)
			countSort(A, exp, 0, A.length-1);
	}
	
	public static void sortMSD(int[] A) {
		int max = Arrays.stream(A).max().getAsInt();
		int maxDigits = max==0 ? 0 : (int)(Math.log10(max)) + 1;
		int exp = (int) Math.pow(10, maxDigits-1);
		
		MSDhelper(A, exp, 0, A.length-1);
	}
	private static void MSDhelper(int[] A, int exp, int start, int end) {
		if (start >= end || exp <= 0) 
            return;
		int[] counts = countSort(A, exp, start, end);
		
        for(int i=0; i<10; i++) {
        	if(counts[i] == 0)
        		continue;
        	end = start + counts[i]-1;
        	System.out.println(exp + " " + start + "-" + end);
        	MSDhelper(A, exp/10, start, end);
        	start = end + 1;
        }
	}
	public static int[] countSort(int[] A, int exp, int start, int end) {
		int[] sorted = new int[end-start+1], counts = new int[10];
		for(int i=start; i<=end; i++)
			counts[A[i]/exp % 10]++;
		int[] temp = Arrays.copyOf(counts, 10);
		for(int i=1; i<10; i++)
			counts[i] += counts[i-1];
		
		for(int i=end; i>=start; i--) 
			sorted[--counts[A[i]/exp % 10]] = A[i];
		
		for(int i=0; i<sorted.length; i++)
			A[i+start] = sorted[i];
		
		return temp;
	}
}