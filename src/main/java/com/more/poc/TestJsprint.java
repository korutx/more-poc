package com.more.poc;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.core.util.VehicleRoutingTransportCostsMatrix;
import com.graphhopper.jsprit.io.problem.VrpXMLWriter;

import java.util.Collection;

public class TestJsprint {

    /**
     * We can employ one vehicle(-type) located at (10,10) with one capacity dimension,
     * e.g. weight, and a capacity value of 2 to deliver four customers located at [(5,7),(5,13),(15,7),(15,13)], each
     * with a demand that has a weight of 1. All employed vehicles need to return to their start-locations.
     */
    public static void testBuildAProblem(){
        /*
         * get a vehicle type-builder and build a type with the typeId "vehicleType" and a capacity of 2
         * you are free to add an arbitrary number of capacity dimensions with .addCapacityDimension(dimensionIndex,dimensionValue)
         */
        final int WEIGHT_INDEX = 0;
        VehicleTypeImpl.Builder vehicleTypeBuilder = VehicleTypeImpl.Builder.newInstance("vehicleType")
                .addCapacityDimension(WEIGHT_INDEX,2);
        VehicleType vehicleType = vehicleTypeBuilder.build();

        /*
         * get a vehicle-builder and build a vehicle located at (10,10) with type "vehicleType"
         */
        VehicleImpl.Builder vehicleBuilder = VehicleImpl.Builder.newInstance("vehicle");
        vehicleBuilder.setStartLocation(Location.newInstance(10, 10));
        vehicleBuilder.setType(vehicleType);
        VehicleImpl vehicle = vehicleBuilder.build();

        /*
         * build services with id 1...4 at the required locations, each with a capacity-demand of 1.
         * Note, that the builder allows chaining which makes building quite handy
         */
        Service service1 = Service.Builder.newInstance("1")
                .addSizeDimension(WEIGHT_INDEX,1)
                .setLocation(Location.newInstance(5, 7))
                .build();
        Service service2 = Service.Builder.newInstance("2")
                .addSizeDimension(WEIGHT_INDEX,1)
                .setLocation(Location.newInstance(5, 13))
                .build();
        Service service3 = Service.Builder.newInstance("3")
                .addSizeDimension(WEIGHT_INDEX,1)
                .setLocation(Location.newInstance(15, 7))
                .build();
        Service service4 = Service.Builder.newInstance("4")
                .addSizeDimension(WEIGHT_INDEX,1)
                .setLocation(Location.newInstance(15, 13))
                .build();

        /*
         * again define a builder to build the VehicleRoutingProblem
         */
        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        vrpBuilder.addVehicle(vehicle);
        vrpBuilder.addJob(service1).addJob(service2).addJob(service3).addJob(service4);
//        VehicleRoutingTransportCostsMatrix costsMatrix = new VehicleRoutingTransportCostsMatrix.
//        vrpBuilder.setRoutingCost()
        /*
         * build the problem
         * by default, the problem is specified such that FleetSize is INFINITE, i.e. an infinite number of
         * the defined vehicles can be used to solve the problem
         * by default, transport costs are computed as Euclidean distances
         */
        VehicleRoutingProblem problem = vrpBuilder.build();

        /*
         * get the algorithm out-of-the-box.
         */
        VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(problem);

        /*
         * and search a solution which returns a collection of solutions (here only one solution is constructed)
         */
        Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();

        /*
         * use the static helper-method in the utility class Solutions to get the best solution (in terms of least costs)
         */
        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);

        SolutionPrinter.print(problem, bestSolution, SolutionPrinter.Print.CONCISE);

        new VrpXMLWriter(problem, solutions).write("problem-with-solution.xml");
    }

    public static void main(String[] args){
        System.out.println("Hello World");
        testBuildAProblem();
    }
}
