package it.unibg.progetto.api.app;



import it.unibg.progetto.api.cli.components.GlobalScanner;

public class LauncherMain {

    public static void main(String[] args) {

        

        System.out.println("Seleziona modalità di avvio:");
        System.out.println("1 - Grafica");
        System.out.println("2 - Terminale");
        System.out.print("Scelta: ");

        String scelta = GlobalScanner.scanner.nextLine();

        if (scelta.equals("1")) {
            GuiMain.main(args);
        } else if (scelta.equals("2")) {
            ApiMain.main(new String[] {});
        } else {
            System.out.println("Scelta non valida.");
        }
    }
}