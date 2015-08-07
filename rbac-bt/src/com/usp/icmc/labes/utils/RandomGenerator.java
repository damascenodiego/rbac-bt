package com.usp.icmc.labes.utils;

import java.util.Random;

public class RandomGenerator {
	
	private static RandomGenerator instance;
	
	private Random rnd;
	private long seed;
	
	private RandomGenerator(){
		rnd = new Random();
	}
	
	public static RandomGenerator getInstance() {
		if (instance == null) instance = new RandomGenerator(); 
		return instance;
	}
	
	public void setSeed(long seed) {
		this.seed = seed;
		rnd.setSeed(this.seed);
	}
	
	public Random getRnd() {
		return rnd;
	}

}
