public class Main {
    public static void main(String[] args) {
//        Apotheker apotheker = new Apotheker(1,"MohamedLoul", "Apotheker");
//        Tandart tandart= new Tandart(1, "MohamedLoul","Tandart");
//        Fysiotherapeut fysiotherapeut = new Fysiotherapeut(1,"MohamedLoul","Fysiotherapeut");
//        HuisArts huisArts = new HuisArts(1,"MohamedLoul", "HuisArts");

//        Users users = new HuisArts(1,"MohamedLoul","HuisArts");

//        HuisArts huisArts = new HuisArts(1,"MohamedLoul","HuisArts");
//        Apotheker apotheker = new Apotheker(1,"MohamedLoul","HuisArts");
//        Apotheker.printTekst();


//        if (huisArts instanceof HuisArts)
//        {
//        huisArts.PrintLine();
//        }
//        else {
//            System.out.println("Er is een fout opgetreden");
//        }
//        if (apotheker instanceof Apotheker)
//        {
//            apotheker.printTekst();
//        }
//        else {
//            System.out.println("Er is een fout opgetreden");
//        }


//        Menus menus = new Menus();
//        menus.mainMenu();
//        PatientController patientController = new PatientController();
//        patientController.searchPatientById();
//        patientController.searchPatientByName();
//        patientController.deletePatient();
//        patientController.updatePatient();
//        patientController.displayPatientList();
//        patientController.insertPatient();
//        patientController.retrievePatient();
//        RolesController rolesController = new RolesController();
//        rolesController.retrieveRoles();
//        rolesController.displayRolesList();

//        rolesController.insertRole();
//        MedicationController medicationController = new MedicationController();
//        medicationController.getAllMedicationsWithNames();
//        medicationController.insertMedications();

        AuthenticationServer authenticationServer = new AuthenticationServer();
        Users users = authenticationServer.login("HuisArts");
        if(users !=null){
            if (users.hasType("admin")){
                System.out.println("Access granted to admin functionality.");
            }else if (users.hasType("HuisArts")){
//                System.out.println("Access granted to HuisArts functionality.");
                HuisArts.test();
            }else {
                System.out.println("Access denied. User doesn't have necessary roles.");
            }
        }else {
            System.out.println("Invalid credentials. Access denied.");
        }




    }
}
