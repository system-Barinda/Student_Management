package com.sms;

/**
 * Project : Student Management System
 * My names : Aime Thierry Byiringiro
 * Reg No: 24rp07221
 * Description of the file: Student class for encapsulation of student data and basic business logic.
 */
public class Student {
    // Private fields for encapsulation
    private String regNumber;
    private String name;
    private int mathMarks;
    private int javaMarks;
    private int phpMarks;

    // Constructor
    public Student(String regNumber, String name, int mathMarks, int javaMarks, int phpMarks) {
        this.regNumber = regNumber;
        this.name = name;
        this.mathMarks = mathMarks;
        this.javaMarks = javaMarks;
        this.phpMarks = phpMarks;
    }

    // Getters and Setters
    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMathMarks() {
        return mathMarks;
    }

    public void setMathMarks(int mathMarks) {
        this.mathMarks = mathMarks;
    }

    public int getJavaMarks() {
        return javaMarks;
    }

    public void setJavaMarks(int javaMarks) {
        this.javaMarks = javaMarks;
    }

    public int getPhpMarks() {
        return phpMarks;
    }

    public void setPhpMarks(int phpMarks) {
        this.phpMarks = phpMarks;
    }

    /**
     * Calculates the average marks for the student.
     * @return The average marks as a double.
     */
    public double getAverageMarks() {
        // Simple, short, and clear explanation: Calculate the average of the three subjects.
        return (mathMarks + javaMarks + phpMarks) / 3.0;
    }

    /**
     * Returns student data as an array of objects for use in JTable.
     * @return Object array containing student details.
     */
    public Object[] toObjectArray() {
        // Simple, short, and clear explanation: Format student data for display in the table.
        return new Object[]{regNumber, name, mathMarks, javaMarks, phpMarks, String.format("%.2f", getAverageMarks())};
    }
}
