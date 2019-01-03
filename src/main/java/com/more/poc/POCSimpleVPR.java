package com.more.poc;

import com.graphhopper.jsprit.analysis.toolbox.GraphStreamViewer;
import com.graphhopper.jsprit.analysis.toolbox.Plotter;
import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TimeWindow;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.CrowFlyCosts;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.core.util.VehicleRoutingTransportCostsMatrix;
import com.graphhopper.jsprit.io.problem.VrpXMLWriter;

import java.util.Collection;

public class POCSimpleVPR {


    private static final int WEIGHT_INDEX = 0;

    private static final int CAPACITY = 10;

    /**
     * We can employ one vehicle(-type) located at (10,10) with one capacity dimension,
     * e.g. weight, and a capacity value of 2 to deliver four customers located at [(5,7),(5,13),(15,7),(15,13)], each
     * with a demand that has a weight of 1. All employed vehicles need to return to their start-locations.
     */
    public static void testBuildAProblem(){

        /*
         * get a vehicle type-builder and build a type with the typeId "vehicleType" and a capacity of 2
         * you are free to add an arbitrary number of capacity dimensions with
         * .addCapacityDimension(dimensionIndex,dimensionValue)
         */
        VehicleType vehicleType = VehicleTypeImpl
                .Builder
                .newInstance("someVehicleType")
                .addCapacityDimension(WEIGHT_INDEX, CAPACITY)
                .build();
        
        Location 
        	l00 = Location.newInstance(0, 0),
        	l11 = Location.newInstance(1, 1),
        	l22 = Location.newInstance(2, 2),
        	l31 = Location.newInstance(3, 1);
        /*
         * get a vehicle-builder and build a vehicle located at (10,10) with type "vehicleType"
         */
        VehicleImpl vehicle = VehicleImpl
                .Builder
                .newInstance("FF0000")
                .setStartLocation(l00)
                .setType(vehicleType)
                .setLatestArrival(10)
                .build();

        Service service1 = Service.Builder.newInstance("1")
                .addSizeDimension(WEIGHT_INDEX,5)
                .setLocation(l11)
                .setServiceTime(1)
                .setTimeWindow(TimeWindow.newInstance(6, 7))
                .build();

        Service service2 = Service.Builder.newInstance("2")
                .addSizeDimension(WEIGHT_INDEX,5)
                .setLocation(l22)
                .setServiceTime(1)
                .setTimeWindow(TimeWindow.newInstance(1,2))
                .build();

        Service service3 = Service.Builder.newInstance("3")
                .addSizeDimension(WEIGHT_INDEX,5)
                .setLocation(l31)
                .setServiceTime(1)
                .setTimeWindow(TimeWindow.newInstance(3,7))
                .build();
        
        VehicleRoutingTransportCostsMatrix costsMatrix = VehicleRoutingTransportCostsMatrix
        		.Builder
        		.newInstance(true)
        		
        		.addTransportTime(l00.getId(), l11.getId(), 1.4142)
        		.addTransportTime(l00.getId(), l22.getId(), 0.8284)
        		.addTransportTime(l00.getId(), l31.getId(), 3.1623)
        		
        		//.addTransportTime(l11.getId(), l00.getId(), 1.4142)
        		.addTransportTime(l11.getId(), l22.getId(), 1.4142)
        		.addTransportTime(l11.getId(), l31.getId(), 2)
        		
        		//.addTransportTime(l22.getId(), l11.getId(), 1.4142)
        		.addTransportTime(l22.getId(), l31.getId(), 1.4142)
        		//.addTransportTime(l22.getId(), l00.getId(), 2.8284)
        		
//        		.addTransportTime(l31.getId(), l11.getId(), 2)
//        		.addTransportTime(l31.getId(), l22.getId(), 1.4142)
//        		.addTransportTime(l31.getId(), l00.getId(), 3.1623)
        		
        		.build();

        /*
         * again define a builder to build the VehicleRoutingProblem
         */
        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        vrpBuilder.addVehicle(vehicle);
        vrpBuilder.setRoutingCost(costsMatrix);
//        vrpBuilder.setFleetSize(VehicleRoutingProblem.FleetSize.FINITE);
        vrpBuilder
        	.addJob(service1)
        	.addJob(service2)
        	.addJob(service3);

        VehicleRoutingProblem problem = vrpBuilder.build();

        /*
         * get the algorithm out-of-the-box.
         */
        VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(problem);

        /*
         * and search a solution which returns a collection of solutions (here only one solution is constructed)
         */
        Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();

        for (VehicleRoutingProblemSolution sol : solutions){
            /*
             * use the static helper-method in the utility class Solutions to get the best solution (in terms of least costs)
             */
//            VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);
            System.out.println("Solution: " + sol.hashCode());
            SolutionPrinter.print(problem, sol, SolutionPrinter.Print.VERBOSE);

            //new VrpXMLWriter(problem, solutions).write("problem-with-solution.xml");

            //new Plotter(problem,sol).plot("solution.png", "solution");

            new GraphStreamViewer(problem, sol)
                    .setCameraView(0, 0, 4)
                    .labelWith(GraphStreamViewer.Label.ID)
                    .setRenderDelay(1000)
                    .display();
        }

    }

    public static void main(String[] args){
        System.out.println("Hello World");
        testBuildAProblem();
    }
}
