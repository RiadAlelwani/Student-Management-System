package infrastructure;

import java.security.MessageDigest;

/**
 * فئة مساعدة للعمليات المتعلقة بكلمات المرور مثل التجزئة والتحقق.
 */
public class PasswordUtil {
    /**
     * تجزئة كلمة مرور نصية باستخدام خوارزمية SHA-256.
     * @param password كلمة المرور النصية المراد تجزئتها
     * @return كلمة المرور المجزأة بتنسيق سداسي عشري
     * @throws Exception إذا لم يتم دعم خوارزمية SHA-256 أو الترميز
     */
    public static String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    /**
     * مقارنة كلمة مرور نصية مع كلمة مرور مجزأة للتحقق من تطابقهما.
     * @param plainPassword كلمة المرور النصية المدخلة من المستخدم
     * @param hashedPassword كلمة المرور المجزأة المخزنة (مثلًا في قاعدة البيانات)
     * @return true إذا تطابقت كلمات المرور، false إذا لم تتطابق
     * @throws Exception إذا فشلت عملية التجزئة
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) throws Exception {
        String hashedInput = hashPassword(plainPassword);
        return hashedInput.equals(hashedPassword);
    }
}