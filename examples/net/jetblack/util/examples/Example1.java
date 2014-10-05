package net.jetblack.util.examples;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import net.jetblack.util.Enumerable;
import net.jetblack.util.invokables.UnaryFunction;

public class Example1 {

	public static void main(String[] args) {

		Collection<Car> cars = Car.create();
		
		for (Map.Entry<String, Collection<Car>> fastCarsByManufacturer : Enumerable.create(cars).where(new UnaryFunction<Car,Boolean>() {
			@Override public Boolean invoke(Car car) {
				return car.getTopSpeed() > 200;
			}
		}).groupBy(new UnaryFunction<Car, String>() {
			@Override public String invoke(Car car) {
				return car.getManufacturer();
			}
		}).entrySet()) {
			System.out.println("Manufacturer: " + fastCarsByManufacturer.getKey());
			for (Car car : Enumerable.create(fastCarsByManufacturer.getValue()).sort(new Comparator<Car>() {

					@Override public int compare(Car car1, Car car2) {
						return Double.compare(car1.getPower(), car2.getPower());
					}
				}
			)) {
				System.out.println(car.toString());
			}
		}
	}

}
