public class UserSession
{
    private static int userId;
    private static String username;

    public static void setUser(int id, String name) {
        userId = id;
        username = name;
    }

    public static int getUserId()
    {
        return userId;
    }

    public static String getUsername() {
        return username;
    }

    public static void clear() {
        userId = 0;
        username = null;
    }
    private int getCurrentUserId() {
        return UserSession.getUserId(); // pulls from session
    }
    public static void setUserId(int id) {
        userId = id ;
    }

}