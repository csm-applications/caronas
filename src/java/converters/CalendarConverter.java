package converters;

import java.util.Calendar;
import java.util.Date;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("CalendarConverter")
public class CalendarConverter implements Converter {
    private static Date dateTime = new Date();
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        Calendar calendar = Calendar.getInstance();
        if(string.length() >5){
            calendar.setTime(dateTime);
            String[] split = string.split("/");
            calendar.set(Integer.valueOf(split[2]),
                    Integer.valueOf(split[1]),
                    Integer.valueOf(split[0]));
            dateTime.setTime(calendar.getTimeInMillis());
        }else{
            calendar.setTime(dateTime);
            String[] split = string.split(":");
            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(split[0]));
            calendar.set(Calendar.MINUTE, Integer.valueOf(split[1]));
            dateTime.setTime(calendar.getTimeInMillis());
        }
        Date toReturn = new Date(calendar.getTimeInMillis());
        return toReturn;
    }//end method
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        return o.toString();
    }//end method
}//end class