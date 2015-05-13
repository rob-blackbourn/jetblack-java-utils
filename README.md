jetblack-java-utils
===================

A collection of utilities.

Enumerable
----------

A trivial lightweight Linq style enumerator library with a fluent API developed against Java SE 1.7.

My use case was a system that provided collections of data to process that were too large to be copied. The Enumerable class is a wrapper around the [Iterable](http://docs.oracle.com/javase/7/docs/api/java/util/Iterator.html) interface providing the typical enumeration operations.

The intention of the library is to be trivial, obvious, simple, and lightweight.

Here is a brief example using a data set derived from the Top Trumps series 1 "Super Cars" deck.

The collection is filtered to remove cars with a top speed of less then 200km/h, grouped by manufacturer, then sorted by power and printed.

```java
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
```
