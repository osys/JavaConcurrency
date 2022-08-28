import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class HiddenIterator {
 
	private final Set<Integer> set = new HashSet<>();
	
	public synchronized void add(Integer i) {
		set.add(i);
	}
	
	public synchronized void remove(Integer i) {
		set.remove(i);
	}
	
	public void addTenThings() {
		Random random = new Random();
		for (int i=0; i < 10 ; i++) {
			add(random.nextInt());
		}
		System.out.println("DEBUG: 添加了十个元素 " + set);
	}
}