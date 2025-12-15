package it.unibg.progetto.api.cli.components;


import java.util.Scanner;

/**
 * it has to be closed in the main
 */
public class GlobalScaner {

	public static Scanner scanner = new Scanner(System.in);
	
	public static void setScanner(Scanner sc) {
        scanner = sc;
    }

}
