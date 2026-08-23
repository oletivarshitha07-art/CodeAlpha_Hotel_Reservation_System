import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// ================= ROOM CLASS =================
class Room {

    int roomNumber;
    String category;
    double price;
    boolean available;

    Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.available = true;
    }

    void displayRoom() {
        System.out.println(
                roomNumber + " | " +
                category + " | Rs." +
                price + " per day | " +
                (available ? "Available" : "Booked")
        );
    }
}


// ================= BOOKING CLASS =================
class Booking {

    int bookingId;
    String customerName;
    int roomNumber;
    String category;
    int days;
    double totalAmount;
    String paymentMethod;

    Booking(int bookingId, String customerName, int roomNumber,
            String category, int days, double totalAmount,
            String paymentMethod) {

        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.days = days;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
    }

    void displayBooking() {

        System.out.println("\n----------------------------------");
        System.out.println("Booking ID    : " + bookingId);
        System.out.println("Customer Name : " + customerName);
        System.out.println("Room Number   : " + roomNumber);
        System.out.println("Room Category : " + category);
        System.out.println("Number Days : " + days);
        System.out.println("Total Amount  : ₹" + totalAmount);
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("----------------------------------");
    }
}


// ================= MAIN CLASS =================
public class HotelReservationSystem {

    static Scanner scanner = new Scanner(System.in);

    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();

    static int nextBookingId = 1;


    // ================= MAIN METHOD =================
    public static void main(String[] args) {

        // Load old bookings from file
        loadBookings();

        // Add hotel rooms
        addRooms();

        int choice;

        do {

            System.out.println("\n======================================");
            System.out.println("       HOTEL RESERVATION SYSTEM");
            System.out.println("======================================");
            System.out.println("1. View All Rooms");
            System.out.println("2. Search Available Rooms");
            System.out.println("3. Book a Room");
            System.out.println("4. View Booking Details");
            System.out.println("5. Cancel Reservation");
            System.out.println("6. Exit");
            System.out.println("======================================");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    viewRooms();
                    break;

                case 2:
                    searchRooms();
                    break;

                case 3:
                    bookRoom();
                    break;

                case 4:
                    viewBooking();
                    break;

                case 5:
                    cancelBooking();
                    break;

                case 6:
                    saveBookings();
                    System.out.println("\nThank you for using the system!");
                    break;

                default:
                    System.out.println("\nInvalid choice!");
            }

        } while (choice != 6);
    }


    // ================= ADD ROOMS =================
    static void addRooms() {

        rooms.add(new Room(101, "Standard", 1500));
        rooms.add(new Room(102, "Standard", 1500));
        rooms.add(new Room(103, "Standard", 1500));

        rooms.add(new Room(201, "Deluxe", 2500));
        rooms.add(new Room(202, "Deluxe", 2500));
        rooms.add(new Room(203, "Deluxe", 2500));

        rooms.add(new Room(301, "Suite", 4000));
        rooms.add(new Room(302, "Suite", 4000));

        // Mark already booked rooms as unavailable
        for (Booking booking : bookings) {

            for (Room room : rooms) {

                if (room.roomNumber == booking.roomNumber) {
                    room.available = false;
                }
            }
        }
    }


    // ================= VIEW ROOMS =================
    static void viewRooms() {

        System.out.println("\n========== HOTEL ROOMS ==========");

        System.out.println(
                "Room | Category | Price | Status"
        );

        System.out.println("--------------------------------------");

        for (Room room : rooms) {
            room.displayRoom();
        }
    }


    // ================= SEARCH ROOMS =================
    static void searchRooms() {

        System.out.println("\n========== SEARCH ROOMS ==========");

        System.out.println("1. Standard");
        System.out.println("2. Deluxe");
        System.out.println("3. Suite");

        System.out.print("Choose category: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        String category;

        if (choice == 1) {
            category = "Standard";
        }
        else if (choice == 2) {
            category = "Deluxe";
        }
        else if (choice == 3) {
            category = "Suite";
        }
        else {
            System.out.println("Invalid category!");
            return;
        }

        boolean found = false;

        System.out.println(
                "\nAvailable " + category + " Rooms:"
        );

        for (Room room : rooms) {

            if (room.category.equals(category)
                    && room.available) {

                room.displayRoom();
                found = true;
            }
        }

        if (!found) {
            System.out.println(
                    "No rooms available in this category."
            );
        }
    }


    // ================= BOOK ROOM =================
    static void bookRoom() {

        System.out.println("\n========== BOOK A ROOM ==========");

        // Display available rooms
        searchAllAvailableRooms();

        System.out.print("\nEnter Room Number: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        Room selectedRoom = null;

        // Find selected room
        for (Room room : rooms) {

            if (room.roomNumber == roomNumber) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Room not found!");
            return;
        }

        if (!selectedRoom.available) {
            System.out.println(
                    "Sorry! This room is already booked."
            );
            return;
        }

        System.out.print("Enter Customer Name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter Number of Days: ");
        int days = scanner.nextInt();
        scanner.nextLine();

        if (days <= 0) {
            System.out.println(
                    "Number of days must be greater than 0."
            );
            return;
        }

        // Calculate total amount
        double totalAmount =
                selectedRoom.price * days;


        // ---------- BOOKING SUMMARY ----------
        System.out.println(
                "\n========== BOOKING SUMMARY =========="
        );

        System.out.println("Customer : " + customerName);
        System.out.println("Room     : " + selectedRoom.roomNumber);
        System.out.println("Category : " + selectedRoom.category);
        System.out.println("Days   : " + days);
        System.out.println("Price    : ₹" + selectedRoom.price);
        System.out.println("Total    : ₹" + totalAmount);


        // ---------- PAYMENT ----------
        System.out.println("\n========== PAYMENT ==========");

        System.out.println("1. UPI");
        System.out.println("2. Credit/Debit Card");
        System.out.println("3. Cash");

        System.out.print("Choose payment method (1/2/3 or UPI/Card/Cash): ");
String paymentChoice = scanner.nextLine().trim();

String paymentMethod;

if (paymentChoice.equals("1") ||
        paymentChoice.equalsIgnoreCase("UPI")) {

    paymentMethod = "UPI";
}
else if (paymentChoice.equals("2") ||
        paymentChoice.equalsIgnoreCase("Card")) {

    paymentMethod = "Credit/Debit Card";
}
else if (paymentChoice.equals("3") ||
        paymentChoice.equalsIgnoreCase("Cash")) {

    paymentMethod = "Cash";
}
else {

    System.out.println("Invalid payment method!");
    return;
}


        // ---------- CREATE BOOKING ----------
        Booking newBooking = new Booking(
                nextBookingId,
                customerName,
                selectedRoom.roomNumber,
                selectedRoom.category,
                days,
                totalAmount,
                paymentMethod
        );

        bookings.add(newBooking);

        // Mark room as booked
        selectedRoom.available = false;

        // Increase booking ID
        nextBookingId++;

        // Save booking
        saveBookings();


        // ---------- SUCCESS MESSAGE ----------
        System.out.println(
                "\n======================================"
        );
        System.out.println(
                "          BOOKING SUCCESSFUL!"
        );
        System.out.println(
                "======================================"
        );

        System.out.println(
                "Booking ID : " + newBooking.bookingId
        );

        System.out.println(
                "Payment    : " + paymentMethod
        );
    }


    // ================= SHOW AVAILABLE ROOMS =================
    static void searchAllAvailableRooms() {

        System.out.println(
                "\n========== AVAILABLE ROOMS =========="
        );

        boolean found = false;

        for (Room room : rooms) {

            if (room.available) {

                room.displayRoom();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms available.");
        }
    }


    // ================= VIEW BOOKING =================
    static void viewBooking() {

        System.out.println(
                "\n========== BOOKING DETAILS =========="
        );

        System.out.print("Enter Booking ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        boolean found = false;

        for (Booking booking : bookings) {

            if (booking.bookingId == id) {

                booking.displayBooking();
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println(
                    "Booking not found!"
            );
        }
    }


    // ================= CANCEL BOOKING =================
    static void cancelBooking() {

        System.out.println(
                "\n========== CANCEL RESERVATION =========="
        );

        System.out.print("Enter Booking ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Booking bookingToCancel = null;

        // Find booking
        for (Booking booking : bookings) {

            if (booking.bookingId == id) {

                bookingToCancel = booking;
                break;
            }
        }

        if (bookingToCancel == null) {

            System.out.println(
                    "Booking not found!"
            );

            return;
        }


        // Make room available again
        for (Room room : rooms) {

            if (room.roomNumber ==
                    bookingToCancel.roomNumber) {

                room.available = true;
                break;
            }
        }


        // Remove booking
        bookings.remove(bookingToCancel);

        // Save changes
        saveBookings();


        System.out.println(
                "\nReservation cancelled successfully!"
        );

        System.out.println(
                "Room " + bookingToCancel.roomNumber +
                " is now available."
        );
    }


    // ================= SAVE BOOKINGS =================
    static void saveBookings() {

        try {

            FileWriter writer =
                    new FileWriter("bookings.txt");

            for (Booking booking : bookings) {

                writer.write(
                        booking.bookingId + "," +
                        booking.customerName + "," +
                        booking.roomNumber + "," +
                        booking.category + "," +
                        booking.days + "," +
                        booking.totalAmount + "," +
                        booking.paymentMethod + "\n"
                );
            }

            writer.close();

        }
        catch (IOException e) {

            System.out.println(
                    "Error saving bookings."
            );
        }
    }


    // ================= LOAD BOOKINGS =================
    static void loadBookings() {

        File file = new File("bookings.txt");

        // If file doesn't exist, start with empty bookings
        if (!file.exists()) {
            return;
        }

        try {

            Scanner fileScanner =
                    new Scanner(file);

            while (fileScanner.hasNextLine()) {

                String line =
                        fileScanner.nextLine();

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data =
                        line.split(",");

                int id =
                        Integer.parseInt(data[0]);

                String name =
                        data[1];

                int roomNumber =
                        Integer.parseInt(data[2]);

                String category =
                        data[3];

                int days =
                        Integer.parseInt(data[4]);

                double amount =
                        Double.parseDouble(data[5]);

                String paymentMethod =
                        data[6];


                Booking booking = new Booking(
                        id,
                        name,
                        roomNumber,
                        category,
                        days,
                        amount,
                        paymentMethod
                );

                bookings.add(booking);


                // Generate next booking ID
                if (id >= nextBookingId) {
                    nextBookingId = id + 1;
                }
            }

            fileScanner.close();

        }
        catch (Exception e) {

            System.out.println(
                    "Error loading bookings."
            );
        }
    }
}