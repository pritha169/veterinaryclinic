package Hospital;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFormattedTextField.AbstractFormatter;

public class DateLabelFormatter extends AbstractFormatter {

    private final String datePattern = "yyyy-MM-dd";
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    @Override
    public Object stringToValue(String text) throws ParseException {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        return dateFormatter.parse(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value == null) {
            return "";
        }

        if (value instanceof Date) {
            return dateFormatter.format((Date) value);
        }

        // If the value is a Calendar (which JDatePicker sometimes returns)
        if (value instanceof Calendar) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }

        // If value is something else, fallback to empty string
        return "";
    }
}
