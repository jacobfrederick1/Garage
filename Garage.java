
//imports
import java.util.*;
import java.io.*;

/**
 * A class called Garage that puts sttings into a Stack with a limit of 7 Once
 * the stack is filled excess strings will be placed in a queue to wait for a
 * spot
 */
public class Garage{

    // declaring StackInts and Queue
    StackInt<String> garage;
    Queue<String> waitList;
    StackInt<String> temp;

    /**
     * No argument constructor Garage, Initializes garage, waitList and temp
     */
    public Garage() {
        garage = new LinkedStack();
        waitList = new LinkedList();
        temp = new LinkedStack();
    }

    /**
     * Garage argument constructor, Initializes garage, waitList and temp
     * 
     * @param fileName the file that hold the string to be read in
     */
    public Garage(String fileName) {
        garage = new LinkedStack();
        waitList = new LinkedList();
        temp = new LinkedStack();

        try {
            File infile = new File(fileName);
            Scanner input = new Scanner(infile);

            // Test if file has text
            while (input.hasNext()) {

                /*
                 * test if string from file equals a or another character goes to appropriate
                 * method depending on leading character
                 */
                if (input.next().equals("a")) {
                    arrival(input.nextLine());
                } else {
                    departure(input.nextLine());
                }
            }
            input.close();
        }
        // catches exception and prints error statement
        catch (FileNotFoundException e) {
            System.out.print("The file was not found!");
        }
    }

    /**
     * Arrival method takes a string and places the string in the Garage if there is
     * less than 7 cars if there are more than 7 cars the string will be placed in
     * the queue as long as there is less than 5 strings in the queue Test if the
     * string is already in the garage and if so does not add the string
     * 
     * @param license The string to be placed in either Garage or waitList if the
     *                string is not already in Garage or the conditions allow for
     *                the string to be added
     * @return True if the string was placed in the Garage or waitList and false if
     *         it was not
     */
    public boolean arrival(String license) {

        // test if the garage has less than 7 strings, if there is more they are added
        // to the queue if the queue has less than 5 strings
        if (numberParked() < 7) {

            // loops through garage to find a match, if it does it breaks the loop
            while (!garage.empty()) {
                if (!garage.peek().equals(license)) {
                    temp.push(garage.pop());
                } else {
                    break;
                }
            }

            /**
             * Test if the garage was empty because the license did not match any of the
             * strings Removes all the strings from temp stack and places them back in
             * garage returning true If the garage wasnt empty the garage is restored and
             * false is returned
             */
            if (garage.empty()) {
                while (!temp.empty()) {
                    garage.push(temp.pop());
                }
                garage.push(license);
                return true;
            } else {
                while (!temp.empty()) {
                    garage.push(temp.pop());
                }
                return false;
            }
        } else if (numberWaiting() < 5) {
            waitList.add(license);
            return true;
        }
        return false;
    }

    /**
     * departure method to search for license plate in garage stack, if found
     * returns number of cars moved otherwise returns -1 and restores the stack
     * 
     * @param license the string value that is to be searched for in the garage
     *                stack
     * @return the number of vehicles moved
     */
    public int departure(String license) {
        int carsMoved = 0;
        int openSpots = carsMoved;
        boolean flag = false;

        /**
         * loops through garage until it is empty adding 1 to carsMoved if the license
         * does not match once a match is found the string is removed, flag becomes true
         * and the loop is broken
         */
        while (!garage.empty()) {
            if (garage.peek().equals(license)) {
                garage.pop();
                flag = true;
                break;
            } else {
                temp.push(garage.pop());
                carsMoved += 1;
            }
        }

        if (!flag) {
            carsMoved = -1;
        }

        // restores the garage stack
        while (!temp.empty()) {
            garage.push(temp.pop());
        }
        while (openSpots >= 0 && !waitList.isEmpty()) {
            garage.push(waitList.remove());
            openSpots -= 1;
        }
        return carsMoved;
    }

    /**
     * Method to return the number are vehicles parked in the garage
     * 
     * @return The number of license in the garage
     */
    public int numberParked() {
        int numberParked = 0;
        /**
         * loops through garage until it is empty and pushes it onto a temp stack adds
         * one to numberParked until the garage is empty once the garage is empty loops
         * through temp stack to return the garage back to original status
         */
        while (!garage.empty()) {
            temp.push(garage.pop());
            numberParked += 1;
        }
        while (!temp.empty()) {
            garage.push(temp.pop());
        }

        return numberParked;
    }

    /**
     * Method to return the number of license in the waitList
     * 
     * @return The number of license in the queue
     */
    public int numberWaiting() {
        return waitList.size();
    }

    /**
     * toString method to return a string with the license in the garage and
     * waitlist places them in order of which they appear.
     */
    public String toString() {
        String inGarage = " ";
        String inLine = " ";

        // loops through waitList to add licesnse to inLine String
        for (String car : waitList) {
            inLine += car + ", ";
        }

        /**
         * loops through garage and places the license in String inGarage and adds
         * string to temp stack once garage is empty temp is looped through restoring
         * Garage
         */
        while (!garage.empty()) {
            inGarage += garage.peek() + ", ";
            temp.push(garage.pop());
        }
        while (!temp.empty()) {
            garage.push(temp.pop());
        }

        return "The cars in the Garage are: " + inGarage + "The cars waiting in line are: " + inLine;
    }
}