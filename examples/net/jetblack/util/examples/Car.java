package net.jetblack.util.examples;

import java.util.Arrays;
import java.util.Collection;

public class Car {

	private final String manufacturer;
	private final String name;
	private final double topSpeed;
	private final double capacity;
	private final int cylinders;
	private final int maxRevs;
	private final double power;
	private final int price;
	
	public Car(String manufacturer, String name, double topSpeed, double capacity, int cylinders, int maxRevs, int power, int price) {
		this.manufacturer = manufacturer;
		this.name = name;
		this.topSpeed = topSpeed;
		this.capacity = capacity;
		this.cylinders = cylinders;
		this.maxRevs = maxRevs;
		this.power = power;
		this.price = price;
	}
	
	public String getManufacturer() {
		return manufacturer;
	}
	
	public String getName() {
		return name;
	}
	
	public double getTopSpeed() {
		return topSpeed;
	}
	
	public double getCapacity() {
		return capacity;
	}
	
	public int getCylinders() {
		return cylinders;
	}
	
	public int getMaxRevs() {
		return maxRevs;
	}

	public double getPower() {
		return power;
	}
	
	public int getPrice() {
		return price;
	}
	
	@Override
	public String toString() {
		return manufacturer + " " + name;
	}
	
	public static Collection<Car> create() {
		return Arrays.asList(
				new Car("Ferrari",	"Dino 308 GT",			248,	2926,	8,	6600,	236,	9217),
				new Car("Porsche",	"Carrera RSR",			240,	2992.5,	6,	6280,	230,	9000),
				new Car("Fiat",		"130 Coupe",			195,	3235,	6,	5600,	165,	7455),
				new Car("Ford",		"Lincoln Continental",	195,	7560,	8,	4400,	208,	8514),
				new Car("Cadillac",	"Fleetwood 60 Special",	180,	7729,	8,	3600,	208,	8000),
				new Car("Buick",	"Riviera",				195,	7468,	8,	4000,	248,	7000),
				new Car("Bitter",	"CD",					215,	5354,	8,	4700,	230,	10000),
				new Car("Rolls Royce","Phantom",			170,	6230,	8,	4000,	220,	30000),
				new Car("Rolls Royce", "Corniche",			193,	6750,	8,	4500,	260,	19012),
				new Car("Lotus",	"Panther",				225,	5343,	12,	6000,	254,	11029),
				new Car("Porsche",	"911 Trubo",			260,	2687,	6,	6300,	280,	15000),
				new Car("Sbarro",	"Tiger",				260,	6332,	8,	4000,	250,	16000),
				new Car("Maserati",	"Bora",					260,	4719,	8,	6000,	310,	11473),
				new Car("BMW",		"3.0 CSL",				220,	3153,	6,	5600,	206,	7000),
				new Car("Ferrari",	"365 GTB 4 Daytona",	290,	4390,	12,	5500,	352,	15000),
				new Car("Lancia",	"HF",					220,	2418,	6,	6600,	180,	4000),
				new Car("De Tomaso","Longchamp 2+2",		250,	5700,	8,	5400,	330,	11000),
				new Car("Mercedes",	"450 SE",				210, 	4520,	8,	5000,	225,	9692),
				new Car("Ferarri",	"BB",					330,	4390,	12,	7500,	360,	17500),
				new Car("Maserati",	"Merak",				240,	2965,	6,	6000,	190,	7821),
				new Car("Monteverdi","Berlinetta Coupe",	255,	7602,	8,	4600,	380,	14000),
				new Car("Bertone",	"Khamson Coupe 2+2",	300,	4930,	8,	6000,	350,	14000),
				new Car("Ferrari",	"365 GTC 4",			300,	4390,	12,	7000,	340,	14584),
				new Car("Lamborghini","P 250 Uracco",		240,	2462,	8,	7800,	220,	9000),
				new Car("Citrpen",	"SM",					225,	2670,	6,	5750,	175,	7226),
				new Car("Lamborghini","Jarama 400 GT 2+2",	260,	3922,	12,	7500,	350,	10935),
				new Car("Lamborghini","Espada 400 GT",		245,	3929,	12,	7700,	370,	13350),
				new Car("Jensen",	"Interceptor III",		220,	7206,	8,	4800,	284,	8717),
				new Car("Monteverdi","Hai 450 SS",			290,	6980,	8,	5000,	450,	16000),
				new Car("Monteverdi","375C",				240,	7207,	8,	4700,	380,	13500),
				new Car("Monteverdi","375/4",				200,	7207,	8,	4800,	390,	14000));
	}
}
