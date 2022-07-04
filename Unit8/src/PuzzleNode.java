import net.jcip.annotations.Immutable;

import java.util.LinkedList;
import java.util.List;

@Immutable
public class PuzzleNode <P, M> {
    /** 位置 */
    final P pos;
    /** 移动位置 */
    final M move;
    /** 上一个位置 */
    final PuzzleNode<P, M> prev;

    public PuzzleNode(P pos, M move, PuzzleNode<P, M> prev) {
        this.pos = pos;
        this.move = move;
        this.prev = prev;
    }

    /** 链表装集合 */
    List<M> asMoveList() {
        List<M> solution = new LinkedList<M>();
        for (PuzzleNode<P, M> node = this; node.move != null; node = node.prev) {
            solution.add(0, node.move);
        }
        return solution;
    }
}
