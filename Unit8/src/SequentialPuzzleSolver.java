import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SequentialPuzzleSolver <P, M> {
    private final Puzzle<P, M> puzzle;
    private final Set<P> seen = new HashSet<P>();

    public SequentialPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
    }

    /** 解决者 */
    public List<M> solve() {
        P pos = puzzle.initialPosition();
        return search(new PuzzleNode<P, M>(pos, null, null));
    }

    /** 箱子移动 */
    private List<M> search(PuzzleNode<P, M> node) {
        // 该位置不存在箱子，或者箱子没有到过这个位置
        if (!seen.contains(node.pos)) {
            seen.add(node.pos);
            // 移动位置是否为本位置
            if (puzzle.isGoal(node.pos)) {
                return node.asMoveList();
            }
            // 获取每个合法移动
            for (M move : puzzle.legalMoves(node.pos)) {
                // 当前箱子位置移动
                P pos = puzzle.move(node.pos, move);
                // 下一个箱子移动
                PuzzleNode<P, M> child = new PuzzleNode<>(pos, move, node);
                List<M> result = this.search(child);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
