import java.util.*;

/**
 * Represents a map, through a nested HashMap storing list of direct flight from a given city
 */
public class FlightMap {
    /**
     * Nested hashmap representing the whole map. Keys are departure and values are hashmaps with (Destination, Cost)
     * pairs
     */
    Map<String, Map<String, Integer>> mapGraph;
    /**
     * Will store the result of findAllRoutes. Consist of a nested hashmap with (Destination, Route List) pairs
     */
    Map<String, ArrayList<String>> routes;
    /**
     * Origin city
     */
    String origin;

    /**
     * Creates the flight map given the origin city and the mapGraph parsed in SearchMap
     */
    public FlightMap(String origin, Map<String, Map<String, Integer>> mapGraph){
        this.mapGraph = mapGraph;
        this.origin = origin;
        this.routes = new HashMap<>();
    }

    /**
     * Implements breadth first search to find cities reachable from origin city
     */
    public Map<String, ArrayList<String>> findAllRoutes(){

        Queue<CityNode> citiesQueue = new LinkedList<>();

        Map<String, Boolean> Visited = new HashMap<>();
        Visited.put(this.origin, Boolean.TRUE);

        // Add cities adjacent to origin city
        for (String adjCity : mapGraph.get(origin).keySet()){
            citiesQueue.add(new CityNode(adjCity, origin));
            ArrayList<String> route = new ArrayList<>();
            route.add(origin);
            route.add(adjCity);
            routes.put(adjCity, route);
        }

        while(!citiesQueue.isEmpty()){
            CityNode currentCity = citiesQueue.remove();
            Visited.put(currentCity.name, Boolean.TRUE);

            if(!routes.containsKey(currentCity.name) && routes.containsKey(currentCity.parent)){
                ArrayList<String> route = new ArrayList<>(routes.get(currentCity.parent));
                route.add(currentCity.name);
                routes.put(currentCity.name, route);
            }

            if(mapGraph.containsKey(currentCity.name))
                for (String adjCity : mapGraph.get(currentCity.name).keySet() ){
                    if(!Visited.containsKey(adjCity)) {
                        citiesQueue.add(new CityNode(adjCity, currentCity.name));
                    }
                }
        }

        return routes;
    }
    /**
     * Calculates cost of the from origin city to destination
     */
    public int calculateRouteCost(String destination){
        if (!routes.containsKey(destination)){ return -1; }

        int cost = 0;
        ArrayList<String> route = new ArrayList<>(routes.get(destination));

        for (int i = 0; i < route.size()-2; i++){
            // Goes to city i hashMap and gets the cost to go to city i+1
            cost += mapGraph.get(route.get(i)).get(route.get(i+1));
        }

        return cost;
    }


}
