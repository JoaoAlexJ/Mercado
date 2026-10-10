package Auth;

public class BarcodeValidator {

    public static boolean isValid(String barcode){

        if (barcode == null || !barcode.matches("\\d{13}")){
            return false;
        }

        int sum = 0;

        for (int i = 0; i < 12 ; i++) {

            int weight = (i % 2 == 0) ? 1 : 3;
            int number = Character.getNumericValue(barcode.charAt(i));
            sum += number * weight;

        }

        int lastDigit = (10 - (sum % 10) % 10);

        return lastDigit == Character.getNumericValue(barcode.charAt(12));


    }
}
