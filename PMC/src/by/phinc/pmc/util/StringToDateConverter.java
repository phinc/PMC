package by.phinc.pmc.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;

import static by.phinc.pmc.util.Constants.*;

public class StringToDateConverter extends StrutsTypeConverter {
	
	private DateFormat dateFormat;
	
	
	public StringToDateConverter() {
		super();
		ResourceBundle bundle = ResourceBundle.getBundle(
				RESORCE_BUNDLE);	
		dateFormat = new SimpleDateFormat(bundle.getString("date.format"));
	}

	@Override
	public Object convertFromString(Map map, String[] str, Class toClass) {
		if (str == null || str.length == 0 || str[0].trim().length() == 0) {
            return null;
        }
        try {
            return dateFormat.parse(str[0]);
        } catch (ParseException e) {
            throw new TypeConversionException("Unable to convert given object to date: " + str[0]);
        }

	}

	@Override
	public String convertToString(Map arg0, Object date) {
		if (date != null && date instanceof Date) {         
            return dateFormat.format(date);
        } else {
            return null;
        }

	}

}
