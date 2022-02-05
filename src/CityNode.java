/**
 * CityNode: Represents a node in the map stored in the FlightMap instance. Contains the name of the city and
 * the name of the parent city in the route.
 */
public class CityNode {
    /**
     * Identifier of the city
     */
    String name;
    /**
     * City from which the flight arrived
     */
    String parent;

    /**
     * Creates a new city node given name of city and of parent city
     */
    public CityNode(String name, String parent){
        this.name = name;
        this.parent = parent;
    }
}
