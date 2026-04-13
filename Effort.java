package projectcode;

public enum Effort
{
    LOW, MEDIUM, HIGH;

    @Override
    public String toString()
    {
        // Capitalize first letter only
        String s = name();
        return s.charAt(0) + s.substring(1).toLowerCase();
    }
}
