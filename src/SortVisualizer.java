import java.awt.*;
import java.util.*;
import javax.swing.JPanel;
public class SortVisualizer extends JPanel{
	private int[] A, counts, sorted, tempCounts;
	private static int s, countsS;
	private static final int xi = 10, aY = 100, countsY = aY+300, sortedY = countsY+100;
	private int incCountIdx=-1,decCountIdx=-1, accumulateCountsIdx=-1;
	private Arrow arrow;
	private boolean floatNumbers, wave;
	private Set<Integer> partitions = new HashSet<>();
	private int sortedIdx=-1;
	private int speed=5;
	private int partitionStart;
	private int hNumbers;
	private int[] waveHeights;
	public SortVisualizer(int[] A, int windowWidth) {
		this.A = A;
		s = (windowWidth-30)/50;
		countsS = (windowWidth-30)/20;
		hNumbers = sortedY+s/2;
		counts = new int[10];
		sorted = new int[A.length];
		Arrays.fill(sorted, -1);
		arrow = new Arrow(150, Color.RED);
		arrow.setX(-1000);
	}
	
	public int[] getA() {
		return A;
	}
	public void setA(int[] a) {
		A = a;
	}
	public int[] getCounts() {
		return counts;
	}
	public void setCounts(int[] counts) {
		this.counts = counts;
	}
	public static int getS() {
		return s;
	}
	public static int getXi() {
		return xi;
	}
	public static int getAy() {
		return aY;
	}
	public static int getCountsy() {
		return countsY;
	}
	public Arrow getArrow() {
		return arrow;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawA(g);
		drawCounts(g);
		drawSorted(g);
		arrow.draw(g);
	}
	private void drawA(Graphics g) {
		if(!wave) {
			for(int i=0; i<A.length; i++) {
				int x = s*i + xi;
				if(i <= sortedIdx) {
					g.setColor(Color.YELLOW);
					g.fillRect(x, aY, s, s);
				}
				g.setColor(Color.BLACK);
				g.drawRect(x, aY, s, s);
				g.drawString(A[i]+"", x + 1, aY + s/2);
				
				if(partitions.contains(i)) {
					Graphics2D g2 = (Graphics2D) g;
					g2.setStroke(new BasicStroke(2));
					g2.setColor(Color.BLUE);
					g2.drawLine(x, aY+s, x, aY);	
					g2.setStroke(new BasicStroke(1));
				}
			}
		}else {
			for(int i=0; i<A.length; i++) {
				int x = s*i + xi;
				g.setColor(Color.BLACK);
				g.drawRect(x, aY - waveHeights[i], s, s);
				g.drawString(A[i]+"", x + 1, aY - waveHeights[i] + s/2);
			}
		}
	}
	private void drawCounts(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for(int i=0; i<10; i++) {
			int x = countsS*i +xi;
			int height = counts[i]*3;
				
			g2.setColor(Color.BLUE);
			g2.fillRect(x, countsY-height, countsS, height);
			outline(g2, x, countsY-height, countsS, height, Color.BLACK);
			
			if (i == incCountIdx)
				outline(g2, x, countsY-height, countsS, height, Color.GREEN);
			else if(i == decCountIdx)
				outline(g2, x, countsY-height, countsS, height, Color.RED);
            else if(i == accumulateCountsIdx) {
				outline(g2, x, countsY-height, countsS, height, Color.YELLOW);
				outline(g2, x-countsS, countsY-counts[i-1]*3, countsS, counts[i-1]*3, Color.YELLOW);
			}
			
			g.setColor(Color.BLACK);
			g2.drawString(counts[i]+"", x + countsS/3, countsY - height-4);
			g2.drawString(i + "", x + countsS/3, countsY+s);
		}
	}
	private void outline(Graphics2D g2, int x, int y, int w, int h, Color c) {
		g2.setColor(c);
		g2.setStroke(new BasicStroke(2));
		g2.drawRect(x, y, w, h);
		g2.setStroke(new BasicStroke(1));
	}
	private void drawSorted(Graphics g) {
		for(int i=0; i<sorted.length; i++) {
			int x = s*i + xi + partitionStart*s;
			g.drawRect(x, sortedY, s, s);
			if(sorted[i] > -1)
				g.drawString(sorted[i]+"", x + 1, hNumbers);
			g.drawString(i+"", x+s/3, sortedY+3*s/2);
		}
	}
	public void radixSortLSD() {
		int max = Arrays.stream(A).max().getAsInt();
		for(int exp=1; max/exp>0; exp*=10) {
			countSortLSD(exp);
		}
		repaint();
		wave();
		sortedIdx = A.length-1;
		repaint();
	}
	private void fillCounts(int[] counts, int exp, int start, int end) {
		for(int i=start; i<=end; i++) {
			arrow.setX(xi + s*i);
			repaint();
			wait(3000/speed);
			incCountIdx = A[i]/exp %10;
			repaint();
			wait(3000/speed);
			counts[A[i]/exp %10]++;		
			repaint();
			wait(3000/speed);
			
			
		}
		reset();
	}
	private void accumulateCounts(int[] counts) {
		for(int i=1; i<10; i++) {
			accumulateCountsIdx = i;
			repaint();
			wait(3000/speed);
			counts[i] += counts[i-1];
			repaint();
			wait(3000/speed);
		}
		reset();
	}
	private void fillSorted(int[] counts, int exp, int start, int end) {
		for(int i=end; i>=start; i--) {
			arrow.setX(xi + s*i);
			decCountIdx = A[i]/exp %10;
			repaint();
			wait(3000/speed);
			sorted[--counts[A[i]/exp % 10]] = A[i];
			
			repaint();
			wait(3000/speed);
		}
		reset();
	}
	private void transferNumbers() {
		floatNumbers = true;
		for(hNumbers=sortedY; hNumbers>=aY+s/2; hNumbers--) {
			wait(100/speed);
			repaint();
		}
		hNumbers = sortedY+s/2;
		floatNumbers = false;
	}
	private void countSortLSD(int exp) {
		fillCounts(counts, exp, 0, A.length-1);
		accumulateCounts(counts);
		fillSorted(counts, exp, 0, A.length-1);
		
		
		Arrays.fill(counts, 0);
		//copy back
		transferNumbers();
		for(int i=0; i<A.length; i++) {
			A[i] = sorted[i];
			sorted[i] = -1;
		}
		repaint();
		wait(3000/speed);
	}
	
	public void radixSortMSD() {
		int max = Arrays.stream(A).max().getAsInt();
		int maxDigits = max==0 ? 0 : (int)(Math.log10(max)) + 1;
		int exp = (int) Math.pow(10, maxDigits-1);
		
		MSDhelper(exp, 0, A.length-1);
		repaint();
		wave();
		repaint();
	}
	
	private void MSDhelper(int exp, int start, int end) {
		if(start>=end || exp<=0)
			return;
		partitionStart = start;
		int[] cnts = countSortMSD(exp, start, end);
		
		if(exp > 1)
			displayPartitions(cnts, start, end);
		
		for(int i=0; i<10; i++) {
			if(cnts[i] == 0)
				continue;
			end = start + cnts[i] - 1;
			MSDhelper(exp/10, start, end);
			sortedIdx = end;
			start = end+1;
		}
		
		repaint();
		
		
	}
	
	private void displayPartitions(int[] cnts, int start, int end) {
		for(int i=0; i<10; i++) {
			if(cnts[i] == 0)
				continue;
			end = start + cnts[i] - 1;
			partitions.add(end+1);
			start = end+1;
		}
		repaint();
		wait(3000/speed);
	}
	private int[] countSortMSD(int exp, int start, int end) {
		int[] cnts = new int[10];
		sorted = new int[end-start+1];
		Arrays.fill(sorted, -1);
		counts = cnts;
		fillCounts(cnts, exp, start, end);
		tempCounts = Arrays.copyOf(counts, 10);
		accumulateCounts(cnts);
		fillSorted(cnts, exp, start, end);
		
		//copy back
		transferNumbers();
		for(int i=0; i<sorted.length; i++) {
			A[i+start] = sorted[i];
			sorted[i] = -1;
		}
		repaint();
		wait(3000/speed);
		repaint();
		wait(3000/speed);
		return tempCounts;
	}
	private static void wait(int milisecs) {
		try {
			Thread.sleep(milisecs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void reset() {
		incCountIdx=-1;
		decCountIdx=-1;
		accumulateCountsIdx = -1;
		floatNumbers = false;
		arrow.setX(-1000);
	}
	
	private void wave() {
		partitions.clear();
	    int maxHeight = 5; 
	    int delay = 40; 
	    wave = true;
	    int waveLength = A.length + maxHeight * 2; 
	    waveHeights = new int[A.length];

	    for (int step = 0; step < waveLength; step++) {
	        for (int i = 0; i < A.length; i++) {
	            int wavePosition = step - i * 2; 
	            if (wavePosition >= 0 && wavePosition <= maxHeight)
	                waveHeights[i] = wavePosition;
	            else if (wavePosition > maxHeight && wavePosition <= 2 * maxHeight) 
	                waveHeights[i] = 2 * maxHeight - wavePosition;
	            else
	                waveHeights[i] = 0;
	            
	        }
	        repaint();
	        wait(delay);
	    }
	    wave = false;
	}
}
