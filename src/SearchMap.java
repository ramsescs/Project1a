import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.exit;

/**
* Main class for execution. Creates an instance of FlightMap using the file pass as argument, then
* creates a file with the name passed and writes result of FlightMap.searchAllRoutes method to it.
 */
public class SearchMap {
    /**
     * @param args: Include path for input file and name/route of the output file
     */
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Incorrect number of arguments");
            exit(-1);
        }

        // HashMap to represent flight graph
        Map<String, Map<String, Integer>> mapGraph = new HashMap<>();

        String origin = null;

        // Read
        try {
            File inputFile = new File(args[0]);
            Scanner reader = new Scanner(inputFile);

            // Read First Line (Origin City)
            if(reader.hasNextLine()){
                String line = reader.nextLine();
                origin = String.valueOf(line.charAt(0));

                // Regex Checking for File Format
                Pattern pattern = Pattern.compile("[A-Za-z]");
                Matcher matcher = pattern.matcher(origin);
                if(!matcher.find()){
                    System.out.println("Incorrect File Format: Origin City Name");
                    exit(-1);
                }
            }else{
                System.out.println("Incorrect File Format: Blank File");
                exit(-1);
            }

            // Read rest of lines (Direct Flights)
            while (reader.hasNextLine()) {
                String[] flightString = reader.nextLine().split(" ");
                String depart = flightString[0];
                String arrival = flightString[1];
                int cost = Integer.parseInt(flightString[2]);

                // Regex Checking for File Format
                Pattern pattern = Pattern.compile("[A-Za-z]");
                Matcher matcher = pattern.matcher(depart);
                boolean matchDep = matcher.find();
                matcher = pattern.matcher(arrival);
                boolean matchArr = matcher.find();
                pattern = Pattern.compile("\\d+");
                matcher = pattern.matcher(String.valueOf(cost));
                boolean matchCost = matcher.find();

                if(!matchArr || !matchCost || !matchDep){
                    System.out.println("Incorrect File Format: Direct Flight");
                    exit(-1);
                }

                if(!mapGraph.containsKey(depart)){
                    mapGraph.put(depart, new HashMap<>());
                }

                mapGraph.get(depart).put(arrival, cost);
            }
            reader.close();

            System.out.println(mapGraph);


        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

        FlightMap myMap = new FlightMap(origin, mapGraph);

        Map<String, ArrayList<String>> routes = myMap.findAllRoutes();

        String formatStr = "%-15s %-25s %-15s%n";
        String buffer = "";

        buffer += String.format(formatStr, "Destination", "Flight Route from " + origin, "Total Cost");

        for (String destination : routes.keySet()){
            buffer += String.format(formatStr, destination, routes.get(destination), myMap.calculateRouteCost(destination));
        }

        System.out.println(buffer);

        // Write
        try {
            File file = new File(args[1]);
            file.createNewFile();
            FileWriter writer = new FileWriter(file, false);
            writer.write(buffer);
            writer.flush();
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("Error in file name");
        }
    }
}
