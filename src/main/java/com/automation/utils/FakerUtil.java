package com.automation.utils;

import com.github.javafaker.Faker;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class FakerUtil {
	private static final ThreadLocal<Faker> faker = ThreadLocal.withInitial(() 
			->  new Faker(Locale.US));   
	
//	private static final Random random = new Random();

    // Keywords based on your specific list
    private static final String[] CATEGORIES = {
        "Building", "Business", "Travel", "Car", "Cell Phone", "Consulting", 
        "Insurance", "Fuel", "Marketing", "Maintenance", "Legal Services", 
        "Office", "Utilities", "Equipment", "General", "Financial"
    };

    private static final String[] SUFFIXES = {
        "Expense", "Services", "Lease", "Rent", "Fees", "Repayment", 
        "Settlements", "Rebuild", "Provider", "Labour", "Insurance"
    };

    public static String generateAccountName() {
    	
    	Faker fakerInstance = faker.get();
    	   
        // Pick a random category and suffix
    	String category = CATEGORIES[ThreadLocalRandom.current().nextInt(CATEGORIES.length)];
    	String suffix = SUFFIXES[ThreadLocalRandom.current().nextInt(SUFFIXES.length)];
        
        // Add a random number at the end to ensure it is always UNIQUE for parallel runs
        int uniqueId = fakerInstance.number().numberBetween(1000, 9999);
        
        // Example output: "Marketing Expense 5421" or "Legal Services Rent 1092"
        return category + " " + suffix + " " + uniqueId;
    }
}
