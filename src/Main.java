

public class Main {
    public static void main(String[] args) {

            CinemaSystem system = DemoData.buildSystem();
            new UI(system).run();



        CinemaSystem system1 = DemoData.buildSystem();

        Cinema cinema1 = system.getCinemas().get(0); // albo znajdź po nazwie
        Screening screening1 = cinema1.getProgrammeForNextWeek().get(0);
        Screening screening2 = cinema1.getProgrammeForNextWeek().get(1); // jeśli istnieje


      //Demo
        System.out.println("Demo for bying a ticket:");
        System.out.println(" ");
        try {
            screening1.buyTickets("R1S01");
        } catch (Exception e) {
            System.out.println("Expected error: " + e.getMessage());
            System.out.println(" ");
        }

        System.out.println("  <<As a guest>>");
        Order guestOrder = screening1.buyTickets("R2S01", "R2S02");
        System.out.println("  Gueset ordered tickets:");
        for (Ticket t : guestOrder.getTickets()) System.out.println("  " + t);
        System.out.println(" ");

        System.out.println("  <<As a logged-in user:>>");
        Customer ania = new Customer("c1", "Ania");
        Order aniaOrder = screening2.buyTickets(ania, "R3S01");
        System.out.println("  Ania's purchased tickets:");
        for (Ticket t : ania.getTickets()) System.out.println("  " + t);
    }
        }




