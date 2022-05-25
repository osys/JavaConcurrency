import net.jcip.annotations.Immutable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * @author osys
 */
@Immutable
public final class ThreeStooges {
    private final Set<String> stooges = new HashSet<>();

    public ThreeStooges() {
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
    }

    public boolean isStooge(String name) {
        return stooges.contains(name);
    }

    public String getStoogeNames() {
        List<String> stooges = new Vector<>();
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
        return stooges.toString();
    }

    public static void main(String[] args) {
        ThreeStooges threeStooges = new ThreeStooges();
        System.out.println("threeStooges.stooges = " + threeStooges.stooges);
        // threeStooges.stooges = [Moe, Larry, Curly]
    }
}