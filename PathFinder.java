package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PathFinder {
    private PathFinderProduct pathFinderProduct = new PathFinderProduct();
	private ArrayList<Point> open;
    private ArrayList<Point> closed;
    public PathFinder() {
    	this.open = new ArrayList<Point>();
        this.closed = new ArrayList<Point>();
        pathFinderProduct.setParents(new HashMap<Point, Point>());
        pathFinderProduct.setTotalCost(new HashMap<Point, Integer>());
    }
    
    public ArrayList<Point> findPath(Creature creature, Point start, Point end, int maxTries) {
        open.clear();
        closed.clear();
        pathFinderProduct.getParents().clear();
        pathFinderProduct.getTotalCost().clear();
    	
        open.add(start);
        
        for (int tries = 0; tries < maxTries && open.size() > 0; tries++){
            Point closest = getClosestPoint(end);
            
            open.remove(closest);
            closed.add(closest);

            if (closest.equals(end))
                return createPath(start, closest);
            else
                checkNeighbors(creature, end, closest);
        }
        return null;
    }

	private Point getClosestPoint(Point end) {
		Point closest = open.get(0);
		for (Point other : open){
		    if (pathFinderProduct.totalCost(other, end) < pathFinderProduct.totalCost(closest, end))
		        closest = other;
		}
		return closest;
	}

	private void checkNeighbors(Creature creature, Point end, Point closest) {
		for (Point neighbor : closest.neighbors8()) {
		    if (closed.contains(neighbor)
		    		|| !creature.canEnter(neighbor.x, neighbor.y, creature.z)
		    		&& !neighbor.equals(end))
		        continue;
			
		    if (open.contains(neighbor))
				pathFinderProduct.reParentNeighborIfNecessary(closest, neighbor, this.open);
		    else
		        pathFinderProduct.reParentNeighbor(closest, neighbor, this.open);
		}
	}

	private ArrayList<Point> createPath(Point start, Point end) {
		ArrayList<Point> path = new ArrayList<Point>();

		while (!end.equals(start)) {
		    path.add(end);
		    end = pathFinderProduct.getParents().get(end);
		}

		Collections.reverse(path);
		return path;
	}
}
