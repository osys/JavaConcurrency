import java.util.Set;

/**
 * “搬箱子” 谜题的抽象
 *
 *
 * Created by osys on 2022/08/28 21:48.
 * @param <P> 位置
 * @param <M> 移动
 */
public interface Puzzle <P, M> {
    /** 初始化 */
    P initialPosition();

    /** 移动位置是否为本位置 */
    boolean isGoal(P position);

    /** 合法移动 */
    Set<M> legalMoves(P position);

    /** 搬箱子 */
    P move(P position, M move);
}
