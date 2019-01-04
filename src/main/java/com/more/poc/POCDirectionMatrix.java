package com.more.poc;

import java.io.IOException;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.LatLng;

public class POCDirectionMatrix {

	public static void main(String[] args) throws ApiException, InterruptedException, IOException {
		GeoApiContext context = new GeoApiContext.Builder()
				.apiKey("")
				.build();
		DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
			.origins(
				new LatLng(-33.419151,-70.598601),
				new LatLng(-33.424524,-70.61804)
			)
			.destinations(
				new LatLng(-33.444948,-70.659523),
				new LatLng(-33.423009,-70.621231),
				new LatLng(-33.423009,-70.621231),
				new LatLng(-33.449298,-70.650236)
			)
			.await();
		
		for (DistanceMatrixRow row : matrix.rows) {
			System.out.println("row --- " + row.hashCode());
			for(DistanceMatrixElement element: row.elements){
				System.out.println(element);
			}
		}
		
		
			
		
	}
}
