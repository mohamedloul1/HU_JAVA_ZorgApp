public class ProgrammaExit {

    public static void programmaExit() {
        System.out.println("\u001B[31mHet programma wordt 6 seconden uitgevoerd...\u001B[0m");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\u001B[31mHet programma is afgelopen.\u001B[0m");
    }
}
