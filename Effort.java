package projectcode;

public enum Effort
{ //enum for effort levels
    LOW,
    MEDIUM,
    HIGH;

    @Override
    public String toString()
    {
        // Capitalize first letter only for easy reading
        String s = name();
        return s.charAt(0) + s.substring(1).toLowerCase();
    }
}
