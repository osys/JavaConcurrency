import net.jcip.annotations.ThreadSafe;

import java.awt.Point;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 机动车追踪器
 * @author osys
 */
@ThreadSafe
public class DelegatingVehicleTracker {
    /** 机动车位置 */
    private ConcurrentMap<String, Point> locations;
    /** 机动车位置(不可修改的，即最初位置) */
    private final Map<String, Point> unmodifiableMap;

    public DelegatingVehicleTracker(Map<String, Point> points) {
        locations = new ConcurrentHashMap<String, Point>(points);
        unmodifiableMap = Collections.unmodifiableMap(locations);
    }

    /** 所有机动车位置(不可变的) */
    public Map<String, Point> getLocations() {
        return unmodifiableMap;
    }

    /**
     * 某机动车位置
     * @param id 机动车 id
     * @return 机动车位置
     */
    public Point getLocation(String id) {
        return locations.get(id);
    }

    /**
     * 修改机动车位置(需要存在该机动车)
     * @param id 机动车 id
     * @param x 机动车 x 坐标
     * @param y 机动车 y 坐标
     */
    public void setLocation(String id, int x, int y) {
        if (locations.replace(id, new Point(x, y)) == null) {
            throw new IllegalArgumentException("车辆名称无效: " + id);
        }
    }

    /**
     * 机动车移动
     * @param evt 机动车移动事件
     */
    public void vehicleMoved(VehicleMovedEvent evt) {
        // 位置
        Point local = evt.point;
        // 修改机动车位置(需要存在该机动车)
        setLocation(evt.vehicleId, local.x, local.y);
    }

    /** 所有机动车位置(可变的) */
    public Map<String, Point> getLocationsAsStatic() {
        return Collections.unmodifiableMap(
                new HashMap<String, Point>(locations));
    }

    /**
     * 打印机动车位置
     * @param vehicleId 机动车id
     * @param local 机动车位置
     */
    public void renderVehicle(String vehicleId, Point local) {
        System.out.println("机动车 " + vehicleId + " 位置：" + local);
    }

    /** 机动车移动事件 */
    static class VehicleMovedEvent {
        Point point;
        String vehicleId;

        public VehicleMovedEvent(Point point, String vehicleId) {
            this.point = point;
            this.vehicleId = vehicleId;
        }
    }
}